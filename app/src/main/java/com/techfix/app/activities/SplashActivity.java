package com.techfix.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvProgress;
    private int currentProgress = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);
        tvProgress = findViewById(R.id.tvProgress);
        View content = findViewById(R.id.content);

        // Fade-in animation
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(700);
        fadeIn.setFillAfter(true);
        content.startAnimation(fadeIn);

        // Animate progress bar
        animateProgress();

        // Button listeners
        findViewById(R.id.btnSkip).setOnClickListener(v -> goToLogin());
        findViewById(R.id.btnSignIn).setOnClickListener(v -> goToLogin());
        findViewById(R.id.btnGetStarted).setOnClickListener(v -> goToRegister());

        // Auto-check session — if already logged in, skip to main
        SessionManager session = new SessionManager(this);
        if (session.isLoggedIn()) {
            handler.postDelayed(this::goToMain, 1500);
        }
    }

    private void animateProgress() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentProgress < 100) {
                    currentProgress += 2;
                    progressBar.setProgress(currentProgress);
                    tvProgress.setText(currentProgress + "%");
                    handler.postDelayed(this, 56);
                } else {
                    // Navigate after progress reaches 100%
                    SessionManager session = new SessionManager(SplashActivity.this);
                    if (session.isLoggedIn()) {
                        goToMain();
                    } else {
                        // Small delay for the user to see 100% before transitioning
                        handler.postDelayed(SplashActivity.this::goToLogin, 500);
                    }
                }
            }
        }, 56);
    }

    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void goToRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
