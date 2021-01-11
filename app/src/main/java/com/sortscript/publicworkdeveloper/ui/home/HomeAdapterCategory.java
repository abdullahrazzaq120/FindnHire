package com.sortscript.publicworkdeveloper.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sortscript.publicworkdeveloper.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapterCategory extends RecyclerView.Adapter<HomeHolderCategory>{
    Context c;
    ArrayList<HomeModelCategory> models;

    public HomeAdapterCategory(Context c, ArrayList<HomeModelCategory> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public HomeHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_category_design, null);
        return new HomeHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolderCategory holder, int position) {

        holder.mTextViewCategory.setText(models.get(position).getWorker_title_category());
        holder.mImageViewCategory.setImageResource(models.get(position).getWorker_image_category());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
