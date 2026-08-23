package com.techfix.app.utils;

import android.graphics.Color;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {

    private FormatUtils() {}

    /** Format LKR price: "Rs. 29,500" */
    public static String formatLKR(long amount) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("en", "LK"));
        return "Rs. " + nf.format(amount);
    }

    /** Format LKR price from double */
    public static String formatLKR(double amount) {
        return formatLKR((long) amount);
    }

    /** Returns status color hex string */
    public static int statusColor(String status) {
        switch (status) {
            case "Repair in Progress": return Color.parseColor("#F59E0B");
            case "Ready for Pickup":
            case "Completed":          return Color.parseColor("#10B981");
            case "Canceled":           return Color.parseColor("#EF4444");
            default:                   return Color.parseColor("#0066FF");
        }
    }

    /** Returns log-type color */
    public static int logColor(String type) {
        switch (type) {
            case "diagnostic": return Color.parseColor("#6366F1");
            case "progress":   return Color.parseColor("#F59E0B");
            case "success":    return Color.parseColor("#10B981");
            case "alert":      return Color.parseColor("#EF4444");
            default:           return Color.parseColor("#0066FF");
        }
    }

    /** Returns icon resource name for device type (used with reflection or switch) */
    public static String deviceTypeIcon(String deviceType) {
        switch (deviceType) {
            case "laptop": return "laptop";
            case "tablet": return "tablet";
            case "desktop": return "desktop";
            default: return "phone";
        }
    }

    /** Truncate long string for list display */
    public static String truncate(String text, int maxLength) {
        if (text == null) return "";
        return text.length() > maxLength ? text.substring(0, maxLength) + "…" : text;
    }

    /** First letter uppercase avatar */
    public static String getInitial(String name) {
        if (name == null || name.isEmpty()) return "?";
        return String.valueOf(name.charAt(0)).toUpperCase();
    }
}
