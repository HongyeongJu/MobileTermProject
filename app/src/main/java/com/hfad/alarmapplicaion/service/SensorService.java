package com.hfad.alarmapplicaion.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.IBinder;

import android.util.Log;
import android.widget.Toast;

import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;

public class SensorService extends Service implements SensorEventListener {

    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;

    private static final int SHAKE_THRESHOLD = 800;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;

    int cnt=0;
    int count=10;
    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;
    private CountDownTimer countDownTimer;

    String roomTitle;
    String userId;

    private FirebaseSystem mFirebaseSystem;

    public void onCreate() {
        super.onCreate();



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("ODH_SENSOR","SENSOR");
        count=10;
        mFirebaseSystem = FirebaseSystem.getInstance(getApplicationContext());
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        countDownTimer();
        countDownTimer.start();
        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor, SensorManager.SENSOR_DELAY_GAME);

        roomTitle = intent.getStringExtra("roomTitle");
        userId = intent.getStringExtra("userId");


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented")
        return  null;
    }
    private void countDownTimer() {
        countDownTimer = new CountDownTimer(10000,1000) {
            public void onTick(long millisUntilFinished) {

                count--;
                Log.d("ODH_count", String.valueOf(count));

            }
            public void onFinish() {
               // Toast.makeText(getApplicationContext(), cnt, Toast.LENGTH_SHORT).show();
                if(cnt > 12) {
                    mFirebaseSystem.changeWakeUpState(roomTitle, userId);
                    mFirebaseSystem.addPoint(500,userId);
                }
                Log.d("ODH_cnt", String.valueOf(cnt));
                onDestroy();
            }
        };
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

        if (sensorManager != null)
            sensorManager.unregisterListener(this);
        Toast.makeText(getApplicationContext(), "상태창이 변화했습니다.", Toast.LENGTH_SHORT).show();
        Log.d("ODH","Sensor Finish");
        try{
            countDownTimer.cancel();
        }catch (Exception e){
            countDownTimer=null;
        }
    }

/*    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // intent: startService() 호출 시 넘기는 intent 객체
        // flags: service start 요청에 대한 부가 정보. 0, START_FLAG_REDELIVERY, START_FLAG_RETRY
        // startId: start 요청을 나타내는 unique integer id
        return super.onStartCommand(intent, flags, startId);
    }*/
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if (gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    // 이벤트발생!!

                    cnt++;
                    //Toast.makeText(this.getApplicationContext(),"흔들렸다!",Toast.LENGTH_SHORT);
                    //text.setText(String.format("%d",cnt));
                    Log.d("ODH_cnt_shake", String.valueOf(cnt));
                }

                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }

        }
    }
}
