package com.sortscript.publicworkdeveloper.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sortscript.publicworkdeveloper.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView mRecyclerView;
    HomeAdapterCategory myAdapter;
    GridLayoutManager gridLayoutManager;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = view.findViewById(R.id.rv_home_category);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);

        getActivity().setTitle("Workers");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        myAdapter = new HomeAdapterCategory(getActivity(), getMyList());
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);

                }
            }, 50);
        }

        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private ArrayList<HomeModelCategory> getMyList() {

        ArrayList<HomeModelCategory> models = new ArrayList<>();


        //0
        HomeModelCategory m = new HomeModelCategory();
        m.setWorker_title_category("Mason");
        m.setWorker_image_category(R.drawable.mason);
        models.add(m);

        //1
        m = new HomeModelCategory();
        m.setWorker_title_category("Plumber");
        m.setWorker_image_category(R.drawable.plumber);
        models.add(m);

        //2
        m = new HomeModelCategory();
        m.setWorker_title_category("Electrician");
        m.setWorker_image_category(R.drawable.electrician);
        models.add(m);

        //3
        m = new HomeModelCategory();
        m.setWorker_title_category("Gardener");
        m.setWorker_image_category(R.drawable.gardener);
        models.add(m);

        //4
        m = new HomeModelCategory();
        m.setWorker_title_category("Painter");
        m.setWorker_image_category(R.drawable.painter);
        models.add(m);

        //5
        m = new HomeModelCategory();
        m.setWorker_title_category("Welder");
        m.setWorker_image_category(R.drawable.welder);
        models.add(m);

        //6
        m = new HomeModelCategory();
        m.setWorker_title_category("Carpenter");
        m.setWorker_image_category(R.drawable.carpenter);
        models.add(m);

        //7
        m = new HomeModelCategory();
        m.setWorker_title_category("Saloon");
        m.setWorker_image_category(R.drawable.saloon);
        models.add(m);

        //8
        m = new HomeModelCategory();
        m.setWorker_title_category("Beautician");
        m.setWorker_image_category(R.drawable.beautician);
        models.add(m);

        //9
        m = new HomeModelCategory();
        m.setWorker_title_category("Event Planner");
        m.setWorker_image_category(R.drawable.eventplanner);
        models.add(m);

        //10
        m = new HomeModelCategory();
        m.setWorker_title_category("Demak");
        m.setWorker_image_category(R.drawable.demak);
        models.add(m);

        //11
        m = new HomeModelCategory();
        m.setWorker_title_category("Pickup");
        m.setWorker_image_category(R.drawable.pickup);
        models.add(m);

        //12
        m = new HomeModelCategory();
        m.setWorker_title_category("Washerman");
        m.setWorker_image_category(R.drawable.washerman);
        models.add(m);

        //13
        m = new HomeModelCategory();
        m.setWorker_title_category("Nursary");
        m.setWorker_image_category(R.drawable.nursary);
        models.add(m);

        //14
        m = new HomeModelCategory();
        m.setWorker_title_category("Milkman");
        m.setWorker_image_category(R.drawable.milkman);
        models.add(m);

        //15
        m = new HomeModelCategory();
        m.setWorker_title_category("House Cleaner");
        m.setWorker_image_category(R.drawable.cleaner);
        models.add(m);

        //16
        m = new HomeModelCategory();
        m.setWorker_title_category("Food Delivery Boy");
        m.setWorker_image_category(R.drawable.deleiveryboy);
        models.add(m);

        //17
        m = new HomeModelCategory();
        m.setWorker_title_category("Water Boser");
        m.setWorker_image_category(R.drawable.waterboser);
        models.add(m);

        //18
        m = new HomeModelCategory();
        m.setWorker_title_category("Building Material");
        m.setWorker_image_category(R.drawable.buildingmaterial);
        models.add(m);

        //19
        m = new HomeModelCategory();
        m.setWorker_title_category("Construction Machines");
        m.setWorker_image_category(R.drawable.constructionmachines);
        models.add(m);

        //20
        m = new HomeModelCategory();
        m.setWorker_title_category("Motor Boring");
        m.setWorker_image_category(R.drawable.motorboring);
        models.add(m);

        return models;
    }
}