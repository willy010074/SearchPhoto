package com.example.chungyu.topic;

/**
 * Created by holichih on 2017/9/9.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 057029 on 2016/11/25.
 */

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private  List<Fragment>  fragmentList;
    public ViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}