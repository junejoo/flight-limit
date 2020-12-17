package com.finalexam.capstone1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class MypageActivity extends Activity {

    private ArrayList<String> list_menu;
    private BaseAdapter_mypage adapter;
    private ListView mListView;

    private ImageButton btn_home, btn_profile;
    private TextView login, email, birth;
    private String id, st_email, st_birth;
    private String CurState = "CheckAlarm"; //알람 조회 페이지에서 뒤로가기로 이동할 구간을 구분하기 위함

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_profile);

        getWindow().setWindowAnimations(0); //화면전환 효과 제거

        email = (TextView) findViewById(R.id.email);
        birth = (TextView) findViewById(R.id.birth);
        mListView = (ListView) findViewById(R.id.list_mypage);
        btn_home = (ImageButton)findViewById(R.id.btn_mp_home);
        btn_profile = (ImageButton)findViewById(R.id.btn_mp_profile);
        login=(TextView) findViewById(R.id.login);

        PreferenceManager pref = new PreferenceManager(this);
        id = pref.getValue("id", null);
        st_email = pref.getValue("e_mail", null);
        st_birth = pref.getValue("date_of_birth", null);

        Intent intent = getIntent();
        /*id = intent.getStringExtra("id");
        st_email = intent.getStringExtra("e_mail");
        st_birth = intent.getStringExtra("date_of_birth");
        password = intent.getStringExtra("password");
        */
        if(id!=null) {
            login.setText(id+"님 안녕하세요");
            login.setOnClickListener(null);
            email.setText(st_email);
            birth.setText(st_birth);
        }
        else {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }

        list_menu = new ArrayList<String>();
        if(id!=null){
            list_menu.add("개인정보"); list_menu.add("알람설정");
            list_menu.add("알람목록"); list_menu.add("로그아웃");
        }
        adapter = new BaseAdapter_mypage(this, list_menu);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(), i+"번째 id="+l, Toast.LENGTH_SHORT).show();
                if(i == 0){ // 개인정보
                    Intent intent = new Intent(view.getContext(), MemberInfoActivity.class);
                    startActivity(intent);
                    finish();

                }
                else if(i == 1){    // 알람설정

                }
                else if (i == 2) {  // 알람목록
                    Intent intent = new Intent(view.getContext(), MyAlarmsActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    /*intent.putExtra("id", id);
                    intent.putExtra("password", password);
                    intent.putExtra("e_mail", st_email);
                    intent.putExtra("date_of_birth", st_birth);
                     */
                    intent.putExtra("CurState", CurState);
                    startActivity(intent);
                    finish();
                }
                else if(i==3){  // 로그아웃
                    AlertDialog.Builder alert = new AlertDialog.Builder(MypageActivity.this);
                    alert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            PreferenceManager pref = new PreferenceManager(MypageActivity.this);
                            pref.clear();
                            Intent intent = getIntent();
                            startActivity(intent);
                            finish();

                        }
                    });
                    alert.setMessage("정말 로그아웃하시겠습니까?");
                    alert.show();
                }
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MypageActivity.this, MainActivity.class);
                /*intent.putExtra("id", id);
                intent.putExtra("password", password);
                intent.putExtra("e_mail", st_email);
                intent.putExtra("date_of_birth", st_birth);
                 */
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You're looking mypage already", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(MypageActivity.this, MainActivity.class);
        //startActivity(intent);
        finish();
    }

    class BaseAdapter_mypage extends BaseAdapter {

        private ArrayList<String> list;
        private Context context;

        BaseAdapter_mypage(Context context, ArrayList<String> data) {
            this.context = context;
            this.list = data;
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.mypage_profile_listitem, viewGroup, false);
            }

            final String str = list.get(i);
            TextView tv = (TextView) view.findViewById(R.id.tv_mypage_list);
            tv.setText(str);

            return view;
        }
    }


}

