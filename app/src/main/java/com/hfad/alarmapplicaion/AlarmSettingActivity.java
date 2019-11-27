package com.hfad.alarmapplicaion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmSettingActivity extends AppCompatActivity implements View.OnClickListener{


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

    boolean[] days;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

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


        days = new boolean[7];
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.monday:
                days[0]= true;
                break;
            case R.id.tuesday:
                days[1]= true;
                break;
            case R.id.monday:
                days[0]= true;
                break;
            case R.id.monday:
                days[0]= true;
                break;

        }
    }
}
