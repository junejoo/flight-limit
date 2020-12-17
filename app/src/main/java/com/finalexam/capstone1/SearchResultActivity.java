package com.finalexam.capstone1;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SearchResultActivity extends BaseActivity {

    private static final String TAG = "SEARCH RESULT ACTIVITY";
    private TextView tv_date, tv_dep, tv_dep_kr, tv_arr, tv_arr_kr, tv_noResult, tv_arrdate;
    private Button btn_save;
    private ImageView i_round, i_oneway;
    private ListView lv_search;
    private ArrayList<FlightResult[]> list;
    private ProgressBar progressBar; // 로딩
    private LinearLayout progress_layout;
    private String id, arr, dep, date, redate, CurState = "FromAlarm";
    private int adlt, chld;
    private boolean round;
    private FlightResult[] flightResults;
    private FlightResult[][] flightResultsRound;
    private ArrayList<Integer> price = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_new);

        getWindow().setWindowAnimations(0); //화면전환 효과 제거
        tv_date = (TextView) findViewById(R.id.tv_fsearch_date);
        tv_dep = (TextView) findViewById(R.id.tv_fsearch_dep);
        tv_dep_kr = (TextView) findViewById(R.id.tv_fsearch_dep_kr);
        i_round = (ImageView) findViewById(R.id.res_round);
        i_oneway = (ImageView) findViewById(R.id.res_oneway);
        tv_arr = (TextView) findViewById(R.id.tv_fsearch_arr);
        tv_arr_kr = (TextView) findViewById(R.id.tv_fsearch_arr_kr);
        tv_arrdate = (TextView) findViewById(R.id.res_arrdate);
        lv_search = (ListView) findViewById(R.id.lv_search);
//        progressBar = findViewById(R.id.progress_bar);              // 어디있는 컴포넌트???
//        tv_noResult = (TextView) findViewById(R.id.tv_noResult);     // 어디있는 컴포넌트???
        btn_save = (Button) findViewById(R.id.b_result);

//        Intent intent = getIntent();
//        arr = intent.getStringExtra("ARRIVAL");
//        dep = intent.getStringExtra("DEPARTURE");
//        date = intent.getStringExtra("DATE");
//        adlt = intent.getIntExtra("ADULT", 0);
//        chld = intent.getIntExtra("CHILD", 0);

        PreferenceManager pref = new PreferenceManager(this);
        arr = pref.getValue("ARRIVAL", null);
        dep = pref.getValue("DEPARTURE", null);
        date = pref.getValue("DATE", null);
        redate = pref.getValue("RETURN", null);
        adlt = pref.getValue("ADULT", 0);
        chld = pref.getValue("CHILD", 0);
        round = pref.getValue("ROUND", true);
//        Log.d("resultof", date + "," + redate);
        Log.d("resultof", arr+"," +redate +"," );

        if (round) {
            i_oneway.setVisibility(View.GONE);
            tv_arrdate.setText(redate);
        }
        else {
            i_round.setVisibility(View.GONE);
            tv_arrdate.setText("");
        }

        tv_date.setText(date);
        tv_dep.setText(dep);
        for (Airport a : SearchActivity.getList()) {
            if (a.getName_en().equals(dep))
                tv_dep_kr.setText(a.getCity());
        }
        tv_arr.setText(arr);
        for (Airport a : SearchActivity.getList()) {
            if (a.getName_en().equals(arr)) tv_arr_kr.setText(a.getCity());
        }


        /*lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Flight> list_detail = new ArrayList<>();

                // TODO : 세부정보 표시 or 예약페이지 연결
                FlightResult[] lv_item = list.get(position);
//                Toast.makeText(getApplicationContext(), list_detail + "clicked", Toast.LENGTH_SHORT).show();
                Log.d(TAG, list_detail + "clicked");

            }
        }); */


        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();   // listView 데이터 구성
        progressON();

        // 출발, 도착, 날짜, 인원 수 데이터 넘겨줌
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PreferenceManager pref = new PreferenceManager(SearchResultActivity.this);
                id = pref.getValue("id", null);
                // 로그인 상태
//                if (id != null) {
                    Intent intent = new Intent(view.getContext(), SetAlarmActivity.class);
                    startActivity(intent);
//                }
                // 비로그인 상태
//                else {
//                    androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(SearchResultActivity.this);
//                    alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(view.getContext(), LoginActivity.class);
//                            // 로그인 완료 후 검색화면으로 복귀할 수 있도록 CurState 전달
//                            intent.putExtra("CurState", CurState);
//                            startActivity(intent);
//                        }
//                    });
//                    alert.setMessage("먼저 로그인해주세요");
//                    alert.show();
//                }
            }
        });
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, FlightResult[]> {
        FlightOfferSearch[] flightOffers;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(FlightResult[] flightResults) {
            list = new ArrayList<FlightResult[]>();
            if (flightResultsRound != null) {
                for (int i = 0; i < flightResultsRound.length; i++) {

                    list.add(flightResultsRound[i]);
                    price.add(flightResultsRound[i][0].getPrice());
                }
                JsonArray jsonArray = new JsonArray();
                for (int i = 0; i < price.size(); i++) {
                    jsonArray.add(price.get(i));
                }
//                Log.d("search json", jsonArray.toString());
                SearchListViewAdapter adapter = new SearchListViewAdapter(list);
                lv_search.setAdapter(adapter);
                lv_search.setVisibility(View.VISIBLE);

                PreferenceManager pref = new PreferenceManager(SearchResultActivity.this);
                pref.put("PRICEJSON", jsonArray.toString());
            }

            progressOFF();
        }//onPostExcute()

        @Override
        protected FlightResult[] doInBackground(Void... voids) {
            try {
                Amadeus amadeus = Amadeus
                        .builder("kFN2xdf3AsPrita2tUv5HWUeXcvM6fdL", "q92QKuEUEuIFbzDd")
                        .build();

//                Log.d("resultof", arr + dep + date + adlt + chld);
                // Flight Choice Prediction
// Note that the example calls 2 APIs: Flight Offers Search & Flight Choice Prediction

                if(!round){
                    flightOffers = amadeus.shopping.flightOffersSearch.get(
                            Params.with("originLocationCode", dep)
                                    .and("destinationLocationCode", arr)
                                    .and("departureDate", date)
//                                    .and("returnDate", redate)
                                    .and("adults", adlt).and("children", chld).and("currencyCode", "KRW"));

                }else{
                    flightOffers = amadeus.shopping.flightOffersSearch.get(
                            Params.with("originLocationCode", dep)
                                    .and("destinationLocationCode", arr)
                                    .and("departureDate", date)
                                    .and("returnDate", redate)
                                    .and("adults", adlt).and("children", chld).and("currencyCode", "KRW"));
                }



                // Using a JSonObject
                JsonObject result = flightOffers[1].getResponse().getResult();
                JsonArray result2 = (JsonArray) result.get("data"); //항공권 정보
                Log.d("result2", Integer.toString(result2.size()));
                flightResultsRound = new FlightResult[result2.size()][2];
                if(!round){
                    flightResultsRound = new FlightResult[result2.size()][1];
                    flightResults = new FlightResult[result2.size()];
                } else{
                    flightResultsRound = new FlightResult[result2.size()][2];
                    flightResults = new FlightResult[result2.size()*2];
                }

                for (int i = 0; i < result2.size(); i++) {
                    JsonObject result3 = (JsonObject) result2.get(i); //oneway(?), price
                    JsonObject price = (JsonObject) result3.get("price");
                    double price_total1 = Double.parseDouble(String.valueOf(price.get("total")).substring(1, String.valueOf(price.get("total")).length() - 1));
                    int price_total = Integer.parseInt(String.valueOf(Math.round(price_total1)));
                    JsonArray result4 = (JsonArray) result3.get("itineraries");
                    for (int k = 0; k < result4.size(); k++) {
                        JsonObject result5 = (JsonObject) result4.get(k);
                        //왕복일때 size 재야함(for문)
                        //Log.d("result5", result5.toString());
                        String totalTime = String.valueOf(result5.get("duration")).substring(3, String.valueOf(result5.get("duration")).length() - 1);//총비행시간

                        JsonArray result6 = (JsonArray) result5.get("segments"); //경유일 때 구분 for문 필요
                        flightResultsRound[i][k] = new FlightResult(result6.size());

                        flightResultsRound[i][k].setPrice(price_total);
                        flightResultsRound[i][k].setTotalTime(totalTime);

                        for (int j = 0; j < result6.size(); j++) {
                            JsonObject result7 = (JsonObject) result6.get(j);
                            JsonObject departure = (JsonObject) result7.get("departure");
                            String departure_code = String.valueOf(departure.get("iataCode")).substring(1, 4);
                            flightResultsRound[i][k].setDep_code(departure_code, j);

                            String departure_time = String.valueOf(departure.get("at"));
                            String departure_time2 = departure_time.substring(1, departure_time.length() - 1);
                            flightResultsRound[i][k].setDep_time(departure_time2, j);


                            JsonObject arrival = (JsonObject) result7.get("arrival");
                            String arrival_code = String.valueOf(arrival.get("iataCode")).substring(1, 4);
                            flightResultsRound[i][k].setArr_code(arrival_code, j);

                            //
                            String arrival_time = String.valueOf(arrival.get("at"));
                            String arrival_time2 = arrival_time.substring(1, arrival_time.length() - 1);
                            flightResultsRound[i][k].setArr_time(arrival_time2, j);

                            String carrierCode = String.valueOf(result7.get("carrierCode")).substring(1, 3);
                            flightResultsRound[i][k].setCarrierCode(carrierCode, j);


                            String number = String.valueOf(result7.get("number"));
                            String number2 = number.substring(1, number.length() - 1);
                            flightResultsRound[i][k].setNumber(number2, j);

                            String duration = String.valueOf(result7.get("duration"));
                            String duration2 = duration.substring(3, duration.length() - 1);
                            flightResultsRound[i][k].setDuration(duration2, j);
                        }
                    }
                    //list.add(new Flight(carrierCode, departure_time, arrival_time, Double.toString(price_total)));
                }

                /*for(int i=0; i<flightResultsRound.length; i++){
                    for(int j =0; j<flightResultsRound[i].length; j++){
                        flightResults[i*2+j] = new FlightResult(flightResultsRound[i][j]);
                    }
                }*/

                /*for (int i = 0; i < result2.size(); i++) {
                    JsonObject result3 = (JsonObject) result2.get(i); //oneway(?), price
                    JsonObject price = (JsonObject) result3.get("price");
                    double price_total1 = Double.parseDouble(String.valueOf(price.get("total")).substring(1, String.valueOf(price.get("total")).length() - 1));
                    int price_total = Integer.parseInt(String.valueOf(Math.round(price_total1)));
                    JsonArray result4 = (JsonArray) result3.get("itineraries");
                    JsonObject result5 = (JsonObject) result4.get(0); //왕복일때 size 재야함(for문)
                    //Log.d("result5", result5.toString());
                    String totalTime = String.valueOf(result5.get("duration")).substring(3, String.valueOf(result5.get("duration")).length() - 1);//총비행시간

                    JsonArray result6 = (JsonArray) result5.get("segments"); //경유일 때 구분 for문 필요
                    flightResults[i] = new FlightResult(result6.size());

                    flightResults[i].setPrice(price_total);
                    flightResults[i].setTotalTime(totalTime);

                    for (int j = 0; j < result6.size(); j++) {
                        JsonObject result7 = (JsonObject) result6.get(j);
                        JsonObject departure = (JsonObject) result7.get("departure");
                        String departure_code = String.valueOf(departure.get("iataCode")).substring(1, 4);
                        flightResults[i].setDep_code(departure_code, j);

                        String departure_time = String.valueOf(departure.get("at"));
                        String departure_time2 = departure_time.substring(1,departure_time.length()-1);
                        flightResults[i].setDep_time(departure_time2, j);


                        JsonObject arrival = (JsonObject) result7.get("arrival");
                        String arrival_code = String.valueOf(arrival.get("iataCode")).substring(1, 4);
                        flightResults[i].setArr_code(arrival_code, j);

                        //
                        String arrival_time = String.valueOf(arrival.get("at"));
                        String arrival_time2 = arrival_time.substring(1,arrival_time.length()-1);
                        flightResults[i].setArr_time(arrival_time2, j);

                        String carrierCode = String.valueOf(result7.get("carrierCode")).substring(1, 3);
                        flightResults[i].setCarrierCode(carrierCode, j);


                        String number = String.valueOf(result7.get("number"));
                        String number2 = number.substring(1,number.length()-1);
                        flightResults[i].setNumber(number2, j);

                        String duration = String.valueOf(result7.get("duration"));
                        String duration2 = duration.substring(3,duration.length()-1);
                        flightResults[i].setDuration(duration2, j);
                    }
                    //list.add(new Flight(carrierCode, departure_time, arrival_time, Double.toString(price_total)));
                }
                */

                Log.d("resultof", String.valueOf(result));
                //Log.d("flight_length", Integer.toString(flightResults.length));
            } catch (ResponseException e) {
                e.printStackTrace();
            }

            AssetManager assetManager = getAssets();

            try {
                InputStream is = assetManager.open("jsons/flight_code.json");
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

                for(int i = 0; i<flightResultsRound.length; i++){
                    for(int j=0; j<flightResultsRound[i].length; j++){
                        for(int k =0; k<flightResultsRound[i][j].getDepCodeSize(); k++){
                            for(int l =0; l<jsonArray.length(); l++){
                                JSONObject jo = jsonArray.getJSONObject(l);

                                String en_name = jo.getString("영문명");
                                String ko_name = jo.getString("한글명");
                                String iata = jo.getString("IATA_CODE");
                                String icao = jo.getString("ICAO_CODE");
                                if(flightResultsRound[i][j].getCarrierCode(k).equals(iata)||flightResultsRound[i][j].getCarrierCode(k).equals(icao)){
                                    flightResultsRound[i][j].setCarrier_eng(en_name, k);
                                    flightResultsRound[i][j].setCarrier_kor(ko_name, k);
                                    //Log.d("영문명", en_name);
                                    break;
                                }
                            }
                        }

                    }
                }


            }catch (IOException e){e.printStackTrace();}
            catch (JSONException e){e.printStackTrace();}

            //return flightResults;
            return new FlightResult[0];
        }
    }//class JSoupAsyncTask
}