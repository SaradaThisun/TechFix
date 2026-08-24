package com.example.techtrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techtrack.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LinearLayout btnQuickBooking = findViewById(R.id.btnQuickBooking);
        LinearLayout btnQuickTracking = findViewById(R.id.btnQuickTracking);
        LinearLayout btnQuickBranches = findViewById(R.id.btnQuickBranches);
        LinearLayout btnQuickServices = findViewById(R.id.btnQuickServices);
        LinearLayout ticketCard = findViewById(R.id.ticketCard);
        LinearLayout emergencyBanner = findViewById(R.id.emergencyBanner);
        TextView tvViewDetails = findViewById(R.id.tvViewDetails);
        TextView tvBranchPill = findViewById(R.id.tvBranchPill);
        ImageView ivBell = findViewById(R.id.ivBell);

        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navServices = findViewById(R.id.navServices);
        LinearLayout navBooking = findViewById(R.id.navBooking);
        LinearLayout navTracking = findViewById(R.id.navTracking);
        LinearLayout navBranches = findViewById(R.id.navBranches);
        LinearLayout navHistory = findViewById(R.id.navHistory);

        btnQuickBooking.setOnClickListener(v -> showComingSoon("Booking"));
        btnQuickTracking.setOnClickListener(v -> showComingSoon("Tracking"));
        btnQuickBranches.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, BranchesActivity.class)));
        btnQuickServices.setOnClickListener(v -> showComingSoon("Services / Catalog"));
        ticketCard.setOnClickListener(v -> showComingSoon("Tracking"));
        tvViewDetails.setOnClickListener(v -> showComingSoon("Tracking"));
        emergencyBanner.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, BranchesActivity.class)));
        tvBranchPill.setOnClickListener(v ->
                Toast.makeText(this, "Branch switching coming soon", Toast.LENGTH_SHORT).show());
        ivBell.setOnClickListener(v ->
                Toast.makeText(this, "Notifications coming soon", Toast.LENGTH_SHORT).show());

        navHome.setOnClickListener(v -> {});
        navServices.setOnClickListener(v -> showComingSoon("Services / Catalog"));
        navBooking.setOnClickListener(v -> showComingSoon("Booking"));
        navTracking.setOnClickListener(v -> showComingSoon("Tracking"));
        navBranches.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, BranchesActivity.class)));
        navHistory.setOnClickListener(v -> showComingSoon("History"));
    }

    private void showComingSoon(String screenName) {
        Toast.makeText(this, screenName + " screen coming soon", Toast.LENGTH_SHORT).show();
    }
}