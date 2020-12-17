package com.finalexam.capstone1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;

import static com.finalexam.capstone1.SearchActivity.TAG;

public class SetAlarmActivity extends Activity {

    private Button btn_save;
    private EditText ed_price_limit;
//    private float price_limit;
//    private ImageView graph;
//    private String id, password, st_email, st_birth, adlt, chld, limit;
//    private boolean round;
    private LineChart priceChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_create);

        getWindow().setWindowAnimations(0); //화면전환 효과 제거
        ed_price_limit = findViewById(R.id.price_limit);
        btn_save = (Button)findViewById(R.id.btn_fsavealarm2);
        priceChart = (LineChart) findViewById(R.id.price_chart);

        // intent -> preferencdManager
//        Intent intent = getIntent();
//        final String arr = intent.getStringExtra("ARRIVAL");
//        final String dep = intent.getStringExtra("DEPARTURE");
//        final String date = intent.getStringExtra("DATE");
////        final int int_adlt = intent.getIntExtra("ADULT", 0);
//        adlt = String.valueOf(intent.getIntExtra("ADULT", 0));
////        final int int_chld = intent.getIntExtra("CHILD", 0);
//        chld = String.valueOf(intent.getIntExtra("CHILD", 0));
//        id = intent.getStringExtra("id");
//        st_email = intent.getStringExtra("e_mail");
//        st_birth = intent.getStringExtra("date_of_birth");
//        password = intent.getStringExtra("password");
//        round = intent.getBooleanExtra("ROUND", true);

        // 주소로 전달하기 때문에 전체 String으로 저장해도 문제 없을 듯 함
        final PreferenceManager pref = new PreferenceManager(this);
        final String arr = pref.getValue("ARRIVAL", null);
        final String dep = pref.getValue("DEPARTURE", null);
        final String date = pref.getValue("DATE", null);
        final String arrdate = pref.getValue("RETURN", null);
//        final int int_adlt = pref.getValue("ADULT", 0);
//        final int int_chld = pref.getValue("CHILD", 0);
        final String adlt = String.valueOf(pref.getValue("ADULT", 0));
        final String chld = String.valueOf(pref.getValue("CHILD", 0));
//        final float float_limit = pref.getValue("PRICELIMIT", 0.f);
        final boolean round = pref.getValue("ROUND", true);
        String priceJson = pref.getValue("PRICEJSON", null);

        Log.d(TAG, "POST response code pricelimit at pricedistribution");

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final float price_limit = Float.parseFloat(ed_price_limit.getText().toString());
                final String price_limit = ed_price_limit.getText().toString();
                Log.d(TAG, "POST response code pricelimit at setalarmactivity" + price_limit);

                // 지정가 공백 불가능
//                if(ed_price_limit.getText().toString().replace(" ", "").equals("")){
                if(price_limit.replace(" ", "").equals("")){
                    Toast.makeText(SetAlarmActivity.this, "가격이 빈칸 일 수 없습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(view.getContext(), MyAlarmsActivity.class);
                    startActivity(intent);

                    final String id = pref.getValue("id", null);
                    Log.d(TAG, "POST response code aa " + id);

//                    Log.d(TAG, "dept " + arrdate);
                    String roundway;
                    if (round){roundway="1";}
                    else{roundway="0";}

//                    PriceDistributionActivity.SaveAlarmActivity task = new PriceDistributionActivity.SaveAlarmActivity();
//                    task.execute("http://" + "synergyflight.dothome.co.kr" + "/insert_alarm_data.php", dep, arr, date, adlt, chld, limit);
                    SetAlarmActivity.SaveAlarmActivity task = new SetAlarmActivity.SaveAlarmActivity();

                    task.execute("http://" + "52.78.216.182" + "/insert_alarm_data.php", dep, arr, date, arrdate, adlt, chld, roundway, price_limit, id);

                }
            }
        });// button.setOnClickListener

        drawChart(priceChart, priceJson);

    }//OnCreate

    // 가격 분포도 그리기
    void drawChart(LineChart chart, String json) {
        ArrayList<Integer> prices = new ArrayList<Integer>();
        List<Entry> entries = new ArrayList<>();
        ArrayList<Integer> itemList = new ArrayList<Integer>();
        ArrayList<Integer> cntList = new ArrayList<Integer>();

        int max=1;
        if(json!=null){
            try {
                JSONArray jsonArray = new JSONArray(json);
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
        lineDataSet.setLineWidth(2);
        /*lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleColor(Color.BLUE);
        lineDataSet.setDrawCircleHole(true);

        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);*/
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(max+1);

        YAxis yLAxis = chart.getAxisLeft();
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
        YAxis yRAxis = chart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);
        chart.setDoubleTapToZoomEnabled(false);
    }

    // member_info db로 토큰 등 전송
    public class SaveAlarmActivity extends AsyncTask<String, Void, String> {

        private static final String TAG = "SaveAlarmActivity";
        //ProgressDialog progressDialog;


        @Override
        protected String doInBackground(String... params) {

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];
//            dep, arr, date, adlt, chld, limit

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            // TODO : 저장 시 왕복, 도착지에서 출발 날짜 포함하기
            String dept_city= (String) params[1];
            String arr_city = (String) params[2];
            String dept_date = (String) params[3];
            String arr_date = (String) params[4];
            int adult = Integer.parseInt(params[5]);
            int child = Integer.parseInt(params[6]);
//            String airline_info = (String) params[6];
            String round = params[7];
            float price_limit = Float.parseFloat(params[8]);
            String id=(String) params[9];

//            System.out.println("in" + token);

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // ex : String postParameters = "name=" + name + "&country=" + country;
            Log.d(TAG, dept_city+arr_city+ dept_date+ arr_date+adult+child+round+price_limit+id);

            String postParameters = "dept_city=" + dept_city + "&arr_city=" + arr_city + "&dept_date=" + dept_date+
                    "&arr_date=" + arr_date + "&adult=" + adult + "&child=" + child + "&round=" + round +"&price_limit=" + price_limit + "&id=" + id;

//            System.out.println("in" + price_limit+ adult);
            Log.d(TAG, "POST response code aa " + id);

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
