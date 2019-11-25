package com.hfad.alarmapplicaion.ui.main;

import android.content.Intent;
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
import com.hfad.alarmapplicaion.R;
import com.hfad.alarmapplicaion.adapter.AlarmRoomListAdapter;
import com.hfad.alarmapplicaion.model.AlarmRoom;

import java.util.ArrayList;

public class AlarmListFragment extends Fragment implements ListView.OnItemClickListener {


    ListView listView;
    Button addButton;

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

        ArrayList<AlarmRoom> list = new ArrayList<>();

        list.add(new AlarmRoom("알람방1", "12:00PM"));
        list.add(new AlarmRoom("알람방2", "13:00AM"));

        AlarmRoomListAdapter adapter = new AlarmRoomListAdapter(getContext(), R.layout.alarmlistitem, list);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(this);
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
}
