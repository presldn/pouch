package com.presldn.pouch.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.mynameismidori.currencypicker.ExtendedCurrency;
import com.presldn.pouch.database.Profile;

public class PouchApplication extends Application {

    private static final String PREF_TOTAL_SAVED = "total_saved";
    private static final String PREF_CURRENCY_SYMBOL = "currency_symbol";

    private static PouchApplication INSTANCE;
    private SharedPreferences preferences;
    private Profile profile;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        preferences = getSharedPreferences("com.presldn.pouch.profile", MODE_PRIVATE);
        createProfile();
    }

    private void createProfile() {
        int totalMoneySaved = preferences.getInt(PREF_TOTAL_SAVED, 0);
        String currencySymbol = preferences.getString(PREF_CURRENCY_SYMBOL,
                ExtendedCurrency.getCurrencyByName("British Pound").getSymbol());
        profile = new Profile(currencySymbol, totalMoneySaved);
    }

    public void saveProfile(Profile profile) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_TOTAL_SAVED, profile.getTotalSaved());
        editor.putString(PREF_CURRENCY_SYMBOL, profile.getCurrencySymbol());
        editor.apply();
    }

    public Profile getProfile() {
        return profile;
    }

    public static synchronized PouchApplication getInstance() {
        return INSTANCE;
    }
}
