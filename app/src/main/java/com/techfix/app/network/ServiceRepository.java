package com.techfix.app.network;

import android.content.Context;
import android.util.Log;

import com.techfix.app.models.RepairService;
import com.techfix.app.models.SparePart;
import com.techfix.app.utils.MockData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository that tries the Retrofit API first,
 * and falls back to local MockData if the network call fails.
 */
public class ServiceRepository {

    private static final String TAG = "ServiceRepository";
    private final ApiService api;

    public interface RepoCallback<T> {
        void onResult(T data, boolean fromNetwork);
        void onError(String error);
    }

    public ServiceRepository() {
        this.api = RetrofitClient.getApiService();
    }

    public void getRepairServices(Context context, RepoCallback<List<RepairService>> callback) {
        api.getRepairServices().enqueue(new Callback<List<RepairService>>() {
            @Override
            public void onResponse(Call<List<RepairService>> call,
                                   Response<List<RepairService>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body(), true);
                } else {
                    Log.w(TAG, "API returned empty; using mock data");
                    callback.onResult(MockData.getRepairServices(), false);
                }
            }

            @Override
            public void onFailure(Call<List<RepairService>> call, Throwable t) {
                Log.w(TAG, "Network error; using mock data: " + t.getMessage());
                callback.onResult(MockData.getRepairServices(), false);
            }
        });
    }

    public void getSpareParts(Context context, RepoCallback<List<SparePart>> callback) {
        api.getSpareParts().enqueue(new Callback<List<SparePart>>() {
            @Override
            public void onResponse(Call<List<SparePart>> call,
                                   Response<List<SparePart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body(), true);
                } else {
                    callback.onResult(MockData.getSpareParts(), false);
                }
            }

            @Override
            public void onFailure(Call<List<SparePart>> call, Throwable t) {
                callback.onResult(MockData.getSpareParts(), false);
            }
        });
    }
}
