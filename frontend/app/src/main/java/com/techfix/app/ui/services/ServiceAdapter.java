package com.techfix.app.ui.services;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.data.model.RepairService;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    public interface OnServiceClickListener {
        void onServiceClick(RepairService service);
    }

    private List<RepairService> services;
    private final OnServiceClickListener listener;

    public ServiceAdapter(List<RepairService> services, OnServiceClickListener listener) {
        this.services = services;
        this.listener = listener;
    }

    public void updateData(List<RepairService> newServices) {
        this.services = newServices;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        RepairService service = services.get(position);
        holder.tvCategory.setText(service.getCategoryName() != null ? service.getCategoryName() : "");
        holder.tvServiceName.setText(service.getName());
        holder.tvDescription.setText(service.getDescription() != null ? service.getDescription() : "");
        holder.tvPrice.setText("Rs. " + service.getPrice());

        holder.itemView.setOnClickListener(v -> listener.onServiceClick(service));
    }

    @Override
    public int getItemCount() {
        return services == null ? 0 : services.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvServiceName, tvDescription, tvPrice;

        ServiceViewHolder(View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}