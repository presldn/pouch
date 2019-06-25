package com.presldn.pouch.ui.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.presldn.pouch.R;
import com.presldn.pouch.databinding.IntroFragmentBinding;
import com.presldn.pouch.viewmodels.IntroViewModel;


public class IntroFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int mPosition;
    private IntroViewModel mModel;

    public IntroFragment() {
        // Required empty public constructor
    }

    public static IntroFragment newInstance(int position) {
        IntroFragment fragment = new IntroFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            mModel = ViewModelProviders.of(getActivity()).get(IntroViewModel.class);
        }

        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        IntroFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.intro_fragment, container, false);
        binding.setPage(mModel.getSliderPage(mPosition));


        return binding.getRoot();
    }

}
