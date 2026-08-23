package com.techfix.app.ui.branches;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techfix.app.R;
import com.techfix.app.data.api.ApiService;
import com.techfix.app.data.api.RetrofitClient;
import com.techfix.app.data.model.Branch;
import com.techfix.app.utils.LocationHelper;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BranchesFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_CODE = 100;

    private GoogleMap googleMap;
    private TextView tvNearestBranch;
    private View progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_branches, container, false);
        tvNearestBranch = view.findViewById(R.id.tvNearestBranch);
        progressBar = view.findViewById(R.id.progressBar);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        if (hasLocationPermission()) {
            googleMap.setMyLocationEnabled(true);
            loadNearbyBranches();
        } else {
            requestLocationPermission();
        }
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            }
            loadNearbyBranches();
        } else {
            loadAllBranches();
        }
    }

    private void loadNearbyBranches() {
        progressBar.setVisibility(View.VISIBLE);
        LocationHelper.getCurrentLocation(requireContext(), location -> {
            if (location != null) {
                fetchNearby(location);
            } else {
                loadAllBranches();
            }
        });
    }

    private void fetchNearby(Location location) {
        ApiService api = RetrofitClient.getInstance(requireContext());
        api.getNearbyBranches(location.getLatitude(), location.getLongitude())
                .enqueue(new Callback<List<Branch>>() {
                    @Override
                    public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            showBranchesOnMap(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Branch>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Could not load branches: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadAllBranches() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService api = RetrofitClient.getInstance(requireContext());
        api.getBranches().enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    showBranchesOnMap(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Could not load branches: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showBranchesOnMap(List<Branch> branches) {
        if (googleMap == null || branches.isEmpty()) return;
        googleMap.clear();

        for (Branch b : branches) {
            LatLng pos = new LatLng(b.getLatitude(), b.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(b.getName())
                    .snippet(b.getAddress()));
        }

        Branch first = branches.get(0);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(first.getLatitude(), first.getLongitude()), 9f));

        if (first.getDistanceKm() != null) {
            tvNearestBranch.setVisibility(View.VISIBLE);
            tvNearestBranch.setText(String.format(Locale.getDefault(),
                    "Nearest: %s (%.1f km away)", first.getName(), first.getDistanceKm()));
        }
    }
}