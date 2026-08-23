package com.techfix.app.ui.repairs;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.data.model.RepairRequest;
import com.techfix.app.utils.StatusUtil;

import java.util.List;

public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.RepairViewHolder> {

    public interface OnRepairClickListener {
        void onRepairClick(RepairRequest repair);
    }

    private List<RepairRequest> repairs;
    private final OnRepairClickListener listener;

    public RepairAdapter(List<RepairRequest> repairs, OnRepairClickListener listener) {
        this.repairs = repairs;
        this.listener = listener;
    }

    public void updateData(List<RepairRequest> newRepairs) {
        this.repairs = newRepairs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RepairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repair_request, parent, false);
        return new RepairViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepairViewHolder holder, int position) {
        RepairRequest repair = repairs.get(position);
        holder.tvServiceName.setText(repair.getServiceName() != null ? repair.getServiceName() : "Repair Request");
        holder.tvBranch.setText(repair.getBranchName() != null ? repair.getBranchName() : "");
        holder.tvDate.setText("Requested: " + repair.getRequestedDate());
        holder.tvStatus.setText(repair.getStatus().replace("_", " "));
        holder.tvStatus.setBackgroundColor(Color.parseColor(StatusUtil.colorFor(repair.getStatus())));

        holder.itemView.setOnClickListener(v -> listener.onRepairClick(repair));
    }

    @Override
    public int getItemCount() {
        return repairs == null ? 0 : repairs.size();
    }

    static class RepairViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName, tvBranch, tvDate, tvStatus;

        RepairViewHolder(View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvBranch = itemView.findViewById(R.id.tvBranch);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
