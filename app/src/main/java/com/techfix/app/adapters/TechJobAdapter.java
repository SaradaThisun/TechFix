package com.techfix.app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.techfix.app.R;
import com.techfix.app.models.RepairTicket;
import com.techfix.app.models.TechnicianJob;

import java.util.List;

public class TechJobAdapter extends RecyclerView.Adapter<TechJobAdapter.VH> {

    public interface OnStageChangeListener {
        void onStageChange(RepairTicket ticket, String newStage);
    }

    private static final String[] STAGES = {
            "Assigned to Technician", "Repair in Progress", "Ready for Pickup", "Completed", "Canceled"
    };

    private final Context context;
    private final List<RepairTicket> tickets;
    private final OnStageChangeListener listener;

    public TechJobAdapter(Context context, List<RepairTicket> tickets, OnStageChangeListener listener) {
        this.context  = context;
        this.tickets  = tickets;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_technician_job, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        RepairTicket t = tickets.get(pos);

        h.tvTicketNum.setText(t.getId());
        h.tvBench.setText("Bench #0" + (pos + 1));
        h.tvDeviceModel.setText(t.getDeviceModel());
        h.tvCustomerNotes.setText("Customer: " + t.getCustomerName() + "\n" + (t.getCustomerNotes() != null ? t.getCustomerNotes() : ""));
        h.tvEta.setText("⏱ " + t.getEstimatedCompletion());

        // Priority badge
        boolean urgent = t.getId().contains("TF-9"); // Fake urgency check
        h.tvPriority.setText(urgent ? "Urgent" : "Standard");
        h.tvPriority.setBackgroundResource(urgent ? R.drawable.bg_danger : R.drawable.bg_warning);
        h.tvPriority.setTextColor(urgent ? Color.parseColor("#EF4444") : Color.parseColor("#F59E0B"));

        // Stage badge
        h.tvStage.setText(t.getStatus());

        // Device icon/photo
        String photoUrl = t.getDevicePhoto();
        if (photoUrl != null && !photoUrl.isEmpty()) {
            h.ivDevicePhoto.setVisibility(View.VISIBLE);
            h.ivDeviceIcon.setVisibility(View.GONE);
            h.btnViewPhoto.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(photoUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(h.ivDevicePhoto);
            
            // Allow clicking the small photo to view large version
            h.ivDevicePhoto.setOnClickListener(v -> showPhotoDialog(photoUrl, t.getId()));
        } else {
            h.ivDevicePhoto.setVisibility(View.GONE);
            h.ivDeviceIcon.setVisibility(View.VISIBLE);
            h.btnViewPhoto.setVisibility(View.GONE);
        }

        String type = t.getDeviceType() != null ? t.getDeviceType() : "phone";
        switch (type) {
            case "laptop": h.ivDeviceIcon.setText("💻"); break;
            case "tablet": h.ivDeviceIcon.setText("📒"); break;
            default:       h.ivDeviceIcon.setText("📱"); break;
        }

        // Open Workbench button — shows stage picker
        h.btnOpenWorkbench.setOnClickListener(v -> showStagePicker(t));

        // View Photo button
        h.btnViewPhoto.setOnClickListener(v -> showPhotoDialog(t.getDevicePhoto(), t.getId()));
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

    private void showStagePicker(RepairTicket ticket) {
        new android.app.AlertDialog.Builder(context)
                .setTitle("Update Stage: " + ticket.getId())
                .setItems(STAGES, (dialog, which) -> {
                    String newStage = STAGES[which];
                    listener.onStageChange(ticket, newStage);
                })
                .show();
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivDevicePhoto;
        TextView ivDeviceIcon, tvTicketNum, tvBench, tvPriority,
                 tvDeviceModel, tvCustomerNotes, tvStage, tvEta;
        Button btnOpenWorkbench, btnViewPhoto;

        VH(@NonNull View v) {
            super(v);
            ivDevicePhoto     = v.findViewById(R.id.ivDevicePhoto);
            ivDeviceIcon      = v.findViewById(R.id.ivDeviceIcon);
            tvTicketNum       = v.findViewById(R.id.tvTicketNum);
            tvBench           = v.findViewById(R.id.tvBench);
            tvPriority        = v.findViewById(R.id.tvPriority);
            tvDeviceModel     = v.findViewById(R.id.tvDeviceModel);
            tvCustomerNotes   = v.findViewById(R.id.tvCustomerNotes);
            tvStage           = v.findViewById(R.id.tvStage);
            tvEta             = v.findViewById(R.id.tvEta);
            btnOpenWorkbench  = v.findViewById(R.id.btnOpenWorkbench);
            btnViewPhoto      = v.findViewById(R.id.btnViewPhoto);
        }
    }
}
