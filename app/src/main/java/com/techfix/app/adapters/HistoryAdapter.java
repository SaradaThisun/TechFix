package com.techfix.app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.models.HistoryItem;
import com.techfix.app.utils.FormatUtils;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VH> {

    public interface OnRebookListener {
        void onRebook(HistoryItem item);
    }

    private final Context context;
    private final List<HistoryItem> items;
    private final OnRebookListener listener;

    public HistoryAdapter(Context context, List<HistoryItem> items, OnRebookListener listener) {
        this.context  = context;
        this.items    = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        HistoryItem item = items.get(pos);

        h.tvRefId.setText(item.getReferenceId());
        h.tvInvoice.setText(item.getInvoiceNumber());
        h.tvDeviceName.setText(item.getDeviceName());
        h.tvServiceSummary.setText(item.getServiceSummary());
        h.tvDate.setText("📅 " + item.getRepairDate());
        h.tvBranch.setText("📍 " + item.getBranch());

        // Status badge
        boolean completed = "Completed".equals(item.getStatus());
        h.tvStatus.setText(completed ? "✓ Completed" : "✕ " + item.getStatus());
        h.tvStatus.setBackgroundResource(completed ? R.drawable.bg_success : R.drawable.bg_danger);
        h.tvStatus.setTextColor(completed ? Color.parseColor("#10B981") : Color.parseColor("#EF4444"));

        // Left accent bar color
        h.accentBar.setBackgroundColor(completed ? Color.parseColor("#10B981") : Color.parseColor("#EF4444"));

        // Device icon
        switch (item.getDeviceType()) {
            case "laptop":  h.ivDeviceIcon.setText("💻"); break;
            case "tablet":  h.ivDeviceIcon.setText("📒"); break;
            default:        h.ivDeviceIcon.setText("📱"); break;
        }

        // Cost
        if (item.getTotalCostLKR() > 0) {
            h.tvCost.setText(FormatUtils.formatLKR(item.getTotalCostLKR()));
            h.tvCost.setTextColor(Color.parseColor("#0066FF"));
        } else {
            h.tvCost.setText("—");
            h.tvCost.setTextColor(context.getColor(R.color.text_muted));
        }

        // Warranty badge
        if (item.getWarrantyUntil() != null && !item.getWarrantyUntil().isEmpty()) {
            h.tvWarranty.setVisibility(View.VISIBLE);
            h.tvWarranty.setText("🛡 Warranty until " + item.getWarrantyUntil());
        } else {
            h.tvWarranty.setVisibility(View.GONE);
        }

        // Invoice button
        h.btnInvoice.setOnClickListener(v -> {
            android.widget.Toast.makeText(context,
                    "Downloading " + item.getInvoiceNumber() + "…",
                    android.widget.Toast.LENGTH_SHORT).show();
        });

        // Re-book only for canceled
        if ("Canceled".equals(item.getStatus())) {
            h.btnRebook.setVisibility(View.VISIBLE);
            h.btnRebook.setOnClickListener(v -> listener.onRebook(item));
        } else {
            h.btnRebook.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        View accentBar;
        TextView tvRefId, tvInvoice, tvStatus, tvDeviceName, tvServiceSummary,
                 tvDate, tvBranch, tvCost, tvWarranty, btnInvoice, btnRebook;
        TextView ivDeviceIcon;

        VH(@NonNull View v) {
            super(v);
            accentBar       = v.findViewById(R.id.accentBar);
            ivDeviceIcon    = v.findViewById(R.id.ivDeviceIcon);
            tvRefId         = v.findViewById(R.id.tvRefId);
            tvInvoice       = v.findViewById(R.id.tvInvoice);
            tvStatus        = v.findViewById(R.id.tvStatus);
            tvDeviceName    = v.findViewById(R.id.tvDeviceName);
            tvServiceSummary = v.findViewById(R.id.tvServiceSummary);
            tvDate          = v.findViewById(R.id.tvDate);
            tvBranch        = v.findViewById(R.id.tvBranch);
            tvCost          = v.findViewById(R.id.tvCost);
            tvWarranty      = v.findViewById(R.id.tvWarranty);
            btnInvoice      = v.findViewById(R.id.btnInvoice);
            btnRebook       = v.findViewById(R.id.btnRebook);
        }
    }
}
