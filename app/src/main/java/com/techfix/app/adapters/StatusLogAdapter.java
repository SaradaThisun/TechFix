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
import com.techfix.app.models.RepairTicket;
import com.techfix.app.utils.FormatUtils;

import java.util.List;

public class StatusLogAdapter extends RecyclerView.Adapter<StatusLogAdapter.VH> {

    private final Context context;
    private final List<RepairTicket.StatusLogEntry> logs;

    public StatusLogAdapter(Context context, List<RepairTicket.StatusLogEntry> logs) {
        this.context = context;
        this.logs    = logs;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_status_log, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        RepairTicket.StatusLogEntry log = logs.get(pos);

        h.tvTitle.setText(log.title);
        h.tvDescription.setText(log.description);
        h.tvTimestamp.setText(log.timestamp);
        h.tvTech.setText("by " + log.technicianName);

        int color = FormatUtils.logColor(log.statusType);
        h.colorDot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        View colorDot;
        TextView tvTitle, tvDescription, tvTimestamp, tvTech;

        VH(@NonNull View v) {
            super(v);
            colorDot      = v.findViewById(R.id.colorDot);
            tvTitle       = v.findViewById(R.id.tvTitle);
            tvDescription = v.findViewById(R.id.tvDescription);
            tvTimestamp   = v.findViewById(R.id.tvTimestamp);
            tvTech        = v.findViewById(R.id.tvTech);
        }
    }
}
