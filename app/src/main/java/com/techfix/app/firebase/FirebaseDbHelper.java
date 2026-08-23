package com.techfix.app.firebase;

import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.techfix.app.models.DispatchRequest;
import com.techfix.app.models.HistoryItem;
import com.techfix.app.models.PartMovementLog;
import com.techfix.app.models.RepairTicket;
import com.techfix.app.models.SparePart;
import com.techfix.app.models.TechnicianJob;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDbHelper {

    private static final DatabaseReference db = FirebaseDatabase.getInstance("https://techfix-98b60-default-rtdb.firebaseio.com/").getReference();
    private static final StorageReference storage = FirebaseStorage.getInstance().getReference();

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onFailure(String error);
    }

    // ─── Repair Tickets ───────────────────────────────────────────────────────

    public static void saveTicket(RepairTicket ticket, DataCallback<Void> callback) {
        db.child("tickets").child(ticket.getId()).setValue(ticket)
            .addOnSuccessListener(v -> callback.onSuccess(null))
            .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public static void updateTicketStatus(String ticketId, String status, DataCallback<Void> callback) {
        db.child("tickets").child(ticketId).child("status").setValue(status)
            .addOnSuccessListener(v -> callback.onSuccess(null))
            .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public static void updateTicketPayment(String ticketId, DataCallback<Void> callback) {
        db.child("tickets").child(ticketId).child("isPaid").setValue(true)
            .addOnSuccessListener(v -> {
                db.child("tickets").child(ticketId).child("status").setValue("Completed")
                    .addOnSuccessListener(v2 -> callback.onSuccess(null))
                    .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
            })
            .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public static void fetchTicketsForUser(String userId, DataCallback<List<RepairTicket>> callback) {
        db.child("tickets").orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<RepairTicket> tickets = new ArrayList<>();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        RepairTicket t = s.getValue(RepairTicket.class);
                        if (t != null) tickets.add(t);
                    }
                    callback.onSuccess(tickets);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onFailure(error.getMessage());
                }
            });
    }

    public static ValueEventListener listenToTicketsForUser(String userId, DataCallback<List<RepairTicket>> callback) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<RepairTicket> tickets = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    RepairTicket t = s.getValue(RepairTicket.class);
                    if (t != null) tickets.add(t);
                }
                callback.onSuccess(tickets);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onFailure(error.getMessage());
            }
        };
        db.child("tickets").orderByChild("userId").equalTo(userId).addValueEventListener(listener);
        return listener;
    }

    public static void stopListening(ValueEventListener listener) {
        if (listener != null) {
            db.removeEventListener(listener);
        }
    }

    public static void fetchAllTickets(DataCallback<List<RepairTicket>> callback) {
        db.child("tickets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<RepairTicket> tickets = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    RepairTicket t = s.getValue(RepairTicket.class);
                    if (t != null) tickets.add(t);
                }
                callback.onSuccess(tickets);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onFailure(error.getMessage());
            }
        });
    }

    // ─── History ──────────────────────────────────────────────────────────────

    public static void saveHistoryItem(HistoryItem item, DataCallback<Void> callback) {
        db.child("history").child(item.getUserId()).child(item.getId()).setValue(item)
            .addOnSuccessListener(v -> callback.onSuccess(null))
            .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public static void fetchHistoryForUser(String userId, DataCallback<List<HistoryItem>> callback) {
        db.child("history").child(userId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<HistoryItem> items = new ArrayList<>();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        HistoryItem item = s.getValue(HistoryItem.class);
                        if (item != null) items.add(item);
                    }
                    callback.onSuccess(items);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onFailure(error.getMessage());
                }
            });
    }

    // ─── Spare Parts ──────────────────────────────────────────────────────────

    public static void fetchSpareParts(DataCallback<List<SparePart>> callback) {
        db.child("spare_parts")
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<SparePart> parts = new ArrayList<>();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        SparePart part = s.getValue(SparePart.class);
                        if (part != null) parts.add(part);
                    }
                    callback.onSuccess(parts);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onFailure(error.getMessage());
                }
            });
    }

    public static void updateSparePartStock(String partId, int colomboStock,
                                            int galleStock, DataCallback<Void> callback) {
        db.child("spare_parts").child(partId).child("colomboStock").setValue(colomboStock);
        db.child("spare_parts").child(partId).child("galleStock").setValue(galleStock)
            .addOnSuccessListener(v -> callback.onSuccess(null))
            .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public static void saveSparePart(SparePart part, DataCallback<Void> callback) {
        db.child("spare_parts").child(part.getId()).setValue(part)
            .addOnSuccessListener(v -> callback.onSuccess(null))
            .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public static void deleteSparePart(String partId, DataCallback<Void> callback) {
        db.child("spare_parts").child(partId).removeValue()
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public static void saveMovementLog(PartMovementLog log, DataCallback<Void> callback) {
        db.child("inventory_logs").child(log.getId()).setValue(log)
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public static void fetchMovementLogs(DataCallback<List<PartMovementLog>> callback) {
        db.child("inventory_logs").orderByChild("timestamp")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<PartMovementLog> logs = new ArrayList<>();
                        for (DataSnapshot s : snapshot.getChildren()) {
                            PartMovementLog log = s.getValue(PartMovementLog.class);
                            if (log != null) logs.add(0, log); // Newest first
                        }
                        callback.onSuccess(logs);
                    }
                    @Override public void onCancelled(DatabaseError error) {
                        callback.onFailure(error.getMessage());
                    }
                });
    }

    // ─── Dispatch Requests ────────────────────────────────────────────────────

    public static void fetchDispatchRequests(DataCallback<List<DispatchRequest>> callback) {
        db.child("dispatch_requests")
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<DispatchRequest> requests = new ArrayList<>();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        DispatchRequest req = s.getValue(DispatchRequest.class);
                        if (req != null) requests.add(req);
                    }
                    callback.onSuccess(requests);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onFailure(error.getMessage());
                }
            });
    }

    public static void updateDispatchStatus(String requestId, String status,
                                            String assignedBranch, String assignedTech,
                                            DataCallback<Void> callback) {
        DatabaseReference ref = db.child("dispatch_requests").child(requestId);
        ref.child("status").setValue(status);
        if (assignedBranch != null) ref.child("assignedBranch").setValue(assignedBranch);
        if (assignedTech != null) ref.child("assignedTech").setValue(assignedTech);
        ref.child("status").setValue(status)
            .addOnSuccessListener(v -> callback.onSuccess(null))
            .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // ─── Technician Jobs ──────────────────────────────────────────────────────

    public static void fetchTechnicianJobs(String branch, DataCallback<List<TechnicianJob>> callback) {
        db.child("technician_jobs").orderByChild("branch").equalTo(branch)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<TechnicianJob> jobs = new ArrayList<>();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        TechnicianJob job = s.getValue(TechnicianJob.class);
                        if (job != null) jobs.add(job);
                    }
                    callback.onSuccess(jobs);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onFailure(error.getMessage());
                }
            });
    }

    public static void updateJobStage(String jobId, String stage, DataCallback<Void> callback) {
        db.child("technician_jobs").child(jobId).child("currentStage").setValue(stage)
            .addOnSuccessListener(v -> callback.onSuccess(null))
            .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // ─── Submit new appointment ───────────────────────────────────────────────

    public static void submitAppointment(RepairTicket ticket, DataCallback<Void> callback) {
        saveTicket(ticket, callback);
    }

    // ─── Image Upload ─────────────────────────────────────────────────────────

    public static void uploadImage(Uri fileUri, String folder, DataCallback<String> callback) {
        if (fileUri == null) {
            callback.onFailure("File URI is null");
            return;
        }

        try {
            // Read bytes from URI to be safe
            android.content.Context ctx = com.techfix.app.TechFixApp.getInstance();
            java.io.InputStream is = ctx.getContentResolver().openInputStream(fileUri);
            if (is == null) {
                callback.onFailure("Could not open file stream");
                return;
            }
            
            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
            int nRead;
            byte[] temp = new byte[16384];
            while ((nRead = is.read(temp, 0, temp.length)) != -1) {
                buffer.write(temp, 0, nRead);
            }
            byte[] data = buffer.toByteArray();
            is.close();

            String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
            StorageReference fileRef = storage.child(folder).child(fileName);

            fileRef.putBytes(data)
                .continueWithTask(task -> {
                    if (!task.isSuccessful() && task.getException() != null) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                })
                .addOnSuccessListener(uri -> callback.onSuccess(uri.toString()))
                .addOnFailureListener(e -> {
                    String error = e.getMessage();
                    if (error != null && error.contains("Object does not exist")) {
                        error = "Object not found in bucket: " + fileRef.getBucket() + ". Ensure Storage is initialized in Firebase Console.";
                    }
                    callback.onFailure("Photo upload failed: " + error);
                });

        } catch (Exception e) {
            callback.onFailure("File access error: " + e.getMessage());
        }
    }
}
