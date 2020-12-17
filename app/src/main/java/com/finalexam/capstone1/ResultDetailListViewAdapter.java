package com.finalexam.capstone1;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;

public class ResultDetailListViewAdapter extends BaseAdapter {

    private ArrayList<FlightDetail> list = new ArrayList<>();
    public ResultDetailListViewAdapter(ArrayList list) {
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dialog_result_detail_listitem , viewGroup, false);
        }

        TextView tv_rd_dep = view.findViewById(R.id.tv_rd_dep);
        TextView tv_rd_dep_time = view.findViewById(R.id.tv_rd_dep_time);
        TextView tv_rd_arr = view.findViewById(R.id.tv_rd_arr);
        TextView tv_rd_arr_time = view.findViewById(R.id.tv_rd_arr_time);
        TextView tv_rd_carrierCode = view.findViewById(R.id.tv_rd_carriercode);
        TextView tv_rd_number = view.findViewById(R.id.tv_rd_number);
        TextView tv_rd_time = view.findViewById(R.id.tv_rd_time);
        TextView tv_rd_dep_airport = view.findViewById(R.id.tv_rd_dep_airport);
        TextView tv_rd_arr_airport = view.findViewById(R.id.tv_rd_arr_airport);

        final FlightDetail lv_item = list.get(position);
        tv_rd_carrierCode.setText(lv_item.getCarrier_kor()+" " +lv_item.getCarrierCode());
        tv_rd_arr_time.setText(lv_item.getArr_time().substring(0,10) + " "+ lv_item.getArr_time().substring(11,lv_item.getArr_time().length()));
        tv_rd_dep_time.setText(lv_item.getDep_time().substring(0,10) + " "+lv_item.getDep_time().substring(11,lv_item.getDep_time().length()));
        tv_rd_number.setText(lv_item.getNumber());
        tv_rd_time.setText(lv_item.getDuration());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        String dep_airport =null , dep_country = null, arr_airport =null , arr_country=null;

        AssetManager assetManager = context.getAssets();

        try {
            InputStream is = assetManager.open("jsons/airport_code.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }
            String jsonData = buffer.toString();

            JSONArray jsonArray = new JSONArray(jsonData);

            for(int k =0; k<jsonArray.length(); k++){
                JSONObject jo = jsonArray.getJSONObject(k);

                String kor_name = jo.getString("한글명");
                String country_kor = jo.getString("국가명_한글");
                String iata = jo.getString("IATA코드");
                String icao = jo.getString("ICAO코드");

                if(lv_item.getDep_code().equals(iata)){
                    dep_airport = kor_name;
                    dep_country = country_kor;
                }

                if(lv_item.getArr_code().equals(iata)){
                    arr_airport = kor_name;
                    arr_country = country_kor;
                }
            }

        }catch (IOException e){e.printStackTrace();}
        catch (JSONException e){e.printStackTrace();}

        tv_rd_dep.setText(lv_item.getDep_code());
        tv_rd_arr.setText(lv_item.getArr_code());
        tv_rd_dep_airport.setText("("+dep_country+" "+dep_airport+")");
        tv_rd_arr_airport.setText("("+arr_country+" "+arr_airport+")");

        return view;
    }
}

