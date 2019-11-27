package com.hfad.alarmapplicaion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmSettingActivity extends AppCompatActivity implements View.OnClickListener , CompoundButton.OnCheckedChangeListener, TimePicker.OnTimeChangedListener {


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

    boolean[] days = new boolean[7];
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

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.settingbutton){
            Toast.makeText(getApplicationContext(), "hour"  +String.valueOf(hourOfDay) ,Toast.LENGTH_SHORT).show();
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
}
