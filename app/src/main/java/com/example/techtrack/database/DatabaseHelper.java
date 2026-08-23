package com.example.techtrack.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "techtrack.db";
    private static final int DATABASE_VERSION = 1;

    // Branches table
    public static final String TABLE_BRANCHES = "branches";
    public static final String COL_BRANCH_ID = "id";
    public static final String COL_BRANCH_NAME = "name";
    public static final String COL_BRANCH_SHORT_NAME = "short_name";
    public static final String COL_BRANCH_ADDRESS = "address";
    public static final String COL_BRANCH_CITY = "city";
    public static final String COL_BRANCH_PHONE = "phone";
    public static final String COL_BRANCH_ALT_PHONE = "alt_phone";
    public static final String COL_BRANCH_HOURS = "hours";
    public static final String COL_BRANCH_WEEKEND_HOURS = "weekend_hours";
    public static final String COL_BRANCH_RATING = "rating";
    public static final String COL_BRANCH_REVIEWS_COUNT = "reviews_count";
    public static final String COL_BRANCH_TECHS_AVAILABLE = "technicians_available";
    public static final String COL_BRANCH_STOCK_LEVEL = "spare_parts_stock_level";
    public static final String COL_BRANCH_LATITUDE = "latitude";
    public static final String COL_BRANCH_LONGITUDE = "longitude";

    // Services table
    public static final String TABLE_SERVICES = "services";
    public static final String COL_SERVICE_ID = "id";
    public static final String COL_SERVICE_TITLE = "title";
    public static final String COL_SERVICE_CATEGORY = "category";
    public static final String COL_SERVICE_DEVICE_TYPE = "device_type";
    public static final String COL_SERVICE_PRICE = "price_lkr";
    public static final String COL_SERVICE_ESTIMATED_TIME = "estimated_time";
    public static final String COL_SERVICE_POPULAR = "popular";
    public static final String COL_SERVICE_WARRANTY_DAYS = "warranty_days";
    public static final String COL_SERVICE_DESCRIPTION = "description";

    // Repair history table
    public static final String TABLE_HISTORY = "history";
    public static final String COL_HISTORY_ID = "id";
    public static final String COL_HISTORY_REFERENCE_ID = "reference_id";
    public static final String COL_HISTORY_DEVICE_NAME = "device_name";
    public static final String COL_HISTORY_DEVICE_TYPE = "device_type";
    public static final String COL_HISTORY_REPAIR_DATE = "repair_date";
    public static final String COL_HISTORY_SERVICE_SUMMARY = "service_summary";
    public static final String COL_HISTORY_BRANCH = "branch";
    public static final String COL_HISTORY_TOTAL_COST = "total_cost_lkr";
    public static final String COL_HISTORY_STATUS = "status";
    public static final String COL_HISTORY_WARRANTY_UNTIL = "warranty_until";
    public static final String COL_HISTORY_INVOICE_NUMBER = "invoice_number";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBranches = "CREATE TABLE " + TABLE_BRANCHES + " (" +
                COL_BRANCH_ID + " TEXT PRIMARY KEY, " +
                COL_BRANCH_NAME + " TEXT, " +
                COL_BRANCH_SHORT_NAME + " TEXT, " +
                COL_BRANCH_ADDRESS + " TEXT, " +
                COL_BRANCH_CITY + " TEXT, " +
                COL_BRANCH_PHONE + " TEXT, " +
                COL_BRANCH_ALT_PHONE + " TEXT, " +
                COL_BRANCH_HOURS + " TEXT, " +
                COL_BRANCH_WEEKEND_HOURS + " TEXT, " +
                COL_BRANCH_RATING + " REAL, " +
                COL_BRANCH_REVIEWS_COUNT + " INTEGER, " +
                COL_BRANCH_TECHS_AVAILABLE + " INTEGER, " +
                COL_BRANCH_STOCK_LEVEL + " TEXT, " +
                COL_BRANCH_LATITUDE + " REAL, " +
                COL_BRANCH_LONGITUDE + " REAL)";

        String createServices = "CREATE TABLE " + TABLE_SERVICES + " (" +
                COL_SERVICE_ID + " TEXT PRIMARY KEY, " +
                COL_SERVICE_TITLE + " TEXT, " +
                COL_SERVICE_CATEGORY + " TEXT, " +
                COL_SERVICE_DEVICE_TYPE + " TEXT, " +
                COL_SERVICE_PRICE + " REAL, " +
                COL_SERVICE_ESTIMATED_TIME + " TEXT, " +
                COL_SERVICE_POPULAR + " INTEGER, " +
                COL_SERVICE_WARRANTY_DAYS + " INTEGER, " +
                COL_SERVICE_DESCRIPTION + " TEXT)";

        String createHistory = "CREATE TABLE " + TABLE_HISTORY + " (" +
                COL_HISTORY_ID + " TEXT PRIMARY KEY, " +
                COL_HISTORY_REFERENCE_ID + " TEXT, " +
                COL_HISTORY_DEVICE_NAME + " TEXT, " +
                COL_HISTORY_DEVICE_TYPE + " TEXT, " +
                COL_HISTORY_REPAIR_DATE + " TEXT, " +
                COL_HISTORY_SERVICE_SUMMARY + " TEXT, " +
                COL_HISTORY_BRANCH + " TEXT, " +
                COL_HISTORY_TOTAL_COST + " REAL, " +
                COL_HISTORY_STATUS + " TEXT, " +
                COL_HISTORY_WARRANTY_UNTIL + " TEXT, " +
                COL_HISTORY_INVOICE_NUMBER + " TEXT)";

        db.execSQL(createBranches);
        db.execSQL(createServices);
        db.execSQL(createHistory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }
}