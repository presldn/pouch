package com.presldn.pouch.ui.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.presldn.pouch.R;
import com.presldn.pouch.ui.fragments.DashboardFragment;
import com.presldn.pouch.ui.fragments.PouchListFragment;
import com.presldn.pouch.ui.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTitle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_goals:
                    changeFragment(new PouchListFragment());
                    mTitle.setText(getString(R.string.app_name));
                    return true;
                case R.id.navigation_dashboard:
                    changeFragment(new DashboardFragment());
                    mTitle.setText(getString(R.string.title_dashboard));
                    return true;
                case R.id.navigation_settings:
                    changeFragment(new SettingsFragment());
                    mTitle.setText(getString(R.string.title_settings));
                    return true;
            }
            return false;
        }
    };


    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        checkFirstTimeUse();

//        MobileAds.initialize(this, getString(R.string.app_id));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mTitle = findViewById(R.id.title);

//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                mAdView.setVisibility(View.VISIBLE);
//                Log.d(MainActivity.class.getSimpleName(), "onAdLoaded: ad loaded");
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fail.
//                Log.d(MainActivity.class.getSimpleName(), "onAdFailedToLoad: add failed to load");
//                mAdView.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        });


        changeFragment(new PouchListFragment());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void checkFirstTimeUse() {
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                boolean test = true;

                //  If the activity has never started before...
                // TODO: 03/01/2019 change back to isFirstStart when done testing
                if (test) {

                    //  Launch app intro
                    final Intent i = new Intent(MainActivity.this, TutorialActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
