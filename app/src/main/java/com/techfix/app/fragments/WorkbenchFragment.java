package com.techfix.app.fragments;

import android.graphics.Color;
import android.os.Bundle;
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
import com.techfix.app.adapters.TechJobAdapter;
import com.techfix.app.firebase.FirebaseDbHelper;
import com.techfix.app.models.RepairTicket;
import com.techfix.app.models.SparePart;
import com.techfix.app.utils.FormatUtils;
import com.techfix.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchFragment extends Fragment {

    private RecyclerView rvJobs;
    private TechJobAdapter adapter;
    private List<RepairTicket> allTickets = new ArrayList<>();
    private List<RepairTicket> filtered = new ArrayList<>();
    private String priorityFilter = "All";
    private TextView filterAll, filterUrgent, filterStandard;
    private TextView tvActive;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workbench, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager session = ((MainActivity) requireActivity()).getSession();
        TextView tvAvatar = view.findViewById(R.id.tvAvatar);
        TextView tvName   = view.findViewById(R.id.tvStaffName);
        TextView tvRole   = view.findViewById(R.id.tvStaffRole);

        if (tvAvatar != null) tvAvatar.setText(FormatUtils.getInitial(session.getFullName()));
        if (tvName != null) tvName.setText(session.getFullName());
        if (tvRole != null) tvRole.setText(session.getRole() + " · " + session.getBranch());

        view.findViewById(R.id.profileArea).setOnClickListener(v -> 
                ((MainActivity) requireActivity()).showProfileDialog());

        tvActive       = view.findViewById(R.id.tvActiveJobs);
        filterAll      = view.findViewById(R.id.filterAll);
        filterUrgent   = view.findViewById(R.id.filterUrgent);
        filterStandard = view.findViewById(R.id.filterStandard);
        rvJobs         = view.findViewById(R.id.rvJobs);

        adapter = new TechJobAdapter(requireContext(), filtered, (ticket, newStage) -> {
            updateStageInFirebase(ticket, newStage);
        });
        rvJobs.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvJobs.setAdapter(adapter);

        filterAll.setOnClickListener(v      -> selectFilter("All"));
        filterUrgent.setOnClickListener(v   -> selectFilter("Urgent"));
        filterStandard.setOnClickListener(v -> selectFilter("Standard"));

        loadRealData(session);
    }

    private void loadRealData(SessionManager session) {
        FirebaseDbHelper.fetchAllTickets(new FirebaseDbHelper.DataCallback<List<RepairTicket>>() {
            @Override
            public void onSuccess(List<RepairTicket> data) {
                if (isAdded()) {
                    allTickets.clear();
                    if (data != null) {
                        for (RepairTicket t : data) {
                            if (t.getUserId() == null || t.getUserId().isEmpty()) {
                                android.util.Log.e("Workbench", "Critical: Loaded ticket " + t.getId() + " with NULL userId");
                            }
                            
                            // Only show if assigned to this tech OR if it's in their branch and unassigned but needs repair
                            // Based on the user request, it should appear in Workbench after assignment.
                            if (session.getFullName().equals(t.getTechnicianName()) || 
                               (session.getBranch().equals(t.getBranch()) && !"Request Received".equals(t.getStatus()) && !"Completed".equals(t.getStatus()) && !"Canceled".equals(t.getStatus()))) {
                                allTickets.add(t);
                            }
                        }
                    }
                    applyFilters();
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                    allTickets.clear();
                    applyFilters();
                }
            }
        });
    }

    private void updateStageInFirebase(RepairTicket ticket, String stage) {
        if ("Ready for Pickup".equals(stage)) {
            showBillingDialog(ticket);
        } else if ("Awaiting Customer Approval".equals(stage)) {
            showDiagnosticDialog(ticket);
        } else {
            performStageUpdate(ticket, stage);
        }
    }

    private void showDiagnosticDialog(RepairTicket ticket) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_billing, null);
        EditText etServiceFee = dialogView.findViewById(R.id.etServiceFee);
        EditText etPartsFee = dialogView.findViewById(R.id.etPartsFee);
        
        // Pre-fill if already exists
        if (ticket.getServiceFeeLKR() > 0) etServiceFee.setText(String.valueOf(ticket.getServiceFeeLKR()));
        if (ticket.getPartsFeeLKR() > 0) etPartsFee.setText(String.valueOf(ticket.getPartsFeeLKR()));

        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Diagnostic Estimate: " + ticket.getId())
                .setView(dialogView)
                .setPositiveButton("Submit Estimate", (dialog, which) -> {
                    String sFee = etServiceFee.getText().toString().trim();
                    String pFee = etPartsFee.getText().toString().trim();
                    
                    long serviceFee = 0;
                    long partsFee = 0;
                    
                    try {
                        if (!sFee.isEmpty()) serviceFee = Long.parseLong(sFee);
                        if (!pFee.isEmpty()) partsFee = Long.parseLong(pFee);
                    } catch (NumberFormatException e) {
                        Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show();
                    }
                    
                    ticket.setServiceFeeLKR(serviceFee);
                    ticket.setPartsFeeLKR(partsFee);
                    ticket.setTotalCostLKR(serviceFee + partsFee);
                    
                    performStageUpdate(ticket, "Awaiting Customer Approval");
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showBillingDialog(RepairTicket ticket) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_billing, null);
        EditText etServiceFee = dialogView.findViewById(R.id.etServiceFee);
        EditText etPartsFee = dialogView.findViewById(R.id.etPartsFee);

        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Final Billing: " + ticket.getId())
                .setView(dialogView)
                .setPositiveButton("Confirm & Mark Ready", (dialog, which) -> {
                    String sFee = etServiceFee.getText().toString().trim();
                    String pFee = etPartsFee.getText().toString().trim();
                    
                    long serviceFee = 0;
                    long partsFee = 0;
                    
                    try {
                        if (!sFee.isEmpty()) serviceFee = Long.parseLong(sFee);
                        if (!pFee.isEmpty()) partsFee = Long.parseLong(pFee);
                    } catch (NumberFormatException e) {
                        Toast.makeText(requireContext(), "Invalid amount entered", Toast.LENGTH_SHORT).show();
                    }
                    
                    ticket.setServiceFeeLKR(serviceFee);
                    ticket.setPartsFeeLKR(partsFee);
                    ticket.setTotalCostLKR(serviceFee + partsFee);
                    
                    // Also prompt for part used to deduct from inventory
                    showPartDeductionDialog(ticket);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showPartDeductionDialog(RepairTicket ticket) {
        FirebaseDbHelper.fetchSpareParts(new FirebaseDbHelper.DataCallback<List<SparePart>>() {
            @Override
            public void onSuccess(List<SparePart> partsList) {
                if (isAdded()) {
                    if (partsList == null || partsList.isEmpty()) {
                        performStageUpdate(ticket, "Ready for Pickup");
                        return;
                    }

                    // Clean the list of any nulls
                    List<SparePart> parts = new ArrayList<>();
                    for (SparePart p : partsList) {
                        if (p != null && p.getId() != null) parts.add(p);
                    }

                    String[] partNames = new String[parts.size() + 1];
                    partNames[0] = "No parts used (Manual repair)";
                    for (int i = 0; i < parts.size(); i++) {
                        SparePart p = parts.get(i);
                        String name = p.getName();
                        String pid = p.getPartId() != null ? p.getPartId() : p.getId();
                        partNames[i+1] = (name != null && !name.isEmpty()) ? name : "Part: " + pid;
                    }

                    new android.app.AlertDialog.Builder(requireContext())
                            .setTitle("Inventory Sync: Select Part Used")
                            .setItems(partNames, (dialog, which) -> {
                                if (which > 0) {
                                    SparePart used = parts.get(which - 1);
                                    deductPart(used, ticket);
                                } else {
                                    performStageUpdate(ticket, "Ready for Pickup");
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            }

            @Override
            public void onFailure(String error) {
                performStageUpdate(ticket, "Ready for Pickup");
            }
        });
    }

    private void deductPart(SparePart part, RepairTicket ticket) {
        int colombo = part.getColomboStock();
        int galle = part.getGalleStock();
        
        String branch = (ticket.getBranch() != null) ? ticket.getBranch() : "Colombo Branch";
        if (branch.contains("Colombo")) {
            colombo = Math.max(0, colombo - 1);
        } else {
            galle = Math.max(0, galle - 1);
        }
        
        FirebaseDbHelper.updateSparePartStock(part.getId(), colombo, galle, new FirebaseDbHelper.DataCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                performStageUpdate(ticket, "Ready for Pickup");
            }

            @Override
            public void onFailure(String error) {
                performStageUpdate(ticket, "Ready for Pickup");
            }
        });
    }

    private void performStageUpdate(RepairTicket ticket, String stage) {
        if (ticket.getUserId() == null || ticket.getUserId().isEmpty()) {
            android.util.Log.e("Workbench", "Warning: userId is missing for ticket " + ticket.getId());
        }

        ticket.setStatus(stage);
        
        // Progress mapping
        int progress = 25;
        int stepIndex = 1; // Default to Assigned (Step 2)
        
        if ("Awaiting Customer Approval".equals(stage)) {
            progress = 35;
            stepIndex = 1;
        } else if ("Repair in Progress".equals(stage)) {
            progress = 50;
            stepIndex = 2;
        } else if ("Ready for Pickup".equals(stage)) {
            progress = 90;
            stepIndex = 3;
        } else if ("Completed".equals(stage)) {
            progress = 100;
            stepIndex = 3;
        } else if ("Canceled".equals(stage)) {
            progress = 0;
            stepIndex = 0;
        }
        
        ticket.setProgressPercent(progress);
        ticket.setCurrentStepIndex(stepIndex);

        // Update timeline
        String nowTime = new java.text.SimpleDateFormat("hh:mm a", java.util.Locale.US).format(new java.util.Date());
        if (ticket.getTimelineSteps() != null && !ticket.getTimelineSteps().isEmpty()) {
            for (int i = 0; i < ticket.getTimelineSteps().size(); i++) {
                RepairTicket.TimelineStep step = ticket.getTimelineSteps().get(i);
                if (i < stepIndex) {
                    step.isCompleted = true;
                    step.isCurrent = false;
                } else if (i == stepIndex) {
                    step.isCompleted = false;
                    step.isCurrent = true;
                    step.timestamp = nowTime;
                } else {
                    step.isCompleted = false;
                    step.isCurrent = false;
                }
            }
        }

        // Add to Status Logs for the customer to see
        if (ticket.getStatusLogs() == null) ticket.setStatusLogs(new ArrayList<>());
        ticket.getStatusLogs().add(0, new RepairTicket.StatusLogEntry(
                "log_" + System.currentTimeMillis(),
                "Stage Updated: " + stage,
                "The repair has moved to: " + stage,
                nowTime,
                ticket.getTechnicianName(),
                "progress"
        ));

        // Update ETA if starting repair
        if ("Repair in Progress".equals(stage) && "Pending Diagnostics".equals(ticket.getEstimatedCompletion())) {
            ticket.setEstimatedCompletion("2 - 4 Hours");
        }

        FirebaseDbHelper.saveTicket(ticket, new FirebaseDbHelper.DataCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                if (isAdded()) {
                    android.widget.Toast.makeText(requireContext(), "✓ Stage updated to " + stage, android.widget.Toast.LENGTH_SHORT).show();
                    loadRealData(((MainActivity) requireActivity()).getSession());
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded()) {
                    android.widget.Toast.makeText(requireContext(), "Update failed: " + error, android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectFilter(String filter) {
        priorityFilter = filter;

        for (TextView tv : new TextView[]{filterAll, filterUrgent, filterStandard}) {
            boolean active = tv.getText().toString().equals(filter);
            tv.setBackgroundResource(active ? R.drawable.bg_pill_accent : R.drawable.bg_pill_surface);
            tv.setTextColor(active ? Color.parseColor("#0066FF")
                    : requireContext().getColor(R.color.text_dim));
            tv.setTypeface(null, active ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        }

        applyFilters();
    }

    private void applyFilters() {
        filtered.clear();
        int activeCount = 0;
        for (RepairTicket t : allTickets) {
            boolean urgent = t.getId().contains("TF-9");
            String urgency = urgent ? "Urgent" : "Standard";
            
            boolean matchFilter = "All".equals(priorityFilter) || urgency.equals(priorityFilter);
            if (matchFilter) {
                filtered.add(t);
                if (!"Completed".equals(t.getStatus())) activeCount++;
            }
        }
        adapter.notifyDataSetChanged();
        if (tvActive != null) tvActive.setText(String.valueOf(activeCount));
    }
}
