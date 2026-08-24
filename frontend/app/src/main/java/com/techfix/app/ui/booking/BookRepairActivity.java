package com.techfix.app.ui.booking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.techfix.app.R;
import com.techfix.app.data.api.ApiService;
import com.techfix.app.data.api.RetrofitClient;
import com.techfix.app.data.model.RepairRequestCreatedResponse;
import com.techfix.app.utils.LocationHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepairActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 200;

    private TextView tvServiceName, tvServicePrice, tvLocationStatus;
    private EditText etDeviceModel, etIssue;
    private ImageView ivDevicePhoto;
    private Button btnTakePhoto, btnSubmit;
    private ProgressBar progressBar;

    private int serviceId, categoryId;
    private File photoFile;
    private Uri photoUri;
    private Location currentLocation;

    private ActivityResultLauncher<Uri> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_repair);

        tvServiceName = findViewById(R.id.tvServiceName);
        tvServicePrice = findViewById(R.id.tvServicePrice);
        tvLocationStatus = findViewById(R.id.tvLocationStatus);
        etDeviceModel = findViewById(R.id.etDeviceModel);
        etIssue = findViewById(R.id.etIssue);
        ivDevicePhoto = findViewById(R.id.ivDevicePhoto);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);

        serviceId = getIntent().getIntExtra("service_id", -1);
        categoryId = getIntent().getIntExtra("category_id", -1);
        String serviceName = getIntent().getStringExtra("service_name");
        String servicePrice = getIntent().getStringExtra("service_price");

        tvServiceName.setText(serviceName);
        tvServicePrice.setText("Rs. " + servicePrice);

        // Register the camera launcher (must be done before onStart/onResume)
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
            if (success) {
                ivDevicePhoto.setImageURI(photoUri);
            } else {
                Toast.makeText(this, "Photo capture cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        btnTakePhoto.setOnClickListener(v -> checkCameraPermissionAndLaunch());
        btnSubmit.setOnClickListener(v -> submitRequest());

        fetchLocation();
    }

    private void fetchLocation() {
        tvLocationStatus.setText("Getting your location...");
        LocationHelper.getCurrentLocation(this, location -> {
            currentLocation = location;
            if (location != null) {
                tvLocationStatus.setText("Location captured (used to assign nearest branch)");
            } else {
                tvLocationStatus.setText("Could not get location. Please enable GPS and try again.");
            }
        });
    }

    private void checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
        }
    }

    private void launchCamera() {
        try {
            photoFile = File.createTempFile("device_photo_", ".jpg", getCacheDir());
            photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
            cameraLauncher.launch(photoUri);
        } catch (IOException e) {
            Toast.makeText(this, "Could not create photo file", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitRequest() {
        String deviceModel = etDeviceModel.getText().toString().trim();
        String issue = etIssue.getText().toString().trim();

        if (currentLocation == null) {
            Toast.makeText(this, "Location not available yet. Please wait or check GPS.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnSubmit.setEnabled(false);

        // Use tomorrow, 10:00 AM as the requested date (simple default, no date picker needed)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String requestedDate = fmt.format(cal.getTime());

        RequestBody serviceIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(serviceId));
        RequestBody categoryIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(categoryId));
        RequestBody deviceModelBody = RequestBody.create(MediaType.parse("text/plain"), deviceModel);
        RequestBody issueBody = RequestBody.create(MediaType.parse("text/plain"), issue);
        RequestBody latBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(currentLocation.getLatitude()));
        RequestBody lngBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(currentLocation.getLongitude()));
        RequestBody dateBody = RequestBody.create(MediaType.parse("text/plain"), requestedDate);

        MultipartBody.Part imagePart = null;
        if (photoFile != null && photoFile.exists()) {
            RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpeg"), photoFile);
            imagePart = MultipartBody.Part.createFormData("device_image", photoFile.getName(), photoBody);
        }

        ApiService api = RetrofitClient.getInstance(this);
        Call<RepairRequestCreatedResponse> call = api.createRepairRequest(
                serviceIdBody, categoryIdBody, deviceModelBody, issueBody,
                latBody, lngBody, dateBody, imagePart
        );

        call.enqueue(new Callback<RepairRequestCreatedResponse>() {
            @Override
            public void onResponse(Call<RepairRequestCreatedResponse> call, Response<RepairRequestCreatedResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    RepairRequestCreatedResponse body = response.body();
                    Toast.makeText(BookRepairActivity.this,
                            "Booked! Assigned to " + body.getBranch().getName(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(BookRepairActivity.this, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RepairRequestCreatedResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);
                Toast.makeText(BookRepairActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}