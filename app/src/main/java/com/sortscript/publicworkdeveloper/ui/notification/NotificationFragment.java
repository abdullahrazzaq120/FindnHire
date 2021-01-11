package com.sortscript.publicworkdeveloper.ui.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.sortscript.publicworkdeveloper.Adapters.ViewPagerAdapter;
import com.sortscript.publicworkdeveloper.R;
import com.sortscript.publicworkdeveloper.ui.post.FragmentCurrentService;
import com.sortscript.publicworkdeveloper.ui.post.FragmentServicesAvailed;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

public class NotificationFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private NotificationViewModel notificationViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notification, container, false);

        tabLayout = root.findViewById(R.id.tablayout2_id);
        viewPager = root.findViewById(R.id.viewpager2_id);
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        //add fragment here
        adapter.AddFragment(new FragmentChatNotify(), "Chats");
        adapter.AddFragment(new FragmentOffersNotify(), "Offers");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }
}