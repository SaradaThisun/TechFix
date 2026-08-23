package com.techfix.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.techfix.app.R;
import com.techfix.app.fragments.BookingFragment;
import com.techfix.app.fragments.BranchesFragment;
import com.techfix.app.fragments.DispatchFragment;
import com.techfix.app.fragments.HistoryFragment;
import com.techfix.app.fragments.HomeFragment;
import com.techfix.app.fragments.InventoryFragment;
import com.techfix.app.fragments.PaymentFragment;
import com.techfix.app.fragments.ServicesFragment;
import com.techfix.app.fragments.TrackingFragment;
import com.techfix.app.fragments.WorkbenchFragment;
import com.techfix.app.utils.FormatUtils;
import com.techfix.app.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    // Navigation views — customer
    private LinearLayout navHome, navCatalog, navBook, navTrack, navHubs, navHistory;
    // Navigation views — staff
    private LinearLayout navDispatch, navWorkbench, navInventory;
    private LinearLayout bottomNavBar, staffNavBar;

    private SessionManager session;
    private String currentTab = "Home";
    private boolean isStaffMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(this);
        isStaffMode = session.isStaff();

        bindCustomerNav();
        bindStaffNav();
        applyMode();

        if (isStaffMode) {
            switchTo("Dispatch");
        } else {
            switchTo("Home");
        }
    }

    private void bindCustomerNav() {
        bottomNavBar = findViewById(R.id.bottomNavBar);
        navHome    = findViewById(R.id.navHome);
        navCatalog = findViewById(R.id.navCatalog);
        navBook    = findViewById(R.id.navBook);
        navTrack   = findViewById(R.id.navTrack);
        navHubs    = findViewById(R.id.navHubs);
        navHistory = findViewById(R.id.navHistory);

        navHome.setOnClickListener(v    -> switchTo("Home"));
        navCatalog.setOnClickListener(v -> switchTo("Catalog"));
        navBook.setOnClickListener(v    -> switchTo("Book"));
        navTrack.setOnClickListener(v   -> switchTo("Track"));
        navHubs.setOnClickListener(v    -> switchTo("Hubs"));
        navHistory.setOnClickListener(v -> switchTo("History"));
    }

    private void bindStaffNav() {
        staffNavBar    = findViewById(R.id.staffNavBar);
        navDispatch    = findViewById(R.id.navDispatch);
        navWorkbench   = findViewById(R.id.navWorkbench);
        navInventory   = findViewById(R.id.navInventory);

        navDispatch.setOnClickListener(v   -> switchTo("Dispatch"));
        navWorkbench.setOnClickListener(v  -> switchTo("Workbench"));
        navInventory.setOnClickListener(v  -> switchTo("Inventory"));
    }

    private void applyMode() {
        if (isStaffMode) {
            bottomNavBar.setVisibility(View.GONE);
            staffNavBar.setVisibility(View.VISIBLE);
        } else {
            bottomNavBar.setVisibility(View.VISIBLE);
            staffNavBar.setVisibility(View.GONE);
        }
    }

    public void switchTo(String tab) {
        switchTo(tab, null);
    }

    public void switchTo(String tab, Bundle args) {
        currentTab = tab;
        Fragment fragment = getFragmentForTab(tab);
        if (args != null) {
            fragment.setArguments(args);
        }
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragmentContainer, fragment);
        tx.commit();
    }

    private Fragment getFragmentForTab(String tab) {
        switch (tab) {
            case "Catalog":   return new ServicesFragment();
            case "Book":      return new BookingFragment();
            case "Track":     return new TrackingFragment();
            case "Hubs":      return new BranchesFragment();
            case "History":   return new HistoryFragment();
            case "Payment":   return new PaymentFragment();
            case "Dispatch":  return new DispatchFragment();
            case "Workbench": return new WorkbenchFragment();
            case "Inventory": return new InventoryFragment();
            default:          return new HomeFragment();
        }
    }

    /** Called from fragments to navigate between tabs */
    public void navigateTo(String tab) {
        switchTo(tab, null);
    }

    public void navigateTo(String tab, Bundle args) {
        switchTo(tab, args);
    }

    public void logout() {
        com.techfix.app.firebase.FirebaseAuthHelper.signOut();
        session.clearSession();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void showProfileDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View v = getLayoutInflater().inflate(R.layout.dialog_profile, null);
        dialog.setContentView(v);

        ((TextView) v.findViewById(R.id.dialogAvatar)).setText(FormatUtils.getInitial(session.getFullName()));
        ((TextView) v.findViewById(R.id.dialogName)).setText(session.getFullName());
        ((TextView) v.findViewById(R.id.dialogRole)).setText(session.getRole());
        ((TextView) v.findViewById(R.id.dialogEmail)).setText(session.getEmail());
        ((TextView) v.findViewById(R.id.dialogPhone)).setText(session.getPhone());
        ((TextView) v.findViewById(R.id.dialogBranch)).setText(session.getBranch());

        v.findViewById(R.id.btnSignOut).setOnClickListener(view -> {
            dialog.dismiss();
            logout();
        });

        dialog.show();
    }

    public SessionManager getSession() {
        return session;
    }
}
