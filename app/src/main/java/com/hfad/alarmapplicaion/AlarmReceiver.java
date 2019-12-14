package com.hfad.alarmapplicaion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
   Context context1;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context1=context;
        Intent sIntent = new Intent(context, AlarmService.class);

        //intent.getExtras().getString("Time");
        sIntent.putExtra("Time1",intent.getExtras().getString("Time"));

        // Oreo(26) 버전 이후부터는 Background 에서 실행을 금지하기 때문에 Foreground 에서 실행해야 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context1.startForegroundService(sIntent);
        } else {
            context1.startService(sIntent);
        }
        Log.d("ODH","Reciver");
    }
}
