package com.hfad.alarmapplicaion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hfad.alarmapplicaion.adapter.GroupMemberListAdapter;
import com.hfad.alarmapplicaion.model.ChatRoom;
import com.hfad.alarmapplicaion.model.RoomPeople;

import java.util.ArrayList;

public class AlarmRoomActivity extends AppCompatActivity {

    ArrayList<RoomPeople> members;
    ListView listView;
    TextView mAlarmRoomTitle;
    TextView mAlarmRoomTime;
    Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_room);

       /*
        listView = (ListView)findViewById(R.id.grouplistview);

        members = new ArrayList<>();

        members.add(new GroupMember(R.drawable.man, "홍영주", "일어나기싫어", false));
        members.add(new GroupMember(R.drawable.woman, "이서영", "좋아", true));
        members.add(new GroupMember(R.drawable.woman, "백송희", "가자", true));
        members.add(new GroupMember(R.drawable.man, "오동현", "일어나볼까?", true));

        GroupMemberListAdapter adapter = new GroupMemberListAdapter(getApplicationContext(), R.layout.member_item_list, members);
        listView.setAdapter(adapter);
*/

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
    }
}
