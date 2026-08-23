package com.example.techtrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techtrack.MainActivity;
import com.example.techtrack.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etIdentifier, etPassword;
    private TextView tvError, tvForgot, tvCreateAccount;
    private Button btnLogin, btnGoogle;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etIdentifier = findViewById(R.id.etIdentifier);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);
        tvForgot = findViewById(R.id.tvForgot);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnGoogle.setOnClickListener(v -> attemptLogin());

        tvCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        tvForgot.setOnClickListener(v ->
                Toast.makeText(this, "Password reset coming soon", Toast.LENGTH_SHORT).show());
    }

    private void attemptLogin() {
        String identifier = etIdentifier.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(identifier) || TextUtils.isEmpty(password)) {
            tvError.setText("Please enter your email/phone and password.");
            tvError.setVisibility(TextView.VISIBLE);
            return;
        }
        tvError.setVisibility(TextView.GONE);
        btnLogin.setEnabled(false);
        btnLogin.setText("Signing in...");

        // Simulate network delay, same as RN version (1500ms)
        handler.postDelayed(() -> {
            String normalized = identifier.toLowerCase();
            boolean isStaff = normalized.contains("staff")
                    || normalized.contains("technician")
                    || normalized.contains("admin");

            // TODO: replace with real API call via ApiClient.getApiService().login(...)
            Toast.makeText(this,
                    isStaff ? "Logged in as staff" : "Logged in as customer",
                    Toast.LENGTH_SHORT).show();

            // TODO: replace with MainAppActivity once it's built
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }, 1500);
    }
}