package com.techfix.app.utils;

public class StatusUtil {
    public static String colorFor(String status) {
        switch (status) {
            case "PENDING": return "#FFA000";
            case "ASSIGNED": return "#1976D2";
            case "IN_PROGRESS": return "#7B1FA2";
            case "COMPLETED": return "#388E3C";
            case "CANCELLED": return "#D32F2F";
            default: return "#757575";
        }
    }
}