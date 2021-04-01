package com.sortscript.findnhire.View_Pager_Adapters;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> firstFragment= new ArrayList<>();
    private final List<String> firstTitles= new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return firstFragment.get(position);
    }

    @Override
    public int getCount() {
        return firstTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return firstTitles.get(position);
    }

    public void AddFragment(Fragment fragment, String title)
    {
        firstFragment.add(fragment);
        firstTitles.add(title);
    }
}
