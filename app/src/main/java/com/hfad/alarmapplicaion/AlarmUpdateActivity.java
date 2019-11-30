package com.hfad.alarmapplicaion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.model.ChatRoom;
import com.hfad.alarmapplicaion.model.User;

// 알람방의 정보를 수정할 수 있는 화면
public class AlarmUpdateActivity extends AppCompatActivity implements View.OnClickListener , CompoundButton.OnCheckedChangeListener, TimePicker.OnTimeChangedListener{

    User myUserInfo;
    TextView mRoomName;
    TimePicker mTimePicker;
    CheckBox mMonday;
    CheckBox mTuesday;
    CheckBox mWednesday;
    CheckBox mThursday;
    CheckBox mFriday;
    CheckBox mSaturday;
    CheckBox mSunday;
    Button mSettingButton;
    ChatRoom chatRoom;

    int hourOfDay = 0;
    int minute = 0;
    private FirebaseSystem mFirebaseSystem;

    boolean[] days = new boolean[7];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_update);

        chatRoom = (ChatRoom)getIntent().getSerializableExtra("chat");       // 알람방 정보를 가져온다.

        mFirebaseSystem = FirebaseSystem.getInstance(getApplicationContext());
        mRoomName = (TextView)findViewById(R.id.roomName);
        mTimePicker=  (TimePicker)findViewById(R.id.timePicker);
        mMonday = (CheckBox)findViewById(R.id.monday);
        mTuesday = (CheckBox)findViewById(R.id.tuesday);
        mWednesday = (CheckBox)findViewById(R.id.wednesday);
        mThursday = (CheckBox)findViewById(R.id.thursday);
        mFriday = (CheckBox)findViewById(R.id.friday);
        mSaturday = (CheckBox)findViewById(R.id.saturday);
        mSunday = (CheckBox)findViewById(R.id.sunday);
        mSettingButton = (Button)findViewById(R.id.settingbutton);

        mMonday.setOnCheckedChangeListener(this);
        mTuesday.setOnCheckedChangeListener(this);
        mWednesday.setOnCheckedChangeListener(this);
        mThursday.setOnCheckedChangeListener(this);
        mFriday.setOnCheckedChangeListener(this);
        mSaturday.setOnCheckedChangeListener(this);
        mSunday.setOnCheckedChangeListener(this);
        mSettingButton.setOnClickListener(this);

        mTimePicker.setHour(chatRoom.hour);
        mTimePicker.setMinute(chatRoom.minute);
        mTimePicker.setOnTimeChangedListener(this);
        mRoomName.setText(chatRoom.roomTitle);

        mMonday.setChecked(chatRoom.monday);
        mTuesday.setChecked(chatRoom.tuesday);
        mWednesday.setChecked(chatRoom.wednesday);
        mThursday.setChecked(chatRoom.thursday);
        mFriday.setChecked(chatRoom.friday);
        mSaturday.setChecked(chatRoom.saturday);
        mSunday.setChecked(chatRoom.sunday);

        hourOfDay = chatRoom.hour;
        minute = chatRoom.minute;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.monday:
                days[0] = isChecked;
                break;
            case R.id.tuesday:
                days[1] = isChecked;
                break;
            case R.id.wednesday:
                days[2] = isChecked;
                break;
            case R.id.thursday:
                days[3] = isChecked;
                break;
            case R.id.friday:
                days[4] = isChecked;
                break;
            case R.id.saturday:
                days[5] = isChecked;
                break;
            case R.id.sunday:
                days[6] = isChecked;
                break;
        }
    }

    // 타임피커가 바뀌면 자동으로 갱신
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.settingbutton){
            int hour = hourOfDay;
            int min = minute;

            mFirebaseSystem.updateAlarmRoom(chatRoom, hour, min, days);
            Toast.makeText(getApplicationContext(), "알람방 시간을 바꾸었습니다.", Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);
            finish();
        }
    }
}
