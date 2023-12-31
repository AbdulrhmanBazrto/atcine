package com.gnusl.actine.ui.Mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.GenresClickEvents;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.interfaces.LoadMoreCategoriesDelegate;
import com.gnusl.actine.model.Category;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.Mobile.activity.AuthActivity;
import com.gnusl.actine.ui.Mobile.activity.WatchActivity;
import com.gnusl.actine.ui.Mobile.activity.WatchActivity2;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.gnusl.actine.util.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final RecyclerView.RecycledViewPool recycledViewPool;
    private final RecyclerView homeRecycler;

    private final HomeMovieClick homeMovieClick;
    private final GenresClickEvents genresClickEvents;
    private final LoadMoreCategoriesDelegate loadMoreCategoriesDelegate;

    private Context mContext;
    private Show trendShow;
    private List<Category> categories = new ArrayList<>();

    private static int HOLDER_MOVIE = 0;
    private static int HOLDER_MOVIE_LIST = 1;

    HomeMovieListAdapter continueToWatchAdapter;


    public HomeAdapter(Context context, RecyclerView homeRecycler, HomeMovieClick homeMovieClick, GenresClickEvents genresClickEvents, LoadMoreCategoriesDelegate loadMoreCategoriesDelegate) {
        this.mContext = context;
        this.homeMovieClick = homeMovieClick;
        this.homeRecycler = homeRecycler;
        this.genresClickEvents = genresClickEvents;
        this.loadMoreCategoriesDelegate = loadMoreCategoriesDelegate;
        this.recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == HOLDER_MOVIE) {
            view = inflater.inflate(R.layout.item_home_movie, parent, false);
            return new HomeAdapter.MovieViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_home_movie_list, parent, false);
            return new HomeAdapter.MovieListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MovieViewHolder) {
            ((MovieViewHolder) holder).bind();
        } else if (holder instanceof MovieListViewHolder) {
            Category category = categories.get(position);
            String name = category.getTitle();
            int id = category.getId();
            ((MovieListViewHolder) holder).bind(id, name, category.getShows());
        }

//        if (position == categoriesName.size()) {
//            if (loadMoreCategoriesDelegate != null)
//                loadMoreCategoriesDelegate.loadMoreCategories(categoriesName.size());
//        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HOLDER_MOVIE_LIST;
        } else {
            return HOLDER_MOVIE_LIST;
        }
    }

    @Override
    public int getItemCount() {
        if (categories.size() == 0)
            return 0;
        return categories.size();
    }

    public void setData(Show trendMovie, List<Category> categories) {
        this.trendShow = trendMovie;
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void addNewContinueToWatch(Show latestPlayedShow) {
        if (continueToWatchAdapter != null){
            List<Show> list = continueToWatchAdapter.getList();
            boolean alreadyAdded = false;
            for (Show show : list) {
                if (show.getId() == latestPlayedShow.getId()){
                    alreadyAdded = true;
                }
            }
            if (!alreadyAdded) {
                list.add(0,latestPlayedShow);
                continueToWatchAdapter.setList(list);
            }
        }
    }


    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMovieImage;
        Button btnInfo, btnAddToMyList, btnPlay;
        TextView tvTrendName;

        MovieViewHolder(View itemView) {
            super(itemView);
            ivMovieImage = itemView.findViewById(R.id.iv_movie_image);
            btnPlay = itemView.findViewById(R.id.btn_play);
            btnAddToMyList = itemView.findViewById(R.id.btn_add_to_my_list);
            btnInfo = itemView.findViewById(R.id.btn_info);
            tvTrendName = itemView.findViewById(R.id.tv_trend_name);
            Utils.setOnFocusScale(btnPlay);
            Utils.setOnFocusScale(btnAddToMyList);
            Utils.setOnFocusScale(btnInfo);
            btnPlay.requestFocus();
        }

        public void bind() {

            Picasso.with(mContext).load(trendShow.getCoverImageUrl()).into(ivMovieImage);

            if (!(trendShow.getIsMovie() || trendShow.getIsEpisode())) {
                btnPlay.setVisibility(View.GONE);
                btnAddToMyList.setVisibility(View.GONE);
            } else {
                btnPlay.setVisibility(View.VISIBLE);
                btnAddToMyList.setVisibility(View.VISIBLE);
            }

            tvTrendName.setText(trendShow.getTitle());

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WatchActivity.class);
                    intent.putExtra("show", trendShow);
                    mContext.startActivity(intent);
                }
            });

            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeMovieClick != null) {
                        if (trendShow.getIsMovie())
                            homeMovieClick.onClickMovie(trendShow, null);
                        else
                            homeMovieClick.onClickSeries(trendShow);
                    }

                }
            });

            if (trendShow.getIsFavourite()) {
                btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_check_white), null, null, null);
            }

            btnAddToMyList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataLoader.postRequest(Urls.MovieFavorite.getLink().replaceAll("%id%", String.valueOf(trendShow.getId())), new ConnectionDelegate() {
                        @Override
                        public void onConnectionError(int code, String message) {
                            if (code == 401) {
                                SharedPreferencesUtils.clear();
                                mContext.startActivity(new Intent(mContext, AuthActivity.class));
                            }
                        }

                        @Override
                        public void onConnectionError(ANError anError) {
//                            Toast.makeText(mContext, "error happened", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onConnectionSuccess(JSONObject jsonObject) {
                            if (jsonObject.optString("status").equalsIgnoreCase("added")) {
                                trendShow.setIsFavourite(true);
                                btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_check_white), null, null, null);
                            } else {
                                trendShow.setIsFavourite(false);
                                btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_mylist), null, null, null);
                            }
                        }
                    });
                }
            });

        }
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        TextView tvListTitle, tvMore;
        RecyclerView rvMovieList;

        MovieListViewHolder(View itemView) {
            super(itemView);

            tvListTitle = itemView.findViewById(R.id.tv_list_title);
            tvMore = itemView.findViewById(R.id.tv_more);
            rvMovieList = itemView.findViewById(R.id.rv_movie_list);

            Utils.setOnFocusScale(tvMore);
        }

        public void bind(int id, String name, List<Show> movies) {

            tvListTitle.setText(name);
            tvListTitle.setTransitionName("transition" + id + name);
            rvMovieList.setTransitionName("transition_rv" + id + name);
            Log.d("CATEGORY_id", String.valueOf(id));
            Log.d("CATEGORY_NAME", name);
            if (name.equalsIgnoreCase("random") || name.equalsIgnoreCase("favourite") || name.equalsIgnoreCase("not completed")) {
                tvMore.setVisibility(View.GONE);
//                tvMore.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (genresClickEvents != null) {
//                            Category category = new Category();
//                            category.setId(id);
//                            category.setTitle(name);
//                            genresClickEvents.onSelectGenres(category, null);
//                        }
//                    }
//                });
            } else {
                tvMore.setVisibility(View.VISIBLE);
                tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (genresClickEvents != null) {
                            Category category = new Category();
                            category.setId(id);
                            category.setTitle(name);
                            genresClickEvents.onSelectGenres(category, tvListTitle, rvMovieList);
                        }
                    }
                });
            }

            rvMovieList.setRecycledViewPool(recycledViewPool);

            HomeMovieListAdapter homeMovieListAdapter = new HomeMovieListAdapter(mContext, movies, homeMovieClick,"Mobile");

            if (name.equalsIgnoreCase("متابعة مشاهدة") || name.equalsIgnoreCase("Continue To Watch")){
                continueToWatchAdapter =  homeMovieListAdapter;
            }

//            homeMovieListAdapter.setHasStableIds(false);

            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

            rvMovieList.setLayoutManager(layoutManager);

            rvMovieList.swapAdapter(homeMovieListAdapter, false);
            rvMovieList.scheduleLayoutAnimation();

        }
    }

    public Show getTrendShow() {
        return trendShow;
    }

    public void setTrendShow(Show trendShow) {
        this.trendShow = trendShow;
        notifyItemChanged(0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
