package com.finalexam.capstone1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.BreakIterator;


public class AlarmEdit extends AppCompatActivity {
    private TextView t_dept_eng, t_dept_kr, t_dept_date, t_arr_eng, t_arr_kr, t_arr_date, t_way, t_adult, t_child;
    private EditText e_pricelimit;
    private Button b_save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_detail);

        t_dept_eng = (TextView) findViewById(R.id.tv_dept_eng);
        t_dept_kr = (TextView) findViewById(R.id.tv_dept_kr);
        t_dept_date = (TextView) findViewById(R.id.tv_dept_date);
        t_arr_eng = (TextView) findViewById(R.id.tv_arr_eng);
        t_arr_kr = (TextView) findViewById(R.id.tv_arr_kr);
        t_arr_date = (TextView) findViewById(R.id.tv_arr_date);
        t_way = (TextView) findViewById(R.id.tv_way);
        e_pricelimit = (EditText) findViewById(R.id.ed_price_limit);
        t_adult = (TextView) findViewById(R.id.tv_adult);
        t_child = (TextView) findViewById(R.id.tv_child);
        b_save = (Button) findViewById(R.id.b_save);

        Intent intent = getIntent();

        // intent 객체에 데이터를 실어서 보내기
        // 리스트뷰 클릭시 인텐트 (Intent) 생성하고 position 값을 이용하여 인텐트로 넘길값들을 넘긴다
        final String code = intent.getStringExtra("code_alarm");
        final String dept_city = intent.getStringExtra("dept_city");
        final String dept_date = intent.getStringExtra("dept_date");
        final String arr_city = intent.getStringExtra("arr_city");
        final String arr_date = intent.getStringExtra("arr_date");
        final String adult = intent.getStringExtra("adult");
        final String child = intent.getStringExtra("child");
        final String round = intent.getStringExtra("round");
        final String price_limit = intent.getStringExtra("price_limit");

//        Log.d("AlarmEdit", code+dept_city+dept_date+arr_city+arr_date+adult+child+round+price_limit);

        //todo : t_dept_kr, t_arr_kr 은 한국어로 어떻게 번역할 지
//        t_dept_kr.setText();
//        t_arr_kr.setText();
        t_dept_eng.setText(dept_city);
        t_dept_date.setText(dept_date);
        t_arr_eng.setText(arr_city);

        if (arr_date.equals("0000-00-00")) {
            t_arr_date.setText("");
        } else {
            t_arr_date.setText(arr_date);
        }

        if (round.equals("1")) {
            t_way.setText("Round Trip");
        } else {
            t_way.setText("One Way Trip");
        }

        t_adult.setText(adult);
        t_child.setText(child);
        e_pricelimit.setHint(price_limit);


        //버튼 눌렀을 때
        //edittext 받은 값과 기존 price_limit 값 다르면 갱신
        //변경사항 db 갱신
        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(price_limit.equals("0000-00-00"))
                //공백일 때
                if (price_limit.equals(e_pricelimit.getText().toString()) || e_pricelimit.getText().toString().replace(" ", "").equals("")) {
                    Toast.makeText(AlarmEdit.this, "값 변경이 없습니다.", Toast.LENGTH_SHORT).show();
                }

                //공백이 아닐 때, 값이 다를 때
                //db 갱신
                else {
                    PreferenceManager pref = new PreferenceManager(AlarmEdit.this);
                    final String id = pref.getValue("id", null);

                    ChangeAlarmActivity task = new ChangeAlarmActivity();
                    task.execute("http://" + "52.78.216.182" + "/change_price_limit.php", id, code, e_pricelimit.getText().toString());

                    Toast.makeText(AlarmEdit.this, "지정가가 변경되었습니다.", Toast.LENGTH_SHORT).show();

                    //MyAlarmsActivity 갱신
                    Intent intent = new Intent(AlarmEdit.this, MyAlarmsActivity.class);
                    startActivity(intent);
                }
                //액티비티 종료
                finish();
            }
        });


    }

    //인텐트 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    // member_info db로 토큰 등 전송
    public class ChangeAlarmActivity extends AsyncTask<String, Void, String> {

        private static final String TAG = "AlarmEditActivity";
        //ProgressDialog progressDialog;


        @Override
        protected String doInBackground(String... params) {

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];
//            dep, arr, date, adlt, chld, limit

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.

            String id = (String)params[1];
            String code=(String) params[2];
            String price_limit = (String)params[3];

//            System.out.println("in" + token);

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // ex : String postParameters = "name=" + name + "&country=" + country;

            String postParameters = "id=" + id + "&code=" + code + "&price_limit=" + price_limit;

            Log.d("postParameters", postParameters);



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

                Log.d(TAG, "AlarmEditactivity: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }

    }
}