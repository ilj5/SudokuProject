package com.example.sudokuproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SudokuActivity extends AppCompatActivity {
    private FrameLayout frmView;
    private CustomView cv;

    private Handler gameEndHandler;

    private SudokuManager.Difficulty difficulty;

    private FirebaseDatabase myDB;

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

        frmView = findViewById(R.id.frmView);

        difficulty = SudokuManager.Difficulty.TESTING;

        gameEndHandler = new Handler(){
            public void handleMessage(Message msg){
                boolean win = false;
                if (msg.arg1 == 1) {
                    win = true;
                }
                finishGameDialog(win);
            }
        };

        myDB = FirebaseDatabase.getInstance("https://sudokuproject-67973-default-rtdb.firebaseio.com/");

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
                dialog.dismiss();
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
            cv = new CustomView(this, frmView.getWidth(), frmView.getHeight(), gameEndHandler, difficulty);
            frmView.addView(cv);
            DatabaseReference myRef = myDB.getReference().push().child("Board");
            ArrayList<Integer> temp = new ArrayList<>();
            int[][] puzzle = cv.getBoard();
            int k = 0;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    temp.add(k, puzzle[i][j]);
                    k++;
                }
            }
            myRef.setValue(temp);
        }
    }
}