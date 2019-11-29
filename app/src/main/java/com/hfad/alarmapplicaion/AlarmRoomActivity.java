package com.hfad.alarmapplicaion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hfad.alarmapplicaion.adapter.GroupMemberListAdapter;
import com.hfad.alarmapplicaion.model.ChatRoom;
import com.hfad.alarmapplicaion.model.RoomPeople;

import java.util.ArrayList;

public class AlarmRoomActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    ArrayList<RoomPeople> members;
    ListView listView;
    TextView mAlarmRoomTitle;
    TextView mAlarmRoomTime;
    Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_room);

        listView = (ListView)findViewById(R.id.grouplistview);
        Intent intent = getIntent();
        ChatRoom chat = (ChatRoom)intent.getSerializableExtra("chatRoom");

        mAlarmRoomTime = (TextView)findViewById(R.id.alarmRoomTitle);
        mAlarmRoomTitle =(TextView)findViewById(R.id.alarmTime);
        mBackButton = (Button)findViewById(R.id.backbutton);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAlarmRoomTitle.setText(chat.roomTitle);
        mAlarmRoomTime.setText(chat.hour + ":" + chat.minute);

        members = (ArrayList<RoomPeople>)chat.peoples;

        GroupMemberListAdapter adapter = new GroupMemberListAdapter(getApplicationContext(), R.layout.member_item_list, members);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    // 리스트 아이템을 눌렀을때 호출되는 함수
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RoomPeople people = members.get(position);
        String phoneNumber =people.phone;
        if(phoneNumber != null){
            // 누르면 해당 사용자에게 전화걸기
            String tel = "tel:" + people.phone;
            Intent intent = new Intent("android.intent.action.DIAL", Uri.parse(tel));
            startActivity(intent);
        }else {
            Toast.makeText(getApplicationContext(), "폰 번호가 없습니다.", Toast.LENGTH_SHORT).show();
        }

    }



}
