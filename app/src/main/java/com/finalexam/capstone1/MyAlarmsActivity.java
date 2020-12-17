package com.finalexam.capstone1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.view.View.GONE;

public class MyAlarmsActivity extends AppCompatActivity {

    public static Context CONTEXT;
//    String url = "http://synergyflight.dothome.co.kr/get_alarm_data.php";
    String url = "http://52.78.216.182/get_alarm_data.php";
    ListView lv_alarm;
    ArrayList<Alarm> list;
    public GetAlarm alrm;
    private List<HashMap<String, String>> alarmList = null;
    private String id;
    AlarmListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myalarms);

        CONTEXT = this; //액티비티 갱신 위한 변수

        getWindow().setWindowAnimations(0); //화면전환 효과 제거
        lv_alarm = (ListView) findViewById(R.id.lv_alarm);

//        Intent intent = getIntent();
        PreferenceManager pref = new PreferenceManager(this);
        id = pref.getValue("id", null);

        alrm = new GetAlarm();
        alarmList = new ArrayList<HashMap<String, String>>();

        //알람 목록 받아옴
        try {
//            alarmList = alrm.execute(url).get();
//            alarmList = alrm.execute(url, id).get();
            alarmList = alrm.execute(url, id, String.valueOf(-1)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Insert_Listview();

        adapter = new AlarmListAdapter(list);
        lv_alarm.setAdapter(adapter);

        // 한 번 클릭 -> 상세정보(adapter) // 길게 클릭 -> 삭제

        // list view click evnet
        lv_alarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MyAlarmsActivity.this, "clicked" + alarmList.get(position).get("code_alarm"), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(
                        getApplicationContext(), // 현재화면의 제어권자
                        AlarmEdit.class); // 다음넘어갈 화면

                // intent 객체에 데이터를 실어서 보내기
                // 리스트뷰 클릭시 인텐트 (Intent) 생성하고 position 값을 이용하여 인텐트로 넘길값들을 넘긴다
                intent.putExtra("code_alarm", alarmList.get(position).get("code_alarm"));
                intent.putExtra("dept_city", alarmList.get(position).get("dept_city"));
                intent.putExtra("dept_date", alarmList.get(position).get("dept_date"));
                intent.putExtra("arr_city", alarmList.get(position).get("arr_city"));
                intent.putExtra("arr_date", alarmList.get(position).get("arr_date"));
                intent.putExtra("adult", alarmList.get(position).get("adult"));
                intent.putExtra("child", alarmList.get(position).get("child"));
                intent.putExtra("round", alarmList.get(position).get("round"));
                intent.putExtra("price_limit", alarmList.get(position).get("price_limit"));

                startActivity(intent);
            }
        });

        lv_alarm.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id2) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });

                //delete alarm
                alert.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //search code_alarm in listview with position then delete
//                        Toast.makeText(MyAlarmsActivity.this, "인덱스는 " + alarmList.get(position).get("code_alarm") + "입니다", Toast.LENGTH_SHORT).show();

                        //알람 목록 받아옴
                        try {
                            alrm = new GetAlarm();
                            String index=alarmList.get(position).get("code_alarm");
                            alarmList = new ArrayList<HashMap<String, String>>();
                            alarmList=alrm.execute(url, id, index).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //insert alarm data to list view
                        Insert_Listview();
                        adapter = new AlarmListAdapter(list);
                        lv_alarm.setAdapter(adapter);
                    }
                });
                alert.setMessage("해당 알람을 지우겠습니까?");
                alert.show();

                return true;
            }
        });
    }//OnCreate()

    @Override
    public void onBackPressed() {
//        switch (CurState){
//            case "FromHome":
//                Intent intent = new Intent(MyAlarmsActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case "SetAlarm":
//                intent = new Intent(MyAlarmsActivity.this, MypageActivity.class);
//                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//                finish();
//                break;
//        }

        // 항상 홈 화면으로, 그 사이 액티비티 전체 삭제 (Flags)
        Intent intent = new Intent(MyAlarmsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    void Insert_Listview() {
        list = new ArrayList<>();

        for(int i = 0; i < alarmList.size(); i++){
            // TODO : airline 삭제, 왕복여부, 출발날짜 추가
            list.add(new Alarm(alarmList.get(i).get("dept_city"), alarmList.get(i).get("arr_city"),
                    Integer.parseInt(alarmList.get(i).get("adult")), Integer.parseInt(alarmList.get(i).get("child")), alarmList.get(i).get("dept_date"),
                    alarmList.get(i).get("arr_date"), alarmList.get(i).get("price_limit"), alarmList.get(i).get("round")));
            //            alarmList.get(i).get("airline_info")
        }
    }


    class AlarmListAdapter extends BaseAdapter {

        ArrayList<Alarm> list;

        AlarmListAdapter(ArrayList<Alarm> list) {
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

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.mypage_alarm_listitem , viewGroup, false);
            }

            TextView tv_dep = (TextView) view.findViewById(R.id.arm_dep);
            TextView tv_arr = (TextView) view.findViewById(R.id.arm_arr);
            TextView tv_depdate = (TextView) view.findViewById(R.id.arm_depdate);
            TextView tv_arrdate = (TextView) view.findViewById(R.id.arm_arrdate);
            TextView tv_price = (TextView) view.findViewById(R.id.tv_alarm_price);
            TextView tv_detail = (TextView) view.findViewById(R.id.tv_alarm_detail);

            Alarm lv_item = list.get(i);
            tv_dep.setText(lv_item.dept);
            tv_arr.setText(lv_item.arrv);
            tv_depdate.setText(lv_item.depdate);
            tv_price.setText(lv_item.price);
            Log.d("TAG", "bb " + lv_item.round);
//            tv_detail.setText(lv_item.adlt + " Adult, " + lv_item.airl);
            if(lv_item.round.equals("1")) { tv_arrdate.setText( lv_item.arrdate ); } else { tv_arrdate.setText( "" );}
            Log.d("TAG", "cc " + lv_item.adlt);
            tv_detail.setText(lv_item.adlt + " Adult, " + lv_item.chld + " Child");





            return view;
        }
    }

    class GetAlarm extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL phpUrl = new URL(params[0]);
                String id = new String(params[1]);
                String code = new String(params[2]);
                String postParameters = "id=" + id + "&code_alarm=" + code;

                Log.d("aa", "POST response code - " + id);
                HttpURLConnection conn = (HttpURLConnection) phpUrl.openConnection();

                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    conn.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                    conn.connect();

                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(postParameters.getBytes("UTF-8")); //전송할 데이터가 저장된 변수를 이곳에 입력합니다.

                    outputStream.flush();
                    outputStream.close();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while (true) {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String str = jsonHtml.toString();

            try {
                // PHP에서 받아온 JSON 데이터를 JSON오브젝트로 변환
                JSONObject jObject = new JSONObject(str);
                // results라는 key는 JSON배열로 되어있다.
                JSONArray results = jObject.getJSONArray("result");

//            System.out.println(results); //json으로 받아옴
                for (int i = 0; i < results.length(); i++) {
                    JSONObject alarm_info = results.getJSONObject(i);

                    String code_alarm = alarm_info.getString("code_alarm");
                    String dept_city = alarm_info.getString("dept_city");
                    String dept_date = alarm_info.getString("dept_date");
                    String arr_city = alarm_info.getString("arr_city");
                    String arr_date = alarm_info.getString("arr_date");
                    String adult = alarm_info.getString("adult");
                    String child = alarm_info.getString("child");
                    String round=alarm_info.getString("round");
//                    String clss = alarm_info.getString("class");
//                    String airline_info = alarm_info.getString("airline_info");
                    String price_limit = alarm_info.getString("price_limit");

                    HashMap<String, String> alarmMap = new HashMap<String, String>();
                    alarmMap.put("code_alarm", code_alarm);
                    alarmMap.put("dept_city", dept_city);
                    alarmMap.put("dept_date", dept_date);
                    alarmMap.put("arr_city", arr_city);
                    alarmMap.put("arr_date", arr_date);
                    alarmMap.put("adult", adult);
                    alarmMap.put("child", child);
//                    alarmMap.put("class", clss);
//                    alarmMap.put("airline_info", airline_info);
                    alarmMap.put("round", round);
                    alarmMap.put("price_limit", price_limit);

                    alarmList.add(alarmMap);


                }
//                System.out.println(alarmList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return alarmList;
        }//DoInBackGround()
    }//class GetAlarm
}//MyAlarmActivity
