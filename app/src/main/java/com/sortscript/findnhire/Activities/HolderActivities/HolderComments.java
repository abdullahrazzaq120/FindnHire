package com.sortscript.findnhire.Activities.HolderActivities;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.sortscript.findnhire.R;

public class HolderComments extends RecyclerView.ViewHolder {

    public View view;
    public CircularImageView commenterUserImageIv;
    public TextView commenterUserNameTv, commentsTextUserTv;

    public HolderComments(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        commenterUserImageIv = itemView.findViewById(R.id.commenterUserImageIvId);
        commenterUserNameTv = itemView.findViewById(R.id.commenterUserNameTvId);
        commentsTextUserTv = itemView.findViewById(R.id.commentsTextUserTvId);
    }
}
