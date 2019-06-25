package com.presldn.pouch.ui.fragments;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.presldn.pouch.R;
import com.presldn.pouch.database.Pouch;
import com.presldn.pouch.database.Transaction;
import com.presldn.pouch.databinding.FragmentPouchInfoBinding;
import com.presldn.pouch.utils.Helper;
import com.presldn.pouch.viewmodels.PouchViewModel;

import java.util.List;
import java.util.Objects;

public class PouchInfoFragment extends Fragment {

    private PouchViewModel mPouchViewModel;

    private FragmentPouchInfoBinding mBinding;

    public PouchInfoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPouchViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PouchViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_pouch_info, container, false);
        final View view = mBinding.getRoot();
        mPouchViewModel.getPouch().observe(this, new Observer<Pouch>() {
            @Override
            public void onChanged(@Nullable final Pouch pouch) {
                if (pouch != null) {
                    mBinding.setPouch(pouch);

                    if (pouch.getGoal() > 0) {
                        mBinding.withGoalLayout.setVisibility(View.VISIBLE);
                        mBinding.noGoalLayout.setVisibility(View.INVISIBLE);

                        int progress = Helper.getProgress(pouch.getGoal(), pouch.getBalance());
                        mBinding.pouchProgressBar.setProgressWithAnimation(progress > 0 ? progress : 0);
                        mBinding.goalReachedLabel.setVisibility(pouch.goalMet() ? View.VISIBLE : View.INVISIBLE);

                    } else {
                        mBinding.withGoalLayout.setVisibility(View.INVISIBLE);
                        mBinding.noGoalLayout.setVisibility(View.VISIBLE);
                    }

                    mBinding.cashoutBtn.setVisibility(pouch.goalMet() || pouch.getGoal() <= 0 ? View.VISIBLE : View.INVISIBLE);
                    mBinding.cashoutBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom)
                                    .setTitle("Cash Out")
                                    .setMessage("Are you sure you want to cash out?")
                                    .setNegativeButton("No", null)
                                    .setPositiveButton("Cash Out", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Helper.toastMessage(getContext(), "Cashed Out!");
                                            mPouchViewModel.deletePouch();
                                            Snackbar.make(view, "Cashed out!", Snackbar.LENGTH_LONG)
                                                    .setAction(R.string.alright, null).show();
                                            Objects.requireNonNull(getActivity()).finish();
                                        }
                                    })
                                    .create();
                            alertDialog.show();
                        }
                    });

                }
            }
        });

        mPouchViewModel.getTransactions().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                if (transactions != null) {
                }
            }
        });

        return view;
    }
}
