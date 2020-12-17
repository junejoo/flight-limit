package com.finalexam.capstone1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    // auto
    // id, e_mail, date_of_birth

    public static final String PREFERENCES_NAME = "rebuild_preference";
    public static final String PREF_INTRO_USER_AGREEMENT = "PREF_USER_AGREEMENT";
    public static final String PREF_MAIN_VALUE = "PREF_MAIN_VALUE";
    static Context mContext;
    public PreferenceManager(Context c){
        mContext = c;
    }

    public void put(String key, String value){
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, boolean value){
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void put(String key, int value){
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void put(String key, float value){
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public String getValue(String key, String dftValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCES_NAME,Activity.MODE_PRIVATE);
        try{
            return pref.getString(key, dftValue);
        } catch(Exception e){
            return dftValue;
        }
    }

    public int getValue(String key, int dftValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCES_NAME,Activity.MODE_PRIVATE);
        try{
            return pref.getInt(key, dftValue);
        } catch(Exception e){
            return dftValue;
        }
    }

    public boolean getValue(String key, boolean dftValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCES_NAME,Activity.MODE_PRIVATE);
        try{
            return pref.getBoolean(key, dftValue);
        } catch(Exception e){
            return dftValue;
        }
    }

    public float getValue(String key, float dftValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCES_NAME,Activity.MODE_PRIVATE);
        try{
            return pref.getFloat(key, dftValue);
        } catch(Exception e){
            return dftValue;
        }
    }

    public void removeKey(String key){
        SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCES_NAME,Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.commit();
    }

    public void clear(){
        SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCES_NAME,Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }
}
