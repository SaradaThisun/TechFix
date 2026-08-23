package com.techfix.app.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.activities.MainActivity;
import com.techfix.app.adapters.HistoryAdapter;
import com.techfix.app.database.AppDatabase;
import com.techfix.app.firebase.FirebaseDbHelper;
import com.techfix.app.models.HistoryItem;
import com.techfix.app.models.RepairTicket;
import com.techfix.app.utils.MockData;
import com.techfix.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView rvHistory;
    private HistoryAdapter adapter;
    private List<HistoryItem> allItems = new ArrayList<>();
    private List<HistoryItem> filtered = new ArrayList<>();
    private String activeFilter = "All";
    private TextView filterAll, filterCompleted, filterCanceled, tvRecordCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager session = ((MainActivity) requireActivity()).getSession();
        
        tvRecordCount   = view.findViewById(R.id.tvRecordCount);
        rvHistory       = view.findViewById(R.id.rvHistory);
        filterAll       = view.findViewById(R.id.filterAll);
        filterCompleted = view.findViewById(R.id.filterCompleted);
        filterCanceled  = view.findViewById(R.id.filterCanceled);

        adapter = new HistoryAdapter(requireContext(), filtered, item -> {
            // Re-book: navigate to booking
            ((MainActivity) requireActivity()).navigateTo("Book");
        });
        rvHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvHistory.setAdapter(adapter);

        filterAll.setOnClickListener(v       -> applyFilter("All"));
        filterCompleted.setOnClickListener(v -> applyFilter("Completed"));
        filterCanceled.setOnClickListener(v  -> applyFilter("Canceled"));

        loadRealData(session.getUid());
        observeLocalData(session.getUid());
    }

    private void observeLocalData(String userId) {
        AppDatabase.getInstance(requireContext()).historyDao().getHistoryByUser(userId)
                .observe(getViewLifecycleOwner(), items -> {
                    if (items != null && !items.isEmpty()) {
                        // Merge local items with Firebase items, prioritizing local/latest
                        for (HistoryItem localItem : items) {
                            boolean exists = false;
                            for (int i = 0; i < allItems.size(); i++) {
                                if (allItems.get(i).getId().equals(localItem.getId())) {
                                    allItems.set(i, localItem);
                                    exists = true;
                                    break;
                                }
                            }
                            if (!exists) {
                                allItems.add(0, localItem);
                            }
                        }
                        applyFilter(activeFilter);
                    }
                });
    }

    private void loadRealData(String userId) {
        FirebaseDbHelper.fetchTicketsForUser(userId, new FirebaseDbHelper.DataCallback<List<RepairTicket>>() {
            @Override
            public void onSuccess(List<RepairTicket> data) {
                if (isAdded()) {
                    allItems.clear();
                    if (data != null) {
                        for (RepairTicket t : data) {
                            if ("Completed".equals(t.getStatus()) || "Canceled".equals(t.getStatus())) {
                                HistoryItem mapped = mapToHistory(t);
                                boolean exists = false;
                                for (int i = 0; i < allItems.size(); i++) {
                                    if (allItems.get(i).getId().equals(mapped.getId())) {
                                        exists = true;
                                        break;
                                    }
                                }
                                if (!exists) allItems.add(mapped);
                            }
                        }
                    }
                    // Also check for explicit history items in Firebase
                    FirebaseDbHelper.fetchHistoryForUser(userId, new FirebaseDbHelper.DataCallback<List<HistoryItem>>() {
                        @Override
                        public void onSuccess(List<HistoryItem> historyData) {
                            if (historyData != null) {
                                for (HistoryItem h : historyData) {
                                    boolean exists = false;
                                    for (int i = 0; i < allItems.size(); i++) {
                                        if (allItems.get(i).getId().equals(h.getId())) {
                                            exists = true;
                                            break;
                                        }
                                    }
                                    if (!exists) allItems.add(h);
                                }
                            }
                            applyFilter(activeFilter);
                        }
                        @Override public void onFailure(String error) {}
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded()) {
                    allItems.addAll(MockData.getHistoryItems(userId));
                    applyFilter("All");
                }
            }
        });
    }

    private HistoryItem mapToHistory(RepairTicket t) {
        HistoryItem h = new HistoryItem();
        h.setId("hist-" + t.getId());
        h.setReferenceId(t.getId());
        h.setDeviceName(t.getDeviceModel());
        h.setDeviceType(t.getDeviceType());
        h.setRepairDate(t.getCreatedAt());
        h.setServiceSummary(t.getIssue());
        h.setBranch(t.getBranch());
        h.setTotalCostLKR(t.getTotalCostLKR());
        h.setStatus(t.getStatus());
        h.setInvoiceNumber("INV-" + t.getId().replace("#", ""));
        return h;
    }

    private void applyFilter(String filter) {
        activeFilter = filter;

        // Update chip styles
        int accent = Color.parseColor("#0066FF");
        int dim    = requireContext().getColor(R.color.text_dim);
        filterAll.setBackgroundResource("All".equals(filter)
                ? R.drawable.bg_btn_primary : R.drawable.bg_pill_surface);
        filterAll.setTextColor("All".equals(filter) ? Color.WHITE : dim);

        filterCompleted.setBackgroundResource("Completed".equals(filter)
                ? R.drawable.bg_btn_primary : R.drawable.bg_pill_surface);
        filterCompleted.setTextColor("Completed".equals(filter) ? Color.WHITE : dim);

        filterCanceled.setBackgroundResource("Canceled".equals(filter)
                ? R.drawable.bg_btn_primary : R.drawable.bg_pill_surface);
        filterCanceled.setTextColor("Canceled".equals(filter) ? Color.WHITE : dim);

        filtered.clear();
        for (HistoryItem item : allItems) {
            if ("All".equals(filter) || item.getStatus().equals(filter)) {
                filtered.add(item);
            }
        }
        adapter.notifyDataSetChanged();
        tvRecordCount.setText(filtered.size() + " records found");
    }
}
