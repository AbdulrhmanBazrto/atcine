package com.gnusl.actine.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.CommentLongClickEvent;
import com.gnusl.actine.model.Comment;
import com.gnusl.actine.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private Context mContext;
    private List<Comment> comments = new ArrayList<>();
    private CommentLongClickEvent commentLongClickEvent;


    public CommentsAdapter(Context context, List<Comment> comments, CommentLongClickEvent commentLongClickEvent) {
        this.mContext = context;
        this.comments = comments;
        this.commentLongClickEvent  =commentLongClickEvent;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_comment, parent, false);
        return new CommentsViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final CommentsViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvComment.setText(comment.getComment());
        holder.tvUserName.setText(comment.getProfile().getName());
        holder.tvCommentTime.setText(comment.getCreatedAt());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (comment.getProfile().getId() == SharedPreferencesUtils.getCurrentProfile()) {
                    if (commentLongClickEvent != null)
                        commentLongClickEvent.onLongClickComment(comment);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setList(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName, tvCommentTime, tvComment;

        CommentsViewHolder(View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvCommentTime = itemView.findViewById(R.id.tv_comment_time);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
        }
    }
}
