package com.example.sudokuproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SudokuActivity extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout frmView;
    private CustomView cv;

    private Handler gameEndHandler;

    private SudokuManager.Difficulty difficulty;

    private Button btSettings;

    private int[][] puzzleBoard, lockedPuzzleBoard;

    private boolean isContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sudoku);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        if (intent.getSerializableExtra("difficulty") == null) {
            puzzleBoard = (int[][]) intent.getSerializableExtra("puzzleBoard");
            lockedPuzzleBoard = (int[][]) intent.getSerializableExtra("lockedPuzzleBoard");
            isContinue = true;
        } else {
            difficulty = (SudokuManager.Difficulty) intent.getSerializableExtra("difficulty");
            difficulty = SudokuManager.Difficulty.TESTING;
            isContinue = false;
        }

        frmView = findViewById(R.id.frmView);


        gameEndHandler = new Handler(){
            public void handleMessage(Message msg){
                boolean win = msg.arg1 == 1;
                finishGameDialog(win);
            }
        };

        btSettings = findViewById(R.id.btSettings);

        btSettings.setOnClickListener(this);

    }

    private void finishGameDialog(boolean win) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String str;
        if (win) {
            str = "You have completed the sudoku successfully!\nDo you want to go back to the main menu or start another game in the " + difficulty.getName() + " difficulty?";
        } else {
            str = "You have not completed the sudoku successfully\nDo you want to go back to the main menu, continue trying to complete the sudoku successfully or start another game in the " + difficulty.getName() + " difficulty?";
        }
        builder.setMessage(str);
        builder.setCancelable(false);

        builder.setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //changes the screen to the main menu activity
                Intent intent = new Intent(SudokuActivity.this, MainActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //create a new sudoku board with the same difficulty as the one that was just finished
                cv.newGame();
                dialog.dismiss();
            }
        });

        if (!win) {
            builder.setNeutralButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //continues the same game and sudoku board without changing anything so the player will have another chance to complete the board successfully
                    dialog.cancel();
                    cv.resumeGame();
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if ( cv == null && hasFocus) {
            if (isContinue) {
                cv = new CustomView(this, frmView.getWidth(), frmView.getHeight(), gameEndHandler, puzzleBoard, lockedPuzzleBoard);
            } else {
                cv = new CustomView(this, frmView.getWidth(), frmView.getHeight(), gameEndHandler, difficulty);
            }
            frmView.addView(cv);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User'sBoard").child(user.getUid()).child("savedBoard");

                lockedPuzzleBoard = cv.getLockedPuzzleBoard();
                puzzleBoard = cv.getBoard();
                ArrayList<Integer> boardData = new ArrayList<>();

                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boardData.add(puzzleBoard[i][j]);
                    }
                }

                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        boardData.add(lockedPuzzleBoard[i][j]);
                    }
                }

                myRef.setValue(boardData);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btSettings) {
            Intent intent = new Intent(SudokuActivity.this, Settings.class);
            intent.putExtra("last_activity", 1);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicManager.getInstance(this).pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.getInstance(this).start();
    }
}