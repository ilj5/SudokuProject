package com.example.sudokuproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sudokuproject.utils.SchedulerUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Settings extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Button btResume, btBackToMain;

    private SeekBar sbMusic;

    private int lastActivity;

    private ArrayList<Integer> boardData;

    private static final float VOLUME_CONVERSION = 100f;//converting a number between 100 and 1 to a number between 1.0 and 0.0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SchedulerUtils.unscheduleJob(Settings.this);

        btResume = findViewById(R.id.btResume);
        btBackToMain = findViewById(R.id.btBackToMain);

        sbMusic = findViewById(R.id.sbMusic);

        btResume.setOnClickListener(this);
        btBackToMain.setOnClickListener(this);

        sbMusic.setOnSeekBarChangeListener(this);

        Intent intent = getIntent();

        lastActivity = intent.getIntExtra("last_activity", 0);

        float volume = MusicManager.getInstance(this).getVolume();

        sbMusic.setProgress((int) (volume * VOLUME_CONVERSION));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v == btResume) {
            if (lastActivity == 0) {
                intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                intent = new Intent(Settings.this, SudokuActivity.class);
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User's Board").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("savedBoard");

                myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            DataSnapshot dataSnapshot =  task.getResult();
                            boardData = new ArrayList<>();

                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                boardData.add(child.getValue(Integer.class));
                            }

                            int[][] puzzleBoard = new int[9][9];
                            int[][] lockedPuzzleBoard = new int[9][9];



                            for (int i = 0; i < 81; i++) {
                                puzzleBoard[i / 9][i % 9] = boardData.get(i);
                            }

                            for (int i = 0; i < 81; i++) {
                                lockedPuzzleBoard[i / 9][i % 9] = boardData.get(i + 81);
                            }

                            intent.putExtra("puzzleBoard", puzzleBoard);
                            intent.putExtra("lockedPuzzleBoard", lockedPuzzleBoard);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }

        } else if (v == btBackToMain) {
            intent = new Intent(Settings.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float volume = sbMusic.getProgress() / VOLUME_CONVERSION;
        MusicManager.getInstance(this).setVolume(volume);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SchedulerUtils.scheduleJob(Settings.this);
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