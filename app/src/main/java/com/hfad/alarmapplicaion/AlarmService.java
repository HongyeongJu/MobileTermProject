package com.hfad.alarmapplicaion;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
/*import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;*/
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class AlarmService extends Service {

    private static final String CHANNEL_1_ID = "channel1";
    private NotificationManagerCompat notificationManager;
    private Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this); // 노티피케이션 설정
    }

    public void onDestroy(){ // 서비스가 종료될때
        super.onDestroy();
        stopForeground(true);   // 노티피케이션 알람 종료
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Foreground 에서 실행되면 Notification 을 보여줘야 됨
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Oreo(26) 버전 이후 버전부터는 channel 이 필요함
            String channelId =  createNotificationChannel();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
            Notification notification = builder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //.setCategory(Notification.CATEGORY_SERVICE)
                    .build();

            startForeground(1, notification);
        }

        // 알람창 호출
        Intent intent1 = new Intent(this, AlarmRingingActivity.class);
        //Intent in = new Intent(this, AlarmActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent1.putExtra("Time2",intent.getExtras().getString("Time1"));
        startActivity(intent1);

        Log.d("AlarmService", "Alarm");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }

        stopSelf();

        return START_NOT_STICKY;
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

        return channelId;
    }
}
