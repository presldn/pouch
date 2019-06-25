package com.presldn.pouch.ui.activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.presldn.pouch.R;
import com.presldn.pouch.database.Pouch;
import com.presldn.pouch.databinding.ActivityConfigPouchBinding;
import com.presldn.pouch.viewmodels.PouchConfigViewModel;

import java.text.ParseException;
import java.util.Objects;

public class PouchConfigActivity extends AppCompatActivity {
    private static final String TAG = "PouchConfigActivity";

    public static final String POUCH = "pouch";

    private PouchConfigViewModel mPouchConfigViewModel;

    private Pouch pouch;
    private boolean update;

    private String currentName;

    private ActivityConfigPouchBinding mBinding;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!(TextUtils.isEmpty(s))) {
                mBinding.fab.setEnabled(true);
                mBinding.fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorText)));
            } else {
                mBinding.fab.setEnabled(false);
                mBinding.fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDisabled)));
            }
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_pouch);

        mPouchConfigViewModel = ViewModelProviders.of(this).get(PouchConfigViewModel.class);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_config_pouch);

        setSupportActionBar(mBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mBinding.fab.setEnabled(false);
        mBinding.pouchNameEt.addTextChangedListener(mTextWatcher);

        if ((pouch = (Pouch) getIntent().getSerializableExtra("pouch")) != null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Update " + pouch.getName());
            update = true;
            currentName = pouch.getName();
            mBinding.setPouchToEdit(pouch);
        }
    }

    private void setResult(Pouch pouch, int flag) {
        setResult(flag, new Intent().putExtra(POUCH, pouch));
        finish();
    }

    public void config(View view) {
        if (TextUtils.isEmpty(mBinding.pouchNameEt.getText())) {
            mBinding.pouchNameEt.setError(getString(R.string.name_error));

        } else {
            if (!update) {
                //If creating goal, check if name exists in database
                if (mPouchConfigViewModel.nameExists(mBinding.pouchNameEt.getText().toString())) {
                    mBinding.pouchNameEt.setError(getString(R.string.existing_name));
                } else {
                    config();
                }
            } else {
                //if its update, check if the new name is different to the old name; if it is, check
                //the name in the database
                if (!currentName.equals(mBinding.pouchNameEt.getText().toString())) {
                    if (mPouchConfigViewModel.nameExists(mBinding.pouchNameEt.getText().toString())) {
                        mBinding.pouchNameEt.setError(getString(R.string.existing_name));
                    } else {
                        config();
                    }
                } else {
                    config();
                }
            }
        }

    }

    private void config() {
        // FIXME: 03/01/2019 Name actually doesnt change for some absurd reason
        int goal;
        try {
            goal = (int) mBinding.goalAmountEt.getCurrencyDouble() * 100;
        } catch (ParseException | NumberFormatException e) {
            goal = 0;
        }


        if (update) {
            Log.d(TAG, "config: name: " + mBinding.pouchNameEt.getText().toString());
            pouch.setName(mBinding.pouchNameEt.getText().toString());
            Log.d(TAG, "config: " + pouch.getName());
            pouch.setGoal(goal);
        } else {

            pouch = new Pouch(mBinding.pouchNameEt.getText().toString(), goal);
        }

        setResult(pouch, RESULT_OK);
        finish();
    }
}
