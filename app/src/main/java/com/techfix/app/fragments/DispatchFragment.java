package com.techfix.app.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.activities.MainActivity;
import com.techfix.app.adapters.DispatchAdapter;
import com.techfix.app.firebase.FirebaseDbHelper;
import com.techfix.app.models.DispatchRequest;
import com.techfix.app.models.RepairTicket;
import com.techfix.app.utils.FormatUtils;
import com.techfix.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class DispatchFragment extends Fragment {

    private RecyclerView rvRequests;
    private DispatchAdapter adapter;
    private List<RepairTicket> allTickets = new ArrayList<>();
    private List<DispatchRequest> filtered = new ArrayList<>();
    private String activeBranch = "Colombo Branch";
    private String urgencyFilter = "All";
    private String searchQuery = "";
    private TextView tabColombo, tabGalle, urgencyAll, urgencyUrgent, urgencyStandard;
    private TextView tvPending, tvDispatched;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dispatch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager session = ((MainActivity) requireActivity()).getSession();
        TextView tvAvatar = view.findViewById(R.id.tvAvatar);
        if (tvAvatar != null) tvAvatar.setText(FormatUtils.getInitial(session.getFullName()));
        
        view.findViewById(R.id.profileArea).setOnClickListener(v -> 
                ((MainActivity) requireActivity()).showProfileDialog());

        tvPending    = view.findViewById(R.id.tvPendingCount);
        tvDispatched = view.findViewById(R.id.tvDispatchedCount);
        tabColombo   = view.findViewById(R.id.tabColombo);
        tabGalle     = view.findViewById(R.id.tabGalle);
        urgencyAll     = view.findViewById(R.id.urgencyAll);
        urgencyUrgent  = view.findViewById(R.id.urgencyUrgent);
        urgencyStandard = view.findViewById(R.id.urgencyStandard);
        rvRequests   = view.findViewById(R.id.rvRequests);

        adapter = new DispatchAdapter(requireContext(), filtered,
                new DispatchAdapter.DispatchListener() {
                    @Override public void onAutoAssign(DispatchRequest req) { autoAssign(req); }
                    @Override public void onAssignTech(DispatchRequest req) { showTechPicker(req); }
                    @Override public void onCallCustomer(DispatchRequest req) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + req.getCustomerPhone())));
                    }
                });
        rvRequests.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvRequests.setAdapter(adapter);

        tabColombo.setOnClickListener(v -> selectBranch("Colombo Branch"));
        tabGalle.setOnClickListener(v   -> selectBranch("Galle Branch"));

        urgencyAll.setOnClickListener(v     -> selectUrgency("All"));
        urgencyUrgent.setOnClickListener(v  -> selectUrgency("Urgent"));
        urgencyStandard.setOnClickListener(v-> selectUrgency("Standard"));

        EditText etSearch = view.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                searchQuery = s.toString().toLowerCase();
                applyFilters();
            }
        });

        loadRealData();
    }

    private void loadRealData() {
        FirebaseDbHelper.fetchAllTickets(new FirebaseDbHelper.DataCallback<List<RepairTicket>>() {
            @Override
            public void onSuccess(List<RepairTicket> data) {
                if (isAdded()) {
                    allTickets = data != null ? data : new ArrayList<>();
                    applyFilters();
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error fetching data: " + error, Toast.LENGTH_SHORT).show();
                    allTickets = new ArrayList<>();
                    applyFilters();
                }
            }
        });
    }


    private void selectBranch(String branch) {
        activeBranch = branch;
        tabColombo.setBackgroundResource("Colombo Branch".equals(branch)
                ? R.drawable.bg_btn_primary : R.drawable.bg_pill_surface);
        tabColombo.setTextColor("Colombo Branch".equals(branch) ? Color.WHITE
                : requireContext().getColor(R.color.text_dim));
        tabGalle.setBackgroundResource("Galle Branch".equals(branch)
                ? R.drawable.bg_btn_primary : R.drawable.bg_pill_surface);
        tabGalle.setTextColor("Galle Branch".equals(branch) ? Color.WHITE
                : requireContext().getColor(R.color.text_dim));
        applyFilters();
    }

    private void selectUrgency(String urgency) {
        urgencyFilter = urgency;
        for (TextView tv : new TextView[]{urgencyAll, urgencyUrgent, urgencyStandard}) {
            String label = tv.getText().toString();
            boolean active = label.equals(urgency);
            tv.setBackgroundResource(active ? R.drawable.bg_pill_accent : R.drawable.bg_pill_surface);
            tv.setTextColor(active ? Color.parseColor("#0066FF")
                    : requireContext().getColor(R.color.text_dim));
            tv.setTypeface(null, active ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        }
        applyFilters();
    }

    private void applyFilters() {
        filtered.clear();
        int pending = 0, done = 0;

        for (RepairTicket t : allTickets) {
            // Map to UI model
            DispatchRequest d = new DispatchRequest();
            d.setId(t.getId());
            d.setCustomerName(t.getCustomerName() != null ? t.getCustomerName() : "Customer");
            d.setCustomerPhone(t.getCustomerPhone() != null ? t.getCustomerPhone() : "");
            d.setCustomerLocation("Branch Drop-off");
            d.setDeviceType(t.getDeviceType());
            d.setDeviceModel(t.getDeviceModel());
            d.setIssueSummary(t.getIssue());
            d.setUrgency("Standard");
            d.setSubmittedTime(t.getCreatedAt());
            d.setAssignedBranch(t.getBranch());
            d.setAssignedTech(t.getTechnicianName());
            d.setAutoMatchBranch(t.getBranch());
            d.setPartInStock(true);
            d.setTechniciansAvailable(5);
            d.setDevicePhoto(t.getDevicePhoto());

            if ("Request Received".equals(t.getStatus())) {
                d.setStatus("Pending Dispatch");
                pending++;
            } else {
                d.setStatus("Dispatched");
                done++;
            }

            // Apply Branch & Search & Urgency filters
            boolean matchBranch = t.getBranch().equals(activeBranch);
            boolean matchUrgency = "All".equals(urgencyFilter) || d.getUrgency().equals(urgencyFilter);
            boolean matchSearch = searchQuery.isEmpty()
                    || d.getDeviceModel().toLowerCase().contains(searchQuery)
                    || d.getCustomerName().toLowerCase().contains(searchQuery);

            if (matchBranch && matchUrgency && matchSearch) {
                filtered.add(d);
            }
        }

        adapter.notifyDataSetChanged();
        if (tvPending != null) tvPending.setText(String.valueOf(pending));
        if (tvDispatched != null) tvDispatched.setText(String.valueOf(done));
    }

    private void autoAssign(DispatchRequest req) {
        updateTicketInFirebase(req.getId(), "Assigned to Technician", 
                req.getAutoMatchBranch(), "Kasun Weerasinghe", "Senior Technician", "0771234567", 4.8f);
    }


    private void showTechPicker(DispatchRequest req) {
        String[] techs = {"Kasun Weerasinghe", "Amara Dissanayake", "Rohan Jayawardena"};
        String[] roles = {"Senior Technician", "Hardware Specialist", "Repair Lead"};
        String[] phones = {"0771234567", "0777654321", "0712233445"};
        float[] ratings = {4.8f, 4.5f, 4.9f};

        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Assign Technician")
                .setItems(techs, (dialog, which) -> {
                    updateTicketInFirebase(req.getId(), "Assigned to Technician", 
                            activeBranch, techs[which], roles[which], phones[which], ratings[which]);
                }).show();
    }

    private void updateTicketInFirebase(String ticketId, String status, String branch, String tech, 
                                        String role, String phone, float rating) {
        // Find local ticket first
        RepairTicket target = null;
        for (RepairTicket t : allTickets) {
            if (t.getId().equals(ticketId)) {
                target = t;
                break;
            }
        }

        if (target == null) return;

        target.setStatus(status);
        target.setBranch(branch);
        target.setTechnicianName(tech);
        target.setTechnicianRole(role);
        target.setTechnicianPhone(phone);
        target.setTechnicianRating(rating);
        target.setProgressPercent(25);
        target.setCurrentStepIndex(1); // Moving to Diagnostics (Step 2)

        // Update timeline
        if (target.getTimelineSteps() != null) {
            String now = new java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(new java.util.Date());
            for (int i = 0; i < target.getTimelineSteps().size(); i++) {
                RepairTicket.TimelineStep step = target.getTimelineSteps().get(i);
                if (i == 0) {
                    step.isCompleted = true;
                    step.isCurrent = false;
                } else if (i == 1) {
                    step.isCompleted = false;
                    step.isCurrent = true;
                    step.timestamp = now;
                }
            }
        }

        // Add Status Log
        if (target.getStatusLogs() == null) target.setStatusLogs(new ArrayList<>());
        target.getStatusLogs().add(0, new RepairTicket.StatusLogEntry(
                "log_" + System.currentTimeMillis(),
                "Technician Assigned",
                tech + " has been assigned to your repair at " + branch + ".",
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(new java.util.Date()),
                "System",
                "success"
        ));

        FirebaseDbHelper.saveTicket(target, new FirebaseDbHelper.DataCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "✓ Technician Assigned", Toast.LENGTH_SHORT).show();
                    loadRealData(); // Refresh list
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Failed to update: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
