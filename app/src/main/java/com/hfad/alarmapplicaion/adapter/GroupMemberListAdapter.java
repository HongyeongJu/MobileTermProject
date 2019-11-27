package com.hfad.alarmapplicaion.adapter;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hfad.alarmapplicaion.R;
import com.hfad.alarmapplicaion.model.RoomPeople;

import java.util.ArrayList;

public class GroupMemberListAdapter extends ArrayAdapter<RoomPeople>
{
    Context context;
    int resId;
    ArrayList<RoomPeople> datas;
    public GroupMemberListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RoomPeople> datas) {
        super(context, resource, datas);

        this.context = context;
        this.resId = resource;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        Log.d("datasSize", String.valueOf(datas.size()));

        return datas.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);
            GroupMemeberHolder holder = new GroupMemeberHolder(convertView);
            convertView.setTag(holder);
        }

        GroupMemeberHolder holder=  (GroupMemeberHolder)convertView.getTag();

        ImageView photoImageView = holder.imageView;
        TextView nameView = holder.nameView;
      //  TextView stateMessageView = holder.stateMessageView;
        View isWakeView = holder.isWakeView;

        RoomPeople data = datas.get(position);

        //gender가 false면 남자
        if(data.gender){
            photoImageView.setImageResource(R.drawable.man);
        }else{// true면 여자
            photoImageView.setImageResource(R.drawable.woman);
        }
        nameView.setText(data.id);  // 일단 이름이아니라 id를 사용
        //stateMessageView.setText(data.stateMessage);  상태메시지
        if(data.isTurnOff){
            isWakeView.setBackgroundColor(Color.BLUE);
        }else
        {
            isWakeView.setBackgroundColor(Color.RED);
        }

        return convertView;
    }

    class GroupMemeberHolder {
        public ImageView imageView;
        public TextView nameView;
      //  public TextView stateMessageView;
        public View isWakeView;

        public GroupMemeberHolder(View root){
            imageView = (ImageView)root.findViewById(R.id.photoimageview);
            nameView = (TextView)root.findViewById(R.id.nametextview);
        //    stateMessageView = (TextView)root.findViewById(R.id.statemessagetextview);
            isWakeView = (View)root.findViewById(R.id.iswakeview);
        }
    }
}