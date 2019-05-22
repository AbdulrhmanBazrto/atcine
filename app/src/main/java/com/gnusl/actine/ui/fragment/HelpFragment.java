package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.actine.R;
import com.gnusl.actine.ui.custom.CustomAppBarWithBack;


public class HelpFragment extends Fragment implements View.OnClickListener {

    View inflatedView;

    private CustomAppBarWithBack cubHelpWithBack;

    public HelpFragment() {
    }

    public static HelpFragment newInstance() {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_help, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        cubHelpWithBack = inflatedView.findViewById(R.id.cub_help_with_back);

        cubHelpWithBack.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        cubHelpWithBack.getTvTitle().setText("Help");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

}