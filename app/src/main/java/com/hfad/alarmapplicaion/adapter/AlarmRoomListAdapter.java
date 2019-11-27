package com.hfad.alarmapplicaion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hfad.alarmapplicaion.R;
import com.hfad.alarmapplicaion.model.ChatRoom;

import java.util.ArrayList;

public class AlarmRoomListAdapter extends ArrayAdapter<ChatRoom> {

    Context context;
    int resId;
    ArrayList<ChatRoom> datas;

    public AlarmRoomListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ChatRoom> datas) {
        super(context, resource);

        this.context = context;
        this.resId = resource;
        this.datas = datas;
    }

    @Override
    public int getCount(){
        return datas.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);
            AlarmRoomHolder holder = new AlarmRoomHolder(convertView);
            convertView.setTag(holder);
        }

        AlarmRoomHolder holder = (AlarmRoomHolder)convertView.getTag();

        TextView alarmRoomTitleView = holder.titleView;
        TextView alarmRoomTimeView = holder.timeView;

        final ChatRoom data = datas.get(position);

        alarmRoomTitleView.setText(data.roomTitle);
        alarmRoomTimeView.setText(data.hour + ":" + data.minute);

        return convertView;
    }


    class AlarmRoomHolder{
        public TextView titleView;
        public TextView timeView;

        public AlarmRoomHolder(View root){
            titleView = (TextView)root.findViewById(R.id.alarmListItemTitle);
            timeView = (TextView)root.findViewById(R.id.alarmListItemTime);
        }
    }
}
