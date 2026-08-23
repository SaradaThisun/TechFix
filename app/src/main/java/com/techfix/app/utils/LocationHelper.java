package com.techfix.app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

/**
 * Wraps FusedLocationProviderClient and calculates nearest TechFix branch.
 */
public class LocationHelper {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // Branch coordinates
    private static final double COLOMBO_LAT = 6.8845;
    private static final double COLOMBO_LNG = 79.8756;
    private static final double GALLE_LAT   = 6.0367;
    private static final double GALLE_LNG   = 80.2170;

    public interface LocationCallback {
        void onLocationReceived(Location location, String nearestBranch, double distanceKm);
        void onPermissionDenied();
        void onError(String error);
    }

    public static void getNearestBranch(Context context, LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (context instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                     Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
            callback.onPermissionDenied();
            return;
        }

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
        CancellationTokenSource cts = new CancellationTokenSource();

        client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.getToken())
              .addOnSuccessListener(location -> {
                  if (location == null) {
                      callback.onError("Could not determine location");
                      return;
                  }
                  double distColombo = distanceKm(location.getLatitude(), location.getLongitude(),
                          COLOMBO_LAT, COLOMBO_LNG);
                  double distGalle   = distanceKm(location.getLatitude(), location.getLongitude(),
                          GALLE_LAT, GALLE_LNG);

                  String nearest = distColombo <= distGalle ? "Colombo Branch" : "Galle Branch";
                  double dist    = Math.min(distColombo, distGalle);
                  callback.onLocationReceived(location, nearest, dist);
              })
              .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    /**
     * Haversine formula — returns distance in kilometres.
     */
    public static double distanceKm(double lat1, double lng1, double lat2, double lng2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    public static boolean hasLocationPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
