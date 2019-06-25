package com.presldn.pouch.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PouchDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Pouch pouch);

    @Query("SELECT * FROM pouch_table")
    LiveData<List<Pouch>> getAllPouches();

    @Query("SELECT * FROM pouch_table WHERE pouch_name = :name LIMIT 1")
    LiveData<Pouch> getPouch(String name);

    @Query("SELECT COUNT(*) FROM pouch_table WHERE pouch_name = :name")
    int getNameCount(String name);

    @Update
    void update(Pouch pouch);

    @Query("SELECT *, MAX(balance) FROM pouch_table LIMIT 1")
    LiveData<Pouch> getMostSaved();

    @Query("SELECT *, MIN(remaining) FROM pouch_table WHERE remaining > 0 LIMIT 1")
    LiveData<Pouch> getAlmostCompletedPouch();

    @Query("DELETE FROM pouch_table")
    void deleteAll();

    @Delete
    void delete(Pouch pouch);

}
