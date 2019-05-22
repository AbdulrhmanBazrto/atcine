package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.ProfileClick;

import java.util.ArrayList;
import java.util.List;


public class ProfilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> imageList = new ArrayList<>();
    private Context mContext;
    private ProfileClick profileClick;

    private static final int HOLDER_ADD_PROFILE = 0;
    private static final int HOLDER_PROFILE = 1;


    public ProfilesAdapter(Context context, ProfileClick profileClick) {
        this.mContext = context;
        this.profileClick = profileClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == HOLDER_PROFILE) {
            view = inflater.inflate(R.layout.item_profile, parent, false);
            return new ProfilesAdapter.ProfileViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_add_profile, parent, false);
            return new ProfilesAdapter.AddProfileViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ProfileViewHolder)
            ((ProfileViewHolder) holder).bind();
        else if (holder instanceof AddProfileViewHolder)
            ((AddProfileViewHolder) holder).bind();


    }

    @Override
    public int getItemCount() {
//        if (imageList.size() == 0)
//            return 1;
//        else
//            return imageList.size() + 1;
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
//        if (imageList.size() == 0)
//            return HOLDER_ADD_PROFILE;
//        else
        if (position == imageList.size())
            return HOLDER_ADD_PROFILE;
        else
            return HOLDER_PROFILE;
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {

        TextView tvProfileName;
        ImageView ivProfile;

        ProfileViewHolder(View itemView) {
            super(itemView);
            tvProfileName = itemView.findViewById(R.id.tv_profile_name);
            ivProfile = itemView.findViewById(R.id.iv_profile);

        }

        public void bind() {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profileClick.onClickProfile();
                }
            });

        }
    }


    class AddProfileViewHolder extends RecyclerView.ViewHolder {

        AddProfileViewHolder(View itemView) {
            super(itemView);

        }

        public void bind() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profileClick.onClickProfile();
                }
            });
        }
    }


}