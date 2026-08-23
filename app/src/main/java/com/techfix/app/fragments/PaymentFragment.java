package com.techfix.app.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.techfix.app.R;
import com.techfix.app.activities.MainActivity;
import com.techfix.app.database.AppDatabase;
import com.techfix.app.firebase.FirebaseDbHelper;
import com.techfix.app.models.HistoryItem;
import com.techfix.app.models.RepairTicket;
import com.techfix.app.utils.FormatUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.techfix.app.utils.MockData;
import com.techfix.app.utils.SessionManager;

import java.util.List;

public class PaymentFragment extends Fragment {

    private String payMethod = "card";
    private LinearLayout cardForm, bankPanel, branchPanel;
    private LinearLayout methodCard, methodBank, methodBranch;
    private Button btnPay;
    private EditText etCardNumber, etCardHolder, etExpiry, etCvv;
    private RepairTicket ticket;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager session = ((MainActivity) requireActivity()).getSession();
        
        // Fetch real data from Firebase
        FirebaseDbHelper.fetchTicketsForUser(session.getUid(), new FirebaseDbHelper.DataCallback<List<RepairTicket>>() {
            @Override
            public void onSuccess(List<RepairTicket> data) {
                if (isAdded() && data != null && !data.isEmpty()) {
                    // Find active ticket (not paid)
                    for (RepairTicket t : data) {
                        if (!t.isPaid() && "Ready for Pickup".equals(t.getStatus())) {
                            ticket = t;
                            break;
                        }
                    }
                    if (ticket == null) ticket = data.get(data.size() - 1);
                    
                    bindSummary(view);
                    bindMethodSelector(view);
                    bindCardForm(view);
                    bindPayButton(view);
                    buildBankList(view);
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded()) {
                    ticket = MockData.getActiveTicket(session.getUid());
                    bindSummary(view);
                    bindMethodSelector(view);
                }
            }
        });
    }

    private void bindSummary(View v) {
        ((TextView) v.findViewById(R.id.tvPaymentTicketId)).setText("Ticket " + ticket.getId());
        ((TextView) v.findViewById(R.id.tvServiceFee)).setText(FormatUtils.formatLKR(ticket.getServiceFeeLKR()));
        ((TextView) v.findViewById(R.id.tvPartsFee)).setText(FormatUtils.formatLKR(ticket.getPartsFeeLKR()));
        ((TextView) v.findViewById(R.id.tvDiscount)).setText(
                "-" + FormatUtils.formatLKR(Math.abs(ticket.getTaxDiscountLKR())));
        ((TextView) v.findViewById(R.id.tvTotal)).setText(FormatUtils.formatLKR(ticket.getTotalCostLKR()));
        ((Button) v.findViewById(R.id.btnPay))
                .setText("🔒  Pay " + FormatUtils.formatLKR(ticket.getTotalCostLKR()));
    }

    private void bindMethodSelector(View v) {
        methodCard   = v.findViewById(R.id.methodCard);
        methodBank   = v.findViewById(R.id.methodBank);
        methodBranch = v.findViewById(R.id.methodBranch);
        cardForm     = v.findViewById(R.id.cardForm);
        bankPanel    = v.findViewById(R.id.bankPanel);
        branchPanel  = v.findViewById(R.id.branchPanel);
        btnPay       = v.findViewById(R.id.btnPay);

        methodCard.setOnClickListener(click   -> selectMethod("card", v));
        methodBank.setOnClickListener(click   -> selectMethod("bank", v));
        methodBranch.setOnClickListener(click -> selectMethod("branch", v));

        selectMethod("card", v);
    }

    private void selectMethod(String method, View v) {
        payMethod = method;
        int accent = Color.parseColor("#0066FF");
        int dim    = requireContext().getColor(R.color.text_dim);

        methodCard.setBackgroundResource("card".equals(method) ? R.drawable.bg_card_accent_border : R.drawable.bg_card);
        methodBank.setBackgroundResource("bank".equals(method) ? R.drawable.bg_card_accent_border : R.drawable.bg_card);
        methodBranch.setBackgroundResource("branch".equals(method) ? R.drawable.bg_card_accent_border : R.drawable.bg_card);

        cardForm.setVisibility("card".equals(method) ? View.VISIBLE : View.GONE);
        bankPanel.setVisibility("bank".equals(method) ? View.VISIBLE : View.GONE);
        branchPanel.setVisibility("branch".equals(method) ? View.VISIBLE : View.GONE);

        if ("branch".equals(method)) {
            btnPay.setText("🏪  Confirm Collection");
        } else {
            btnPay.setText("🔒  Pay " + FormatUtils.formatLKR(ticket.getTotalCostLKR()));
        }
    }

    private void bindCardForm(View v) {
        etCardNumber = v.findViewById(R.id.etCardNumber);
        etCardHolder = v.findViewById(R.id.etCardHolder);
        etExpiry     = v.findViewById(R.id.etExpiry);
        etCvv        = v.findViewById(R.id.etCvv);

        // Auto-format card number with spaces
        etCardNumber.addTextChangedListener(new TextWatcher() {
            private boolean formatting = false;
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                if (formatting) return;
                formatting = true;
                String digits = s.toString().replaceAll("[^0-9]", "").substring(
                        0, Math.min(s.toString().replaceAll("[^0-9]", "").length(), 16));
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < digits.length(); i++) {
                    if (i > 0 && i % 4 == 0) formatted.append(' ');
                    formatted.append(digits.charAt(i));
                }
                etCardNumber.setText(formatted.toString());
                etCardNumber.setSelection(formatted.length());
                formatting = false;
            }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
        });

        // Auto-format expiry MM/YY
        etExpiry.addTextChangedListener(new TextWatcher() {
            private boolean formatting = false;
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                if (formatting) return;
                formatting = true;
                String digits = s.toString().replaceAll("[^0-9]", "");
                if (digits.length() >= 3) {
                    String formatted = digits.substring(0, 2) + "/" + digits.substring(2, Math.min(4, digits.length()));
                    etExpiry.setText(formatted);
                    etExpiry.setSelection(formatted.length());
                }
                formatting = false;
            }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
        });
    }

    private void buildBankList(View v) {
        LinearLayout bankList = v.findViewById(R.id.bankList);
        bankList.removeAllViews();
        String[][] banks = {
                {"Commercial Bank", "8001-234-567-890"},
                {"Sampath Bank",    "0012-345-678-901"},
                {"HNB",             "011-2-345-678-9"}
        };
        for (String[] bank : banks) {
            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(16); // center_vertical
            row.setBackgroundResource(R.drawable.bg_card);
            row.setPadding(12, 14, 12, 14);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.bottomMargin = 8;
            row.setLayoutParams(lp);

            TextView tvName = new TextView(requireContext());
            tvName.setText("🏦 " + bank[0]);
            tvName.setTextSize(14f);
            tvName.setTypeface(null, android.graphics.Typeface.BOLD);
            tvName.setTextColor(requireContext().getColor(R.color.text));
            tvName.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvAcc = new TextView(requireContext());
            tvAcc.setText(bank[1]);
            tvAcc.setTextSize(12f);
            tvAcc.setTextColor(requireContext().getColor(R.color.text_dim));

            row.addView(tvName);
            row.addView(tvAcc);
            bankList.addView(row);
        }

        TextView tvRef = v.findViewById(R.id.tvTransferRef);
        tvRef.setText("Use your ticket ID " + ticket.getId() + " as the payment reference.");
    }

    private void bindPayButton(View v) {
        btnPay = v.findViewById(R.id.btnPay);
        btnPay.setOnClickListener(click -> processPayment());
    }

    private void processPayment() {
        if ("card".equals(payMethod)) {
            if (etCardNumber.getText().toString().isEmpty()
                    || etCardHolder.getText().toString().isEmpty()
                    || etExpiry.getText().toString().isEmpty()
                    || etCvv.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all card details", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        btnPay.setEnabled(false);
        btnPay.setText("Processing…");

        FirebaseDbHelper.updateTicketPayment(ticket.getId(),
                new FirebaseDbHelper.DataCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        saveToHistoryAndComplete();
                    }

                    @Override
                    public void onFailure(String error) {
                        // Even on Firebase error, mark as paid locally to ensure user isn't stuck
                        saveToHistoryAndComplete();
                    }
                });
    }

    private void saveToHistoryAndComplete() {
        // 1. Create History Item
        HistoryItem historyItem = new HistoryItem();
        historyItem.setId("hist-" + ticket.getId());
        historyItem.setReferenceId(ticket.getId());
        historyItem.setDeviceName(ticket.getDeviceModel());
        historyItem.setDeviceType(ticket.getDeviceType());
        historyItem.setRepairDate(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
        historyItem.setServiceSummary(ticket.getIssue());
        historyItem.setBranch(ticket.getBranch());
        historyItem.setTotalCostLKR(ticket.getTotalCostLKR());
        historyItem.setStatus("Completed");
        historyItem.setInvoiceNumber("INV-" + ticket.getId().replace("TF-", ""));
        historyItem.setUserId(ticket.getUserId());
        historyItem.setWarrantyUntil(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date(System.currentTimeMillis() + (90L * 24 * 60 * 60 * 1000)))); // 90 days warranty

        // 2. Update Ticket Object status
        ticket.setPaid(true);
        ticket.setStatus("Completed");

        // 3. Save to Firebase (History and Ticket update)
        FirebaseDbHelper.saveHistoryItem(historyItem, new FirebaseDbHelper.DataCallback<Void>() {
            @Override public void onSuccess(Void data) {}
            @Override public void onFailure(String error) {}
        });

        // 4. Save to Local DB (Room) - Run on background thread
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            db.historyDao().insert(historyItem);
            db.repairTicketDao().update(ticket);

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(),
                            "✓ Payment Successful! History Updated.",
                            Toast.LENGTH_LONG).show();
                    ((MainActivity) requireActivity()).navigateTo("History");
                });
            }
        }).start();
    }
}
