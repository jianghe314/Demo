package com.kangzhan.student.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kangzhan.student.R;

import java.util.List;

/**
 * Created by kangzhan011 on 2017/5/27.
 */

public class GuideAdapter extends PagerAdapter {
    private Context context;
    private int[] data;
    private List<View> viewItem;
    public GuideAdapter(Context context, int[] data, List<View> viewItem) {
        this.context=context;
        this.data=data;
        this.viewItem=viewItem;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewItem.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View convertView=viewItem.get(position);
        container.addView(convertView);
        return viewItem.get(position);
    }

}
