package com.finalexam.capstone1;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;
import java.util.Locale;

public class AirportDialog extends Dialog {

    // debug
    static final String TAG = "AirportDialog";

    AirportDialog m_aDialog;
    Button btn;
    private AirportListViewAdapter adapter; // 리스트뷰에 연결할 어답터
    private EditText et_airport;
    private ListView lv_airport;

    private List<Airport> list; // 원본 리스트

    AirportDialog(Context context, List<Airport> list) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.list = list;
        Log.d(TAG, "AIRPORT DIALOG 생성됨");   // DEBUG
    }

    void callFunction(Button btn) {
        this.btn = btn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_airport);
        m_aDialog = this;
        Log.d(TAG, "커스텀 다이얼로그로 팝업창 생성됨");

        init();

        et_airport.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String text = et_airport.getText().toString()
                        .toUpperCase(Locale.getDefault());
                Log.d(TAG, "검색창 검색 시작 -> " + text);   // DEBUG
                adapter.filter(text);
            }
        });
    }

    void init() {
        Log.d(TAG, "Dialog init 동작 시작");

        et_airport = (EditText)findViewById(R.id.et_airport);
        lv_airport = (ListView)findViewById(R.id.lv_airport);

        adapter = new AirportListViewAdapter(getContext(), list);   // 리스트를 어댑터에 장착
        lv_airport.setAdapter(adapter); // 어댑터를 리스트뷰에 장착

        lv_airport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Airport airport = adapter.getAirport(i);
                Log.d(TAG, "리스트 아이템 선택됨 : en " + airport.getName_en());
                btn.setText(airport.getName_en());
                m_aDialog.dismiss();     // dialog exit
            }
        });
    }
}