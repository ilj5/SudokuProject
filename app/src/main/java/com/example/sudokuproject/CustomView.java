package com.example.sudokuproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CustomView extends SurfaceView implements Runnable {

    Context context;
    private float wScreen, hScreen; // screen dimensions
    private SurfaceHolder holder; // display surface holder
    private Thread thread; // animation thread

    private Game game;
    private SudokuManager.Difficulty difficulty;

    private boolean inGame;
    private boolean secondTry; //If the player didn't manage to complete the sudoku successfully the first time and he clicks on Continue in the alertDialog. Resets when the player changes anything on the board

    private Handler finishHandler;

    public CustomView(Context context, float wScreen, float hScreen, Handler handler, SudokuManager.Difficulty difficulty) { //creates a new sudoku board
        super(context);
        this.context = context;
        this.wScreen = wScreen;
        this.hScreen = hScreen;
        this.difficulty = difficulty;
        holder = getHolder(); // this display surface holder
        finishHandler = handler;

        newGame();
    }

    public CustomView(Context context, float wScreen, float hScreen, Handler handler, int[][] puzzleBoard, int[][] lockedPuzzleBoard) { //creates an old sudoku board from firebase
        super(context);
        this.context = context;
        this.wScreen = wScreen;
        this.hScreen = hScreen;
        holder = getHolder(); // this display surface holder
        finishHandler = handler;

        continueGame(puzzleBoard, lockedPuzzleBoard);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ex = event.getX();
        float ey = event.getY();
        Board board =game.getBoard();
        NumberBoard nBoard = game.getNumberBoard();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (board.contains(ex, ey) && inGame) {
                board.setSelCell(ex, ey);
                game.changeNumber();
                secondTry = false;
            } else if (nBoard.contains(ex, ey) && inGame) {
                nBoard.setSelCell(ex);
                game.changeNumber();
                secondTry = false;
            }
        }
        if (game.isFilled() && !secondTry && inGame) {
            inGame = false;
            endGame();
        }
        return true;
    }

    public void endGame(){
        //שליחת הודעה לאקטיביטי של המשחק
        Message msg=finishHandler.obtainMessage();
        if (game.winCheck()) {// 1 means a win and a 0 means a lose
            msg.arg1 = 1;
        } else {
            msg.arg1 = 0;
        }
        finishHandler.sendMessage(msg);
    }

    // sets the values to new game
    public void newGame()
    {
        game = new Game(context, wScreen, hScreen, difficulty);
        game.initGame();
        inGame = true;
        secondTry = false;
        //------------ animation thread start----------------
        thread = new Thread(this);
        thread.start();
    }

    //Starts a game using data from firebase
    public void continueGame(int[][] puzzleBoard, int[][] lockedPuzzleBoard) {
        game = new Game(context, wScreen, hScreen, puzzleBoard, lockedPuzzleBoard);
        game.initGame();
        inGame = true;
        secondTry = false;
        //------------ animation thread start----------------
        thread = new Thread(this);
        thread.start();
    }

    //sets second try to true and inGame to true
    public void resumeGame() {
        secondTry = true;
        inGame = true;
        //------------ animation thread start----------------
        thread = new Thread(this);
        thread.start();
    }

    public void drawSurface()
    {
        if( holder.getSurface().isValid())
        {
            Canvas canvas = holder.lockCanvas(); // get canvas and lock to drawing
            if (canvas != null) {
                canvas.drawColor(Color.BLACK); // clear background
                game.draw(canvas);
                holder.unlockCanvasAndPost(canvas); // escape canvas
            }
        }
    }

    @Override
    public void run() {
        while (inGame)
        {

            drawSurface();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    public int[][] getBoard(){
        return game.getPuzzle();
    }

    public int[][] getLockedPuzzleBoard() {return game.getLockedPuzzleBoard();}
}
