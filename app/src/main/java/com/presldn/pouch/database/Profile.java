package com.presldn.pouch.database;

import com.presldn.pouch.application.PouchApplication;

public class Profile {

    private String currencySymbol;
    private int totalSaved;

    public Profile(String currencySymbol, int totalSaved) {
        this.currencySymbol = currencySymbol;
        this.totalSaved = totalSaved;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
        PouchApplication.getInstance().saveProfile(this);
    }

    public int getTotalSaved() {
        return totalSaved;
    }

    public void add(int amount) {
        if (amount > 0) {
            totalSaved += amount;
            PouchApplication.getInstance().saveProfile(this);
        }
    }

    public void remove(int amount) {
        if (amount > 0 && amount <= totalSaved) {
            totalSaved -= amount;
            PouchApplication.getInstance().saveProfile(this);
        }
    }

}
