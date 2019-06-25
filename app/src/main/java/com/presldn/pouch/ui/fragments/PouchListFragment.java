package com.presldn.pouch.ui.fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.presldn.pouch.R;
import com.presldn.pouch.database.Pouch;
import com.presldn.pouch.databinding.FragmentPouchListBinding;
import com.presldn.pouch.ui.activities.PouchConfigActivity;
import com.presldn.pouch.ui.adapters.PouchRecyclerViewAdapter;
import com.presldn.pouch.viewmodels.MainViewModel;

import java.util.List;

public class PouchListFragment extends Fragment {

    public static final int NEW_POUCH_ACTIVITY_REQUEST_CODE = 1;

    private FragmentPouchListBinding mBinding;

    private MainViewModel mMainViewModel;
    private PouchRecyclerViewAdapter pouchRecyclerViewAdapter;

    public PouchListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_pouch_list, container, false);
        View rootView = mBinding.getRoot();

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        pouchRecyclerViewAdapter = new PouchRecyclerViewAdapter(getContext());
        mBinding.pouchRecyclerView.setAdapter(pouchRecyclerViewAdapter);
        mMainViewModel.getPouches().observe(this, new Observer<List<Pouch>>() {
            @Override
            public void onChanged(@Nullable List<Pouch> pouches) {

                if (pouches != null) {
                    pouchRecyclerViewAdapter.setPouches(pouches);
                    checkPouchSize(pouchRecyclerViewAdapter.getItemCount());
                }
            }
        });

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PouchConfigActivity.class);
                startActivityForResult(intent, NEW_POUCH_ACTIVITY_REQUEST_CODE);
            }
        });



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.pouchRecyclerView.setAdapter(pouchRecyclerViewAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_POUCH_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mMainViewModel.insertPouch((Pouch) data.getSerializableExtra(PouchConfigActivity.POUCH));

            Snackbar snackbar = Snackbar.make(mBinding.fab, R.string.pouch_added, Snackbar.LENGTH_SHORT)
                    .setActionTextColor(Color.WHITE)
                    .setAction(R.string.alright, null);

            // get snackbar view
            View snackbarView = snackbar.getView();

            // change snackbar text color
            int snackbarTextId = android.support.design.R.id.snackbar_text;
            TextView textView = snackbarView.findViewById(snackbarTextId);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));

            snackbar.show();
        }
    }

    private void checkPouchSize(int itemCount) {
        if (itemCount > 0) {
            mBinding.pouchRecyclerView.setVisibility(View.VISIBLE);
            mBinding.emptyListTv.setVisibility(View.INVISIBLE);
        } else {
            mBinding.emptyListTv.setVisibility(View.VISIBLE);
            mBinding.pouchRecyclerView.setVisibility(View.INVISIBLE);
        }
    }
}
