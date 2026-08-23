package com.example.techtrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.techtrack.MainActivity;
import com.example.techtrack.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister, btnBranchColombo, btnBranchGalle;
    private TextView tvError, tvSignIn, tvStrength;
    private String selectedBranch = "Colombo"; // Default
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBranchColombo = findViewById(R.id.btnBranchColombo);
        btnBranchGalle = findViewById(R.id.btnBranchGalle);
        tvError = findViewById(R.id.tvError);
        tvSignIn = findViewById(R.id.tvSignIn);
        tvStrength = findViewById(R.id.tvStrength);

        // Branch Selection Logic
        btnBranchColombo.setOnClickListener(v -> selectBranch("Colombo"));
        btnBranchGalle.setOnClickListener(v -> selectBranch("Galle"));

        // Navigation to Login
        tvSignIn.setOnClickListener(v -> finish());

        // Registration Logic
        btnRegister.setOnClickListener(v -> attemptRegistration());
    }

    private void selectBranch(String branch) {
        selectedBranch = branch;
        if (branch.equals("Colombo")) {
            btnBranchColombo.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.accent));
            btnBranchColombo.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnBranchGalle.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.surface));
            btnBranchGalle.setTextColor(ContextCompat.getColor(this, R.color.text_dim));
        } else {
            btnBranchGalle.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.accent));
            btnBranchGalle.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnBranchColombo.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.surface));
            btnBranchColombo.setTextColor(ContextCompat.getColor(this, R.color.text_dim));
        }
    }

    private void attemptRegistration() {
        String name = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String confirmPass = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            showError("Please fill in all required fields.");
            return;
        }

        if (!pass.equals(confirmPass)) {
            showError("Passwords do not match.");
            return;
        }

        tvError.setVisibility(View.GONE);
        btnRegister.setEnabled(false);
        btnRegister.setText("Creating Account...");

        // Simulate API Call
        handler.postDelayed(() -> {
            Toast.makeText(this, "Registration Successful for " + name, Toast.LENGTH_LONG).show();
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }, 2000);
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }
}
