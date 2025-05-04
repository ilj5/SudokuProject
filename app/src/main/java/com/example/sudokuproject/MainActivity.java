package com.example.sudokuproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sudokuproject.utils.SchedulerUtils;
import com.google.android.material.slider.Slider;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Slider.OnChangeListener {

    private Button btLogOut, btStart, btSettings;

    private Slider slDifficulty;

    private TextView tvDifficulty;

    private SudokuManager.Difficulty difficulty;

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

        SchedulerUtils.unscheduleJob(MainActivity.this);

        btLogOut = findViewById(R.id.btLogOut);
        btSettings = findViewById(R.id.btSettings);
        btStart = findViewById(R.id.btStart);

        slDifficulty = findViewById(R.id.slDifficulty);

        tvDifficulty = findViewById(R.id.tvDifficulty);

        slDifficulty.addOnChangeListener(this);

        btStart.setOnClickListener(this);
        btSettings.setOnClickListener(this);

        Intent intent = getIntent();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Date date = new Date();
//        long time = date.getTime();
//
//        SharedPreferences sharedPref = getSharedPreferences("UserLastPlayedDate", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//
//        editor.putLong("date", time);
//
//        editor.apply();
//
//        SchedulerUtils.scheduleJob(MainActivity.this);
    }
}