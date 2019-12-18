package com.hfad.alarmapplicaion;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.hfad.alarmapplicaion.service.SensorService;

/*import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;*/


public class AlarmService extends Service {
    NotificationManager notificationManager;
    NotificationChannel mChannel;
    Notification noti;
    Notification.Builder notiBuilder;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

       /* if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "default";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("알람시작")
                    .setContentText("알람음이 재생됩니다.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            startForeground(100, notification);*/

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




        Log.d("chatTitle2", intent.getExtras().getString("chatTitle1"));
        // 알람창 호출
        Intent intent1 = new Intent(this, AlarmRingingActivity.class);
        //Intent in = new Intent(this, AlarmActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);


        intent1.putExtra("Time2",intent.getExtras().getString("Time1"));
        intent1.putExtra("chatTitle2", intent.getExtras().getString("chatTitle1"));
        intent1.putExtra("myUserId", intent.getExtras().getString("myUserId"));

        Log.d("chatTitle2", intent.getExtras().getString("chatTitle1"));
        startActivity(intent1);

        Log.d("AlarmService", "Alarm");

        if(Build.VERSION.SDK_INT >= 26){
            Log.d("MusicService","Android version Oreo!");

            // NotificationChannel(String id, CharSequence name, int importance);
            mChannel = new NotificationChannel("music_service_channel_id",
                    "music_service_channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            // Notification.VISIBILITY_PUBLIC
            // Notification.VISIBILITY_SECRET

            // 노티피케이션 매니저 초기화
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 노티피케이션 채널 생성
            notificationManager.createNotificationChannel(mChannel);
            // 노티피케이션 빌더 객체 생성
            notiBuilder = new Notification.Builder(this, mChannel.getId());

        }
        else{
            // 노티피케이션 빌더 객체 생성
            notiBuilder = new Notification.Builder(this);
        }

        // Intent 객체 생성 - MainActivity 클래스를 실행하기 위한 Intent 객체
        Intent in = new Intent(this, MainActivity.class);

        // Intent 객체를 이용하여 PendingIntent 객체를 생성 - Activity를 실행하기 위한 PendingIntent
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);

        // Notification.Builder 객체를 이용하여 Notification 객체 생성
        noti = notiBuilder.setContentTitle("공동알람")
                .setContentText(intent.getExtras().getString("chatTitle1")+" 알람이 울립니다.")       //알람명 띄우기0
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .build();


        // foregound service 설정 - startForeground() 메소드 호출, 위에서 생성한 nofication 객체 넘겨줌
        startForeground(123, noti);

        //****************************************
        Log.d("ODH","AlarmService");
       return START_NOT_STICKY;
       // return START_REDELIVER_INTENT;
       // return START_STICKY;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String channelId = "Alarm";
        String channelName = getString(R.string.app_name);
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        //channel.setDescription(channelName);
        channel.setSound(null, null);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        startService(new Intent(this, SensorService.class));
        return channelId;
    }
}
