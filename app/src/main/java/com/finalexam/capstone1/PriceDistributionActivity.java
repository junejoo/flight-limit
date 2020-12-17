package com.finalexam.capstone1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PriceDistributionActivity extends Activity {

    String TAG = "PriceDiatributionActivity";
    private Button btn_save;
//    ImageButton btn_home, btn_profile;
    String adlt, chld, limit, id;
//    private String CurState = "SetAlarm";
    private LineChart priceChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_alarm2);

        getWindow().setWindowAnimations(0); //화면전환 효과 제거

        PreferenceManager pref = new PreferenceManager(this);
        final String arr = pref.getValue("ARRIVAL", null);
        final String dep = pref.getValue("DEPARTURE", null);
        final String date = pref.getValue("DATE", null);
        final int int_adlt = pref.getValue("ADULT", 0);
        final int int_chld = pref.getValue("CHILD", 0);
        final float float_limit = pref.getValue("PRICELIMIT", 0.f);
        String priceJson = pref.getValue("PRICEJSON", null);

        Log.d(TAG, "POST response code pricelimit at pricedistribution" + float_limit);



        btn_save = (Button) findViewById(R.id.btn_fsavealarm3);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MyAlarmsActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("CurState", CurState);
                startActivity(intent);

                adlt= String.valueOf(int_adlt);
                chld= String.valueOf(int_chld);
                limit=String.valueOf(float_limit);

                PreferenceManager pref = new PreferenceManager(PriceDistributionActivity.this);
                id = pref.getValue("id", null);
                Log.d(TAG, "POST response code aa " + id);

                SaveAlarmActivity task = new SaveAlarmActivity();
                task.execute("http://" + "52.78.216.182" + "/insert_alarm_data.php", dep, arr, date, adlt, chld, limit, id);
            }
        });



        priceChart = (LineChart) findViewById(R.id.price_chart);
        ArrayList<Integer> prices = new ArrayList<Integer>();
        List<Entry> entries = new ArrayList<>();
        ArrayList<Integer> itemList = new ArrayList<Integer>();
        ArrayList<Integer> cntList = new ArrayList<Integer>();

        int max=1;
        if(priceJson!=null){
            try {
                JSONArray jsonArray = new JSONArray(priceJson);
                for(int i=0; i<jsonArray.length(); i++){
                    String price = jsonArray.optString(i);
                    prices.add(Integer.parseInt(price));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        itemList.add(prices.get(0)); int p = prices.get(0);
        for(int i=0; i<prices.size(); i++){
            if(prices.get(i)!=p){
                p = prices.get(i);
                itemList.add(p);
            }
        }
        int cnt;

        for(int i=0; i<itemList.size(); i++){
            cnt=0;
            for(int j=0; j<prices.size(); j++){
                if(prices.get(j).equals(itemList.get(i))){
                    cnt++;
                }
            }
            cntList.add(cnt);
        }

        max=cntList.get(0);
        if(cntList.size()>15){
            for(int i =0; i<15; i++){
                entries.add(new Entry(cntList.get(i), itemList.get(i)));
                if(cntList.get(i)>max){
                    max = cntList.get(i);
                }
            }
        } else{
            for(int i =0; i<cntList.size(); i++){
                entries.add(new Entry(cntList.get(i), itemList.get(i)));
            }
        }



        //Log.d("itemList", itemList.toString());

        LineDataSet lineDataSet = new LineDataSet(entries, "가격분포");
        lineDataSet.setLineWidth(10);
        lineDataSet.setCircleColor(Color.BLUE);
        lineDataSet.setCircleRadius(6);
        /*
        lineDataSet.setCircleHoleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleColor(Color.BLUE);
        lineDataSet.setDrawCircleHole(true);

        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);*/
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        priceChart.setData(lineData);

        XAxis xAxis = priceChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(max+1);

        YAxis yLAxis = priceChart.getAxisLeft();
        //yLAxis.setLabelCount(10);
        int min=0; int n=0;
        if(itemList.get(0)>100000){
            n=(itemList.get(0)/100000);
            min = n*100000;
        }else if(itemList.get(0)>10000){
            n=(itemList.get(0)/10000);
            min = n*10000;
        }
        //yLAxis.setAxisMinimum(min);
        yLAxis.setInverted(true);
        Log.d("n", Integer.toString(min));
        YAxis yRAxis = priceChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);
        priceChart.setDoubleTapToZoomEnabled(false);

    }

    // member_info db로 토큰 등 전송
    public class SaveAlarmActivity extends AsyncTask<String, Void, String> {

        private static final String TAG = "SaveAlarmActivity";
        //ProgressDialog progressDialog;


        @Override
        protected String doInBackground(String... params) {

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];
//            dep, arr, date, adlt, chld, airline, limit)

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            // TODO : 저장 시 왕복, 도착지에서 출발 날짜 포함하기
            String dept_city= (String) params[1];
            String arr_city = (String) params[2];
            String dept_date = (String) params[3];
            int adult = Integer.parseInt(params[4]);
            int child = Integer.parseInt(params[5]);
            float price_limit = Float.parseFloat(params[6]);
            String id=(String) params[7];

//            System.out.println("in" + token);

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // ex : String postParameters = "name=" + name + "&country=" + country;
            String postParameters = "dept_city=" + dept_city + "&arr_city=" + arr_city + "&dept_date=" + dept_date
                    + "&adult=" + adult + "&child=" + child + "&price_limit=" + price_limit+"&id="+id;

            Log.d(TAG, "POST response code aa " + id);

//            System.out.println("in" + price_limit+ adult);
            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.

                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.

                httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8")); //전송할 데이터가 저장된 변수를 이곳에 입력합니다.

                outputStream.flush();
                outputStream.close();


                // 응답을 읽습니다.

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                    // 정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();
                } else {

                    // 에러 발생

                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "RegisterActivity: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }

    }
}
