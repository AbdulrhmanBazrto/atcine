package com.gnusl.actine.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.AppCategories;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.MyListAdapter;
import com.gnusl.actine.ui.custom.CustomAppBarWithSelectAndBack;
import com.gnusl.actine.util.Constants;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.List;


public class MySeriesFragment extends Fragment implements View.OnClickListener, HomeMovieClick, ConnectionDelegate {

    View inflatedView;

    private RecyclerView rvMyList;
    private MyListAdapter myListAdapter;
    private KProgressHUD progressHUD;

    public MySeriesFragment() {
    }

    public static MySeriesFragment newInstance() {
        MySeriesFragment fragment = new MySeriesFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_my_list, container, false);
            init();
        } else {
            ViewGroup parent = (ViewGroup) inflatedView.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }

        return inflatedView;
    }

    private void init() {

        rvMyList = inflatedView.findViewById(R.id.rv_my_list);
        myListAdapter = new MyListAdapter(getActivity(), this);

        GridLayoutManager layoutManager;
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            // on a large screen device ...
            layoutManager = new GridLayoutManager(getActivity(), 5);
        } else if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            // on a large screen device ...
            layoutManager = new GridLayoutManager(getActivity(), 5);
        } else {
            layoutManager = new GridLayoutManager(getActivity(), 3);
        }

        rvMyList.setLayoutManager(layoutManager);

        rvMyList.setAdapter(myListAdapter);

        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.please_wait))
                .setMaxProgress(100)
                .show();


        DataLoader.getRequest(Urls.SeriesMyList.getLink(), this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    @Override
    public void onClickMovie(Show show, ImageView ivThumbnail) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof MoreContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), show);
                bundle.putString("transition", ViewCompat.getTransitionName(ivThumbnail));
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail);
            }
        }
    }

    @Override
    public void onClickSeries(Show series) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof MoreContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                bundle.putString("type", "season");
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null);
            }
        }
    }

    @Override
    public void onConnectionError(int code, String message) {
        if (progressHUD != null)
            progressHUD.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        if (progressHUD != null)
            progressHUD.dismiss();
//        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (progressHUD != null)
            progressHUD.dismiss();

        List<Show> series = Show.newList(jsonObject.optJSONArray("series"), false, false, false);
        myListAdapter.setList(series);

        if (series.isEmpty()) {
            inflatedView.findViewById(R.id.hint).setVisibility(View.VISIBLE);
        } else {
            inflatedView.findViewById(R.id.hint).setVisibility(View.GONE);
        }


    }
}
