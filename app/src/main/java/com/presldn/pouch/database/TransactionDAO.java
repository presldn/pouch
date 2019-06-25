package com.presldn.pouch.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TransactionDAO {

    @Insert
    void insert(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("SELECT * FROM transaction_table WHERE pouch_name = :pouchName")
    LiveData<List<Transaction>> getPouchesTransactions(String pouchName);

    @Query("SELECT * FROM transaction_table ORDER BY date DESC LIMIT 10")
    LiveData<List<Transaction>> getRecentTransactions();

}
