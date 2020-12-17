package com.finalexam.capstone1;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTabHost;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ResultDetailActivity extends FragmentActivity {
    //private ListView lv_detail;
    private Button btn_detail_ok;
    private ArrayList<FlightDetail> list;
    private ArrayList<FlightDetail[]> flightDetailArrayList;
    private TextView tv_rd_total;
    private FlightDetail[][] flightDetails;
    private ViewPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private String arr, dep;
    private String[] totalTime;
    private TabLayout tabLayout;
    private boolean round;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_detail);

        Intent intent = getIntent();
        FlightResult[] flightResult = (FlightResult[]) intent.getSerializableExtra("OBJECT");

        getWindow().setWindowAnimations(0);

        PreferenceManager pref = new PreferenceManager(this);
        arr = pref.getValue("ARRIVAL", null);
        dep = pref.getValue("DEPARTURE", null);
        round = pref.getValue("ROUND", true);

        //lv_detail = findViewById(R.id.lv_detail);
        btn_detail_ok = findViewById(R.id.btn_detail_ok);
        btn_detail_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
/*
        list = new ArrayList<FlightDetail>();
        if (flightResult!=null){
            for(int i =0; i<flightResult[0].getDepCodeSize(); i++){
                list.add(new FlightDetail(flightResult[0].getCarrierCode(i), flightResult[0].getCarrier_kor(i), flightResult[0].getNumber(i),
                        flightResult[0].getDep_code(i), flightResult[0].getDep_time(i), flightResult[0].getArr_code(i), flightResult[0].getArr_time(i), flightResult[0].getDuration(i)));

            }
        }
        ResultDetailListViewAdapter adapter = new ResultDetailListViewAdapter(list);
        lv_detail.setAdapter(adapter);
        lv_detail.setVisibility(View.VISIBLE);

        tv_rd_total = findViewById(R.id.tv_rd_total);
        tv_rd_total.setText("Total Time: "+flightResult[0].getTotalTime());
*/
        tabLayout = findViewById(R.id.layout_tab);
        if(round){
            tabLayout.addTab(tabLayout.newTab().setText(dep+"→"+arr));
            tabLayout.addTab(tabLayout.newTab().setText(arr+"→"+dep));
        }else{
            tabLayout.setVisibility(View.GONE);
        }


        flightDetailArrayList = new ArrayList<FlightDetail[]>();
        int max=0;
        if(round){
            if(flightResult[0].getDepCodeSize()>=flightResult[1].getDepCodeSize()){
                max=flightResult[0].getDepCodeSize();
            } else{
                max = flightResult[1].getDepCodeSize();
            }
        } else{
            max = flightResult[0].getDepCodeSize();
        }
        flightDetails = new FlightDetail[flightResult.length][max];
        totalTime = new String[2];
        if(flightResult!=null){
            for(int i=0; i<flightResult.length; i++){
                for(int j=0; j<flightResult[i].getDepCodeSize(); j++){
                    flightDetails[i][j]=new FlightDetail(flightResult[i].getCarrierCode(j), flightResult[i].getCarrier_kor(j), flightResult[i].getNumber(j),
                            flightResult[i].getDep_code(j), flightResult[i].getDep_time(j), flightResult[i].getArr_code(j), flightResult[i].getArr_time(j), flightResult[i].getDuration(j));
                }
                totalTime[i]=flightResult[i].getTotalTime();
            }
            for(int i=0; i<flightDetails.length; i++){
                flightDetailArrayList.add(flightDetails[i]);
            }
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new ViewPagerAdapter(this, flightDetailArrayList, totalTime, round);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
