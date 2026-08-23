package com.techfix.app.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techfix.app.models.User;

public class FirebaseAuthHelper {

    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final DatabaseReference db = FirebaseDatabase.getInstance("https://techfix-98b60-default-rtdb.firebaseio.com/").getReference();

    public interface AuthCallback {
        void onSuccess(User user);
        void onFailure(String error);
    }

    // ─── Registration (Customer or Staff) ────────────────────────────────────────

    public static void register(String fullName, String phone, String email,
                                String password, String branch, String role,
                                AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(result -> {
                FirebaseUser fbUser = result.getUser();
                if (fbUser == null) { callback.onFailure("Registration failed"); return; }

                User user = new User(fbUser.getUid(), fullName, email, phone, branch, role);

                // Save to Realtime Database
                db.child("users").child(fbUser.getUid()).setValue(user)
                    .addOnSuccessListener(v -> callback.onSuccess(user))
                    .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
            })
            .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    /** Helper for standard customer registration */
    public static void registerCustomer(String fullName, String phone, String email,
                                        String password, String branch,
                                        AuthCallback callback) {
        register(fullName, phone, email, password, branch, "customer", callback);
    }

    // ─── Login (Customer or Staff) ────────────────────────────────────────────

    public static void login(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(result -> {
                FirebaseUser fbUser = result.getUser();
                if (fbUser == null) { callback.onFailure("Login failed"); return; }

                // Fetch user profile from Realtime DB
                db.child("users").child(fbUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                user.setAuthenticated(true);
                                callback.onSuccess(user);
                            } else {
                                // User record missing — create a basic customer profile
                                User newUser = new User(fbUser.getUid(), "", email, "", "Colombo Branch", "customer");
                                newUser.setAuthenticated(true);
                                db.child("users").child(fbUser.getUid()).setValue(newUser);
                                callback.onSuccess(newUser);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            callback.onFailure(error.getMessage());
                        }
                    });
            })
            .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // ─── Sign Out ─────────────────────────────────────────────────────────────

    public static void signOut() {
        auth.signOut();
    }

    // ─── Get current Firebase user ────────────────────────────────────────────

    public static FirebaseUser getCurrentFirebaseUser() {
        return auth.getCurrentUser();
    }

    public static boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    // ─── Fetch user profile from Realtime DB ──────────────────────────────────

    public static void fetchUserProfile(String uid, AuthCallback callback) {
        db.child("users").child(uid)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        user.setAuthenticated(true);
                        callback.onSuccess(user);
                    } else {
                        callback.onFailure("Profile not found");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onFailure(error.getMessage());
                }
            });
    }

    // ─── Password Reset ───────────────────────────────────────────────────────

    public static void sendPasswordReset(String email, AuthCallback callback) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener(v -> callback.onSuccess(null))
            .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
}
