package com.hfad.alarmapplicaion.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hfad.alarmapplicaion.AlarmService;
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
    private Calendar todayCalendar;
    String strtime;
    public FirebaseSystem mFirebaseSystem;

    public ArrayList<ChatRoom> chats;
    User myUserInfo;
    @Override
    public void onCreate() {
        super.onCreate();





        IntentFilter filter = new IntentFilter();
        filter.addAction("getUser");
        filter.addAction("myAlarmList");
        filter.addAction("startMainActivity");
        filter.addAction("updateMyAlarmList");
        filter.addAction("AlarmReceiver");
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
                Toast.makeText(getApplicationContext(), "현재 내가 참여한 리스트 불러오기서비스", Toast.LENGTH_SHORT).show();
                chats = (ArrayList<ChatRoom>)intent.getSerializableExtra("myAlarmList");
                for(ChatRoom chat : chats){
                    Toast.makeText(getApplicationContext(), chat.roomTitle, Toast.LENGTH_SHORT).show();
                }

                setAlarm();
            }else if(action.equals("startMainActivity")){
                // MainActivity 출력하도록함.
                Toast.makeText(getApplicationContext(),"서비스를 통해 실행", Toast.LENGTH_SHORT).show();
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startIntent.putExtra("user", myUserInfo);
                startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startIntent);
            }else if(action.equals("updateMyAlarmList")){       // 내가 참여한 리스트를 받도록 FirebaseSystem의 getMyAlarmRoomList를 부르도록 한다.
                Log.i("updateMyAlarmList", "updateMyAlarmList");
                mFirebaseSystem.getMyAlarmRoomList(myUserInfo);     // 1. 파이어베이스에서 updateMyAlarmList의 브로드캐스트 호출
                //2. 위 getMyAlarmRoomList를 호출하여 firebaseSystem의 메소드를 호출 (현재 user정보를 사용해서)
                //3. getMyAlarmRoomList메소드는 이 브로드캐스트 리시버의 myAlarmList에 걸려서 chats 갱신함.
                // 4. setAlarm으로 알람을 다시설정함.
            }else if(action.equals("AlarmReceiver")){       // 알람 리시버를 받으면.
                Log.i("알람리시버 받음", "알람리시버 받음");

                Toast.makeText(getApplicationContext(), "알람리비서 받음 ", Toast.LENGTH_SHORT).show();
                Intent sIntent = new Intent(getApplicationContext(), AlarmService.class);

                Log.i("인텐트 호출", "인텐트 호출");
                //intent.getExtras().getString("Time");
                sIntent.putExtra("Time1",intent.getExtras().getString("Time"));

                // Oreo(26) 버전 이후부터는 Background 에서 실행을 금지하기 때문에 Foreground 에서 실행해야 함
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getApplicationContext().startForegroundService(sIntent);
                } else {
                    getApplicationContext().startService(sIntent);
                }


            }
        }
    };

    /* 알람 등록 */
    public void setAlarm() {

        Log.i("setAlarm 설정", "setAlarm설정");
        todayCalendar = Calendar.getInstance();
        Log.i("현재 시간 ", String.valueOf(todayCalendar.get(Calendar.YEAR)) + "년" +
                String.valueOf(todayCalendar.get(Calendar.MONTH)) +"월" +
                String.valueOf(todayCalendar.get(Calendar.DAY_OF_MONTH)) + "일" +
                String.valueOf(todayCalendar.get(Calendar.HOUR_OF_DAY)) + "시" +
                String.valueOf(todayCalendar.get(Calendar.MINUTE))+ "분" +
                String.valueOf(todayCalendar.get(Calendar.SECOND)) + "초");

        for(ChatRoom chat : chats){
            // 알람 시간 설정

            calendar = (Calendar)todayCalendar.clone();
            int year = calendar.get(Calendar.YEAR);
            int month= calendar.get(Calendar.MONTH);

            calendar.set(year, month,
                    calendar.get(Calendar.DAY_OF_MONTH),chat.hour, chat.minute, 0);


            Log.i("Calendar.getInstance()", String.valueOf(Calendar.getInstance()));
            if (calendar.before(todayCalendar)) {

                Log.i("Alarm Time", "알람시간이 현재시간보다 이전 일 수 없음.");
            }else {
                // 현재 시간 이후로 알람을 설정한다.
                Log.i("calendar time", String.valueOf(year) + "년" +
                        String.valueOf(month + 1) +"월" +
                        String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "일" +
                        String.valueOf(chat.hour) + "시" +
                        String.valueOf(chat.minute)+ "분" +
                        String.valueOf(0) + "초");
                calendar.set(year, month, calendar.get(Calendar.DAY_OF_MONTH),chat.hour, chat.minute, 0);
                Intent intent = new Intent("AlarmReceiver");
                //cnt 대신 리퀘스트코드
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int)chat.number, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // 알람 설정
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, this.calendar.getTimeInMillis(), pendingIntent);
            }

            Log.i("calendar Time", String.valueOf(calendar.getTimeInMillis()));
            Log.i("ODH","Alarm Complete");

        }


    }
}
