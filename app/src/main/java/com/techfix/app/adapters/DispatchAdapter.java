package com.techfix.app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techfix.app.R;
import com.techfix.app.models.DispatchRequest;

import java.util.List;

public class DispatchAdapter extends RecyclerView.Adapter<DispatchAdapter.VH> {

    public interface DispatchListener {
        void onAutoAssign(DispatchRequest req);
        void onAssignTech(DispatchRequest req);
        void onCallCustomer(DispatchRequest req);
    }

    private final Context context;
    private final List<DispatchRequest> requests;
    private final DispatchListener listener;

    public DispatchAdapter(Context context, List<DispatchRequest> requests, DispatchListener listener) {
        this.context   = context;
        this.requests  = requests;
        this.listener  = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_dispatch_request, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        DispatchRequest req = requests.get(pos);

        h.tvReqId.setText(req.getId());
        h.tvReqTime.setText(req.getSubmittedTime());
        h.tvDeviceModel.setText(req.getDeviceModel());
        h.tvIssueSummary.setText(req.getIssueSummary());
        h.tvCustomerName.setText("👤 " + req.getCustomerName());
        h.tvLocation.setText("📍 " + req.getCustomerLocation());

        // Urgency badge
        boolean urgent = "Urgent".equals(req.getUrgency());
        h.tvUrgency.setText("● " + req.getUrgency());
        h.tvUrgency.setBackgroundResource(urgent ? R.drawable.bg_danger : R.drawable.bg_warning);
        h.tvUrgency.setTextColor(urgent ? Color.parseColor("#EF4444") : Color.parseColor("#F59E0B"));

        // Device icon
        switch (req.getDeviceType()) {
            case "laptop": h.ivDeviceIcon.setText("💻"); break;
            case "tablet": h.ivDeviceIcon.setText("📒"); break;
            default:       h.ivDeviceIcon.setText("📱"); break;
        }

        // Smart dispatch info
        h.tvAutoMatch.setText("🧭 " + req.getAutoMatchBranch()
                + " — " + String.format("%.1f", req.getAutoMatchDistanceKm()) + " km");
        h.tvTechsAvail.setText("🔧 " + req.getTechniciansAvailable() + " techs available");
        h.tvPartStatus.setText(req.isPartInStock() ? "📦 Parts in stock" : "⚠ Parts NOT in stock");
        h.tvPartStatus.setTextColor(req.isPartInStock()
                ? Color.parseColor("#10B981") : Color.parseColor("#EF4444"));

        // View Photo visibility
        if (req.getDevicePhoto() != null && !req.getDevicePhoto().isEmpty()) {
            h.btnViewPhoto.setVisibility(View.VISIBLE);
        } else {
            h.btnViewPhoto.setVisibility(View.GONE);
        }

        // Show/hide action vs status rows
        boolean pending = "Pending Dispatch".equals(req.getStatus());
        h.actionRow.setVisibility(pending ? View.VISIBLE : View.GONE);
        h.tvDispatchedStatus.setVisibility(pending ? View.GONE : View.VISIBLE);

        if (!pending) {
            String branch = req.getAssignedBranch() != null ? req.getAssignedBranch() : "";
            String tech   = req.getAssignedTech()   != null ? req.getAssignedTech()   : "";
            h.tvDispatchedStatus.setText("✓ " + req.getStatus()
                    + (branch.isEmpty() ? "" : " → " + branch)
                    + (tech.isEmpty()   ? "" : " (" + tech + ")"));
        }

        // Listeners
        h.btnCall.setOnClickListener(v       -> listener.onCallCustomer(req));
        h.btnViewPhoto.setOnClickListener(v -> {
            if (req.getDevicePhoto() != null && !req.getDevicePhoto().isEmpty()) {
                showPhotoDialog(req.getDevicePhoto(), req.getId());
            }
        });
        h.btnAutoAssign.setOnClickListener(v -> listener.onAutoAssign(req));
        h.btnAssignTech.setOnClickListener(v -> listener.onAssignTech(req));
    }

    private void showPhotoDialog(String url, String ticketId) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_view_photo, null);
        ImageView ivLarge = dialogView.findViewById(R.id.ivPhotoLarge);
        TextView tvTitle = dialogView.findViewById(R.id.tvPhotoTitle);

        tvTitle.setText("Device Photo - " + ticketId);

        Glide.with(context)
                .load(url)
                .into(ivLarge);

        new android.app.AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("Close", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView ivDeviceIcon, tvReqId, tvReqTime, tvUrgency, tvDeviceModel, tvIssueSummary,
                 tvCustomerName, tvLocation, tvAutoMatch, tvTechsAvail, tvPartStatus,
                 tvDispatchedStatus, btnCall;
        Button btnViewPhoto;
        LinearLayout actionRow;
        Button btnAutoAssign, btnAssignTech;

        VH(@NonNull View v) {
            super(v);
            ivDeviceIcon       = v.findViewById(R.id.ivDeviceIcon);
            tvReqId            = v.findViewById(R.id.tvReqId);
            tvReqTime          = v.findViewById(R.id.tvReqTime);
            tvUrgency          = v.findViewById(R.id.tvUrgency);
            tvDeviceModel      = v.findViewById(R.id.tvDeviceModel);
            tvIssueSummary     = v.findViewById(R.id.tvIssueSummary);
            tvCustomerName     = v.findViewById(R.id.tvCustomerName);
            tvLocation         = v.findViewById(R.id.tvLocation);
            tvAutoMatch        = v.findViewById(R.id.tvAutoMatch);
            tvTechsAvail       = v.findViewById(R.id.tvTechsAvail);
            tvPartStatus       = v.findViewById(R.id.tvPartStatus);
            tvDispatchedStatus = v.findViewById(R.id.tvDispatchedStatus);
            btnCall            = v.findViewById(R.id.btnCall);
            btnViewPhoto       = v.findViewById(R.id.btnViewPhoto);
            actionRow          = v.findViewById(R.id.actionRow);
            btnAutoAssign      = v.findViewById(R.id.btnAutoAssign);
            btnAssignTech      = v.findViewById(R.id.btnAssignTech);
        }
    }
}
