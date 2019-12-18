package com.hfad.alarmapplicaion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.model.ChatRoom;
import com.hfad.alarmapplicaion.model.RoomPeople;
import com.hfad.alarmapplicaion.model.User;

import java.util.ArrayList;
import java.util.List;

public class AlarmSettingActivity extends AppCompatActivity implements View.OnClickListener , CompoundButton.OnCheckedChangeListener, TimePicker.OnTimeChangedListener {


    User myUserInfo;

    EditText mRoomName;
    TimePicker mTimePicker;
    CheckBox mMonday;
    CheckBox mTuesday;
    CheckBox mWednesday;
    CheckBox mThursday;
    CheckBox mFriday;
    CheckBox mSaturday;
    CheckBox mSunday;
    Button mSettingButton;

    int hourOfDay = 0;
    int minute = 0;

    private FirebaseSystem mFirebaseSystem;

    boolean[] days = new boolean[7];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        mFirebaseSystem = FirebaseSystem.getInstance(getApplicationContext());
        mRoomName = (EditText)findViewById(R.id.roomName);
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

        mTimePicker.setHour(hourOfDay);
        mTimePicker.setMinute(minute);
        mTimePicker.setOnTimeChangedListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("putUser");
        registerReceiver(getUserReceiver, filter);


        Intent getUserIntent = new Intent("getUser");
        sendBroadcast(getUserIntent);       // 현재 유저 정보를 업데이트 하라고 부탁함.

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.settingbutton ){
            String title = mRoomName.getText().toString();
            String tempTitle = title.replace(" ", "");
            if(!tempTitle.equals("")){
                int hour = hourOfDay;
                int min = minute;
                String owner = myUserInfo.id;
                String phoneNumber = myUserInfo.phoneNumber;
                List<RoomPeople> people= new ArrayList<>();
                people.add(new RoomPeople(owner, false, myUserInfo.gender, phoneNumber));       // 방장은 무조건 참여하게된다.
                //people.add(new RoomPeople("테스트", false, true, "000-0000-0000"));

                ChatRoom chatRoom = new ChatRoom(title, hour, min, owner, people, days[0], days[1], days[2], days[3], days[4], days[5], days[6]);

                mFirebaseSystem.addChatRoom(chatRoom);

                setResult(RESULT_OK);
                finish();
            }else {
                Toast.makeText(getApplicationContext(), "방제목을 입력해주세요." , Toast.LENGTH_SHORT).show();
            }

        }
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

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }


    // 유저 정보를 받아오는 리시버
    BroadcastReceiver getUserReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

           // Toast.makeText(getApplicationContext(), "리시버 정상작동", Toast.LENGTH_SHORT).show();
            if(action == "putUser"){
                myUserInfo = (User)intent.getSerializableExtra("user");
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(getUserReceiver);
    }
}
