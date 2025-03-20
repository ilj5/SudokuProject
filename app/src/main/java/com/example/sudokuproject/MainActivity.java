package com.example.sudokuproject;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.slider.Slider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Slider.OnChangeListener {

    private Button btLogOut, btStart, btSettings;

    private Slider slDifficulty;

    private TextView tvDifficulty;

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

    }

    @Override
    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
        Resources res = getResources();
        String[] difficulties = res.getStringArray(R.array.difficulty);

        switch ((int) value) {
            case 1:
                tvDifficulty.setText(difficulties[0]);
                break;
            case 2:
                tvDifficulty.setText(difficulties[1]);
                break;
            case 3:
                tvDifficulty.setText(difficulties[2]);
                break;
            case 4:
                tvDifficulty.setText(difficulties[3]);
                break;
        }
    }
}