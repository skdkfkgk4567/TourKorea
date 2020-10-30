package com.example.sns_project.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sns_project.R;
import com.example.sns_project.fragment.MapFragment;
import com.example.sns_project.fragment.MyPostsFragment;
import com.example.sns_project.fragment.MyTopPostsFragment;
import com.example.sns_project.fragment.RecentPostsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaison on 23/10/16.
 */


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> fragmentTitle = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager, int behavior) {
        super(manager, behavior);
    }
    @Override
    public Fragment getItem(int position)
    {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return super.getPageTitle(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        fragmentTitle.add(title);
    }

}