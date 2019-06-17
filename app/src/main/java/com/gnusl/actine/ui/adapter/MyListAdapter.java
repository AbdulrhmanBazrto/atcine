package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.model.Show;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final HomeMovieClick homeMovieClick;
    private Context mContext;
    private List<Show> movies = new ArrayList<>();


    public MyListAdapter(Context context, HomeMovieClick homeMovieClick) {
        this.mContext = context;
        this.homeMovieClick = homeMovieClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_home_list_movie, parent, false);
        return new MovieListViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        ((MovieListViewHolder) holder).bind();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setList(List<Show> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;

        MovieListViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
        }

        public void bind() {

            Picasso.with(mContext).load(movies.get(getAdapterPosition()).getThumbnailImageUrl()).into(ivThumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Show show = movies.get(getAdapterPosition());
                    if (homeMovieClick != null)
                        if (show.getIsMovie()) {
                            homeMovieClick.onClickMovie(show);
                        } else if (!show.getIsMovie()) {
                            if (show.getIsEpisode()) {
                                homeMovieClick.onClickMovie(show);
                            } else {
                                homeMovieClick.onClickSeries(show);
                            }
                        }
                }
            });
        }
    }
}
