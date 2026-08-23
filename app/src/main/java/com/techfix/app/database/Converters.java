package com.techfix.app.database;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techfix.app.models.RepairTicket;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromTimelineSteps(List<RepairTicket.TimelineStep> steps) {
        if (steps == null) return null;
        return gson.toJson(steps);
    }

    @TypeConverter
    public static List<RepairTicket.TimelineStep> toTimelineSteps(String data) {
        if (data == null) return null;
        Type type = new TypeToken<List<RepairTicket.TimelineStep>>() {}.getType();
        return gson.fromJson(data, type);
    }

    @TypeConverter
    public static String fromStatusLogs(List<RepairTicket.StatusLogEntry> logs) {
        if (logs == null) return null;
        return gson.toJson(logs);
    }

    @TypeConverter
    public static List<RepairTicket.StatusLogEntry> toStatusLogs(String data) {
        if (data == null) return null;
        Type type = new TypeToken<List<RepairTicket.StatusLogEntry>>() {}.getType();
        return gson.fromJson(data, type);
    }

    @TypeConverter
    public static String fromStringList(List<String> list) {
        if (list == null) return null;
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<String> toStringList(String data) {
        if (data == null) return null;
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(data, type);
    }
}
