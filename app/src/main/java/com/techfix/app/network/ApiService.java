package com.techfix.app.network;

import com.techfix.app.models.RepairService;
import com.techfix.app.models.SparePart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit interface for TechFix REST API.
 * Base URL is configured in RetrofitClient.
 * All endpoints return real data when a backend is connected;
 * the app falls back to local mock data when the network is unavailable.
 */
public interface ApiService {

    // ─── Repair Services & Pricing ────────────────────────────────────────────

    @GET("services")
    Call<List<RepairService>> getRepairServices();

    @GET("services/{id}")
    Call<RepairService> getRepairServiceById(@Path("id") String id);

    @GET("services")
    Call<List<RepairService>> getRepairServicesByCategory(@Query("category") String category);

    // ─── Spare Parts Availability ─────────────────────────────────────────────

    @GET("spare-parts")
    Call<List<SparePart>> getSpareParts();

    @GET("spare-parts/{partId}")
    Call<SparePart> getSparePartById(@Path("partId") String partId);

    @GET("spare-parts")
    Call<List<SparePart>> getSparePartsByBranch(@Query("branch") String branch);

    // ─── Service Sync ─────────────────────────────────────────────────────────

    @GET("sync/prices")
    Call<List<RepairService>> syncLatestPrices();
}
