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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mTitle = findViewById(R.id.title);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
