package com.sortscript.publicworkdeveloper.ui.notification.HolderUserOfferNotify;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sortscript.publicworkdeveloper.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HolderUserOfferClass extends RecyclerView.ViewHolder {

    public CircularImageView userOfferNotifyWorkerImageIv;
    public TextView userOfferNotifyWorkerNameTv, userOfferNotifyWorkerSpecificationTv, userOfferNotifyWorkerCategoryTv;
    View view;

    public HolderUserOfferClass(@NonNull View itemView) {
        super(itemView);

        view = itemView;
        userOfferNotifyWorkerImageIv = itemView.findViewById(R.id.userOfferNotifyWorkerImageIvId);
        userOfferNotifyWorkerNameTv = itemView.findViewById(R.id.userOfferNotifyWorkerNameTvId);
        userOfferNotifyWorkerSpecificationTv = itemView.findViewById(R.id.userOfferNotifyWorkerSpecificationTvId);
        userOfferNotifyWorkerCategoryTv = itemView.findViewById(R.id.userOfferNotifyWorkerCategoryTvId);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
    }

    public void setUserOfferNotifyDetails(Context context, String OfferWorkerImage, String OfferWorkerName, String OfferWorkerSpecification, String OfferWorkerCategory) {

        Glide.with(context).load(OfferWorkerImage).into(userOfferNotifyWorkerImageIv);
        userOfferNotifyWorkerNameTv.setText(OfferWorkerName);
        userOfferNotifyWorkerSpecificationTv.setText(OfferWorkerSpecification);
        userOfferNotifyWorkerCategoryTv.setText(OfferWorkerCategory);
    }

    private HolderUserOfferClass.ClickListener mClickListener;

    public interface ClickListener {

        void onItemClick(View view, int position);

    }

    public void setOnclickListener(HolderUserOfferClass.ClickListener clickListener) {
        mClickListener = clickListener;
    }

}
