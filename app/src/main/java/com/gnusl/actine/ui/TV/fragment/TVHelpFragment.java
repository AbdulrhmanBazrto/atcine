package com.gnusl.actine.ui.TV.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.actine.R;


public class TVHelpFragment extends Fragment {

    View inflatedView;

    public TVHelpFragment() {
    }

    public static TVHelpFragment newInstance() {
        TVHelpFragment fragment = new TVHelpFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade_transition));
//        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        if (inflatedView == null) {
        inflatedView = inflater.inflate(R.layout.fragment_tvhelp, container, false);
//        init();
//        }
        return inflatedView;
    }
}
