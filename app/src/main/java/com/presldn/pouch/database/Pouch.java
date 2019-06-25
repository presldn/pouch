package com.presldn.pouch.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "pouch_table")
public class Pouch implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "pouch_name")
    private String name;

    private int balance;
    private int remaining;
    private int goal;

    public Pouch(@NonNull String name, int goal) {
        this.name = name;
        this.balance = 0;
        this.goal = goal;
        this.remaining = goal - this.balance;
    }

    @Ignore
    public void addBalance(int amount) {
        if (amount > 0) {
            balance += amount;
            this.remaining = this.goal - this.balance;
        }
    }

    @Ignore
    public void removeBalance(int amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            this.remaining = this.goal - this.balance;
        }
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public boolean goalMet() {
        return balance >= goal;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
        this.remaining = this.goal - this.balance;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
        this.remaining = this.goal - this.balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pouch pouch = (Pouch) o;
        return Objects.equals(name, pouch.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Pouch{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", goal=" + goal +
                '}';
    }
}
