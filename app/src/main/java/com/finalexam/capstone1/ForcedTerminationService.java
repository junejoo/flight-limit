package com.finalexam.capstone1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ForcedTerminationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        PreferenceManager pref = new PreferenceManager(this);
        boolean b = pref.getValue("auto", false);
        // 자동로그인 해제 시 로그인 정보 삭제
        if (!b) {
            pref.clear();
        }
        stopSelf();
    }
}
