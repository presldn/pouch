package com.presldn.pouch.ui.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
import com.presldn.pouch.BuildConfig;
import com.presldn.pouch.R;
import com.presldn.pouch.application.PouchApplication;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    public static final String APP_PNAME = "com.presldn.pouch";
    public static final String PREF_CURRENCY_NAME = "currency_name";

    private SharedPreferences mPreferences;
    private TextView mVersionNumber;
    private TextView mCurrencyName;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mPreferences = Objects.requireNonNull(getActivity())
                .getSharedPreferences("com.presldn.pouch.profile", MODE_PRIVATE);


        mVersionNumber = view.findViewById(R.id.versionNumber);

        mCurrencyName = view.findViewById(R.id.currency);

        updatePreferences();

        TextView rateApp = view.findViewById(R.id.rate_app);
        rateApp.setOnClickListener(this);

        TextView giveFeedback = view.findViewById(R.id.give_feedback);
        giveFeedback.setOnClickListener(this);

        LinearLayout selectCurrency = view.findViewById(R.id.select_currency);
        selectCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putString(PREF_CURRENCY_NAME, name);
                        editor.apply();

                        PouchApplication.getInstance().getProfile().setCurrencySymbol(symbol);

                        updatePreferences();

                        picker.dismiss();

                    }
                });
                picker.show(getChildFragmentManager(), "CURRENCY_PICKER");
            }
        });

        return view;
    }

    private void updatePreferences() {
        mVersionNumber.setText(BuildConfig.VERSION_NAME);
        mCurrencyName.setText(mPreferences
                .getString(PREF_CURRENCY_NAME, "British Pound"));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rate_app :
                try {
                    Objects.requireNonNull(getContext()).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                } catch (ActivityNotFoundException e) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + APP_PNAME)));
                }
                break;
            case R.id.give_feedback :
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:prezldn@gmail.com"));
                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    break;
                }
                break;
        }
    }
}
