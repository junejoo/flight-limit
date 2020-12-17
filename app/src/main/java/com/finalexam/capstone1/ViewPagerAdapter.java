package com.finalexam.capstone1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<FlightDetail[]> flightDetailArrayList;
    private ArrayList<FlightDetail> list;
    private String[] totalTime;
    private boolean round;

    public ViewPagerAdapter(Context context, ArrayList<FlightDetail[]> arrayList, String[] totalTime, boolean round){
        this.mContext = context;
        this.flightDetailArrayList = new ArrayList<>();
        this.flightDetailArrayList = arrayList;
        this.totalTime = totalTime;
        this.round = round;
    }


    @Override
    public int getCount() {
        if(round){
            return 2;
        }else{
            return 1;
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.result_detail_page1, null);

        list = new ArrayList<>();

        for(int i=0; i<flightDetailArrayList.size(); i++){
            list.add(flightDetailArrayList.get(position)[i]);
        }

        ListView listView = view.findViewById(R.id.lv_detail);
        ResultDetailListViewAdapter adapter = new ResultDetailListViewAdapter(list);
        listView.setAdapter(adapter);
        listView.setVisibility(View.VISIBLE);

        TextView tv_rd_total = view.findViewById(R.id.tv_rd_total);
        tv_rd_total.setText("Total Time: "+totalTime[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (View)object;
    }
}
