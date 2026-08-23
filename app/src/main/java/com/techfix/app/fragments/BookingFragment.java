package com.techfix.app.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;
import com.techfix.app.R;
import com.techfix.app.activities.MainActivity;
import com.techfix.app.database.AppDatabase;
import com.techfix.app.firebase.FirebaseDbHelper;
import com.techfix.app.models.RepairTicket;
import com.techfix.app.utils.LocationHelper;
import com.techfix.app.utils.MockData;
import com.techfix.app.utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingFragment extends Fragment {

    private final String[] DATES = new String[4];
    private final String[] DATE_SUBS = new String[4];
    private static final String[] TIME_SLOTS = {
            "9:00 AM", "10:00 AM", "11:30 AM", "1:00 PM", "2:30 PM", "4:00 PM", "5:30 PM"
    };

    private int currentStep = 0;
    private String selectedDeviceType = "phone";
    private String selectedBranch = "Colombo Branch";
    private String selectedServiceType = "In-Store Drop-off";
    private int selectedDate = 0;
    private String selectedSlot = "";
    private Uri photoUri;

    private View[] stepContents;
    private View[] stepBars;
    private TextView tvStepLabel;
    private Button btnBack, btnContinue;
    private EditText etBrand, etModel, etIssue, etPickupAddress;
    private TextView tvPhotoAttached, tvColomboInv, tvGalleInv, tvColomboTech, tvGalleTech;

    // Camera launcher
    private final ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(), success -> {
                if (success && tvPhotoAttached != null) {
                    tvPhotoAttached.setVisibility(View.VISIBLE);
                }
            });

    private final ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) launchCamera();
            });

    private View rootView; // stored for rebuilds

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_booking, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvStepLabel = view.findViewById(R.id.tvStepLabel);
        btnBack     = view.findViewById(R.id.btnBack);
        btnContinue = view.findViewById(R.id.btnContinue);
        tvPhotoAttached = view.findViewById(R.id.tvPhotoAttached);

        stepContents = new View[]{
                view.findViewById(R.id.step1Content),
                view.findViewById(R.id.step2Content),
                view.findViewById(R.id.step3Content),
                view.findViewById(R.id.step4Content)
        };
        stepBars = new View[]{
                view.findViewById(R.id.step1),
                view.findViewById(R.id.step2),
                view.findViewById(R.id.step3),
                view.findViewById(R.id.step4)
        };

        etBrand  = view.findViewById(R.id.etBrand);
        etModel  = view.findViewById(R.id.etModel);
        etIssue  = view.findViewById(R.id.etIssue);
        etPickupAddress = view.findViewById(R.id.etPickupAddress);

        tvColomboInv = view.findViewById(R.id.tvColomboInventory);
        tvGalleInv   = view.findViewById(R.id.tvGalleInventory);
        tvColomboTech = view.findViewById(R.id.tvColomboTechnicians);
        tvGalleTech   = view.findViewById(R.id.tvGalleTechnicians);

        generateDynamicDates();

        setupDeviceTypeButtons(view);
        setupBrandChips(view);
        setupBranchCards(view);
        setupServiceTypeCards(view);
        setupDateGrid(view);
        setupTimeSlots(view);
        setupAttachPhoto(view);
        setupLocationDetect(view);

        checkArguments();

        btnBack.setOnClickListener(v -> { if (currentStep > 0) goToStep(currentStep - 1); });
        btnContinue.setOnClickListener(v -> {
            if (currentStep < 3) {
                if (canProceed()) goToStep(currentStep + 1);
                else Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                confirmBooking();
            }
        });

        goToStep(0);
    }

    private void generateDynamicDates() {
        SimpleDateFormat sdfLabel = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat sdfSub = new SimpleDateFormat("MMM dd", Locale.getDefault());
        
        for (int i = 0; i < 4; i++) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.add(java.util.Calendar.DATE, i);
            
            if (i == 0) DATES[i] = "Today";
            else if (i == 1) DATES[i] = "Tomorrow";
            else DATES[i] = sdfLabel.format(cal.getTime());
            
            DATE_SUBS[i] = sdfSub.format(cal.getTime());
        }
    }

    private void checkArguments() {
        if (getArguments() != null) {
            String title = getArguments().getString("service_title", "");
            String deviceType = getArguments().getString("device_type", "phone");
            String description = getArguments().getString("service_description", "");
            String branch = getArguments().getString("selected_branch", "");

            if (branch != null && !branch.isEmpty()) {
                selectBranchUI(branch);
            }

            if (!title.isEmpty()) {
                // Pre-fill type (map 'computer' to 'laptop' for UI buttons)
                String typeKey = deviceType;
                if ("computer".equals(deviceType)) typeKey = "laptop";
                
                selectDeviceType(typeKey, 
                        rootView.findViewById(R.id.btnPhone), 
                        rootView.findViewById(R.id.btnLaptop), 
                        rootView.findViewById(R.id.btnTablet));
                
                // Pre-fill brand & model if identifiable
                if (title.contains("iPhone") || title.contains("iPad") || title.contains("MacBook")) {
                    etBrand.setText("Apple");
                } else if (title.contains("Samsung") || title.contains("Galaxy")) {
                    etBrand.setText("Samsung");
                } else if (title.contains("Google") || title.contains("Pixel")) {
                    etBrand.setText("Google");
                }

                // Pre-fill issue
                etIssue.setText(title + "\n" + description);

                // Update chip styles to reflect pre-filled brand
                String brand = etBrand.getText().toString().trim();
                if (!brand.isEmpty()) {
                    LinearLayout brandChips = rootView.findViewById(R.id.brandChips);
                    if (brandChips != null) {
                        for (int i = 0; i < brandChips.getChildCount(); i++) {
                            TextView c = (TextView) brandChips.getChildAt(i);
                            boolean active = c.getText().toString().equalsIgnoreCase(brand);
                            c.setBackgroundResource(active ? R.drawable.bg_pill_accent : R.drawable.bg_pill_surface);
                            c.setTextColor(active ? Color.parseColor("#0066FF") :
                                    getResources().getColor(R.color.text_dim, null));
                        }
                    }
                }
            }
        }
    }

    private void setupDeviceTypeButtons(View view) {
        LinearLayout btnPhone  = view.findViewById(R.id.btnPhone);
        LinearLayout btnLaptop = view.findViewById(R.id.btnLaptop);
        LinearLayout btnTablet = view.findViewById(R.id.btnTablet);

        btnPhone.setOnClickListener(v  -> {
            selectDeviceType("phone",  btnPhone, btnLaptop, btnTablet);
            checkAvailability();
        });
        btnLaptop.setOnClickListener(v -> {
            selectDeviceType("laptop", btnPhone, btnLaptop, btnTablet);
            checkAvailability();
        });
        btnTablet.setOnClickListener(v -> {
            selectDeviceType("tablet", btnPhone, btnLaptop, btnTablet);
            checkAvailability();
        });
    }

    private void selectDeviceType(String type, LinearLayout... btns) {
        selectedDeviceType = type;
        String[] keys = {"phone", "laptop", "tablet"};
        for (int i = 0; i < btns.length; i++) {
            btns[i].setBackgroundResource(keys[i].equals(type)
                    ? R.drawable.bg_btn_primary : R.drawable.bg_card);
        }
    }

    private void setupBrandChips(View view) {
        LinearLayout brandChips = view.findViewById(R.id.brandChips);
        String[] brands = {"Apple", "Samsung", "Google", "OnePlus", "Xiaomi"};
        brandChips.removeAllViews();
        for (String brand : brands) {
            TextView chip = new TextView(requireContext());
            chip.setText(brand);
            chip.setTextSize(13f);
            chip.setPadding(32, 16, 32, 16);
            chip.setClickable(true);
            chip.setBackgroundResource(R.drawable.bg_pill_surface);
            chip.setTextColor(getResources().getColor(R.color.text_dim, null));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(8);
            chip.setLayoutParams(lp);
            chip.setOnClickListener(v -> {
                etBrand.setText(brand);
                // Update chip styles
                for (int i = 0; i < brandChips.getChildCount(); i++) {
                    TextView c = (TextView) brandChips.getChildAt(i);
                    boolean active = c.getText().toString().equals(brand);
                    c.setBackgroundResource(active ? R.drawable.bg_pill_accent : R.drawable.bg_pill_surface);
                    c.setTextColor(active ? Color.parseColor("#0066FF") :
                            getResources().getColor(R.color.text_dim, null));
                }
            });
            brandChips.addView(chip);
        }
    }

    private void setupBranchCards(View view) {
        view.findViewById(R.id.branchColomboCard).setOnClickListener(v -> selectBranchUI("Colombo Branch"));
        view.findViewById(R.id.branchGalleCard).setOnClickListener(v -> selectBranchUI("Galle Branch"));
    }

    private void selectBranchUI(String branch) {
        selectedBranch = branch;
        LinearLayout cardColombo = rootView.findViewById(R.id.branchColomboCard);
        LinearLayout cardGalle   = rootView.findViewById(R.id.branchGalleCard);

        if (cardColombo == null || cardGalle == null) return;

        if ("Colombo Branch".equals(branch)) {
            cardColombo.setBackgroundResource(R.drawable.bg_card_accent_border);
            cardGalle.setBackgroundResource(R.drawable.bg_card);
        } else {
            cardGalle.setBackgroundResource(R.drawable.bg_card_accent_border);
            cardColombo.setBackgroundResource(R.drawable.bg_card);
        }
    }

    private void setupServiceTypeCards(View view) {
        LinearLayout dropoff = view.findViewById(R.id.serviceDropoff);
        LinearLayout courier = view.findViewById(R.id.serviceCourier);
        LinearLayout pickupRow = view.findViewById(R.id.pickupAddressRow);

        dropoff.setOnClickListener(v -> {
            selectedServiceType = "In-Store Drop-off";
            dropoff.setBackgroundResource(R.drawable.bg_card_accent_border);
            courier.setBackgroundResource(R.drawable.bg_card);
            pickupRow.setVisibility(View.GONE);
        });
        courier.setOnClickListener(v -> {
            selectedServiceType = "Courier Pickup";
            courier.setBackgroundResource(R.drawable.bg_card_accent_border);
            dropoff.setBackgroundResource(R.drawable.bg_card);
            pickupRow.setVisibility(View.VISIBLE);
        });
    }

    private void setupDateGrid(View view) {
        LinearLayout dateGrid = view.findViewById(R.id.dateGrid);
        dateGrid.removeAllViews();
        for (int i = 0; i < DATES.length; i++) {
            final int idx = i;
            LinearLayout card = new LinearLayout(requireContext());
            card.setOrientation(LinearLayout.VERTICAL);
            card.setGravity(0x11); // center
            card.setPadding(12, 14, 12, 14);
            card.setBackgroundResource(idx == selectedDate
                    ? R.drawable.bg_btn_primary : R.drawable.bg_card);

            TextView tvLabel = new TextView(requireContext());
            tvLabel.setText(DATES[i]);
            tvLabel.setTextSize(13f);
            tvLabel.setTextColor(idx == selectedDate ? Color.WHITE :
                    getResources().getColor(R.color.text, null));
            tvLabel.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView tvSub = new TextView(requireContext());
            tvSub.setText(DATE_SUBS[i]);
            tvSub.setTextSize(11f);
            tvSub.setTextColor(idx == selectedDate ? 0xCCFFFFFF :
                    getResources().getColor(R.color.text_dim, null));

            card.addView(tvLabel);
            card.addView(tvSub);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            lp.setMarginEnd(6);
            card.setLayoutParams(lp);

            card.setOnClickListener(v -> {
                selectedDate = idx;
                selectedSlot = "";
                setupDateGrid(rootView);
                setupTimeSlots(rootView);
            });
            dateGrid.addView(card);
        }
    }

    private void setupTimeSlots(View view) {
        FlexboxLayout slotGrid = view.findViewById(R.id.timeSlotGrid);
        slotGrid.removeAllViews();

        for (int i = 0; i < TIME_SLOTS.length; i++) {
            final String slot = TIME_SLOTS[i];
            TextView btn = new TextView(requireContext());
            btn.setText(slot);
            btn.setTextSize(13f);
            btn.setPadding(24, 16, 24, 16);
            btn.setGravity(17);
            btn.setClickable(true);
            boolean active = slot.equals(selectedSlot);
            btn.setBackgroundResource(active ? R.drawable.bg_pill_accent : R.drawable.bg_card);
            btn.setTextColor(active ? Color.parseColor("#0066FF") :
                    getResources().getColor(R.color.text_dim, null));

            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 8, 8);
            btn.setLayoutParams(lp);

            btn.setOnClickListener(v -> {
                selectedSlot = slot;
                setupTimeSlots(rootView);
            });
            slotGrid.addView(btn);
        }
    }

    private void setupAttachPhoto(View view) {
        view.findViewById(R.id.btnAttachPhoto).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.CAMERA);
            } else {
                launchCamera();
            }
        });
    }

    private void launchCamera() {
        try {
            File photoFile = createImageFile();
            photoUri = FileProvider.getUriForFile(requireContext(),
                    requireContext().getPackageName() + ".fileprovider", photoFile);
            cameraLauncher.launch(photoUri);
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Could not open camera", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String name = "TECHFIX_" + ts + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(name, ".jpg", storageDir);
    }

    private void setupLocationDetect(View view) {
        view.findViewById(R.id.btnDetectLocation).setOnClickListener(v -> {
            LocationHelper.getNearestBranch(requireActivity(), new LocationHelper.LocationCallback() {
                @Override
                public void onLocationReceived(Location location, String nearest, double dist) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(),
                                "Nearest: " + nearest + " (" + String.format("%.1f", dist) + " km)",
                                Toast.LENGTH_LONG).show();
                        
                        // Select branch based on proximity AND inventory availability (which is checked in checkAvailability())
                        selectBranchUI(nearest);
                        checkAvailability();
                    });
                }
                @Override public void onPermissionDenied() {}
                @Override public void onError(String error) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Location error: " + error, Toast.LENGTH_SHORT).show());
                }
            });
        });
    }

    private boolean canProceed() {
        if (currentStep == 0) {
            return !etBrand.getText().toString().trim().isEmpty()
                    && !etModel.getText().toString().trim().isEmpty()
                    && !etIssue.getText().toString().trim().isEmpty();
        }
        if (currentStep == 1) {
            if ("Courier Pickup".equals(selectedServiceType)) {
                return !etPickupAddress.getText().toString().trim().isEmpty();
            }
            return true;
        }
        if (currentStep == 2) return !selectedSlot.isEmpty();
        return true;
    }

    private void goToStep(int step) {
        currentStep = step;
        String[] stepNames = {"Device & Issue", "Branch & Service", "Date & Time", "Confirm"};
        tvStepLabel.setText("Step " + (step + 1) + " of 4: " + stepNames[step]);

        for (int i = 0; i < stepContents.length; i++) {
            stepContents[i].setVisibility(i == step ? View.VISIBLE : View.GONE);
        }
        for (int i = 0; i < stepBars.length; i++) {
            stepBars[i].setBackgroundResource(i <= step
                    ? R.drawable.bg_btn_primary : R.color.glass_border);
        }

        btnBack.setVisibility(step > 0 ? View.VISIBLE : View.GONE);

        if (step == 1) {
            checkAvailability();
        }

        if (step == 3) {
            btnContinue.setText("Confirm & Book");
            populateSummary();
        } else {
            btnContinue.setText("Continue →");
        }
    }

    private void populateSummary() {
        if (getView() == null) return;
        String brand = etBrand.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String issue = etIssue.getText().toString().trim();

        TextView sv = getView().findViewById(R.id.summaryDeviceValue);
        if (sv != null) sv.setText(brand + " " + model);

        TextView si = getView().findViewById(R.id.summaryIssueValue);
        if (si != null) si.setText(issue);

        TextView sb = getView().findViewById(R.id.summaryBranchValue);
        if (sb != null) sb.setText(selectedBranch);

        TextView sd = getView().findViewById(R.id.summaryDateValue);
        if (sd != null) sd.setText(DATES[selectedDate] + ", " + DATE_SUBS[selectedDate] + " at " + selectedSlot);

        TextView icon = getView().findViewById(R.id.summaryDeviceIcon);
        if (icon != null) {
            if ("laptop".equals(selectedDeviceType)) icon.setText("💻");
            else if ("tablet".equals(selectedDeviceType)) icon.setText("📒");
            else icon.setText("📱");
        }
    }

    private void checkAvailability() {
        if (rootView == null || tvColomboInv == null || tvGalleInv == null || tvColomboTech == null || tvGalleTech == null) return;

        String model = etModel.getText().toString().toLowerCase().trim();
        String brand = etBrand.getText().toString().toLowerCase().trim();
        
        // Find matching part in mock inventory
        com.techfix.app.models.SparePart matchingPart = null;
        List<com.techfix.app.models.SparePart> parts = MockData.getSpareParts();
        for (com.techfix.app.models.SparePart part : parts) {
            if (part.getCompatibleDevices() != null) {
                for (String device : part.getCompatibleDevices()) {
                    if (device.toLowerCase().contains(model) || (!model.isEmpty() && device.toLowerCase().contains(brand))) {
                        matchingPart = part;
                        break;
                    }
                }
            }
            if (matchingPart != null) break;
        }

        // Branch data from MockData
        List<com.techfix.app.models.Branch> branches = MockData.getBranches();
        com.techfix.app.models.Branch colombo = branches.get(0);
        com.techfix.app.models.Branch galle = branches.get(1);

        // Update Colombo Status
        boolean colomboInStock = matchingPart == null || matchingPart.getColomboStock() > 0;
        int colomboTechs = colombo.getTechniciansAvailable();
        
        tvColomboInv.setVisibility(View.VISIBLE);
        tvColomboInv.setText(colomboInStock ? "✅ Required parts in stock" : "⚠️ Parts on order (2-3 days)");
        tvColomboInv.setTextColor(colomboInStock ? Color.parseColor("#2ECC71") : Color.parseColor("#F39C12"));
        
        tvColomboTech.setVisibility(View.VISIBLE);
        tvColomboTech.setText("👨‍🔧 " + colomboTechs + " Technicians available");
        tvColomboTech.setTextColor(colomboTechs > 0 ? Color.parseColor("#3498DB") : Color.parseColor("#E74C3C"));

        // Update Galle Status
        boolean galleInStock = matchingPart == null || matchingPart.getGalleStock() > 0;
        int galleTechs = galle.getTechniciansAvailable();

        tvGalleInv.setVisibility(View.VISIBLE);
        tvGalleInv.setText(galleInStock ? "✅ Required parts in stock" : "⚠️ Parts on order (4-5 days)");
        tvGalleInv.setTextColor(galleInStock ? Color.parseColor("#2ECC71") : Color.parseColor("#F39C12"));
        
        tvGalleTech.setVisibility(View.VISIBLE);
        tvGalleTech.setText("👨‍🔧 " + galleTechs + " Technicians available");
        tvGalleTech.setTextColor(galleTechs > 0 ? Color.parseColor("#3498DB") : Color.parseColor("#E74C3C"));

        // Smart Auto-selection: If current branch has no stock but other has, switch it.
        if (selectedBranch.contains("Colombo") && !colomboInStock && galleInStock) {
            selectBranchUI("Galle Branch");
            Toast.makeText(requireContext(), "Auto-switched to Galle Branch due to part availability", Toast.LENGTH_SHORT).show();
        } else if (selectedBranch.contains("Galle") && !galleInStock && colomboInStock) {
            selectBranchUI("Colombo Branch");
            Toast.makeText(requireContext(), "Auto-switched to Colombo Branch due to part availability", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmBooking() {
        SessionManager session = ((MainActivity) requireActivity()).getSession();
        String brand = etBrand.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String issue = etIssue.getText().toString().trim();

        RepairTicket ticket = new RepairTicket();
        String ticketId = "TF-" + (9000 + (int)(Math.random() * 999));
        ticket.setId(ticketId);
        ticket.setDeviceModel(brand + " " + model);
        ticket.setDeviceType(selectedDeviceType);
        ticket.setCategory(selectedDeviceType.substring(0, 1).toUpperCase() + selectedDeviceType.substring(1));
        ticket.setIssue(issue);
        ticket.setBranch(selectedBranch);
        ticket.setStatus("Request Received");
        ticket.setProgressPercent(10);
        ticket.setCurrentStepIndex(0);
        ticket.setPaid(false);
        ticket.setUserId(session.getUid());
        ticket.setCustomerName(session.getFullName());
        ticket.setCustomerPhone(session.getPhone());
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        ticket.setCreatedAt(now);
        ticket.setEstimatedCompletion("Pending Diagnostics");
        
        // Default technician info (Pending)
        ticket.setTechnicianName("Unassigned");
        ticket.setTechnicianRole("Senior Technician");
        ticket.setTechnicianPhone("");
        ticket.setTechnicianAvatar("");
        ticket.setTechnicianRating(0.0f);
        
        // Initial timeline steps
        List<RepairTicket.TimelineStep> steps = new ArrayList<>();
        steps.add(new RepairTicket.TimelineStep(1, "Request Received", "We have received your repair request.", now, true, false));
        steps.add(new RepairTicket.TimelineStep(2, "Diagnostics", "Technician will inspect your device.", "", false, true));
        steps.add(new RepairTicket.TimelineStep(3, "Repair in Progress", "Fixing the reported issues.", "", false, false));
        steps.add(new RepairTicket.TimelineStep(4, "Ready for Pickup", "Your device is ready!", "", false, false));
        ticket.setTimelineSteps(steps);

        // Initial status log
        List<RepairTicket.StatusLogEntry> logs = new ArrayList<>();
        logs.add(new RepairTicket.StatusLogEntry("log1", "Booking Created", "Your appointment is confirmed for " + DATE_SUBS[selectedDate] + " at " + selectedSlot, now, "System", "success"));
        ticket.setStatusLogs(logs);

        if (photoUri != null) {
            Toast.makeText(requireContext(), "Photo Uploaded!", Toast.LENGTH_SHORT).show();
            ticket.setDevicePhoto(getBase64FromUri(photoUri));
        }
        saveTicketToDb(ticket);
    }

    private void saveTicketToDb(RepairTicket ticket) {
        // 1. Save to Firebase
        FirebaseDbHelper.submitAppointment(ticket, new FirebaseDbHelper.DataCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                // 2. Save to Room Local DB
                new Thread(() -> {
                    AppDatabase.getInstance(requireContext()).repairTicketDao().insert(ticket);
                    if (isAdded()) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "✓ Booking Confirmed & Saved Locally!", Toast.LENGTH_SHORT).show();
                            ((MainActivity) requireActivity()).navigateTo("Track");
                        });
                    }
                }).start();
            }

            @Override
            public void onFailure(String error) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getBase64FromUri(Uri uri) {
        try {
            android.content.Context ctx = requireContext();
            java.io.InputStream inputStream = ctx.getContentResolver().openInputStream(uri);
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(inputStream);
            if (inputStream != null) inputStream.close();

            // Resize to prevent DB bloat (Max width 600px for Base64 efficiency)
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float ratio = (float) width / (float) height;
            int newWidth = 600;
            int newHeight = (int) (newWidth / ratio);
            android.graphics.Bitmap resized = android.graphics.Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            resized.compress(android.graphics.Bitmap.CompressFormat.JPEG, 60, outputStream);
            byte[] bytes = outputStream.toByteArray();
            return "data:image/jpeg;base64," + android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
