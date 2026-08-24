package com.example.techtrack.activities;
import com.example.techtrack.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techtrack.R;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progress = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);
        Button btnSignIn = findViewById(R.id.btnSignIn);
        Button btnGetStarted = findViewById(R.id.btnGetStarted);

        // Simulate loading progress, same as the RN splash (0 -> 100 over ~2.8s)
        runProgressAnimation();

        btnSignIn.setOnClickListener(v -> goToLogin());
        btnGetStarted.setOnClickListener(v -> goToRegister());
    }

    private void runProgressAnimation() {
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (progress < 100) {
                    progress += 2;
                    progressBar.setProgress(progress);
                    handler.postDelayed(this, 56);
                }
            }
        };
        handler.post(progressRunnable);
    }

    private void goToLogin() {
        // TODO: replace with LoginActivity once it's built
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToRegister() {
        // TODO: replace with RegisterActivity once it's built
        Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}