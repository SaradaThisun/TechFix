package com.techfix.app.data.api;

import com.techfix.app.data.model.AuthResponse;
import com.techfix.app.data.model.Branch;
import com.techfix.app.data.model.DeviceCategory;
import com.techfix.app.data.model.LoginRequest;
import com.techfix.app.data.model.RegisterRequest;
import com.techfix.app.data.model.RepairRequestCreatedResponse;
import com.techfix.app.data.model.RepairService;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @GET("branches")
    Call<List<Branch>> getBranches();

    @GET("branches/nearby")
    Call<List<Branch>> getNearbyBranches(@Query("lat") double lat, @Query("lng") double lng);

    @GET("categories")
    Call<List<DeviceCategory>> getCategories();

    @GET("services")
    Call<List<RepairService>> getServices(@Query("category_id") Integer categoryId);

    @Multipart
    @POST("repair-requests")
    Call<RepairRequestCreatedResponse> createRepairRequest(
            @Part("repair_service_id") RequestBody serviceId,
            @Part("device_category_id") RequestBody categoryId,
            @Part("device_model") RequestBody deviceModel,
            @Part("issue_description") RequestBody issueDescription,
            @Part("customer_lat") RequestBody lat,
            @Part("customer_lng") RequestBody lng,
            @Part("requested_date") RequestBody requestedDate,
            @Part MultipartBody.Part deviceImage
    );

    @GET("repair-requests")
    Call<List<com.techfix.app.data.model.RepairRequest>> getMyRepairRequests();

    @GET("repair-requests/{id}")
    Call<com.techfix.app.data.model.RepairRequest> getRepairRequestDetail(@retrofit2.http.Path("id") int id);
}