package com.presldn.pouch.ui.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.presldn.pouch.R;
import com.presldn.pouch.application.PouchApplication;
import com.presldn.pouch.database.Pouch;
import com.presldn.pouch.database.Transaction;
import com.presldn.pouch.databinding.FragmentDashboardBinding;
import com.presldn.pouch.ui.activities.PouchActivity;
import com.presldn.pouch.ui.adapters.RecentTXRecyclerViewAdapter;
import com.presldn.pouch.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";

    private FragmentDashboardBinding mBinding;
    private MainViewModel mMainViewModel;
    private RecentTXRecyclerViewAdapter adapter;


    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_dashboard, container, false);
        View view = mBinding.getRoot();

        adapter = new RecentTXRecyclerViewAdapter();
        mBinding.txRv.setAdapter(adapter);

        mMainViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainViewModel.class);
        mMainViewModel.getRecentTransactions().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(@Nullable List<Transaction> transactions) {
                if (transactions != null) {
                    adapter.setTransactions(transactions);
                    checkTransactionsSize(transactions.size());
                }
            }
        });

        mMainViewModel.getMostSavedPouch().observe(this, new Observer<Pouch>() {
            @Override
            public void onChanged(@Nullable final Pouch pouch) {
                if (pouch != null) {
                    mBinding.setPouch(pouch);

                    mBinding.mostSavedPouchCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), PouchActivity.class);
                            intent.putExtra("pouch_name", pouch.getName());
                            startActivity(intent);

                        }
                    });
                }
            }
        });

        mMainViewModel.getAlmostCompletedPouch().observe(this, new Observer<Pouch>() {
            @Override
            public void onChanged(@Nullable final Pouch pouch) {
                if (pouch != null) {
                    mBinding.setAlmostCompletedPouch(pouch);
                    mBinding.almostCompCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), PouchActivity.class);
                            intent.putExtra("pouch_name", pouch.getName());
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        mBinding.setProfile(PouchApplication.getInstance().getProfile());

        mBinding.setAlmostCompletedPouch(mMainViewModel.getAlmostCompletedPouch().getValue());
        mBinding.setPouch(mMainViewModel.getMostSavedPouch().getValue());
        mBinding.txRv.setAdapter(adapter);

    }

    private void checkTransactionsSize(int itemCount) {
        if (itemCount > 0) {
            mBinding.txRv.setVisibility(View.VISIBLE);
            mBinding.emptyListTv.setVisibility(View.INVISIBLE);
        } else {
            mBinding.emptyListTv.setVisibility(View.VISIBLE);
            mBinding.txRv.setVisibility(View.INVISIBLE);
        }
    }

}
