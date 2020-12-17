package com.finalexam.capstone1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AirportListViewAdapter extends BaseAdapter {

    // DEBUG
    static final String TAG = "AirportListViewAdapter";

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    private ArrayList<Airport> airportList = new ArrayList<>();   // 검색 결과 리스트
    private List<Airport> arrayList;       // 전체 리스트

//    public AirportListViewAdapter(Context context, List<Airport> airportList) {
    public AirportListViewAdapter(Context context, List<Airport> arrayList) {
        this.context = context;
//        this.airportList = airportList;
        inflater = LayoutInflater.from(context);
//        this.arrayList = new ArrayList<Airport>();
//        this.arrayList.addAll(airportList);

        this.arrayList = arrayList;
        this.airportList.addAll(arrayList);
    }

    public class ViewHolder {
        TextView tv_en;
        TextView tv_kr;
    }

    @Override
    public int getCount() {
        return airportList.size();
    }

    @Override
    public Airport getItem(int position) {
        return airportList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        final ViewHolder holder;
        final Airport airport = airportList.get(position);

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.dialog_airport_listitem, null);
            // Locate the TextViews in listview_item.xml
            holder.tv_en = (TextView) view.findViewById(R.id.tv_airport_en);
            holder.tv_kr = (TextView) view.findViewById(R.id.tv_airport_kr);
            holder.tv_en.setText(String.valueOf(airportList.get(position)));
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.tv_en.setText(airport.getName_en());
        holder.tv_kr.setText(airport.getName_kr());

        // Listen for ListView Item Click
//        view.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                Intent intent = new Intent(context, SearchActivity.class);
////                intent.putExtra("AIRPORT_en", airport.name_en);
//                intent.putExtra("AIRPORT_en", airportList.get(arg0));
//                intent.putExtra("AIRPORT_kr", airport.name_kr);
//                context.startActivity(intent);
//
////                if (AirportDialog.dep_arr) {
////                    Button btn_dep = (Button) parent.findViewById(R.id.btn_fsearch_dep);
////                    btn_dep.setText(airportList.get(position).toString());
////                } else {
////                    Button btn_arr = (Button) parent.findViewById(R.id.btn_fsearch_arr);
////                    btn_arr.setText(airportList.get(position).toString());
////                }
//
////                Button btn_dep = (Button) parent.findViewById(R.id.btn_fsearch_dep);
////                btn_dep.setText("SETTING");
//            }
//        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        Log.d(TAG, "filter 함수 " + charText + " 단어로 실행됨");   // DEBUG

        charText = charText.toUpperCase(Locale.getDefault());
        airportList.clear();
        if (charText.length() == 0) {
            airportList.addAll(arrayList);
            Log.d(TAG, "필터링 할 검색어 없음");
        } else {
            for (Airport airport : arrayList) {
//                String name = context.getResources().getString(airport.name_en);
                String name_en = airport.getName_en();
                String name_kr = airport.getName_kr();

                if (name_en.toUpperCase().contains(charText)) {
                    airportList.add(airport);
                    Log.d(TAG, name_en + " 공항이 검색 결과에 추가됨");    // DEBUG
                } else if (name_kr.toUpperCase().contains(charText)) {
                    // TODO : 한글 검색 기능 추가 하기
                    airportList.add(airport);
                    Log.d(TAG, name_kr + " 공항이 검색 결과에 추가됨");    // DEBUG
                }
            }
        }
        notifyDataSetChanged();
    }

    public Airport getAirport(int position) {
        return airportList.get(position);
    }
}
