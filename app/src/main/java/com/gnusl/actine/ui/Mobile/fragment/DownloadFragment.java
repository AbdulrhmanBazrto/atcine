package com.gnusl.actine.ui.Mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.AppCategories;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.ui.Mobile.activity.AuthActivity;
import com.gnusl.actine.ui.Mobile.activity.MainActivity;
import com.gnusl.actine.ui.Mobile.adapter.DownloadsListAdapter;
import com.gnusl.actine.ui.Mobile.adapter.ViewPagerAdapter;
import com.gnusl.actine.ui.Mobile.custom.CustomAppBar;
import com.gnusl.actine.ui.Mobile.custom.LoaderPopUp;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.io.File;
import java.util.List;


public class DownloadFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {

    private View inflatedView;
    private Button btnFindDownload;
    private View clEmptyDownloadList, clDownloadList;
    private RecyclerView rvDownloads;
    private CustomAppBar cubDownload;

    private DownloadsListAdapter downloadsListAdapter;
    private AppCategories currentCategory = AppCategories.Movies;
    private TabLayout tlMainTabLayout;
    private ViewPager vpMainContainer;
    private ViewPagerAdapter adapter;
    private DownloadShowsFragment downloadShowsFragment;

    public DownloadFragment() {
    }

    public static DownloadFragment newInstance() {
        DownloadFragment fragment = new DownloadFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_download, container, false);
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

        findViews();

        setupViewPager(vpMainContainer);
        vpMainContainer.setOffscreenPageLimit(2);

        tlMainTabLayout.setupWithViewPager(vpMainContainer);

        tlMainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
//                changeTabTitle(position);
                vpMainContainer.setCurrentItem(position);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpMainContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void findViews() {
        rvDownloads = inflatedView.findViewById(R.id.rv_downloads);
        btnFindDownload = inflatedView.findViewById(R.id.btn_find_download);
        clEmptyDownloadList = inflatedView.findViewById(R.id.cl_empty_downloads_hint);
        clDownloadList = inflatedView.findViewById(R.id.cl_download_list);
        cubDownload = inflatedView.findViewById(R.id.cub_downloads);
        tlMainTabLayout = inflatedView.findViewById(R.id.tl_main_tab_layout);
        vpMainContainer = inflatedView.findViewById(R.id.vp_main_container);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_find_download: {
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).replaceFragment(1);
                    return;
                }
                clEmptyDownloadList.setVisibility(View.GONE);
                clDownloadList.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser){
//            if (clEmptyDownloadList == null)
//                return;
//
//            Box<DBShow> dbShowBox = ObjectBox.get().boxFor(DBShow.class);
//            List<DBShow> dbShows = dbShowBox.getAll();
//
//            if (dbShows.size() != 0) {
//                clEmptyDownloadList.setVisibility(View.GONE);
//                clDownloadList.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void onConnectionError(int code, String message) {
        LoaderPopUp.dismissLoader();
        if (code == 401) {
            SharedPreferencesUtils.clear();
            startActivity(new Intent(getActivity(), AuthActivity.class));
            getActivity().finish();
        }
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        LoaderPopUp.dismissLoader();
//        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
        // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        LoaderPopUp.dismissLoader();

        if (jsonObject.has("movies")) {
            clEmptyDownloadList.setVisibility(View.GONE);
            clDownloadList.setVisibility(View.VISIBLE);

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
            clEmptyDownloadList.setVisibility(View.GONE);
            clDownloadList.setVisibility(View.VISIBLE);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        if (downloadShowsFragment == null)
            downloadShowsFragment = DownloadShowsFragment.newInstance();
        downloadShowsFragment.setShowType("Series");
        adapter.addFragment(downloadShowsFragment, getActivity().getString(R.string.tv_series));
        DownloadShowsFragment fragment = new DownloadShowsFragment();
        fragment.setShowType("Movies");
        adapter.addFragment(fragment, getActivity().getString(R.string.movies));
        viewPager.setAdapter(adapter);

    }
}
