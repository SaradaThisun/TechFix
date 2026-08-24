package com.example.techtrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techtrack.MainActivity;
import com.example.techtrack.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etEmail, etPassword, etConfirmPassword;
    private TextView tvError, tvStrength, tvSignIn;
    private Button btnBranchColombo, btnBranchGalle, btnRegister;
    private String selectedBranch = "Colombo Branch";
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tvError = findViewById(R.id.tvError);
        tvStrength = findViewById(R.id.tvStrength);
        tvSignIn = findViewById(R.id.tvSignIn);
        btnBranchColombo = findViewById(R.id.btnBranchColombo);
        btnBranchGalle = findViewById(R.id.btnBranchGalle);
        btnRegister = findViewById(R.id.btnRegister);

        btnBranchColombo.setOnClickListener(v -> selectBranch("Colombo Branch"));
        btnBranchGalle.setOnClickListener(v -> selectBranch("Galle Branch"));

        btnRegister.setOnClickListener(v -> attemptRegister());

        tvSignIn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void selectBranch(String branch) {
        selectedBranch = branch;
        if (branch.equals("Colombo Branch")) {
            btnBranchColombo.setBackgroundTintList(getColorStateList(com.example.techtrack.R.color.accent));
            btnBranchColombo.setTextColor(getColor(com.example.techtrack.R.color.white));
            btnBranchGalle.setBackgroundTintList(getColorStateList(com.example.techtrack.R.color.surface));
            btnBranchGalle.setTextColor(getColor(com.example.techtrack.R.color.text_dim));
        } else {
            btnBranchGalle.setBackgroundTintList(getColorStateList(com.example.techtrack.R.color.accent));
            btnBranchGalle.setTextColor(getColor(com.example.techtrack.R.color.white));
            btnBranchColombo.setBackgroundTintList(getColorStateList(com.example.techtrack.R.color.surface));
            btnBranchColombo.setTextColor(getColor(com.example.techtrack.R.color.text_dim));
        }
    }

    private void attemptRegister() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showError("Please fill in all required fields.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }
        if (password.length() < 6) {
            showError("Please use a stronger password.");
            return;
        }

        tvError.setVisibility(TextView.GONE);
        btnRegister.setEnabled(false);
        btnRegister.setText("Creating account...");

        // Simulate network delay, same as RN version (1500ms)
        handler.postDelayed(() -> {
            // TODO: replace with real API call via ApiClient.getApiService().register(...)
            // TODO: replace with MainAppActivity once it's built
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }, 1500);
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(TextView.VISIBLE);
    }
}