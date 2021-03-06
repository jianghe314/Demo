package com.kangzhan.student.Teacher.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/6/6.
 */

public class TeachermainFragment extends FragmentPagerAdapter {
    private ArrayList<Fragment> data;
    public TeachermainFragment(FragmentManager fm, ArrayList<Fragment> fragmentArrayList) {
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
