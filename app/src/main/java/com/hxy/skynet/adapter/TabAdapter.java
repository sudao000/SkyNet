package com.hxy.skynet.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by tangyangkai on 16/5/12.
 */

public class TabAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> tabTitle;


    public TabAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public void setTabTitle(List<String> tabTitle) {
        this.tabTitle = tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    //设置tablayout标题

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle.get(position);

    }
}
