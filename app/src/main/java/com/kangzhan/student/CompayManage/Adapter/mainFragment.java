package com.kangzhan.student.CompayManage.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/6/6.
 */

public class mainFragment extends FragmentPagerAdapter {
    private ArrayList<Fragment> data;
    public mainFragment(FragmentManager fm, ArrayList<Fragment> fragmentArrayList) {
        super(fm);
        data=fragmentArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
