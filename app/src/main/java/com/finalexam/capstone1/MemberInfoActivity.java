package com.finalexam.capstone1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MemberInfoActivity extends AppCompatActivity {
    private TextView info_id, info_birthday, info_email;
    private EditText info_pw, dlgEdt_pw;
    private String id, email, birth, password, dlgpw;
    private Button withdraw;
    private View dialogView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memberinfo);
        info_id = (TextView)findViewById(R.id.info_id);
        info_pw = (EditText)findViewById(R.id.info_pw);
        info_email = (TextView)findViewById(R.id.info_email);
        info_birthday = (TextView)findViewById(R.id.info_birthday);
        withdraw = (Button)findViewById(R.id.withdraw);
        PreferenceManager pref = new PreferenceManager(this);
        id = pref.getValue("id", null);
        email = pref.getValue("e_mail", null);
        birth = pref.getValue("date_of_birth", null);
        password = pref.getValue("password", null);

        if(id!=null) info_id.setText(id);
        if(email!=null) info_email.setText(email);
        if(birth!=null) info_birthday.setText(birth);

        withdraw.setOnClickListener(new View.OnClickListener() {    // 회원탈퇴 버튼
            @Override
            public void onClick(View view) {

                dialogView = (View) View.inflate(MemberInfoActivity.this, R.layout.dialog_member_info,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MemberInfoActivity.this);
                dlg.setTitle("회원 탈퇴");

                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dlgEdt_pw = (EditText) dialogView.findViewById(R.id.dlgEdt_pw);
                        dlgpw = dlgEdt_pw.getText().toString();
                        if(dlgpw.equals(password)){
                            AlertDialog.Builder alert = new AlertDialog.Builder(MemberInfoActivity.this);
                            alert.setTitle("회원 탈퇴");
                            alert.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try{
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if(success){
                                                    PreferenceManager pref = new PreferenceManager(MemberInfoActivity.this);
                                                    pref.clear();   // 로그인 정보 클리어
                                                    Intent intent = new Intent(MemberInfoActivity.this, MainActivity.class);    // 탈퇴 후 홈으로
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else{
                                                    android.app.AlertDialog.Builder dlg = new android.app.AlertDialog.Builder(MemberInfoActivity.this);
                                                    dlg.setMessage("탈퇴하는 과정에서 오류가 생겼습니다 다시 시도해주세요")
                                                            .setNegativeButton("확인", null).create();
                                                    dlg.show();
                                                }
                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    DeleteActivity deleteActivity = new DeleteActivity(id, password, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(MemberInfoActivity.this);
                                    queue.add(deleteActivity);

                                }
                            });
                            alert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();     //닫기
                                }
                            });

                            alert.setMessage("정말 탈퇴하시겠습니까?");
                            alert.show();
                        } else{
                            AlertDialog.Builder alert = new AlertDialog.Builder(MemberInfoActivity.this);
                            alert.setTitle("회원 탈퇴");
                            alert.setNegativeButton("확인", null);
                            alert.setMessage("비밀번호가 맞지 않습니다. 다시 시도해주세요");
                            alert.show();
                        }
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MemberInfoActivity.this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    public class DeleteActivity extends StringRequest {

        final static private String URL="http://52.78.216.182/delete_member_info.php";
        private Map<String, String> map;

        public DeleteActivity(String userID, String pw, Response.Listener<String>listener){
            super(Request.Method.POST, URL, listener,null);

            map = new HashMap<>();
            map.put("id", userID);
//            map.put("password", pw);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }

    }
}
