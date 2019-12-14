package com.hfad.alarmapplicaion.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hfad.alarmapplicaion.AlarmReceiver;
import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.MainActivity;
import com.hfad.alarmapplicaion.model.ChatRoom;
import com.hfad.alarmapplicaion.model.User;

import java.util.ArrayList;
import java.util.Calendar;


// 로그인 되면 로그인 세션을 유지하는 서비스이다.
public class SessionService extends Service {

    public static String receiveUser = "getUser";

    private Calendar calendar;

    public FirebaseSystem mFirebaseSystem;

    public ArrayList<ChatRoom> chats;
    User myUserInfo;
    @Override
    public void onCreate() {
        super.onCreate();
        calendar = Calendar.getInstance();

        IntentFilter filter = new IntentFilter();
        filter.addAction("getUser");
        filter.addAction("myAlarmList");
        filter.addAction("startMainActivity");
        registerReceiver(receiver, filter);

        //Toast.makeText(getApplicationContext(), "서비스 시작", Toast.LENGTH_SHORT).show();

        mFirebaseSystem = FirebaseSystem.getInstance(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 메인 액티비티로 부터 유저 데이터 값을 받아와서 저장을 한다.
        if(myUserInfo == null){
            myUserInfo = (User)intent.getSerializableExtra("user");
        }


        //Toast.makeText(getApplicationContext(), "서비스 시작", Toast.LENGTH_SHORT).show();
        //Log.d("service", "service");


        mFirebaseSystem.getMyAlarmRoomList(myUserInfo);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Toast.makeText(getApplicationContext(), "현재 유저" + myUserInfo.id +"님이 종료했습니다. " , Toast.LENGTH_SHORT).show();        // 로그아웃
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
                //Log.d("세션", "세션이 불려짐");
                Intent intent1 = new Intent("putUser");
                intent1.putExtra("user", myUserInfo);
                sendBroadcast(intent1);
            }else if(action.equals("myAlarmList")){     // 여기서 내가 참여한 리스트를 받는다. 그 리스트들을 나의 데이터베이스에 저장한다.
                //Toast.makeText(getApplicationContext(), "현재 내가 참여한 리스트 불러오기서비스", Toast.LENGTH_SHORT).show();
                chats = (ArrayList<ChatRoom>)intent.getSerializableExtra("myAlarmList");
                for(ChatRoom chat : chats){
                    //Toast.makeText(getApplicationContext(), chat.roomTitle, Toast.LENGTH_SHORT).show();
                }

                setAlarm();
            }else if(action.equals("startMainActivity")){
                // MainActivity 출력하도록함.
                Toast.makeText(getApplicationContext(),"서비스를 통해 실행", Toast.LENGTH_SHORT).show();
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startIntent.putExtra("user", myUserInfo);
                startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startIntent);
            }
        }
    };


    /* 알람 등록 */
    private void setAlarm() {

        for(ChatRoom chat : chats){
            // 알람 시간 설정
            Log.d("chat.hour", String.valueOf(chat.hour));
            Log.d("chat.minute", String.valueOf(chat.minute));

            this.calendar.set(Calendar.HOUR_OF_DAY, chat.hour);
            this.calendar.set(Calendar.MINUTE, chat.minute);
            this.calendar.set(Calendar.SECOND, 0);

            //this.calendar.set(Calendar.HOUR_OF_DAY,H);
            //this.calendar.set(Calendar.HOUR_OF_DAY,M);
            // Receiver 설정
            Intent intent = new Intent(this, AlarmReceiver.class);

            //cnt 대신 리퀘스트코드
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)chat.number, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // 알람 설정
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, this.calendar.getTimeInMillis(), pendingIntent);
        }


    }
}
