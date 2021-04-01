package com.sortscript.findnhire.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sortscript.findnhire.R;

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
        m = new HomeModelCategory();
        m.setWorker_title_category("Plumber");
        m.setWorker_image_category(R.drawable.plumber);
        models.add(m);

        //1
        m = new HomeModelCategory();
        m.setWorker_title_category("Electrician");
        m.setWorker_image_category(R.drawable.electrician);
        models.add(m);

        //2
        m = new HomeModelCategory();
        m.setWorker_title_category("Painter");
        m.setWorker_image_category(R.drawable.painter);
        models.add(m);

        //3
        m = new HomeModelCategory();
        m.setWorker_title_category("Welder");
        m.setWorker_image_category(R.drawable.welder);
        models.add(m);

        //4
        m = new HomeModelCategory();
        m.setWorker_title_category("Carpenter");
        m.setWorker_image_category(R.drawable.carpenter);
        models.add(m);

        return models;
    }
}