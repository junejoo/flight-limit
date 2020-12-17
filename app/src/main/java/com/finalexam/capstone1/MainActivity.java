package com.finalexam.capstone1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button b_login, b_search, b_alarm, b_info, b_logout;
    private TextView t_hello;

    private String id, st_email, st_birth;
    private String CurState = "FromHome";   // 로그인 페이지에서 이동할 구간을 구분하기 위함
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this, ForcedTerminationService.class));

        setContentView(R.layout.home3);
        // 로그아웃 상태 : 로그인, 검색하기 버튼
        // 로그인 상태 : 검색하기, 알람목록, 개인정보, 로그아웃 버튼

        getWindow().setWindowAnimations(0); //화면전환 효과 제거
        t_hello = (TextView) findViewById(R.id.t_hello);
        b_login = (Button) findViewById(R.id.b_login);
        b_search = (Button) findViewById(R.id.b_search);
        b_alarm = (Button) findViewById(R.id.b_alarm);
        b_info = (Button) findViewById(R.id.b_info);
        b_logout = (Button)findViewById(R.id.b_logout);

        // MyPageActivity 동작
        PreferenceManager pref = new PreferenceManager(this);
        id = pref.getValue("id", null);
        st_email = pref.getValue("e_mail", null);
        st_birth = pref.getValue("date_of_birth", null);

        if(id != null) {  // 로그인 완료
            t_hello.setText(id+"님 안녕하세요");
            b_login.setVisibility(View.GONE);   // 공간 차지 X
            b_alarm.setVisibility(View.VISIBLE);
            b_info.setVisibility(View.VISIBLE);
            b_logout.setVisibility(View.VISIBLE);

        }
        else {  // 로그인 안됨
            t_hello.setText("Hey, Good to see you!");
            b_login.setVisibility(View.VISIBLE);
            b_alarm.setVisibility(View.GONE);
            b_info.setVisibility(View.GONE);
            b_logout.setVisibility(View.GONE);
        }

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                // 로그인 완료 후 홈 화면으로 복귀
                intent.putExtra("CurState", CurState);
                startActivity(intent);
                finish();
            }
        });

        b_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });

        b_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyAlarmsActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });

        b_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MemberInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        b_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                // 로그아웃 시도 취소
                alert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                // 로그아웃 실행
                alert.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 로그인 정보 삭제
                        PreferenceManager pref = new PreferenceManager(MainActivity.this);
                        pref.clear();
                        // 화면 재시작
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                });
                alert.setMessage("정말 로그아웃하시겠습니까?");
                alert.show();
            }
        });
    }

    // 앱 종료 확인 절차
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // 종료 시도 취소
        alert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // 종료 실행
        alert.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferenceManager pref = new PreferenceManager(MainActivity.this);
                boolean b = pref.getValue("auto", false);
                // 자동로그인 해제 시 로그인 정보 삭제
                if (!b) {
                    pref.clear();
                }
                ActivityCompat.finishAffinity(MainActivity.this);
            }
        }).setMessage("정말 종료하시겠습니까?");
        alert.show();
    }
}