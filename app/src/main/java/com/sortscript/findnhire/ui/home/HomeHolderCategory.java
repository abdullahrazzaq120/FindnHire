package com.sortscript.findnhire.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sortscript.findnhire.Activities.PostForWorker;
import com.sortscript.findnhire.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeHolderCategory extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView mTextViewCategory;
    ImageView mImageViewCategory;
    private final Context context;

    String[] nodes = {"Plumber", "Electrician", "Painter", "Welder", "Carpenter"};

    public HomeHolderCategory(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();

        mTextViewCategory = itemView.findViewById(R.id.workerTitleCategoryId);
        mImageViewCategory = itemView.findViewById(R.id.workerImageCategoryId);

        itemView.setClickable(true);
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        final Intent intent;
        switch (getAdapterPosition()) {

            case 0:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[0]);
                context.startActivity(intent);
                break;

            case 1:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[1]);
                context.startActivity(intent);
                break;

            case 2:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[2]);
                context.startActivity(intent);
                break;


            case 3:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[3]);
                context.startActivity(intent);
                break;


            case 4:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[4]);
                context.startActivity(intent);
                break;

            default:
                Toast.makeText(context, "Category pending ", Toast.LENGTH_SHORT).show();

        }
    }
}
