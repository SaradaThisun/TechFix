package com.techfix.app.ui.repairs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.data.api.ApiService;
import com.techfix.app.data.api.RetrofitClient;
import com.techfix.app.data.model.RepairRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRepairsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View progressBar;
    private TextView tvEmpty;
    private RepairAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_repairs, container, false);

        recyclerView = view.findViewById(R.id.recyclerRepairs);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        adapter = new RepairAdapter(new ArrayList<>(), repair -> {
            Intent intent = new Intent(requireContext(), RepairDetailActivity.class);
            intent.putExtra("repair_id", repair.getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRepairs(); // refresh every time this tab becomes visible
    }

    private void loadRepairs() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService api = RetrofitClient.getInstance(requireContext());
        api.getMyRepairRequests().enqueue(new Callback<List<RepairRequest>>() {
            @Override
            public void onResponse(Call<List<RepairRequest>> call, Response<List<RepairRequest>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                    tvEmpty.setVisibility(response.body().isEmpty() ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<RepairRequest>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Could not load repairs: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}