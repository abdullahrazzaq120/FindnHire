package com.sortscript.findnhire.ui.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.sortscript.findnhire.View_Pager_Adapters.ViewPagerAdapter;
import com.sortscript.findnhire.R;
import com.sortscript.findnhire.ui.notification.SubFragmentsUserOfferNotify.FragmentChatNotify;
import com.sortscript.findnhire.ui.notification.SubFragmentsUserOfferNotify.FragmentOffersNotify;

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
        adapter.AddFragment(new FragmentOffersNotify(), "Offers");
        adapter.AddFragment(new FragmentChatNotify(), "Chats");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }
}