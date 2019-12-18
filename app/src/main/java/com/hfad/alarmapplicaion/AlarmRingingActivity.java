package com.hfad.alarmapplicaion;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.service.SensorService;

public class AlarmRingingActivity extends AppCompatActivity {


    private MediaPlayer mediaPlayer;
    TextView text;
    private FirebaseSystem mFirebaseSystem;
    String roomTitle;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);
        Intent in=getIntent();
        mFirebaseSystem = FirebaseSystem.getInstance(getApplicationContext());
        String start= in.getStringExtra("Time2");
        roomTitle = in.getStringExtra("chatTitle2");
        userId = in.getStringExtra("myUserId");

        //Log.d("AlarmRinging", "Title" +roomTitle + "userId" + userId);
        text=(TextView)findViewById(R.id.textTime);

        text.setText(start);
        // 알람음 재생
        this.mediaPlayer = MediaPlayer.create(this,R.raw.ouu);
        this.mediaPlayer.start();

        findViewById(R.id.btnClose).setOnClickListener(mClickListener);
        mFirebaseSystem.initializeWakeUpState(roomTitle);           // 다 false 로 바꿔줌.
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // MediaPlayer release
        if (this.mediaPlayer != null) {
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
    }

    /* 알람 종료 */
    private void close() {
        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }

        finish();
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnClose:
                    // 알람 종료
                    Intent intent = new Intent(getApplicationContext(), SensorService.class);
                    intent.putExtra("roomTitle", roomTitle);
                    intent.putExtra("userId", userId);
                    startService(intent);
                    close();
                    break;
            }
        }
    };
}
