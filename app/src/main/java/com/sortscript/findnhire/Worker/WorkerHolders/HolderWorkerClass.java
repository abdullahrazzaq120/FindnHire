package com.sortscript.findnhire.Worker.WorkerHolders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sortscript.findnhire.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HolderWorkerClass extends RecyclerView.ViewHolder {

    public CircularImageView userImageWorkerRequestIv;
    public TextView userNameWorkerRequestTv, userDescriptionWorkerRequestTv, postDateWorkerRequestTv;
    public View view;

    public HolderWorkerClass(@NonNull View itemView) {
        super(itemView);
        view = itemView;

        userImageWorkerRequestIv = itemView.findViewById(R.id.userImageWorkerRequestIvId);
        userNameWorkerRequestTv = itemView.findViewById(R.id.userNameWorkerRequestTvId);
        userDescriptionWorkerRequestTv = itemView.findViewById(R.id.userDescriptionWorkerRequestTvId);
        postDateWorkerRequestTv = itemView.findViewById(R.id.postDateWorkerRequestTvId);

        itemView.setOnClickListener(view -> {

            mClickListener.onItemClick(view, getAdapterPosition());

        });
    }

    public void setRequestsDetails(Context context, String UserName, String UserDescription, String CurrentDate, String UserImage) {

        Glide.with(context).load(UserImage).into(userImageWorkerRequestIv);
        userNameWorkerRequestTv.setText(UserName);
        userDescriptionWorkerRequestTv.setText(UserDescription);
        postDateWorkerRequestTv.setText(CurrentDate);
    }

    private HolderWorkerClass.ClickListener mClickListener;

    public interface ClickListener {

        void onItemClick(View view, int position);

    }

    public void setOnclickListener(HolderWorkerClass.ClickListener clickListener) {
        mClickListener = clickListener;
    }

}
