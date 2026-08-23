package com.techfix.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.techfix.app.models.RepairTicket;

import java.util.List;

@Dao
public interface RepairTicketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RepairTicket ticket);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RepairTicket> tickets);

    @Update
    void update(RepairTicket ticket);

    @Delete
    void delete(RepairTicket ticket);

    @Query("SELECT * FROM repair_tickets WHERE userId = :userId ORDER BY createdAt DESC")
    LiveData<List<RepairTicket>> getTicketsByUser(String userId);

    @Query("SELECT * FROM repair_tickets WHERE id = :id LIMIT 1")
    LiveData<RepairTicket> getTicketById(String id);

    @Query("SELECT * FROM repair_tickets WHERE userId = :userId AND isPaid = 0 ORDER BY createdAt DESC LIMIT 1")
    LiveData<RepairTicket> getActiveTicket(String userId);

    @Query("DELETE FROM repair_tickets WHERE userId = :userId")
    void deleteAllForUser(String userId);
}
