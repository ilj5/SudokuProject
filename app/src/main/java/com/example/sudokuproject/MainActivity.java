package com.example.sudokuproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sudokuproject.utils.SchedulerUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Slider.OnChangeListener {

    private Button btLogOut, btStart, btSettings, btContinue;

    private Slider slDifficulty;

    private TextView tvDifficulty;

    private SudokuManager.Difficulty difficulty;

    private ArrayList<Integer> boardData;

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MusicManager.getInstance(this).start();

        SchedulerUtils.unscheduleJob(MainActivity.this);

        btLogOut = findViewById(R.id.btLogOut);
        btSettings = findViewById(R.id.btSettings);
        btStart = findViewById(R.id.btStart);
        btContinue = findViewById(R.id.btContinue);

        slDifficulty = findViewById(R.id.slDifficulty);

        tvDifficulty = findViewById(R.id.tvDifficulty);

        slDifficulty.addOnChangeListener(this);

        btStart.setOnClickListener(this);
        btSettings.setOnClickListener(this);
        btLogOut.setOnClickListener(this);
        btContinue.setOnClickListener(this);

        Intent intent = getIntent();

        difficulty = SudokuManager.Difficulty.EASY;

        myRef = FirebaseDatabase.getInstance().getReference("User's Board").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("savedBoard");

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (!dataSnapshot.exists()) {
                        btContinue.setVisibility(View.GONE);
                    }
                }
            }});

        checkPermission();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v == btSettings) {
            intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            finish();
        } else if (v == btStart) {
            intent = new Intent(MainActivity.this, SudokuActivity.class);
            intent.putExtra("difficulty", difficulty);
            startActivity(intent);
            finish();
        } else if (v == btLogOut) {
            FirebaseAuth.getInstance().signOut();

            MusicManager.getInstance(this).stop();

            intent = new Intent(MainActivity.this, LogIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears back stack
            startActivity(intent);
            finish(); // close current activity
        } else if (v == btContinue) {
            intent = new Intent(MainActivity.this, SudokuActivity.class);

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
    }

    @Override
    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
        Resources res = getResources();
        String[] difficulties = res.getStringArray(R.array.difficulty);

        switch ((int) value) {
            case 1:
                tvDifficulty.setText(difficulties[0]);
                difficulty = SudokuManager.Difficulty.EASY;
                break;
            case 2:
                tvDifficulty.setText(difficulties[1]);
                difficulty = SudokuManager.Difficulty.STANDARD;
                break;
            case 3:
                tvDifficulty.setText(difficulties[2]);
                difficulty = SudokuManager.Difficulty.HARD;
                break;
            case 4:
                tvDifficulty.setText(difficulties[3]);
                difficulty = SudokuManager.Difficulty.EXTREME;
                break;
        }
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted.
                } else {

                    Toast.makeText(this, "This feature requires the permission to function properly.", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SchedulerUtils.scheduleJob(MainActivity.this);
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