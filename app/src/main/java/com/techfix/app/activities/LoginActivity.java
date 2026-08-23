package com.techfix.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.firebase.FirebaseAuthHelper;
import com.techfix.app.models.User;
import com.techfix.app.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private LinearLayout errorBox;
    private TextView tvError;
    private View btnSignIn;
    private SessionManager session;
    private boolean showingPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(this);

        etEmail    = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        errorBox   = findViewById(R.id.errorBox);
        tvError    = findViewById(R.id.tvError);
        btnSignIn  = findViewById(R.id.btnSignIn);

        // Demo fill
        findViewById(R.id.btnDemoFill).setOnClickListener(v -> {
            etEmail.setText("tharinduerandana7710@gmail.com");
            etPassword.setText("TechFix@2026");
            hideError();
        });

        findViewById(R.id.btnStaffDemo).setOnClickListener(v -> {
            etEmail.setText("staff@techfix.com");
            etPassword.setText("TechFix@2026");
            hideError();
        });

        // Toggle password visibility
        findViewById(R.id.btnTogglePass).setOnClickListener(v -> {
            showingPassword = !showingPassword;
            etPassword.setInputType(showingPassword
                    ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPassword.setSelection(etPassword.getText().length());
            ((TextView) v).setText(showingPassword ? "🙈" : "👁");
        });

        // Sign In
        btnSignIn.setOnClickListener(v -> handleLogin());

        // Forgot password
        findViewById(R.id.btnForgotPassword).setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                showError("Enter your email address first.");
                return;
            }
            FirebaseAuthHelper.sendPasswordReset(email, new FirebaseAuthHelper.AuthCallback() {
                @Override public void onSuccess(User user) {
                    Toast.makeText(LoginActivity.this, "Reset email sent!", Toast.LENGTH_SHORT).show();
                }
                @Override public void onFailure(String error) {
                    showError(error);
                }
            });
        });

        // Go to register
        findViewById(R.id.btnCreateAccount).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }

    private void handleLogin() {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter your email/phone and password.");
            return;
        }

        hideError();
        btnSignIn.setEnabled(false);

        FirebaseAuthHelper.login(email, password, new FirebaseAuthHelper.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                session.saveSession(user.getUid(), user.getFullName(), user.getEmail(),
                        user.getPhone(), user.getBranch(), user.getRole());
                btnSignIn.setEnabled(true);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(String error) {
                btnSignIn.setEnabled(true);
                showError(error != null ? error : "Login failed. Check your credentials.");
            }
        });
    }

    private void showError(String msg) {
        tvError.setText(msg);
        errorBox.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        errorBox.setVisibility(View.GONE);
    }
}
