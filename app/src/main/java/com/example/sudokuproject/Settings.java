package com.example.sudokuproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Settings extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Button btResume, btBackToMain;

    private SeekBar sbSfx, sbMusic;

    private int lastActivity;

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

        btResume = findViewById(R.id.btResume);
        btBackToMain = findViewById(R.id.btBackToMain);

        sbMusic = findViewById(R.id.sbMusic);
        sbSfx = findViewById(R.id.sbSfx);

        btResume.setOnClickListener(this);
        btBackToMain.setOnClickListener(this);

        sbMusic.setOnSeekBarChangeListener(this);
        sbSfx.setOnSeekBarChangeListener(this);

        Intent intent = getIntent();

        lastActivity = intent.getIntExtra("last_activity", 0);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v == btResume) {
            if (lastActivity == 0) {
                intent = new Intent(Settings.this, MainActivity.class);
            } else {
                intent = new Intent(Settings.this, SudokuActivity.class);
            }
            startActivity(intent);
            finish();
        } else if (v == btBackToMain) {
            intent = new Intent(Settings.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == sbMusic) {

        } else if (seekBar == sbSfx) {

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}