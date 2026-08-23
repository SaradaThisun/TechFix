package com.techfix.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.data.api.ApiService;
import com.techfix.app.data.api.RetrofitClient;
import com.techfix.app.data.local.SessionManager;
import com.techfix.app.data.model.AuthResponse;
import com.techfix.app.data.model.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private TextView tvGoRegister;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        tvGoRegister = findViewById(R.id.tvGoRegister);

        session = new SessionManager(this);

        if (session.isLoggedIn()) {
            startActivity(new Intent(this, com.techfix.app.MainActivity.class));
            finish();
            return;
        }

        btnLogin.setOnClickListener(v -> attemptLogin());

        tvGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        ApiService api = RetrofitClient.getInstance(this);
        Call<AuthResponse> call = api.login(new LoginRequest(email, password));

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse body = response.body();
                    session.saveToken(body.getToken());
                    session.saveUser(body.getUser().getId(), body.getUser().getName(), body.getUser().getEmail());

                    Toast.makeText(LoginActivity.this, "Welcome " + body.getUser().getName(), Toast.LENGTH_SHORT).show();
                    // We'll redirect to MainActivity here once we build more screens
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed: check your credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setLoading(false);
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!loading);
    }
}