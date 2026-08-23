package com.techfix.app;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class TechFixApp extends Application {

    private static TechFixApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FirebaseApp.initializeApp(this);
    }

    public static TechFixApp getInstance() {
        return instance;
    }
}
