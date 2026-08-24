package com.techfix.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationHelper {

    public interface LocationCallback {
        void onLocationResult(Location location);
    }

    @SuppressLint("MissingPermission")
    public static void getCurrentLocation(Context context, LocationCallback callback) {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
        client.getLastLocation()
                .addOnSuccessListener(location -> callback.onLocationResult(location))
                .addOnFailureListener(e -> callback.onLocationResult(null));
    }
}