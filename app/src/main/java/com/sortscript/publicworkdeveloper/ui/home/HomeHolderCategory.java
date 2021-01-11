package com.sortscript.publicworkdeveloper.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sortscript.publicworkdeveloper.PostForWorker;
import com.sortscript.publicworkdeveloper.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeHolderCategory extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView mTextViewCategory;
    ImageView mImageViewCategory;
    private final Context context;

    String[] nodes = {"Mason", "Plumber", "Electrician", "Gardener", "Painter", "Welder", "Carpenter", "Saloon", "Beautician", "Event Planner",
            "Demak", "Pickup", "Washerman", "Nursary", "Milkman", "House Cleaner", "Food Delivery Boy", "Water Boser", "Building Material", "Construction Machines",
            "Motor Boring"};

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


            case 5:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[5]);
                context.startActivity(intent);
                break;

            case 6:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[6]);
                context.startActivity(intent);
                break;

            case 7:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[7]);
                context.startActivity(intent);
                break;

            case 8:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[8]);
                context.startActivity(intent);
                break;


            case 9:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[9]);
                context.startActivity(intent);
                break;

            case 10:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[10]);
                context.startActivity(intent);
                break;

            case 11:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[11]);
                context.startActivity(intent);
                break;

            case 12:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[12]);
                context.startActivity(intent);
                break;

            case 13:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[13]);
                context.startActivity(intent);
                break;

            case 14:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[14]);
                context.startActivity(intent);
                break;

            case 15:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[15]);
                context.startActivity(intent);
                break;

            case 16:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[16]);
                context.startActivity(intent);
                break;

            case 17:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[17]);
                context.startActivity(intent);
                break;

            case 18:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[18]);
                context.startActivity(intent);
                break;

            case 19:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[19]);
                context.startActivity(intent);
                break;

            case 20:
                intent = new Intent(context, PostForWorker.class);
                intent.putExtra("Node", nodes[20]);
                context.startActivity(intent);
                break;

            default:
                Toast.makeText(context, "Category pending ", Toast.LENGTH_SHORT).show();

        }
    }
}
