package com.techfix.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techfix.app.ui.branches.BranchesFragment;
import com.techfix.app.ui.profile.ProfileFragment;
import com.techfix.app.ui.repairs.MyRepairsFragment;
import com.techfix.app.ui.services.ServicesFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadFragment(new BranchesFragment());
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();

            if (id == R.id.nav_branches) {
                fragment = new BranchesFragment();
            } else if (id == R.id.nav_services) {
                fragment = new ServicesFragment();
            } else if (id == R.id.nav_repairs) {
                fragment = new MyRepairsFragment();
            } else {
                fragment = new ProfileFragment();
            }

            loadFragment(fragment);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}