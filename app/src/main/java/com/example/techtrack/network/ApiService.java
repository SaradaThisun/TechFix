package com.example.techtrack.network;

import com.example.techtrack.models.BookingFormData;
import com.example.techtrack.models.BranchInfo;
import com.example.techtrack.models.HistoryItem;
import com.example.techtrack.models.RepairServiceItem;
import com.example.techtrack.models.RepairTicket;
import com.example.techtrack.models.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("api/auth/login")
    Call<UserProfile> login(@Body Object loginRequest);

    @POST("api/auth/register")
    Call<UserProfile> register(@Body Object registerRequest);

    @GET("api/branches")
    Call<List<BranchInfo>> getBranches();

    @GET("api/services")
    Call<List<RepairServiceItem>> getServices();

    @POST("api/bookings")
    Call<RepairTicket> createBooking(@Body BookingFormData bookingFormData);

    @GET("api/bookings/{id}")
    Call<RepairTicket> getTicketById(@Path("id") String ticketId);

    @GET("api/history")
    Call<List<HistoryItem>> getRepairHistory();
}