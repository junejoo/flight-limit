package com.finalexam.capstone1;

import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.RippleDrawable;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SearchListViewAdapter extends BaseAdapter {

    private ArrayList<FlightResult[]> list = new ArrayList<>();
    private int size;
    private boolean round;
    public SearchListViewAdapter(ArrayList list) {
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        PreferenceManager pref = new PreferenceManager(context);
        round = pref.getValue("ROUND", true);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.f_search2_listitem , viewGroup, false);
        }

        TextView tv_air = (TextView) view.findViewById(R.id.tv_lv_airline);
        TextView tv_dep = (TextView) view.findViewById(R.id.tv_lv_dep);
        TextView tv_arr = (TextView) view.findViewById(R.id.tv_lv_arr);
        TextView tv_pri = (TextView) view.findViewById(R.id.tv_lv_price);
        TextView tv_air2 = (TextView) view.findViewById(R.id.tv_lv_airline2);
        TextView tv_dep2 = (TextView) view.findViewById(R.id.tv_lv_dep2);
        TextView tv_arr2 = (TextView) view.findViewById(R.id.tv_lv_arr2);
        TextView tv_pri2 = (TextView) view.findViewById(R.id.tv_lv_price2);
        LinearLayout layout_round = view.findViewById(R.id.layout_round);


        final FlightResult[] lv_item = list.get(i);
        tv_air.setText(lv_item[0].getCarrier_kor(0));
        tv_dep.setText(lv_item[0].getDep_time(0).substring(11,16));
        size = lv_item[0].getDepCodeSize();
        tv_arr.setText(lv_item[0].getArr_time(size-1).substring(11,16));

        DecimalFormat myFormatter = new DecimalFormat("###,###");
        String formattedStringPrice = myFormatter.format(lv_item[0].getPrice());
        tv_pri.setText(formattedStringPrice + " 원");

        if(round){
            tv_air2.setText(lv_item[1].getCarrier_kor(0));
            tv_dep2.setText(lv_item[1].getDep_time(0).substring(11,16));
            size = lv_item[1].getDepCodeSize();
            tv_arr2.setText(lv_item[1].getArr_time(size-1).substring(11,16));
            String formattedStringPrice2 = myFormatter.format(lv_item[1].getPrice());
            tv_pri2.setText(formattedStringPrice2 + " 원");
            layout_round.setVisibility(View.VISIBLE);
        }
        else {
            layout_round.setVisibility(View.GONE);
        }
        



        // list view click evnet
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ResultDetailActivity.class);
                intent.putExtra("OBJECT", lv_item);
                context.startActivity(intent);

            }
        });

        return view;
    }
}