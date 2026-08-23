package com.techfix.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Persists lightweight session state between app launches.
 */
public class SessionManager {

    private static final String PREF_NAME = "techfix_session";
    private static final String KEY_UID        = "uid";
    private static final String KEY_FULL_NAME  = "full_name";
    private static final String KEY_EMAIL      = "email";
    private static final String KEY_PHONE      = "phone";
    private static final String KEY_BRANCH     = "branch";
    private static final String KEY_ROLE       = "role";
    private static final String KEY_LOGGED_IN  = "logged_in";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveSession(String uid, String fullName, String email,
                            String phone, String branch, String role) {
        prefs.edit()
             .putString(KEY_UID, uid)
             .putString(KEY_FULL_NAME, fullName)
             .putString(KEY_EMAIL, email)
             .putString(KEY_PHONE, phone)
             .putString(KEY_BRANCH, branch)
             .putString(KEY_ROLE, role)
             .putBoolean(KEY_LOGGED_IN, true)
             .apply();
    }

    public boolean isLoggedIn() { return prefs.getBoolean(KEY_LOGGED_IN, false); }

    public String getUid()      { return prefs.getString(KEY_UID, ""); }
    public String getFullName() { return prefs.getString(KEY_FULL_NAME, ""); }
    public String getEmail()    { return prefs.getString(KEY_EMAIL, ""); }
    public String getPhone()    { return prefs.getString(KEY_PHONE, ""); }
    public String getBranch()   { return prefs.getString(KEY_BRANCH, "Colombo Branch"); }
    public String getRole()     { return prefs.getString(KEY_ROLE, "customer"); }

    public boolean isStaff() { return "staff".equals(getRole()); }

    public void updateBranch(String branch) {
        prefs.edit().putString(KEY_BRANCH, branch).apply();
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
