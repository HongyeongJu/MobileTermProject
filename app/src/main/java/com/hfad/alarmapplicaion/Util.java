package com.hfad.alarmapplicaion;

import android.app.ActivityManager;
import android.content.Context;

import com.hfad.alarmapplicaion.service.SessionService;

public class Util {

    // 실행중인 세션 서비스가 있는지 확인하는 함수
    public static Boolean isRunningSessionService(Context mContext){

        ActivityManager manager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);

        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(SessionService.class.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
