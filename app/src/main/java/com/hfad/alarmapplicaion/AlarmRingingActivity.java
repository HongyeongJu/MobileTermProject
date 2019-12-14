package com.hfad.alarmapplicaion;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmRingingActivity extends AppCompatActivity {


    private MediaPlayer mediaPlayer;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);
        Intent in=getIntent();
        String start= in.getStringExtra("Time2");
        text=(TextView)findViewById(R.id.textTime);

        text.setText(start);
        // 알람음 재생
        this.mediaPlayer = MediaPlayer.create(this,R.raw.ouu);
        this.mediaPlayer.start();

        findViewById(R.id.btnClose).setOnClickListener(mClickListener);

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
                    close();

                    break;
            }
        }
    };
}
