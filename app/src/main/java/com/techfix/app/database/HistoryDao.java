package com.techfix.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.techfix.app.models.HistoryItem;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HistoryItem item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<HistoryItem> items);

    @Update
    void update(HistoryItem item);

    @Delete
    void delete(HistoryItem item);

    @Query("SELECT * FROM history WHERE userId = :userId ORDER BY repairDate DESC")
    LiveData<List<HistoryItem>> getHistoryByUser(String userId);

    @Query("SELECT * FROM history WHERE userId = :userId AND status = :status ORDER BY repairDate DESC")
    LiveData<List<HistoryItem>> getHistoryByStatus(String userId, String status);

    @Query("SELECT * FROM history WHERE id = :id LIMIT 1")
    HistoryItem getHistoryItemById(String id);

    @Query("DELETE FROM history WHERE userId = :userId")
    void deleteAllForUser(String userId);
}
