package com.hfad.alarmapplicaion.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.hfad.alarmapplicaion.model.User;


// 로그인 되면 로그인 세션을 유지하는 서비스이다.
public class SessionService extends Service {

    User myUserInfo;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String name = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");
        String password = intent.getStringExtra("pw");
        int totalPoint = intent.getIntExtra("totalPoint", 0);
        int point = intent.getIntExtra("point", 0);

        myUserInfo = new User(name, id, password, totalPoint, point);



        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
