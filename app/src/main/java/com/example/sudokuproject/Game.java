package com.example.sudokuproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;

public class Game {
    Context context;
    private float wScreen, hScreen; // screen dimensions
    private Bitmap picTrash;
    private SudokuManager.Difficulty difficulty;

    private Board board; // board sprite
    private NumberBoard nBoard; // numberBoard sprite

    private float cell;

    private SudokuManager sudokuManager;

    private int[][] puzzleBoard;
    private final int[][] lockedPuzzleBoard;

    public Game(Context context, float wScreen, float hScreen, SudokuManager.Difficulty difficulty) {
        this.context = context;
        this.wScreen = wScreen;
        this.hScreen = hScreen;
        this.difficulty = difficulty;

        //load images
        picTrash = BitmapFactory.decodeResource(context.getResources(), R.drawable.trashcanicon);

        sudokuManager = new SudokuManager(difficulty);
        sudokuManager.generateNewPuzzle();
        puzzleBoard = sudokuManager.getPuzzle();
        lockedPuzzleBoard = sudokuManager.copyPuzzle();

    }

    public Game(Context context, float wScreen, float hScreen, int[][] puzzleBoard, int[][] lockedPuzzleBoard) {
        this.context = context;
        this.wScreen = wScreen;
        this.hScreen = hScreen;
        this.puzzleBoard = puzzleBoard;
        this.lockedPuzzleBoard = lockedPuzzleBoard;

        //load images
        picTrash = BitmapFactory.decodeResource(context.getResources(), R.drawable.trashcanicon);

        sudokuManager = new SudokuManager(puzzleBoard);
    }

    private void initDimensionsAndLocations()
    {
        // the dimensions of the board and the cells in the board and number board
        float w = wScreen * 0.8f;
        float h = w;
        cell = w / 9;
        float x = wScreen * 0.1f;
        float y = 20 + hScreen * 0.2f;
        board = new Board(x, y, w,h, cell);
        // numberBoard dimensions
        float wNumB = cell * 10;
        float hNumB = cell;
        float xNumB = x - cell / 2;
        float yNumB = y + h + cell;
        nBoard = new NumberBoard(xNumB, yNumB, wNumB, hNumB, cell, picTrash);

    }

    public void initGame()
    {
        initDimensionsAndLocations();

    }

    public void draw(Canvas canvas)
    {
        // drawing all objects
        board.draw(canvas, puzzleBoard, lockedPuzzleBoard);
        nBoard.draw(canvas);

    }

    public void changeNumber() {
        if (!board.isSelected()) {
          return;
        } else if ((!nBoard.isSelected()) || lockedPuzzleBoard[(int) ((board.getSelCellX()) / cell)][(int) ((board.getSelCellY()) / cell)] != 0) {
            nBoard.setSelCell(-1);
            return;
        }
        int selCell = (int) (nBoard.getSelCellX() / cell) + 1;// the selected number on the number board
        int SelCellX = (int) (board.getSelCellX() / cell);// the index of the number in a array
        int SelCellY = (int) (board.getSelCellY() / cell);// the index of the number in a array

        if (selCell == 10) {
            puzzleBoard[SelCellX][SelCellY] = 0;
            return;
        }
        puzzleBoard[SelCellX][SelCellY] = selCell;
        nBoard.setSelCell(-1);
    }

    public boolean isFilled() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (puzzleBoard[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public int[][] copyPuzzle(int [][] toCopy) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                copy[i][j] = toCopy[i][j];
            }
        }
        return copy;
    }

    public boolean winCheck() {
        return sudokuManager.isValid();
    }

    public Board getBoard() {return board;}

    public NumberBoard getNumberBoard() {return nBoard;}


    public int[][] getPuzzle() {
        return sudokuManager.copyPuzzle();
    }

    public int[][] getLockedPuzzleBoard() {return copyPuzzle(lockedPuzzleBoard);}
}
