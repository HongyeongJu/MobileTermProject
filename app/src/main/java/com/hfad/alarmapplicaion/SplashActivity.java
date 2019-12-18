package com.hfad.alarmapplicaion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000);      //3초뒤에 LoginActivity로 이동
    }

    private class splashhandler implements Runnable {
        @Override
        public void run() {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            SplashActivity.this.finish();
        }
    }

    //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 만듬.
    @Override
    public void onBackPressed() {
    }
}
