package com.techfix.app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.models.PromoBanner;

import java.util.List;

public class PromoBannerAdapter extends RecyclerView.Adapter<PromoBannerAdapter.VH> {

    private final Context context;
    private final List<PromoBanner> banners;

    public PromoBannerAdapter(Context context, List<PromoBanner> banners) {
        this.context = context;
        this.banners = banners;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_promo_banner, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        PromoBanner b = banners.get(pos);

        h.tvBadge.setText(b.getBadge());
        h.tvBranch.setText(b.getBranch());
        h.tvTitle.setText(b.getTitle());
        h.tvSubtitle.setText(b.getSubtitle());
        h.tvCode.setText(b.getCode());
        h.tvDiscount.setText(b.getDiscount());

        // Tint card gradient with the banner's own colors
        try {
            int start = Color.parseColor(b.getColorStart());
            int end   = Color.parseColor(b.getColorEnd());
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TL_BR, new int[]{start, end});
            gd.setCornerRadius(context.getResources().getDimensionPixelSize(R.dimen.radius_xl));
            h.promoCard.setBackground(gd);
        } catch (Exception ignored) {
            h.promoCard.setBackgroundResource(R.drawable.bg_promo_gradient);
        }
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        LinearLayout promoCard;
        TextView tvBadge, tvBranch, tvTitle, tvSubtitle, tvCode, tvDiscount, btnClaim;

        VH(@NonNull View v) {
            super(v);
            promoCard  = v.findViewById(R.id.promoCard);
            tvBadge    = v.findViewById(R.id.tvBadge);
            tvBranch   = v.findViewById(R.id.tvBranch);
            tvTitle    = v.findViewById(R.id.tvTitle);
            tvSubtitle = v.findViewById(R.id.tvSubtitle);
            tvCode     = v.findViewById(R.id.tvCode);
            tvDiscount = v.findViewById(R.id.tvDiscount);
            btnClaim   = v.findViewById(R.id.btnClaim);
        }
    }
}
