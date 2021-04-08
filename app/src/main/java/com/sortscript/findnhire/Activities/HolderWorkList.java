package com.sortscript.findnhire.Activities;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.sortscript.findnhire.R;

public class HolderWorkList extends RecyclerView.ViewHolder {

    View view;
    public CircularImageView workerListImageIv;
    public TextView workerListNameTv, workerListProfession, workerListSpecificationTv;

    public HolderWorkList(@NonNull View itemView) {
        super(itemView);
        view = itemView;

        workerListImageIv = itemView.findViewById(R.id.workerListImageIvId);
        workerListNameTv = itemView.findViewById(R.id.workerListNameTvId);
        workerListProfession = itemView.findViewById(R.id.workerListProfessionId);
        workerListSpecificationTv = itemView.findViewById(R.id.workerListSpecificationTvId);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
    }

    private HolderWorkList.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnclickListener(HolderWorkList.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}