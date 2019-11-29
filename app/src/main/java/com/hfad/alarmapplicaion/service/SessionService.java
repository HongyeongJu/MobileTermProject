package com.hfad.alarmapplicaion.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.model.User;


// 로그인 되면 로그인 세션을 유지하는 서비스이다.
public class SessionService extends Service {

    public static String receiveUser = "getUser";

    public FirebaseSystem mFirebaseSystem;

    User myUserInfo;
    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction("getUser");
        registerReceiver(receiver, filter);

        Toast.makeText(getApplicationContext(), "서비스 시작", Toast.LENGTH_SHORT).show();

        mFirebaseSystem = FirebaseSystem.getInstance(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 메인 액티비티로 부터 유저 데이터 값을 받아와서 저장을 한다.
        myUserInfo = (User)intent.getSerializableExtra("user");

        //Toast.makeText(getApplicationContext(), "서비스 시작", Toast.LENGTH_SHORT).show();
        //Log.d("service", "service");

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


    // 브로드캐스트 리시버 객체를 만들어서 현재 유저의 정보를 달라고 할때 유저의 정보를 주도록한다.

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("getUser")){
                Log.d("세션", "세션이 불려짐");
                Intent intent1 = new Intent("putUser");
                intent1.putExtra("user", myUserInfo);
                sendBroadcast(intent1);
            }
        }
    };
}
