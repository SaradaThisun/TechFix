package com.techfix.app.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techfix.app.R;
import com.techfix.app.activities.MainActivity;
import com.techfix.app.adapters.StatusLogAdapter;
import com.techfix.app.firebase.FirebaseDbHelper;
import com.techfix.app.models.RepairTicket;
import com.techfix.app.utils.FormatUtils;
import com.techfix.app.utils.SessionManager;

import java.util.List;

import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class TrackingFragment extends Fragment {

    private View rootView;
    private ValueEventListener ticketListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tracking, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager session = ((MainActivity) requireActivity()).getSession();
        
        // Use real-time listener instead of one-time fetch
        ticketListener = FirebaseDbHelper.listenToTicketsForUser(session.getUid(), new FirebaseDbHelper.DataCallback<List<RepairTicket>>() {
            @Override
            public void onSuccess(List<RepairTicket> data) {
                if (isAdded()) {
                    android.util.Log.d("Tracking", "Received " + (data != null ? data.size() : 0) + " tickets for user " + session.getUid());
                    if (data != null && !data.isEmpty()) {
                        // Find the most relevant active ticket (not completed)
                        RepairTicket ticket = null;
                        for (int i = data.size() - 1; i >= 0; i--) {
                            RepairTicket t = data.get(i);
                            android.util.Log.d("Tracking", "Checking ticket: " + t.getId() + " | Status: " + t.getStatus());
                            if (!"Completed".equals(t.getStatus())) {
                                ticket = t;
                                break;
                            }
                        }
                        
                        if (ticket != null) {
                            android.util.Log.d("Tracking", "Binding ticket: " + ticket.getId() + " | Status: " + ticket.getStatus());
                            bindAll(rootView, ticket);
                            rootView.findViewById(R.id.noDataLayout).setVisibility(View.GONE);
                            rootView.findViewById(R.id.mainScroll).setVisibility(View.VISIBLE);
                        } else {
                            showNoActiveRepair();
                        }
                    } else {
                        showNoActiveRepair();
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded()) {
                    showNoActiveRepair();
                }
            }
        });
    }

    private void showNoActiveRepair() {
        if (rootView == null) return;
        View noData = rootView.findViewById(R.id.noDataLayout);
        View scroll = rootView.findViewById(R.id.mainScroll);
        if (noData != null) noData.setVisibility(View.VISIBLE);
        if (scroll != null) scroll.setVisibility(View.GONE);
        
        TextView tvTitle = rootView.findViewById(R.id.tvNoDataTitle);
        TextView tvDesc = rootView.findViewById(R.id.tvNoDataDesc);
        if (tvTitle != null) tvTitle.setText("No Active Repairs");
        if (tvDesc != null) tvDesc.setText("You don't have any ongoing repair requests at the moment.");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (ticketListener != null) {
            FirebaseDbHelper.stopListening(ticketListener);
        }
    }

    private void bindAll(View view, RepairTicket ticket) {
        bindHeader(view, ticket);
        bindDeviceCard(view, ticket);
        bindApprovalCard(view, ticket);
        bindTechCard(view, ticket);
        buildTimeline(view, ticket);
        bindStatusLogs(view, ticket);
        bindPaymentBar(view, ticket);
    }

    private void bindHeader(View v, RepairTicket t) {
        ((TextView) v.findViewById(R.id.tvTicketIdHeader)).setText(t.getId());
        TextView tvStatus = v.findViewById(R.id.tvStatusBadge);
        tvStatus.setText("● " + t.getStatus());
        tvStatus.setTextColor(Color.WHITE);
    }

    private void bindDeviceCard(View v, RepairTicket t) {
        ((TextView) v.findViewById(R.id.tvDeviceModel)).setText(t.getDeviceModel());
        ((TextView) v.findViewById(R.id.tvDeviceCategory)).setText(t.getCategory());
        ((TextView) v.findViewById(R.id.tvProgressCircle)).setText(t.getProgressPercent() + "%");
        ((TextView) v.findViewById(R.id.tvIssueText)).setText(t.getIssue());
        ((ProgressBar) v.findViewById(R.id.progressBar)).setProgress(t.getProgressPercent());
        ((TextView) v.findViewById(R.id.tvBranchMeta)).setText("📍 " + t.getBranch());
        ((TextView) v.findViewById(R.id.tvEtaMeta)).setText("⏱ ETA: " + t.getEstimatedCompletion());

        android.widget.ImageView ivDevice = v.findViewById(R.id.ivDevicePhoto);
        if (t.getDevicePhoto() != null && !t.getDevicePhoto().isEmpty()) {
            ivDevice.setVisibility(View.VISIBLE);
            Glide.with(requireContext())
                    .load(t.getDevicePhoto())
                    .into(ivDevice);
        } else {
            ivDevice.setVisibility(View.GONE);
        }
    }

    private void bindApprovalCard(View v, RepairTicket t) {
        View card = v.findViewById(R.id.approvalCard);
        if (card == null) return;
        
        if ("Awaiting Customer Approval".equals(t.getStatus())) {
            card.setVisibility(View.VISIBLE);
            ((TextView) v.findViewById(R.id.tvEstimatedTotal)).setText(
                    FormatUtils.formatLKR(t.getTotalCostLKR()));
            
            v.findViewById(R.id.btnApproveQuote).setOnClickListener(click -> {
                approveRepair(t);
            });
        } else {
            card.setVisibility(View.GONE);
        }
    }

    private void approveRepair(RepairTicket t) {
        t.setStatus("Repair in Progress");
        t.setProgressPercent(50);
        t.setCurrentStepIndex(2);
        
        // Update timeline
        if (t.getTimelineSteps() != null) {
            for (int i = 0; i < t.getTimelineSteps().size(); i++) {
                RepairTicket.TimelineStep step = t.getTimelineSteps().get(i);
                if (i < 2) {
                    step.isCompleted = true;
                    step.isCurrent = false;
                } else if (i == 2) {
                    step.isCompleted = false;
                    step.isCurrent = true;
                    step.timestamp = new java.text.SimpleDateFormat("hh:mm a", java.util.Locale.US).format(new java.util.Date());
                }
            }
        }
        
        // Add log
        if (t.getStatusLogs() != null) {
            t.getStatusLogs().add(0, new RepairTicket.StatusLogEntry(
                    "log_" + System.currentTimeMillis(),
                    "Quote Approved",
                    "Customer approved the diagnostic estimate. Repair starting.",
                    new java.text.SimpleDateFormat("hh:mm a", java.util.Locale.US).format(new java.util.Date()),
                    "System",
                    "success"
            ));
        }

        FirebaseDbHelper.saveTicket(t, new FirebaseDbHelper.DataCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                if (isAdded()) {
                    android.widget.Toast.makeText(requireContext(), "✓ Quote Approved! Repair Started.", android.widget.Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(String error) {
                if (isAdded()) {
                    android.widget.Toast.makeText(requireContext(), "Error: " + error, android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bindTechCard(View v, RepairTicket t) {
        CircleImageView ivAvatar = v.findViewById(R.id.ivTechAvatar);
        if (ivAvatar != null) {
            if (t.getTechnicianAvatar() != null && !t.getTechnicianAvatar().isEmpty() && isAdded()) {
                Glide.with(requireContext())
                        .load(t.getTechnicianAvatar())
                        .placeholder(R.drawable.ic_person_placeholder)
                        .error(R.drawable.ic_person_placeholder)
                        .into(ivAvatar);
            } else {
                ivAvatar.setImageResource(R.drawable.ic_person_placeholder);
            }
        }
        
        TextView tvName = v.findViewById(R.id.tvTechName);
        if (tvName != null) tvName.setText(t.getTechnicianName());
        
        TextView tvRole = v.findViewById(R.id.tvTechRole);
        if (tvRole != null) tvRole.setText(t.getTechnicianRole());
        
        TextView tvRating = v.findViewById(R.id.tvTechRating);
        if (tvRating != null) tvRating.setText("⭐ " + t.getTechnicianRating());

        View btnCall = v.findViewById(R.id.btnCallTech);
        if (btnCall != null) {
            btnCall.setOnClickListener(click -> {
                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + t.getTechnicianPhone()));
                startActivity(intent);
            });
        }
    }

    private void buildTimeline(View v, RepairTicket t) {
        LinearLayout container = v.findViewById(R.id.timelineContainer);
        container.removeAllViews();
        if (t.getTimelineSteps() == null) return;

        for (int i = 0; i < t.getTimelineSteps().size(); i++) {
            RepairTicket.TimelineStep step = t.getTimelineSteps().get(i);

            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(0, 0, 0, 24);

            // Icon circle
            TextView icon = new TextView(requireContext());
            icon.setLayoutParams(new LinearLayout.LayoutParams(72, 72));
            icon.setGravity(17);
            icon.setTextSize(14f);
            icon.setTypeface(null, android.graphics.Typeface.BOLD);

            if (step.isCompleted) {
                icon.setText("✓");
                icon.setTextColor(Color.WHITE);
                icon.setBackgroundResource(R.drawable.bg_call_btn); // green circle
            } else if (step.isCurrent) {
                icon.setText(String.valueOf(step.stepNumber));
                icon.setTextColor(Color.parseColor("#0066FF"));
                icon.setBackgroundResource(R.drawable.bg_circle_accent);
            } else {
                icon.setText(String.valueOf(step.stepNumber));
                icon.setTextColor(Color.parseColor("#94A3B8"));
                icon.setBackgroundResource(R.drawable.bg_circle_accent);
            }

            LinearLayout content = new LinearLayout(requireContext());
            content.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams contentLp = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            contentLp.setMarginStart(12);
            content.setLayoutParams(contentLp);

            TextView tvTitle = new TextView(requireContext());
            tvTitle.setText(step.title);
            tvTitle.setTextSize(14f);
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            tvTitle.setTextColor(step.isCurrent ? Color.parseColor("#0066FF")
                    : requireContext().getColor(R.color.text));

            TextView tvDesc = new TextView(requireContext());
            tvDesc.setText(step.description);
            tvDesc.setTextSize(12f);
            tvDesc.setTextColor(requireContext().getColor(R.color.text_dim));
            tvDesc.setPadding(0, 4, 0, 0);

            if (step.timestamp != null && !step.timestamp.isEmpty()) {
                TextView tvTime = new TextView(requireContext());
                tvTime.setText(step.timestamp);
                tvTime.setTextSize(11f);
                tvTime.setTextColor(requireContext().getColor(R.color.text_muted));
                content.addView(tvTitle);
                content.addView(tvTime);
                content.addView(tvDesc);
            } else {
                content.addView(tvTitle);
                content.addView(tvDesc);
            }

            row.addView(icon);
            row.addView(content);
            container.addView(row);
        }
    }

    private void bindStatusLogs(View v, RepairTicket t) {
        RecyclerView rv = v.findViewById(R.id.rvStatusLogs);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        if (t.getStatusLogs() != null) {
            rv.setAdapter(new StatusLogAdapter(requireContext(), t.getStatusLogs()));
        }
    }

    private void bindPaymentBar(View v, RepairTicket t) {
        View payBar = v.findViewById(R.id.paymentBar);
        if (t.isPaid()) {
            payBar.setVisibility(View.GONE);
        } else {
            payBar.setVisibility(View.VISIBLE);
            ((TextView) v.findViewById(R.id.tvTotalAmount)).setText(
                    FormatUtils.formatLKR(t.getTotalCostLKR()));
            v.findViewById(R.id.btnProceedPayment).setOnClickListener(click ->
                    ((MainActivity) requireActivity()).navigateTo("Payment"));
        }
    }
}
