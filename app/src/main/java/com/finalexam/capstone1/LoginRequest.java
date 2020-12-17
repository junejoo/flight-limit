package com.finalexam.capstone1;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    final static private String URL = "http://52.78.216.182/Login.php";
    private Map<String, String> map;

    public LoginRequest(String id, String password, String token, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);

        map = new HashMap<>();
        map.put("id", id);
        map.put("password", password);
        map.put("token", token);

        Log.d("FCM Log", "FCM 토큰: " + token);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return map;
    }
}
