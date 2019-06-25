package com.presldn.pouch.ui.activities;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.presldn.pouch.R;
import com.presldn.pouch.database.Pouch;
import com.presldn.pouch.ui.fragments.PouchInfoFragment;
import com.presldn.pouch.ui.fragments.PouchTransactionFragment;
import com.presldn.pouch.viewmodels.PouchViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PouchActivity extends AppCompatActivity {
    public static final int EDIT_POUCH_ACTIVITY_REQUEST_CODE = 2;

    private PouchViewModel mPouchViewModel;

    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private Fragment[] fragments = {new PouchInfoFragment(), new PouchTransactionFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pouch);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final String pouchName = getIntent().getStringExtra("pouch_name");

        if (pouchName == null) {
            finish();
        }

        mPouchViewModel = ViewModelProviders.of(this).get(PouchViewModel.class);

        mPouchViewModel.getPouch(pouchName).observe(this, new Observer<Pouch>() {
            @Override
            public void onChanged(@Nullable Pouch pouch) {
                if (pouch != null) {
                    Objects.requireNonNull(getSupportActionBar()).setTitle(pouch.getName());
                }

            }
        });


        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_pouch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.pouch_menu_edit:
                editPouch();
                return true;
            case R.id.pouch_menu_delete:
                deletePouch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editPouch() {
        Intent intent = new Intent(PouchActivity.this, PouchConfigActivity.class);
        intent.putExtra(PouchConfigActivity.POUCH, mPouchViewModel.getPouch().getValue());
        startActivityForResult(intent, EDIT_POUCH_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_POUCH_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mPouchViewModel.updatePouch((Pouch) data.getSerializableExtra(PouchConfigActivity.POUCH));
        }
    }

    private void deletePouch() {
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Delete pouch")
                .setMessage("Are you sure you want to delete this pouch?")
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPouchViewModel.deletePouch();
                        dialog.dismiss();
                        finish();
                    }
                })
                .create();
        alertDialog.show();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Fragment[] fragments;

        SectionsPagerAdapter(FragmentManager fm, Fragment[] fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        public void setFragments(Fragment[] fragments) {
            this.fragments = fragments;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (fragments != null)
                return fragments.length;
            else return 0;
        }
    }
}
