package com.hfad.alarmapplicaion.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hfad.alarmapplicaion.AlarmRoomActivity;
import com.hfad.alarmapplicaion.AlarmSettingActivity;
import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.R;
import com.hfad.alarmapplicaion.adapter.AlarmRoomListAdapter;
import com.hfad.alarmapplicaion.model.ChatRoom;

import java.util.ArrayList;

public class AlarmListFragment extends Fragment implements ListView.OnItemClickListener {


    ListView listView;      // 화면에 보여질 리스트뷰
    Button addButton;       // 추가 버튼

    FirebaseSystem mFirebaseSystem;     // 파이어 베이스 시스템 객체

    ArrayList<ChatRoom> chats = new ArrayList<>();          // 브로드 캐스트 리시버로 파이어베이스 시스템에서 intent로 받을 arrayList

    AlarmRoomListAdapter adapter;       // 리스트뷰에 적용할 어뎁터

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_alarm_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addButton = view.findViewById(R.id.addAlarmButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ㅇㅇ", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(getContext(), AlarmSettingActivity.class);
                startActivity(intent);
            }
        });
        listView = view.findViewById(R.id.alarmList);
        listView.setOnItemClickListener(this);
        adapter = new AlarmRoomListAdapter(getContext(), R.layout.alarmlistitem, chats);


        // 브로드 캐스트 리시버를 등록해서 파이어베이스 시스템으로부터 알림방의 리스트를 받아온다.
        IntentFilter filter = new IntentFilter();
        filter.addAction("getAlarmList");
        getContext().registerReceiver(alarmListReceiver, filter);

        //파이어베이스
        mFirebaseSystem = FirebaseSystem.getInstance(getContext());
        mFirebaseSystem.getAlarmRoomList();         // 파이어베이스로 부터 Broadcast로 알람방 리스트의 데이터를 받아오는 메소드

        listView.setAdapter(adapter);       // 어뎁터 설정한다.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), AlarmRoomActivity.class);
        startActivity(intent);
    }

    private BroadcastReceiver alarmListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("getAlarmList")){
                chats = (ArrayList<ChatRoom>)intent.getSerializableExtra("alarmList");      // ArrayList 데이터를 받는다.
                adapter = new AlarmRoomListAdapter(getContext(), R.layout.alarmlistitem, chats);        // 새롭게 ArrayList 어뎁터를 만들어서
                listView.setAdapter(adapter);                                                   // 새롭게 listView에 어댑터를 적용한다.
            }
        }
    };
}
