package com.techfix.app.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techfix.app.R;
import com.techfix.app.activities.MainActivity;
import com.techfix.app.models.Branch;
import com.techfix.app.utils.MockData;

import java.util.List;

public class BranchesFragment extends Fragment {

    private static final String TAG = "BranchesFragment";

    private List<Branch> branches;
    private int selectedIndex = 0;
    private TextView tabColombo, tabGalle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_branches, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        branches = MockData.getBranches();

        tabColombo = view.findViewById(R.id.tabColombo);
        tabGalle   = view.findViewById(R.id.tabGalle);

        tabColombo.setOnClickListener(v -> selectBranch(0));
        tabGalle.setOnClickListener(v   -> selectBranch(1));

        view.findViewById(R.id.mapInterface).setOnClickListener(v -> openGoogleMapsApp());

        view.findViewById(R.id.btnBookBranch).setOnClickListener(v -> {
            Branch b = branches.get(selectedIndex);
            Bundle args = new Bundle();
            args.putString("selected_branch", b.getName());
            ((MainActivity) requireActivity()).navigateTo("Book", args);
        });

        updateBranchCard(view);
    }

    private void selectBranch(int index) {
        selectedIndex = index;

        tabColombo.setBackgroundResource(index == 0 ? R.drawable.bg_btn_primary : R.drawable.bg_pill_surface);
        tabColombo.setTextColor(index == 0 ? Color.WHITE : requireContext().getColor(R.color.text_dim));
        tabGalle.setBackgroundResource(index == 1 ? R.drawable.bg_btn_primary : R.drawable.bg_pill_surface);
        tabGalle.setTextColor(index == 1 ? Color.WHITE : requireContext().getColor(R.color.text_dim));

        if (getView() != null) updateBranchCard(getView());
    }

    private void updateBranchCard(View v) {
        Branch b = branches.get(selectedIndex);

        ((TextView) v.findViewById(R.id.tvBranchName)).setText(b.getName());
        ((TextView) v.findViewById(R.id.tvRating)).setText(
                "⭐ " + b.getRating() + "  (" + b.getReviewsCount() + " reviews)");
        ((TextView) v.findViewById(R.id.tvTechCount)).setText(
                String.valueOf(b.getTechniciansAvailable()));
        ((TextView) v.findViewById(R.id.tvStockLevel)).setText(b.getSparePartsStockLevel());
        ((TextView) v.findViewById(R.id.tvAddress)).setText(b.getAddress());
        ((TextView) v.findViewById(R.id.tvHours)).setText(b.getHours());
        ((TextView) v.findViewById(R.id.tvWeekendHours)).setText(b.getWeekendHours());
        ((TextView) v.findViewById(R.id.tvPhone1)).setText(b.getPhone());
        ((TextView) v.findViewById(R.id.tvPhone2)).setText(b.getAltPhone());

        v.findViewById(R.id.btnPhone1).setOnClickListener(click -> call(b.getPhone()));
        v.findViewById(R.id.btnPhone2).setOnClickListener(click -> call(b.getAltPhone()));

        // Popular services chips
        FlexboxLayout container = v.findViewById(R.id.popularServicesContainer);
        container.removeAllViews();
        if (b.getPopularServices() != null) {
            for (String svc : b.getPopularServices()) {
                TextView chip = new TextView(requireContext());
                chip.setText(svc);
                chip.setTextSize(11f);
                chip.setTextColor(requireContext().getColor(R.color.text_dim));
                chip.setBackgroundResource(R.drawable.bg_pill_surface);
                chip.setPadding(20, 8, 20, 8);
                FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(
                        FlexboxLayout.LayoutParams.WRAP_CONTENT,
                        FlexboxLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 8, 8);
                chip.setLayoutParams(lp);
                container.addView(chip);
            }
        }
    }

    private void call(String phone) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
    }

    private void openGoogleMapsApp() {
        Branch b = branches.get(selectedIndex);
        // Using "google.navigation" intent to open the device's Google Maps app with directions
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + b.getLatitude() + "," + b.getLongitude());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        
        if (mapIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            // Fallback: Open in browser if Google Maps app is not installed
            Uri browserUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + b.getLatitude() + "," + b.getLongitude());
            startActivity(new Intent(Intent.ACTION_VIEW, browserUri));
        }
    }
}
