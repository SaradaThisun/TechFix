package com.techfix.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techfix.app.R;
import com.techfix.app.models.RepairService;
import com.techfix.app.utils.FormatUtils;

import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.VH> {

    public interface OnBookListener {
        void onBook(RepairService service);
    }

    private final Context context;
    private final List<RepairService> services;
    private final OnBookListener listener;

    public ServicesAdapter(Context context, List<RepairService> services, OnBookListener listener) {
        this.context  = context;
        this.services = services;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_service, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        RepairService s = services.get(pos);

        h.tvCategory.setText(s.getCategory());
        h.tvTitle.setText(s.getTitle());
        h.tvDescription.setText(s.getDescription());
        h.tvPrice.setText(FormatUtils.formatLKR(s.getPriceLKR()));
        h.tvTime.setText("⏱ " + s.getEstimatedTime());
        h.tvWarranty.setText("🛡 " + s.getWarrantyDays() + "d Warranty");

        // Popular badge
        h.tvPopular.setVisibility(s.isPopular() ? View.VISIBLE : View.GONE);

        // Service image via Glide
        if (s.getSampleImages() != null && !s.getSampleImages().isEmpty()) {
            Glide.with(context)
                    .load(s.getSampleImages().get(0))
                    .centerCrop()
                    .placeholder(R.color.bg_alt)
                    .into(h.ivServiceImage);
        }

        h.btnBook.setOnClickListener(v -> listener.onBook(s));
        h.itemView.setOnClickListener(v -> listener.onBook(s));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvCategory, tvTitle, tvDescription, tvPrice, tvTime, tvWarranty, tvPopular;
        ImageView ivServiceImage;
        Button btnBook;

        VH(@NonNull View v) {
            super(v);
            tvCategory     = v.findViewById(R.id.tvCategory);
            tvTitle        = v.findViewById(R.id.tvTitle);
            tvDescription  = v.findViewById(R.id.tvDescription);
            tvPrice        = v.findViewById(R.id.tvPrice);
            tvTime         = v.findViewById(R.id.tvTime);
            tvWarranty     = v.findViewById(R.id.tvWarranty);
            tvPopular      = v.findViewById(R.id.tvPopular);
            ivServiceImage = v.findViewById(R.id.ivServiceImage);
            btnBook        = v.findViewById(R.id.btnBook);
        }
    }
}
