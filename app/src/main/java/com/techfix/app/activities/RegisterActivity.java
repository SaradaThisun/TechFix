package com.techfix.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.firebase.FirebaseAuthHelper;
import com.techfix.app.models.User;
import com.techfix.app.utils.SessionManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etEmail, etPassword, etConfirmPassword;
    private LinearLayout errorBox, cardColombo, cardGalle, strengthRow;
    private TextView tvError, tvStrength;
    private View seg1, seg2, seg3, seg4;
    private String selectedBranch = "Colombo Branch";
    private String selectedRole = "customer";
    private boolean showingPassword = false;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        session = new SessionManager(this);

        etFullName        = findViewById(R.id.etFullName);
        etPhone           = findViewById(R.id.etPhone);
        etEmail           = findViewById(R.id.etEmail);
        etPassword        = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        errorBox          = findViewById(R.id.errorBox);
        tvError           = findViewById(R.id.tvError);
        cardColombo       = findViewById(R.id.cardColombo);
        cardGalle         = findViewById(R.id.cardGalle);
        strengthRow       = findViewById(R.id.strengthRow);
        tvStrength        = findViewById(R.id.tvStrength);
        seg1 = findViewById(R.id.seg1);
        seg2 = findViewById(R.id.seg2);
        seg3 = findViewById(R.id.seg3);
        seg4 = findViewById(R.id.seg4);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Demo fill
        findViewById(R.id.btnDemoFill).setOnClickListener(v -> fillDemo("customer"));
        findViewById(R.id.btnStaffDemo).setOnClickListener(v -> fillDemo("staff"));

        // Branch selector
        cardColombo.setOnClickListener(v -> selectBranch("Colombo Branch"));
        cardGalle.setOnClickListener(v -> selectBranch("Galle Branch"));

        // Password toggle
        findViewById(R.id.btnTogglePass).setOnClickListener(v -> {
            showingPassword = !showingPassword;
            int type = showingPassword
                    ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            etPassword.setInputType(type);
            etConfirmPassword.setInputType(type);
            etPassword.setSelection(etPassword.getText().length());
            ((TextView) v).setText(showingPassword ? "🙈" : "👁");
        });

        // Password strength watcher
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateStrength(s.toString());
            }
        });

        // Register button
        findViewById(R.id.btnRegister).setOnClickListener(v -> handleRegister());

        // Sign in link
        findViewById(R.id.btnSignIn).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void fillDemo(String role) {
        selectedRole = role;
        if ("staff".equals(role)) {
            etFullName.setText("Staff Member");
            etEmail.setText("staff@techfix.com");
        } else {
            etFullName.setText("Tharindu Erandana");
            etEmail.setText("tharinduerandana7710@gmail.com");
        }
        etPhone.setText("+94 71 889 9120");
        etPassword.setText("TechFix@2026");
        etConfirmPassword.setText("TechFix@2026");
        selectBranch("Colombo Branch");
        hideError();
    }

    private void selectBranch(String branch) {
        selectedBranch = branch;
        TextView tvColomboName = findViewById(R.id.tvColomboName);
        TextView tvColomboStatus = findViewById(R.id.tvColomboStatus);
        TextView tvGalleName = findViewById(R.id.tvGalleName);
        TextView tvGalleStatus = findViewById(R.id.tvGalleStatus);

        if (tvColomboName == null || tvColomboStatus == null || tvGalleName == null || tvGalleStatus == null) {
            return;
        }

        if ("Colombo Branch".equals(branch)) {
            cardColombo.setBackgroundResource(R.drawable.bg_btn_primary);
            cardGalle.setBackgroundResource(R.drawable.bg_card);
            tvColomboName.setTextColor(Color.WHITE);
            tvColomboStatus.setTextColor(0xCCFFFFFF);
            tvGalleName.setTextColor(getResources().getColor(R.color.text_dim, null));
            tvGalleStatus.setTextColor(getResources().getColor(R.color.text_muted, null));
        } else {
            cardGalle.setBackgroundResource(R.drawable.bg_btn_primary);
            cardColombo.setBackgroundResource(R.drawable.bg_card);
            tvGalleName.setTextColor(Color.WHITE);
            tvGalleStatus.setTextColor(0xCCFFFFFF);
            tvColomboName.setTextColor(getResources().getColor(R.color.text_dim, null));
            tvColomboStatus.setTextColor(getResources().getColor(R.color.text_muted, null));
        }
    }

    private void updateStrength(String password) {
        if (password.isEmpty()) {
            strengthRow.setVisibility(View.GONE);
            return;
        }
        strengthRow.setVisibility(View.VISIBLE);
        int score = 0;
        if (password.length() >= 8) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[0-9].*")) score++;
        if (password.matches(".*[^A-Za-z0-9].*")) score++;

        int[] colors = {
            Color.parseColor("#EF4444"),  // 0 - too short
            Color.parseColor("#EF4444"),  // 1 - weak
            Color.parseColor("#F59E0B"),  // 2 - fair
            Color.parseColor("#3B82F6"),  // 3 - good
            Color.parseColor("#10B981"),  // 4 - strong
        };
        String[] labels = {"Too Short", "Weak", "Fair", "Good", "Strong"};

        int color = colors[score];
        tvStrength.setText(labels[score]);
        tvStrength.setTextColor(color);

        View[] segs = {seg1, seg2, seg3, seg4};
        for (int i = 0; i < 4; i++) {
            segs[i].setBackgroundColor(i < score ? color : Color.parseColor("#E2E8F0"));
        }
    }

    private void handleRegister() {
        String fullName  = etFullName.getText().toString().trim();
        String phone     = etPhone.getText().toString().trim();
        String email     = etEmail.getText().toString().trim();
        String password  = etPassword.getText().toString().trim();
        String confirm   = etConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all required fields.");
            return;
        }
        if (!password.equals(confirm)) {
            showError("Passwords do not match.");
            return;
        }
        if (password.length() < 6) {
            showError("Password must be at least 6 characters.");
            return;
        }

        hideError();
        findViewById(R.id.btnRegister).setEnabled(false);

        FirebaseAuthHelper.register(fullName, phone, email, password, selectedBranch, selectedRole,
                new FirebaseAuthHelper.AuthCallback() {
                    @Override
                    public void onSuccess(User user) {
                        session.saveSession(user.getUid(), user.getFullName(), user.getEmail(),
                                user.getPhone(), user.getBranch(), user.getRole());
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(String error) {
                        findViewById(R.id.btnRegister).setEnabled(true);
                        showError(error != null ? error : "Registration failed. Please try again.");
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
