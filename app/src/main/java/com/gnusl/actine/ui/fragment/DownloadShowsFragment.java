package com.gnusl.actine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.AppCategories;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.DBShow;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.AuthActivity;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.DownloadsListAdapter;
import com.gnusl.actine.ui.adapter.ViewPagerAdapter;
import com.gnusl.actine.ui.custom.CustomAppBar;
import com.gnusl.actine.util.ObjectBox;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.google.android.material.tabs.TabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;


public class DownloadShowsFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {

    private View inflatedView;
    private RecyclerView rvDownloads;

    private DownloadsListAdapter downloadsListAdapter;
    private AppCategories currentCategory = AppCategories.Movies;
    private KProgressHUD progressHUD;
    private String showType;


    public DownloadShowsFragment() {
    }

    public static DownloadShowsFragment newInstance() {
        DownloadShowsFragment fragment = new DownloadShowsFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_download_shows, container, false);
            init();
        } else {
//            ViewGroup parent = (ViewGroup) inflatedView.getParent();
//            if (parent != null) {
//                parent.removeAllViews();
//            }
        }
        return inflatedView;
    }

    private void init() {

        findViews();


        downloadsListAdapter = new DownloadsListAdapter(getActivity());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        rvDownloads.setLayoutManager(gridLayoutManager);

        rvDownloads.setAdapter(downloadsListAdapter);

//        cubDownload.getSpCategory().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0: {
//                        if (currentCategory == AppCategories.Movies)
//                            return;
//                        currentCategory = AppCategories.Movies;
//                        init();
//                        break;
//                    }
//                    case 1: {
//                        if (currentCategory == AppCategories.TvShows)
//                            return;
//                        currentCategory = AppCategories.TvShows;
//                        init();
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                currentCategory = AppCategories.Movies;
//            }
//        });
//
//        cubDownload.getSpGenres().setVisibility(View.GONE);


        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.please_wait))
                .setMaxProgress(100)
                .show();

        Box<DBShow> dbShowBox = ObjectBox.get().boxFor(DBShow.class);
        List<DBShow> dbShows = dbShowBox.getAll();
        if (showType.equalsIgnoreCase("series"))
            DataLoader.getRequest(Urls.SeriesMyList.getLink(), this);
        else
            DataLoader.getRequest(Urls.MoviesMyList.getLink(), this);

        if (dbShows.size() != 0) {

            List<Show> movies = new ArrayList<>();
            for (DBShow dbShow : dbShows) {
                Show show = dbShow.getShowObject();
                File internalStorage = getActivity().getFilesDir();
                File file = new File(internalStorage, show.getTitle() + ".mp4");
                if (file.exists())
                    show.setInStorage(true);
                else
                    show.setInStorage(false);
                movies.add(show);
            }
            downloadsListAdapter.setList(movies);
        }
        if (progressHUD != null)
            progressHUD.dismiss();

    }

    private void findViews() {
        rvDownloads = inflatedView.findViewById(R.id.rv_downloads);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser){
////            if (clEmptyDownloadList == null)
////                return;
//            if (downloadsListAdapter == null)
//                return;
//            Box<DBShow> dbShowBox = ObjectBox.get().boxFor(DBShow.class);
//            List<DBShow> dbShows = dbShowBox.getAll();
//
//            if (dbShows.size() != 0) {
//
//                List<Show> movies = new ArrayList<>();
//                for (DBShow dbShow : dbShows) {
//                    Show show = dbShow.getShowObject();
//                    File internalStorage = getActivity().getFilesDir();
//                    File file = new File(internalStorage, show.getTitle() + ".mp4");
//                    if (file.exists())
//                        show.setInStorage(true);
//                    else
//                        show.setInStorage(false);
//                    movies.add(show);
//                }
//                downloadsListAdapter.setList(movies);
//            }else {
//                downloadsListAdapter.setList(new ArrayList<Show>());
//            }
//        }
//    }

    @Override
    public void onConnectionError(int code, String message) {
        if (progressHUD != null)
            progressHUD.dismiss();
        if (code == 401) {
            SharedPreferencesUtils.clear();
            startActivity(new Intent(getActivity(), AuthActivity.class));
            getActivity().finish();
        }
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

        if (jsonObject.has("movies")) {
//            clEmptyDownloadList.setVisibility(View.GONE);
//            clDownloadList.setVisibility(View.VISIBLE);

            List<Show> movies = Show.newList(jsonObject.optJSONArray("movies"), true, false, false);
            File internalStorage = getActivity().getFilesDir();
            for (Show movie : movies) {
                File file = new File(internalStorage, movie.getTitle() + ".mp4");
                if (file.exists())
                    movie.setInStorage(true);
                else
                    movie.setInStorage(false);
            }
            downloadsListAdapter.setList(movies);

        } else if (jsonObject.has("series")) {
            List<Show> movies = Show.newList(jsonObject.optJSONArray("series"), false, true, true);
            File internalStorage = getActivity().getFilesDir();
            for (Show movie : movies) {
                File file = new File(internalStorage, movie.getTitle() + ".mp4");
                if (file.exists())
                    movie.setInStorage(true);
                else
                    movie.setInStorage(false);
            }
            downloadsListAdapter.setList(movies);
        }

    }

    public void setShowType(String showType) {
        this.showType = showType;
    }
}