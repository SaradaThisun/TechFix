package com.example.techtrack.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.techtrack.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class BranchesActivity extends AppCompatActivity {

    private TextView tabColombo, tabGalle;
    private TextView tvBranchName, tvRating, tvTechCount, tvStockLevel, tvAddress, tvHours, tvDistance;
    private TextView tvCallMainText, tvCallAltText;
    private LinearLayout btnCallMain, btnCallAlt;
    private TextView btnDirections, btnBookBranch;

    private int selectedIndex = 0;

    private final String[] names = {"Colombo Branch", "Galle Branch"};
    private final String[] ratings = {"4.9 (384 reviews)", "4.8 (219 reviews)"};
    private final String[] techCounts = {"8", "5"};
    private final String[] stockLevels = {"High (98%)", "Optimal (94%)"};
    private final boolean[] partsInStock = {true, true}; // toggle to false to test fallback logic
    private final String[] addresses = {
            "TechFix Tower, Liberty Plaza, Colombo 03",
            "TechFix Hub, Gamini Building, Galle"
    };
    private final String[] hours = {
            "Mon - Sat: 8:30 AM - 7:30 PM\nSunday: 9:00 AM - 3:00 PM",
            "Mon - Sat: 9:00 AM - 7:00 PM\nSunday: 9:30 AM - 2:30 PM"
    };
    private final String[] mainPhones = {"+94 11 259 8870", "+94 91 224 5590"};
    private final String[] altPhones = {"+94 77 123 4567", "+94 71 889 9120"};
    private final String[] mapsUrls = {
            "https://maps.google.com/?q=Liberty+Plaza+Colombo",
            "https://maps.google.com/?q=Galle+Fort+Sri+Lanka"
    };
    private final double[] branchLat = {6.9147, 6.0367};
    private final double[] branchLng = {79.8482, 80.2170};
    private FusedLocationProviderClient fusedLocationClient;
    private double userLat = 0;
    private double userLng = 0;
    private boolean hasUserLocation = false;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    fetchLocation();
                } else {
                    tvDistance.setText("Location permission denied");
                    Toast.makeText(this, "Enable location permission to see distance to branches", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);

        tabColombo = findViewById(R.id.tabColombo);
        tabGalle = findViewById(R.id.tabGalle);
        tvBranchName = findViewById(R.id.tvBranchName);
        tvRating = findViewById(R.id.tvRating);
        tvTechCount = findViewById(R.id.tvTechCount);
        tvStockLevel = findViewById(R.id.tvStockLevel);
        tvAddress = findViewById(R.id.tvAddress);
        tvHours = findViewById(R.id.tvHours);
        tvDistance = findViewById(R.id.tvDistance);
        tvCallMainText = findViewById(R.id.tvCallMainText);
        tvCallAltText = findViewById(R.id.tvCallAltText);
        btnCallMain = findViewById(R.id.btnCallMain);
        btnCallAlt = findViewById(R.id.btnCallAlt);
        btnDirections = findViewById(R.id.btnDirections);
        btnBookBranch = findViewById(R.id.btnBookBranch);

        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navServices = findViewById(R.id.navServices);
        LinearLayout navBooking = findViewById(R.id.navBooking);
        LinearLayout navTracking = findViewById(R.id.navTracking);
        LinearLayout navBranches = findViewById(R.id.navBranches);
        LinearLayout navHistory = findViewById(R.id.navHistory);

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(BranchesActivity.this, HomeActivity.class));
            finish();
        });
        navServices.setOnClickListener(v -> Toast.makeText(this, "Services / Catalog screen coming soon", Toast.LENGTH_SHORT).show());
        navBooking.setOnClickListener(v -> Toast.makeText(this, "Booking screen coming soon", Toast.LENGTH_SHORT).show());
        navTracking.setOnClickListener(v -> Toast.makeText(this, "Tracking screen coming soon", Toast.LENGTH_SHORT).show());
        navBranches.setOnClickListener(v -> {}); // already here
        navHistory.setOnClickListener(v -> Toast.makeText(this, "History screen coming soon", Toast.LENGTH_SHORT).show());

        tabColombo.setOnClickListener(v -> selectBranch(0));
        tabGalle.setOnClickListener(v -> selectBranch(1));

        btnCallMain.setOnClickListener(v -> callNumber(mainPhones[selectedIndex]));
        btnCallAlt.setOnClickListener(v -> callNumber(altPhones[selectedIndex]));
        btnDirections.setOnClickListener(v -> openDirections(mapsUrls[selectedIndex]));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        updateUI();
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fetchLocation();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        tvDistance.setText("Getting your location...");
        fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                new com.google.android.gms.tasks.CancellationTokenSource().getToken()
        ).addOnSuccessListener(location -> {
            if (location != null) {
                userLat = location.getLatitude();
                userLng = location.getLongitude();
                hasUserLocation = true;
                updateDistanceDisplay();
                autoSelectNearestBranch();
            } else {
                tvDistance.setText("Location unavailable (check emulator GPS is set)");
            }
        }).addOnFailureListener(e -> tvDistance.setText("Could not get location"));
    }

    private void autoSelectNearestBranch() {
        float distColombo = distanceToBranchKm(0);
        float distGalle = distanceToBranchKm(1);

        boolean colomboAvailable = Integer.parseInt(techCounts[0]) > 0 && partsInStock[0];
        boolean galleAvailable = Integer.parseInt(techCounts[1]) > 0 && partsInStock[1];

        if (colomboAvailable && galleAvailable) {
            // Both fully available -> just pick whichever is physically closer
            selectedIndex = distColombo <= distGalle ? 0 : 1;
        } else if (colomboAvailable) {
            selectedIndex = 0;
            Toast.makeText(this, "Galle Branch lacks technicians or parts right now — assigned to Colombo instead", Toast.LENGTH_LONG).show();
        } else if (galleAvailable) {
            selectedIndex = 1;
            Toast.makeText(this, "Colombo Branch lacks technicians or parts right now — assigned to Galle instead", Toast.LENGTH_LONG).show();
        } else {
            // Neither branch is fully ready -- fall back to nearest anyway
            selectedIndex = distColombo <= distGalle ? 0 : 1;
            Toast.makeText(this, "No branches currently have full technician/parts availability", Toast.LENGTH_LONG).show();
        }

        updateUI();
    }

    private float distanceToBranchKm(int branchIndex) {
        float[] results = new float[1];
        Location.distanceBetween(userLat, userLng, branchLat[branchIndex], branchLng[branchIndex], results);
        return results[0] / 1000f;
    }

    private void updateDistanceDisplay() {
        if (!hasUserLocation) return;
        float distanceKm = distanceToBranchKm(selectedIndex);
        tvDistance.setText(String.format("%.1f km away from you", distanceKm));
    }

    private void selectBranch(int index) {
        selectedIndex = index;
        updateUI();
    }

    private void updateUI() {
        if (selectedIndex == 0) {
            tabColombo.setBackgroundResource(R.drawable.tab_active_bg);
            tabColombo.setTextColor(getColor(R.color.white));
            tabGalle.setBackgroundResource(R.drawable.tab_inactive_bg);
            tabGalle.setTextColor(getColor(R.color.text_dim));
        } else {
            tabGalle.setBackgroundResource(R.drawable.tab_active_bg);
            tabGalle.setTextColor(getColor(R.color.white));
            tabColombo.setBackgroundResource(R.drawable.tab_inactive_bg);
            tabColombo.setTextColor(getColor(R.color.text_dim));
        }

        tvBranchName.setText(names[selectedIndex]);
        tvRating.setText(ratings[selectedIndex]);
        tvTechCount.setText(techCounts[selectedIndex]);
        tvStockLevel.setText(stockLevels[selectedIndex]);
        tvAddress.setText(addresses[selectedIndex]);
        tvHours.setText(hours[selectedIndex]);
        tvCallMainText.setText(mainPhones[selectedIndex]);
        tvCallAltText.setText(altPhones[selectedIndex]);

        if (hasUserLocation) {
            updateDistanceDisplay();
        }
    }

    private void callNumber(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    private void openDirections(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
