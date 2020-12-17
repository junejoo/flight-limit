package com.finalexam.capstone1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

public class CreateActivity extends Activity {

    private Button btn_save;
    ImageButton btn_home, btn_profile;
    private EditText ed_price_limit, ed_airline;
    private float price_limit;
    private String airline;
    private String id, password, st_email, st_birth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_create);

        getWindow().setWindowAnimations(0); //화면전환 효과 제거

        ed_price_limit=findViewById(R.id.price_limit);

        Intent intent = getIntent();
        final String arr = intent.getStringExtra("ARRIVAL");
        final String dep = intent.getStringExtra("DEPARTURE");
        final String date = intent.getStringExtra("DATE");
        final int adlt = intent.getIntExtra("ADULT", 0);
        final int chld = intent.getIntExtra("CHILD", 0);
        id = intent.getStringExtra("id");
        st_email = intent.getStringExtra("e_mail");
        st_birth = intent.getStringExtra("date_of_birth");
        password = intent.getStringExtra("password");

        btn_save = (Button)findViewById(R.id.btn_fsavealarm2);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PriceDistributionActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                price_limit = Float.parseFloat(String.valueOf(ed_price_limit.getText()));
                airline = String.valueOf(ed_airline.getText());

                intent.putExtra("DEPARTURE", dep);
                intent.putExtra("ARRIVAL", arr);
                intent.putExtra("DATE", date);
                intent.putExtra("ADULT", adlt);
                intent.putExtra("CHILD", chld);
                intent.putExtra("PRICELIMIT", price_limit);
                intent.putExtra("AIRLINE", airline);

                intent.putExtra("id", id);
                intent.putExtra("password", password);
                intent.putExtra("e_mail", st_email);
                intent.putExtra("date_of_birth", st_birth);
                startActivity(intent);
            }
        });


    }
}
