package com.techfix.app.ui.repairs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.techfix.app.R;
import com.techfix.app.data.api.ApiService;
import com.techfix.app.data.api.RetrofitClient;
import com.techfix.app.data.model.RepairRequest;
import com.techfix.app.data.model.StatusHistory;
import com.techfix.app.utils.StatusUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairDetailActivity extends AppCompatActivity {

    private ImageView ivDevicePhoto;
    private TextView tvServiceName, tvStatus, tvBranchName, tvBranchAddress,
            tvTechnician, tvDeviceInfo, tvIssue, tvPrice;
    private LinearLayout timelineContainer;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_detail);

        ivDevicePhoto = findViewById(R.id.ivDevicePhoto);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvStatus = findViewById(R.id.tvStatus);
        tvBranchName = findViewById(R.id.tvBranchName);
        tvBranchAddress = findViewById(R.id.tvBranchAddress);
        tvTechnician = findViewById(R.id.tvTechnician);
        tvDeviceInfo = findViewById(R.id.tvDeviceInfo);
        tvIssue = findViewById(R.id.tvIssue);
        tvPrice = findViewById(R.id.tvPrice);
        timelineContainer = findViewById(R.id.timelineContainer);
        progressBar = findViewById(R.id.progressBar);

        int repairId = getIntent().getIntExtra("repair_id", -1);
        if (repairId == -1) {
            finish();
            return;
        }

        loadDetail(repairId);
    }

    private void loadDetail(int id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService api = RetrofitClient.getInstance(this);
        api.getRepairRequestDetail(id).enqueue(new Callback<RepairRequest>() {
            @Override
            public void onResponse(Call<RepairRequest> call, Response<RepairRequest> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    bindData(response.body());
                } else {
                    Toast.makeText(RepairDetailActivity.this, "Could not load repair details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RepairRequest> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RepairDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void bindData(RepairRequest repair) {
        tvServiceName.setText(repair.getServiceName() != null ? repair.getServiceName() : "Repair Request");
        tvStatus.setText(repair.getStatus().replace("_", " "));
        tvStatus.setBackgroundColor(Color.parseColor(StatusUtil.colorFor(repair.getStatus())));

        tvBranchName.setText(repair.getBranchName() != null ? repair.getBranchName() : "");
        tvBranchAddress.setText(repair.getBranchAddress() != null ? repair.getBranchAddress() : "");
        tvTechnician.setText(repair.getTechnicianName() != null
                ? "Technician: " + repair.getTechnicianName()
                : "Technician: not yet assigned");
        tvDeviceInfo.setText("Device: " + repair.getDeviceModel() + " (" + repair.getCategoryName() + ")");
        tvIssue.setText(repair.getIssueDescription() != null ? repair.getIssueDescription() : "");
        tvPrice.setText("Rs. " + repair.getPrice());

        if (repair.getDeviceImageUrl() != null && !repair.getDeviceImageUrl().isEmpty()) {
            ivDevicePhoto.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(RetrofitClient.imageBaseUrl() + repair.getDeviceImageUrl())
                    .into(ivDevicePhoto);
        }

        timelineContainer.removeAllViews();
        if (repair.getHistory() != null) {
            for (StatusHistory step : repair.getHistory()) {
                View stepView = LayoutInflater.from(this).inflate(R.layout.item_timeline_step, timelineContainer, false);
                TextView tvStepStatus = stepView.findViewById(R.id.tvStepStatus);
                TextView tvStepNotes = stepView.findViewById(R.id.tvStepNotes);
                TextView tvStepTime = stepView.findViewById(R.id.tvStepTime);

                tvStepStatus.setText(step.getStatus().replace("_", " "));
                tvStepNotes.setText(step.getNotes() != null ? step.getNotes() : "");
                tvStepTime.setText(step.getUpdatedAt());

                timelineContainer.addView(stepView);
            }
        }
    }
}