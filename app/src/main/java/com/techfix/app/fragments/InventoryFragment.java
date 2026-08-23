package com.techfix.app.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.activities.MainActivity;
import com.techfix.app.adapters.SparePartsAdapter;
import com.techfix.app.database.AppDatabase;
import com.techfix.app.firebase.FirebaseDbHelper;
import com.techfix.app.models.PartMovementLog;
import com.techfix.app.models.SparePart;
import com.techfix.app.utils.FormatUtils;
import com.techfix.app.utils.MockData;
import com.techfix.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {

    private static final String[] CATEGORIES = {
            "All", "Screen Assemblies", "Batteries", "Charging Ports", "Logic Boards", "Camera Modules"
    };

    private RecyclerView rvParts;
    private SparePartsAdapter adapter;
    private List<SparePart> allParts;
    private List<SparePart> filtered;
    private String activeBranch = "Colombo Branch";
    private String activeCategory = "All";
    private String searchQuery = "";
    private boolean showLowOnly = false;
    private LinearLayout categoryChips;
    private TextView tabColombo, tabGalle;
    private TextView tvSkus, tvUnits, tvLow;
    private android.content.Context appContext;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        appContext = requireContext().getApplicationContext();
        db = AppDatabase.getInstance(appContext);
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager session = ((MainActivity) requireActivity()).getSession();
        TextView tvAvatar = view.findViewById(R.id.tvAvatar);
        if (tvAvatar != null) tvAvatar.setText(FormatUtils.getInitial(session.getFullName()));
        
        view.findViewById(R.id.profileArea).setOnClickListener(v -> 
                ((MainActivity) requireActivity()).showProfileDialog());

        allParts  = new ArrayList<>();
        filtered  = new ArrayList<>();

        tabColombo     = view.findViewById(R.id.tabColombo);
        tabGalle       = view.findViewById(R.id.tabGalle);
        categoryChips  = view.findViewById(R.id.categoryChips);
        tvSkus         = view.findViewById(R.id.tvTotalSkus);
        tvUnits        = view.findViewById(R.id.tvUnitsInLab);
        tvLow          = view.findViewById(R.id.tvLowCount);
        rvParts        = view.findViewById(R.id.rvParts);

        tabColombo.setOnClickListener(v -> selectBranch("Colombo Branch"));
        tabGalle.setOnClickListener(v   -> selectBranch("Galle Branch"));

        view.findViewById(R.id.btnLowFilter).setOnClickListener(v -> {
            showLowOnly = !showLowOnly;
            v.setBackgroundResource(showLowOnly ? R.drawable.bg_warning : R.drawable.bg_card);
            applyFilters();
        });

        view.findViewById(R.id.btnCourier).setOnClickListener(v ->
                loadAndShowRealLogs());

        view.findViewById(R.id.btnAddPart).setOnClickListener(v ->
                showAddPartDialog());

        EditText etSearch = view.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                searchQuery = s.toString().toLowerCase();
                applyFilters();
            }
        });

        adapter = new SparePartsAdapter(requireContext(), filtered, activeBranch,
                new SparePartsAdapter.InventoryListener() {
                    @Override
                    public void onTransfer(SparePart part) { showTransferDialog(part); }
                    @Override
                    public void onRestock(SparePart part) { showRestockDialog(part); }
                    @Override
                    public void onLogUsed(SparePart part) { logUsed(part); }
                });
        rvParts.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvParts.setAdapter(adapter);

        buildCategoryChips();
        loadRealData();
        observeLocalData();
    }

    private void observeLocalData() {
        db.sparePartDao().getAllParts()
                .observe(getViewLifecycleOwner(), parts -> {
                    if (parts != null) {
                        allParts.clear();
                        allParts.addAll(parts);
                        applyFilters();
                    }
                });
    }

    private void loadRealData() {
        FirebaseDbHelper.fetchSpareParts(new FirebaseDbHelper.DataCallback<List<SparePart>>() {
            @Override
            public void onSuccess(List<SparePart> data) {
                if (isAdded()) {
                    syncMissingMockData(data);
                    
                    if (data != null && !data.isEmpty()) {
                        new Thread(() -> db.sparePartDao().insertAll(data)).start();
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Sync failed: " + error, Toast.LENGTH_SHORT).show();
                    syncMissingMockData(null);
                }
            }
        });
    }

    private void syncMissingMockData(List<SparePart> existingData) {
        List<SparePart> mock = MockData.getSpareParts();
        List<SparePart> toSync = new ArrayList<>();

        for (SparePart m : mock) {
            SparePart existing = null;
            if (existingData != null) {
                for (SparePart e : existingData) {
                    if (e != null && e.getPartId() != null && e.getPartId().equals(m.getPartId())) {
                        existing = e;
                        break;
                    }
                }
            }
            
            // If missing or missing image URL, sync it
            if (existing == null || existing.getImageUrl() == null) {
                toSync.add(m);
            }
        }

        if (!toSync.isEmpty()) {
            new Thread(() -> db.sparePartDao().insertAll(toSync)).start();
            for (SparePart p : toSync) {
                FirebaseDbHelper.saveSparePart(p, new FirebaseDbHelper.DataCallback<Void>() {
                    @Override public void onSuccess(Void d) {}
                    @Override public void onFailure(String e) {}
                });
            }
        }
    }

    private void selectBranch(String branch) {
        activeBranch = branch;
        tabColombo.setBackgroundResource("Colombo Branch".equals(branch)
                ? R.drawable.bg_btn_primary : R.drawable.bg_pill_surface);
        tabColombo.setTextColor("Colombo Branch".equals(branch) ? Color.WHITE
                : requireContext().getColor(R.color.text_dim));
        tabGalle.setBackgroundResource("Galle Branch".equals(branch)
                ? R.drawable.bg_btn_primary : R.drawable.bg_pill_surface);
        tabGalle.setTextColor("Galle Branch".equals(branch) ? Color.WHITE
                : requireContext().getColor(R.color.text_dim));

        adapter.setActiveBranch(branch);
        updateStats();
        applyFilters();
    }

    private void buildCategoryChips() {
        categoryChips.removeAllViews();
        for (String cat : CATEGORIES) {
            TextView chip = new TextView(requireContext());
            chip.setText(cat);
            chip.setTextSize(12f);
            chip.setPadding(24, 14, 24, 14);
            chip.setClickable(true);
            boolean active = cat.equals(activeCategory);
            chip.setBackgroundResource(active ? R.drawable.bg_btn_primary : R.drawable.bg_pill_surface);
            chip.setTextColor(active ? Color.WHITE : requireContext().getColor(R.color.text_dim));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(8);
            chip.setLayoutParams(lp);
            chip.setOnClickListener(v -> {
                activeCategory = cat;
                buildCategoryChips();
                applyFilters();
            });
            categoryChips.addView(chip);
        }
    }

    private void applyFilters() {
        filtered.clear();
        for (SparePart p : allParts) {
            boolean matchCat = "All".equals(activeCategory) || p.getCategory().equals(activeCategory);
            boolean matchSearch = searchQuery.isEmpty()
                    || p.getName().toLowerCase().contains(searchQuery)
                    || p.getPartId().toLowerCase().contains(searchQuery);
            boolean matchLow = !showLowOnly || !"OK".equals(p.getStockStatus(activeBranch));
            if (matchCat && matchSearch && matchLow) filtered.add(p);
        }
        adapter.notifyDataSetChanged();
        updateStats();
    }

    private void updateStats() {
        tvSkus.setText(String.valueOf(allParts.size()));
        int total = 0;
        int low = 0;
        for (SparePart p : allParts) {
            total += p.getStockForBranch(activeBranch);
            if (!"OK".equals(p.getStockStatus(activeBranch))) low++;
        }
        tvUnits.setText(String.valueOf(total));
        tvLow.setText(String.valueOf(low));
    }

    private void showAddPartDialog() {
        View v = getLayoutInflater().inflate(R.layout.dialog_add_part, null);
        EditText etName = v.findViewById(R.id.etPartName);
        EditText etSku  = v.findViewById(R.id.etPartSku);
        EditText etCol  = v.findViewById(R.id.etColomboStock);
        EditText etGal  = v.findViewById(R.id.etGalleStock);
        EditText etImg  = v.findViewById(R.id.etImageUrl);
        android.widget.Spinner spn = v.findViewById(R.id.spnCategory);

        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, java.util.Arrays.copyOfRange(CATEGORIES, 1, CATEGORIES.length));
        spn.setAdapter(adapter);

        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Add New Spare Part")
                .setView(v)
                .setPositiveButton("Add Part", (d, w) -> {
                    String name = etName.getText().toString().trim();
                    String sku = etSku.getText().toString().trim();
                    String img = etImg.getText().toString().trim();
                    if (name.isEmpty() || sku.isEmpty()) return;

                    SparePart p = new SparePart();
                    p.setId("part_" + System.currentTimeMillis());
                    p.setName(name);
                    p.setPartId(sku);
                    p.setCategory(spn.getSelectedItem().toString());
                    p.setColomboStock(etCol.getText().toString().isEmpty() ? 0 : Integer.parseInt(etCol.getText().toString()));
                    p.setGalleStock(etGal.getText().toString().isEmpty() ? 0 : Integer.parseInt(etGal.getText().toString()));
                    p.setImageUrl(img.isEmpty() ? null : img);
                    p.setUnitCostLKR(5000); // Default
                    p.setOem(true);

                    FirebaseDbHelper.saveSparePart(p, new FirebaseDbHelper.DataCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            new Thread(() -> db.sparePartDao().insert(p)).start();
                            saveLog(p, "ADD", Math.max(p.getColomboStock(), p.getGalleStock()));
                            loadRealData();
                        }
                        @Override public void onFailure(String error) {}
                    });
                })
                .setNegativeButton("Cancel", null).show();
    }

    private void showRestockDialog(SparePart part) {
        View v = getLayoutInflater().inflate(R.layout.dialog_restock, null);
        ((TextView) v.findViewById(R.id.tvRestockTitle)).setText("Restock: " + part.getName());
        EditText etQty = v.findViewById(R.id.etQuantity);

        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Restock Inventory")
                .setView(v)
                .setPositiveButton("Restock", (d, w) -> {
                    String qtyStr = etQty.getText().toString();
                    if (qtyStr.isEmpty()) return;
                    int add = Integer.parseInt(qtyStr);

                    int colombo = part.getColomboStock();
                    int galle = part.getGalleStock();

                    if (activeBranch.contains("Colombo")) colombo += add;
                    else galle += add;

                    int finalCol = colombo;
                    int finalGal = galle;
                    FirebaseDbHelper.updateSparePartStock(part.getId(), colombo, galle, new FirebaseDbHelper.DataCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            new Thread(() -> {
                                part.setColomboStock(finalCol);
                                part.setGalleStock(finalGal);
                                db.sparePartDao().update(part);
                            }).start();
                            saveLog(part, "RESTOCK", add);
                            Toast.makeText(requireContext(), "Restocked " + add + " units to " + activeBranch, Toast.LENGTH_SHORT).show();
                            loadRealData();
                        }
                        @Override public void onFailure(String error) {
                            Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null).show();
    }

    private void saveLog(SparePart part, String type, int qty) {
        SessionManager session = ((MainActivity) requireActivity()).getSession();
        String ts = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(new java.util.Date());
        PartMovementLog log = new PartMovementLog(
                "log_" + System.currentTimeMillis(),
                part.getPartId(),
                part.getName(),
                type,
                qty,
                activeBranch,
                session.getFullName(),
                ts
        );
        FirebaseDbHelper.saveMovementLog(log, new FirebaseDbHelper.DataCallback<Void>() {
            @Override public void onSuccess(Void data) {}
            @Override public void onFailure(String error) {}
        });
    }

    private void loadAndShowRealLogs() {
        FirebaseDbHelper.fetchMovementLogs(new FirebaseDbHelper.DataCallback<List<PartMovementLog>>() {
            @Override
            public void onSuccess(List<PartMovementLog> logs) {
                if (isAdded()) {
                    StringBuilder sb = new StringBuilder("Inventory Audit Trail:\n\n");
                    for (PartMovementLog log : logs) {
                        String icon = "• ";
                        if ("TRANSFER".equals(log.getType())) icon = "⇄ ";
                        else if ("RESTOCK".equals(log.getType())) icon = "➕ ";
                        else if ("USE".equals(log.getType())) icon = "➖ ";
                        
                        sb.append(icon).append(log.getPartName()).append("\n");
                        sb.append("  Action: ").append(log.getType());
                        sb.append(" | Qty: ").append(log.getQuantity());
                        sb.append("\n  By: ").append(log.getStaffName());
                        sb.append(" | ").append(log.getTimestamp()).append("\n\n");
                    }
                    new android.app.AlertDialog.Builder(requireContext())
                            .setTitle("Real-time Inventory Logs")
                            .setMessage(sb.length() > 25 ? sb.toString() : "No audit logs found.")
                            .setPositiveButton("OK", null).show();
                }
            }
            @Override
            public void onFailure(String error) {
                Toast.makeText(requireContext(), "Audit log failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showTransferDialog(SparePart part) {
        String toBranch = activeBranch.contains("Colombo") ? "Galle Branch" : "Colombo Branch";
        int available = part.getStockForBranch(activeBranch);
        if (available == 0) {
            Toast.makeText(requireContext(), "No stock to transfer", Toast.LENGTH_SHORT).show();
            return;
        }
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Transfer to " + toBranch)
                .setMessage("Transfer 1 unit of " + part.getName() + " to " + toBranch + "?")
                .setPositiveButton("Confirm", (d, w) -> {
                    int colombo = part.getColomboStock();
                    int galle = part.getGalleStock();

                    if (activeBranch.contains("Colombo")) {
                        colombo--; galle++;
                    } else {
                        galle--; colombo++;
                    }

                    int finalCol = colombo;
                    int finalGal = galle;
                    FirebaseDbHelper.updateSparePartStock(part.getId(), colombo, galle, new FirebaseDbHelper.DataCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            if (isAdded()) {
                                new Thread(() -> {
                                    part.setColomboStock(finalCol);
                                    part.setGalleStock(finalGal);
                                    db.sparePartDao().update(part);
                                }).start();
                                saveLog(part, "TRANSFER", 1);
                                Toast.makeText(requireContext(), "Transferred 1 unit to " + toBranch, Toast.LENGTH_SHORT).show();
                                loadRealData();
                            }
                        }
                        @Override public void onFailure(String error) {
                            if (isAdded()) Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null).show();
    }

    private void logUsed(SparePart part) {
        int available = part.getStockForBranch(activeBranch);
        if (available == 0) {
            Toast.makeText(requireContext(), "No stock available to use", Toast.LENGTH_SHORT).show();
            return;
        }
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Log Used Part")
                .setMessage("Deduct 1 unit of " + part.getName() + " from " + activeBranch + "?")
                .setPositiveButton("Deduct", (d, w) -> {
                    int colombo = part.getColomboStock();
                    int galle = part.getGalleStock();

                    if (activeBranch.contains("Colombo")) colombo--;
                    else galle--;

                    int finalCol = colombo;
                    int finalGal = galle;
                    FirebaseDbHelper.updateSparePartStock(part.getId(), colombo, galle, new FirebaseDbHelper.DataCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            if (isAdded()) {
                                new Thread(() -> {
                                    part.setColomboStock(finalCol);
                                    part.setGalleStock(finalGal);
                                    db.sparePartDao().update(part);
                                }).start();
                                saveLog(part, "USE", 1);
                                Toast.makeText(requireContext(), "Deducted 1 unit from " + activeBranch, Toast.LENGTH_SHORT).show();
                                loadRealData();
                            }
                        }
                        @Override public void onFailure(String error) {
                            if (isAdded()) Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null).show();
    }
}
