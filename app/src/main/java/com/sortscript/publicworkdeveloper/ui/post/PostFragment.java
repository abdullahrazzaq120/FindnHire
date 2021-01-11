package com.sortscript.publicworkdeveloper.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.sortscript.publicworkdeveloper.Adapters.ViewPagerAdapter;
import com.sortscript.publicworkdeveloper.R;
import com.sortscript.publicworkdeveloper.ui.notification.NotificationViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

public class PostFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private PostViewModel postViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        View root = inflater.inflate(R.layout.fragment_post, container, false);

        tabLayout = root.findViewById(R.id.tablayout_id);
        viewPager = root.findViewById(R.id.viewpager_id);
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        //add fragment here
        adapter.AddFragment(new FragmentCurrentService(), "Current Service");
        adapter.AddFragment(new FragmentServicesAvailed(), "Services Availed");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }
}