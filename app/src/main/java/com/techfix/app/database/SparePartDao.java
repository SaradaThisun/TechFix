package com.techfix.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.techfix.app.models.SparePart;

import java.util.List;

@Dao
public interface SparePartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SparePart part);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SparePart> parts);

    @Update
    void update(SparePart part);

    @Delete
    void delete(SparePart part);

    @Query("SELECT * FROM spare_parts ORDER BY name ASC")
    LiveData<List<SparePart>> getAllParts();

    @Query("SELECT * FROM spare_parts WHERE category = :category ORDER BY name ASC")
    LiveData<List<SparePart>> getPartsByCategory(String category);

    @Query("SELECT * FROM spare_parts WHERE id = :id LIMIT 1")
    SparePart getPartById(String id);

    @Query("DELETE FROM spare_parts")
    void deleteAll();
}
