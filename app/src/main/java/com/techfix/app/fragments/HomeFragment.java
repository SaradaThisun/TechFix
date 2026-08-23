package com.techfix.app.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.techfix.app.R;
import com.techfix.app.activities.MainActivity;
import com.techfix.app.adapters.PromoBannerAdapter;
import com.techfix.app.firebase.FirebaseDbHelper;
import com.techfix.app.models.RepairTicket;
import com.techfix.app.utils.FormatUtils;
import com.techfix.app.utils.MockData;
import com.techfix.app.utils.SessionManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPagerPromo;
    private PromoBannerAdapter bannerAdapter;
    private Timer autoScrollTimer;
    private int currentBanner = 0;
    private ValueEventListener ticketListener;

    // Active ticket views
    private View ticketCard;
    private TextView tvTicketId, tvTicketStatus, tvTicketDevice, tvTicketIssue,
            tvProgress, tvEta, tvTicketBranch;
    private ProgressBar progressRepair;
    private RepairTicket activeTicket;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager session = ((MainActivity) requireActivity()).getSession();
        
        setupHeader(view, session);
        setupPromoBanners(view);
        setupQuickActions(view);
        
        // Initialize ticket views first
        ticketCard     = view.findViewById(R.id.ticketCard);
        tvTicketId     = view.findViewById(R.id.tvTicketId);
        tvTicketStatus = view.findViewById(R.id.tvTicketStatus);
        tvTicketDevice = view.findViewById(R.id.tvTicketDevice);
        tvTicketIssue  = view.findViewById(R.id.tvTicketIssue);
        tvProgress     = view.findViewById(R.id.tvProgress);
        tvEta          = view.findViewById(R.id.tvEta);
        tvTicketBranch = view.findViewById(R.id.tvTicketBranch);
        progressRepair = view.findViewById(R.id.progressRepair);

        // Use real-time listener instead of one-time fetch
        ticketListener = FirebaseDbHelper.listenToTicketsForUser(session.getUid(), new FirebaseDbHelper.DataCallback<List<RepairTicket>>() {
            @Override
            public void onSuccess(List<RepairTicket> data) {
                if (isAdded() && data != null && !data.isEmpty()) {
                    // Find the most relevant active ticket (not completed)
                    activeTicket = data.get(data.size() - 1);
                    for (RepairTicket t : data) {
                        if (!"Completed".equals(t.getStatus())) {
                            activeTicket = t;
                            break;
                        }
                    }
                    updateTicketUI(view);
                } else {
                    // Fallback to mock if no real tickets yet
                    activeTicket = MockData.getActiveTicket(session.getUid());
                    updateTicketUI(view);
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded()) {
                    activeTicket = MockData.getActiveTicket(session.getUid());
                    updateTicketUI(view);
                }
            }
        });

        setupBrandCategories(view);
        setupEmergencyBanner(view);
    }

    private void updateTicketUI(View rootView) {
        if (activeTicket == null || ticketCard == null) return;
        
        ticketCard.setVisibility(View.VISIBLE);
        tvTicketId.setText(activeTicket.getId());
        tvTicketStatus.setText("● " + activeTicket.getStatus());
        tvTicketDevice.setText(activeTicket.getDeviceModel());
        tvTicketIssue.setText(activeTicket.getIssue());
        tvProgress.setText(activeTicket.getProgressPercent() + "% Complete");
        tvEta.setText("ETA: " + activeTicket.getEstimatedCompletion());
        tvTicketBranch.setText("📍 " + activeTicket.getBranch());
        progressRepair.setProgress(activeTicket.getProgressPercent());

        rootView.findViewById(R.id.btnViewDetails).setOnClickListener(v -> navigate("Track"));
        ticketCard.setOnClickListener(v -> navigate("Track"));
    }

    private void setupHeader(View view, SessionManager session) {
        TextView tvAvatar   = view.findViewById(R.id.tvAvatar);
        TextView tvGreeting = view.findViewById(R.id.tvGreeting);
        TextView tvBranch   = view.findViewById(R.id.tvBranch);

        String fullName = session.getFullName();
        String firstName = fullName.contains(" ") ? fullName.split(" ")[0] : fullName;
        
        tvAvatar.setText(FormatUtils.getInitial(fullName));
        tvGreeting.setText("Ayubowan, " + firstName + "!");
        tvBranch.setText("📍 " + session.getBranch() + " ⇄");

        // Profile click
        view.findViewById(R.id.profileArea).setOnClickListener(v -> ((MainActivity) requireActivity()).showProfileDialog());

        view.findViewById(R.id.btnBell).setOnClickListener(v ->
                android.widget.Toast.makeText(requireContext(),
                        "Repair in Progress • #TF-8942", android.widget.Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.branchPill).setOnClickListener(v -> {
            String current = session.getBranch();
            String next = current.contains("Colombo") ? "Galle Branch" : "Colombo Branch";
            session.updateBranch(next);
            tvBranch.setText("📍 " + next + " ⇄");
        });
    }

    private void setupPromoBanners(View view) {
        viewPagerPromo = view.findViewById(R.id.viewPagerPromo);
        DotsIndicator dotsIndicator = view.findViewById(R.id.dotsIndicator);

        bannerAdapter = new PromoBannerAdapter(requireContext(), MockData.getPromoBanners());
        viewPagerPromo.setAdapter(bannerAdapter);
        dotsIndicator.setViewPager2(viewPagerPromo);

        // Set page margin for peeking effect
        viewPagerPromo.setOffscreenPageLimit(1);

        // Auto-scroll every 5 seconds
        Handler handler = new Handler(Looper.getMainLooper());
        autoScrollTimer = new Timer();
        autoScrollTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    if (isAdded() && bannerAdapter != null && bannerAdapter.getItemCount() > 0) {
                        currentBanner = (currentBanner + 1) % bannerAdapter.getItemCount();
                        viewPagerPromo.setCurrentItem(currentBanner, true);
                    }
                });
            }
        }, 5000, 5000);
    }

    private void setupQuickActions(View view) {
        view.findViewById(R.id.quickBook).setOnClickListener(v -> navigate("Book"));
        view.findViewById(R.id.quickTrack).setOnClickListener(v -> navigate("Track"));
        view.findViewById(R.id.quickBranches).setOnClickListener(v -> navigate("Hubs"));
        view.findViewById(R.id.quickCatalog).setOnClickListener(v -> navigate("Catalog"));
    }

    private void setupActiveTicket(View view) {
        // Now handled by updateTicketUI and fetchTicketsForUser
    }

    private void setupBrandCategories(View view) {
        LinearLayout brandContainer = view.findViewById(R.id.brandContainer);
        if (brandContainer == null) return;
        brandContainer.removeAllViews();

        String[] brands = {
                "Samsung", "Apple", "Xiaomi", "Oppo", "Vivo",
                "Realme", "Huawei", "Blackview", "HP", "ASUS",
                "Acer", "MSI", "Dell", "Lenovo", "Google"
        };

        // Unsplash placeholders for brand imagery
        String[] photos = {
                "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=150", // Samsung
                "https://images.unsplash.com/photo-1592899677977-9c10ca588bbd?w=150", // Apple
                "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=150", // Xiaomi
                "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=150", // Oppo
                "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=150", // Vivo
                "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=150", // Realme
                "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=150", // Huawei
                "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=150", // Blackview
                "https://images.unsplash.com/photo-1589561253898-768105ca91a8?w=150", // HP
                "https://images.unsplash.com/photo-1525547718501-0399975c2b00?w=150", // ASUS
                "https://images.unsplash.com/photo-1585241641334-03478546b3f7?w=150", // Acer
                "https://images.unsplash.com/photo-1591799264318-7e6ef8ddb7ea?w=150", // MSI
                "https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=150", // Dell
                "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=150", // Lenovo
                "https://images.unsplash.com/photo-1520189391637-14227ad14d25?w=150"  // Google
        };

        for (int i = 0; i < brands.length; i++) {
            final String brandName = brands[i];
            View card = getLayoutInflater().inflate(R.layout.item_brand_category, brandContainer, false);

            TextView tvName = card.findViewById(R.id.tvBrandName);
            de.hdodenhof.circleimageview.CircleImageView ivBrand = card.findViewById(R.id.ivBrand);

            tvName.setText(brandName);
            if (i < photos.length) {
                com.bumptech.glide.Glide.with(this).load(photos[i])
                        .placeholder(R.drawable.bg_pill_surface)
                        .into(ivBrand);
            }

            card.setOnClickListener(v -> navigate("Catalog"));
            brandContainer.addView(card);
        }
    }

    private void setupCategories(View view) {
        // Replaced by setupBrandCategories
    }

    private void setupEmergencyBanner(View view) {
        view.findViewById(R.id.emergencyBanner).setOnClickListener(v -> navigate("Hubs"));
    }

    private void navigate(String tab) {
        ((MainActivity) requireActivity()).navigateTo(tab);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (autoScrollTimer != null) {
            autoScrollTimer.cancel();
        }
        if (ticketListener != null) {
            FirebaseDbHelper.stopListening(ticketListener);
        }
    }
}
