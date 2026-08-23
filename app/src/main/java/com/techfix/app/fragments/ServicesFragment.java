package com.techfix.app.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.activities.MainActivity;
import com.techfix.app.adapters.ServicesAdapter;
import com.techfix.app.models.RepairService;
import com.techfix.app.utils.MockData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServicesFragment extends Fragment {

    private static final String[] CATEGORIES = {
            "All", "Mobile", "Computers", "Screen Replace", "Battery", "Audio/Port", "Board Level"
    };

    private RecyclerView rvServices;
    private ServicesAdapter adapter;
    private List<RepairService> allServices;
    private List<RepairService> filtered;
    private String activeCategory = "All";
    private String searchQuery = "";
    private LinearLayout categoryChips;
    private TextView tvCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allServices = MockData.getRepairServices();
        filtered    = new ArrayList<>(allServices);

        tvCount     = view.findViewById(R.id.tvServicesCount);
        rvServices  = view.findViewById(R.id.rvServices);
        categoryChips = view.findViewById(R.id.categoryChips);

        tvCount.setText(allServices.size() + " services available");

        // RecyclerView
        adapter = new ServicesAdapter(requireContext(), filtered, service -> {
            Bundle args = new Bundle();
            args.putString("service_title", service.getTitle());
            args.putString("service_category", service.getCategory());
            args.putString("device_type", service.getDeviceType());
            args.putString("service_description", service.getDescription());
            ((MainActivity) requireActivity()).navigateTo("Book", args);
        });
        rvServices.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvServices.setAdapter(adapter);

        // Category chips
        buildChips();

        // Search
        EditText etSearch = view.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().trim().toLowerCase();
                applyFilters();
            }
        });

        TextView btnClear = view.findViewById(R.id.btnClearSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
        btnClear.setOnClickListener(v -> etSearch.setText(""));
    }

    private void buildChips() {
        categoryChips.removeAllViews();
        for (String cat : CATEGORIES) {
            TextView chip = new TextView(requireContext());
            chip.setText(cat);
            chip.setTextSize(13f);
            chip.setPadding(36, 18, 36, 18);
            chip.setClickable(true);

            boolean active = cat.equals(activeCategory);
            chip.setBackgroundResource(active ? R.drawable.bg_btn_primary : R.drawable.bg_pill_surface);
            chip.setTextColor(active ? Color.WHITE : getResources().getColor(R.color.text_dim, null));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(8);
            chip.setLayoutParams(lp);

            chip.setOnClickListener(v -> {
                activeCategory = cat;
                buildChips();
                applyFilters();
            });
            categoryChips.addView(chip);
        }
    }

    private void applyFilters() {
        filtered.clear();
        for (RepairService s : allServices) {
            boolean matchCat = "All".equals(activeCategory);
            if (!matchCat) {
                if ("Mobile".equals(activeCategory)) {
                    matchCat = "mobile".equals(s.getDeviceType());
                } else if ("Computers".equals(activeCategory)) {
                    matchCat = "computer".equals(s.getDeviceType());
                } else {
                    matchCat = s.getCategory().equals(activeCategory);
                }
            }

            boolean matchSearch = searchQuery.isEmpty()
                    || s.getTitle().toLowerCase().contains(searchQuery)
                    || s.getDescription().toLowerCase().contains(searchQuery)
                    || s.getCategory().toLowerCase().contains(searchQuery);
            
            if (matchCat && matchSearch) filtered.add(s);
        }
        adapter.notifyDataSetChanged();
        tvCount.setText(filtered.size() + " services found");
    }
}
