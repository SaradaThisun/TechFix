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
import com.techfix.app.models.SparePart;
import com.techfix.app.utils.FormatUtils;

import java.util.List;

public class SparePartsAdapter extends RecyclerView.Adapter<SparePartsAdapter.VH> {

    public interface InventoryListener {
        void onTransfer(SparePart part);
        void onRestock(SparePart part);
        void onLogUsed(SparePart part);
    }

    private final Context context;
    private final List<SparePart> parts;
    private String activeBranch;
    private final InventoryListener listener;

    public SparePartsAdapter(Context context, List<SparePart> parts,
                             String activeBranch, InventoryListener listener) {
        this.context      = context;
        this.parts        = parts;
        this.activeBranch = activeBranch;
        this.listener     = listener;
    }

    public void setActiveBranch(String branch) {
        this.activeBranch = branch;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_spare_part, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        SparePart p = parts.get(pos);

        // Load image
        Glide.with(context)
                .load(p.getImageUrl())
                .placeholder(R.color.bg_alt)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(h.ivPartImage);

        h.tvPartId.setText(p.getPartId());
        h.tvCategory.setText(p.getCategory());
        h.tvPartName.setText(p.getName());

        // Compatible devices
        String compat = p.getCompatibleDevices() != null
                ? String.join(", ", p.getCompatibleDevices()) : "";
        h.tvCompatible.setText(compat);

        // Cost
        h.tvCost.setText(FormatUtils.formatLKR(p.getUnitCostLKR()) + " / unit");
        h.tvWarranty.setText(p.getWarrantyMonths() + "mo warranty");

        // OEM badge
        h.tvOemBadge.setVisibility(p.isOem() ? View.VISIBLE : View.GONE);

        // Stock badge
        int stock  = p.getStockForBranch(activeBranch);
        String status = p.getStockStatus(activeBranch);

        switch (status) {
            case "OUT":
                h.tvStockBadge.setText("✕ Out of Stock");
                h.tvStockBadge.setBackgroundResource(R.drawable.bg_danger);
                h.tvStockBadge.setTextColor(Color.parseColor("#EF4444"));
                break;
            case "LOW":
                h.tvStockBadge.setText("⚠ Low: " + stock + " units");
                h.tvStockBadge.setBackgroundResource(R.drawable.bg_warning);
                h.tvStockBadge.setTextColor(Color.parseColor("#F59E0B"));
                break;
            default:
                h.tvStockBadge.setText("✓ " + stock + " units");
                h.tvStockBadge.setBackgroundResource(R.drawable.bg_success);
                h.tvStockBadge.setTextColor(Color.parseColor("#10B981"));
                break;
        }

        // Other branch stock
        String otherBranch = activeBranch.contains("Colombo") ? "Galle" : "Colombo";
        int otherStock = activeBranch.contains("Colombo") ? p.getGalleStock() : p.getColomboStock();
        h.tvOtherBranch.setText(otherBranch + " branch: " + otherStock + " units");

        // Disable transfer/log-used when out of stock
        h.btnTransfer.setEnabled(stock > 0);
        h.btnLogUsed.setEnabled(stock > 0);

        h.btnTransfer.setOnClickListener(v -> listener.onTransfer(p));
        h.btnRestock.setOnClickListener(v -> listener.onRestock(p));
        h.btnLogUsed.setOnClickListener(v  -> listener.onLogUsed(p));
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivPartImage;
        TextView tvPartId, tvOemBadge, tvCategory, tvPartName, tvCompatible,
                 tvStockBadge, tvOtherBranch, tvCost, tvWarranty;
        Button btnTransfer, btnRestock, btnLogUsed;

        VH(@NonNull View v) {
            super(v);
            ivPartImage  = v.findViewById(R.id.ivPartImage);
            tvPartId     = v.findViewById(R.id.tvPartId);
            tvOemBadge   = v.findViewById(R.id.tvOemBadge);
            tvCategory   = v.findViewById(R.id.tvCategory);
            tvPartName   = v.findViewById(R.id.tvPartName);
            tvCompatible = v.findViewById(R.id.tvCompatible);
            tvStockBadge = v.findViewById(R.id.tvStockBadge);
            tvOtherBranch= v.findViewById(R.id.tvOtherBranch);
            tvCost       = v.findViewById(R.id.tvCost);
            tvWarranty   = v.findViewById(R.id.tvWarranty);
            btnTransfer  = v.findViewById(R.id.btnTransfer);
            btnRestock   = v.findViewById(R.id.btnRestock);
            btnLogUsed   = v.findViewById(R.id.btnLogUsed);
        }
    }
}
