package com.techfix.app.ui.services;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.data.api.ApiService;
import com.techfix.app.data.api.RetrofitClient;
import com.techfix.app.data.model.RepairService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesFragment extends Fragment {

    private RecyclerView recyclerView;
    private View progressBar;
    private ServiceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        recyclerView = view.findViewById(R.id.recyclerServices);
        progressBar = view.findViewById(R.id.progressBar);

        adapter = new ServiceAdapter(new ArrayList<>(), service -> {
            Intent intent = new Intent(requireContext(), com.techfix.app.ui.booking.BookRepairActivity.class);
            intent.putExtra("service_id", service.getId());
            intent.putExtra("category_id", service.getDeviceCategoryId());
            intent.putExtra("service_name", service.getName());
            intent.putExtra("service_price", service.getPrice());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        loadServices();

        return view;
    }

    private void loadServices() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService api = RetrofitClient.getInstance(requireContext());
        api.getServices(null).enqueue(new Callback<List<RepairService>>() {
            @Override
            public void onResponse(Call<List<RepairService>> call, Response<List<RepairService>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<RepairService>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Could not load services: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}