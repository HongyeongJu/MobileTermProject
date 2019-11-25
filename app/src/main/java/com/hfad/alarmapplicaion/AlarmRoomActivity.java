package com.hfad.alarmapplicaion;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.hfad.alarmapplicaion.adapter.GroupMemberListAdapter;
import com.hfad.alarmapplicaion.model.GroupMember;

import java.util.ArrayList;

public class AlarmRoomActivity extends AppCompatActivity {

    ArrayList<GroupMember> members;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_room);
        listView = (ListView)findViewById(R.id.grouplistview);

        members = new ArrayList<>();

        members.add(new GroupMember(R.drawable.man, "홍영주", "일어나기싫어", false));
        members.add(new GroupMember(R.drawable.woman, "이서영", "좋아", true));
        members.add(new GroupMember(R.drawable.woman, "백송희", "가자", true));
        members.add(new GroupMember(R.drawable.man, "오동현", "일어나볼까?", true));

        GroupMemberListAdapter adapter = new GroupMemberListAdapter(getApplicationContext(), R.layout.member_item_list, members);
        listView.setAdapter(adapter);

    }
}
