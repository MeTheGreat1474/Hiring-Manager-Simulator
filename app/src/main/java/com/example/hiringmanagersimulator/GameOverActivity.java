package com.example.hiringmanagersimulator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class GameOverActivity extends AppCompatActivity {

    public static final String EXTRA_CORRECT      = "extra_correct";
    public static final String EXTRA_MISTAKES     = "extra_mistakes";
    public static final String EXTRA_DAYS         = "extra_days";
    public static final String EXTRA_BONUS        = "extra_bonus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_game_over);

        int correct  = getIntent().getIntExtra(EXTRA_CORRECT,  0);
        int mistakes = getIntent().getIntExtra(EXTRA_MISTAKES, 0);
        int days     = getIntent().getIntExtra(EXTRA_DAYS,     1);
        float bonus  = getIntent().getFloatExtra(EXTRA_BONUS,  0f);

        TextView tvStats = findViewById(R.id.tv_game_over_stats);
        tvStats.setText(
                "CORRECT       :  " + correct  + "\n" +
                "MISTAKES      :  " + mistakes + "\n" +
                "DAYS SURVIVED :  " + days     + "\n\n" +
                "BONUS EARNED  :  " + String.format(Locale.US, "RM %.2f", bonus));

        findViewById(R.id.btn_play_again).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
