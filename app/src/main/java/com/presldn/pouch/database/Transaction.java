package com.presldn.pouch.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "transaction_table", foreignKeys = @ForeignKey(
        entity = Pouch.class,
        parentColumns = "pouch_name",
        childColumns = "pouch_name",
        onDelete = CASCADE
))
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "pouch_name")
    private String pouchName;

    @ColumnInfo(name = "amount")
    private int amount;
    @ColumnInfo(name = "transaction_type")
    private String transactionType;
    @ColumnInfo(name = "date")
    private long date;

    public Transaction(String pouchName, int amount, String transactionType) {
        this.pouchName = pouchName;
        this.amount = amount;
        this.transactionType = transactionType;
        this.date = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public String getPouchName() {
        return pouchName;
    }

    public int getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public long getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPouchName(String pouchName) {
        this.pouchName = pouchName;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
