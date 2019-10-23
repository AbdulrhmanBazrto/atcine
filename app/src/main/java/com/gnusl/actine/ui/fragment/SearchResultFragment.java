package com.gnusl.actine.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.interfaces.LoadMoreDelegate;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.MovieMoreLikeAdapter;
import com.gnusl.actine.util.Constants;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.List;


public class SearchResultFragment extends Fragment implements View.OnClickListener, HomeMovieClick, ConnectionDelegate, LoadMoreDelegate {

    View inflatedView;

    RecyclerView rvSearchResult;
    private MovieMoreLikeAdapter movieMoreLikeAdapter;
    private KProgressHUD progressHUD;

    private String searchType;
    private String searchFor;
    private String key;

    public SearchResultFragment() {
    }

    public static SearchResultFragment newInstance(Bundle bundle) {
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.searchFor = getArguments().getString("searchFor");
            this.searchType = getArguments().getString("searchType");

            this.key = getArguments().getString("key");

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_search_result, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        findViews();

        movieMoreLikeAdapter = new MovieMoreLikeAdapter(getActivity(), this, this);
        GridLayoutManager gridLayoutManager;
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            // on a large screen device ...
            gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        }
        else if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            // on a large screen device ...
            gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        }


        rvSearchResult.setLayoutManager(gridLayoutManager);

        rvSearchResult.setAdapter(movieMoreLikeAdapter);

        String url = "";
        if (searchFor.equalsIgnoreCase("series"))
            url = Urls.Series.getLink();
        else if (searchFor.equalsIgnoreCase("movies"))
            url = Urls.Movies.getLink();

        url = url.substring(0, url.length() - 1);
        url += "?";
        switch (searchType) {
            case "category": {
                url += "category_id=" + key;
                break;
            }
            case "title": {
                url += "title=" + key;
                break;
            }
        }
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.please_wait))
                .setMaxProgress(100)
                .show();


        DataLoader.getRequest(url + "&skip=" + 0 + "&take=" + 10, this);

    }

    private void findViews() {
        rvSearchResult = inflatedView.findViewById(R.id.rv_search_result);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onClickMovie(Show movie) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof SearchContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), movie);
                ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle);
            }
            if (fragment instanceof HomeContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), movie);
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle);
            }
        }
    }

    @Override
    public void onClickSeries(Show series) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof SearchContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                bundle.putString("type", "season");
                ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle);
            }
            if (fragment instanceof HomeContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                bundle.putString("type", "season");
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle);
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
        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (progressHUD != null)
            progressHUD.dismiss();

        if (jsonObject.has("series")) {
            List<Show> series = Show.newList(jsonObject.optJSONArray("series"), false, false, false);
            movieMoreLikeAdapter.setList(series);
        }

        if (jsonObject.has("movies")) {
            List<Show> movies = Show.newList(jsonObject.optJSONArray("movies"), true, false, false);
            movieMoreLikeAdapter.setList(movies);
        }
    }

    @Override
    public void loadMore(int skip) {
        String url = "";
        if (searchFor.equalsIgnoreCase("series"))
            url = Urls.Series.getLink();
        else if (searchFor.equalsIgnoreCase("movies"))
            url = Urls.Movies.getLink();

        url = url.substring(0, url.length() - 1);
        url += "?";
        switch (searchType) {
            case "category": {
                url += "category_id=" + key;
                break;
            }
            case "title": {
                url += "title=" + key;
                break;
            }
        }

        DataLoader.getRequest(url + "&skip=" + skip + "&take=" + 10, this);
    }
}
