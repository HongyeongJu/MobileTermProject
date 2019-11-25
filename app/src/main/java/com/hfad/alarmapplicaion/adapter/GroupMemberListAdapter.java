package com.hfad.alarmapplicaion.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hfad.alarmapplicaion.R;
import com.hfad.alarmapplicaion.model.GroupMember;

import java.util.ArrayList;

public class GroupMemberListAdapter extends ArrayAdapter<GroupMember>
{
    Context context;
    int resId;
    ArrayList<GroupMember> datas;
    public GroupMemberListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<GroupMember> datas) {
        super(context, resource, datas);

        this.context = context;
        this.resId = resource;
        this.datas = datas;
    }

    @Override
    public int getCount() {
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
        TextView stateMessageView = holder.stateMessageView;
        View isWakeView = holder.isWakeView;

        GroupMember data = datas.get(position);

        photoImageView.setImageResource(data.imageUrl);
        nameView.setText(data.name);
        stateMessageView.setText(data.stateMessage);
        if(data.isWake){
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
        public TextView stateMessageView;
        public View isWakeView;

        public GroupMemeberHolder(View root){
            imageView = (ImageView)root.findViewById(R.id.photoimageview);
            nameView = (TextView)root.findViewById(R.id.nametextview);
            stateMessageView = (TextView)root.findViewById(R.id.statemessagetextview);
            isWakeView = (View)root.findViewById(R.id.iswakeview);
        }
    }
}