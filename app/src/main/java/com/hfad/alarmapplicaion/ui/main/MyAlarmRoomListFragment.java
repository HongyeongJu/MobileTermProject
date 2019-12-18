package com.hfad.alarmapplicaion.ui.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hfad.alarmapplicaion.AlarmRoomActivity;
import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.MainActivity;
import com.hfad.alarmapplicaion.R;
import com.hfad.alarmapplicaion.adapter.AlarmRoomListAdapter;
import com.hfad.alarmapplicaion.model.ChatRoom;
import com.hfad.alarmapplicaion.model.RoomPeople;
import com.hfad.alarmapplicaion.model.User;

import java.util.ArrayList;

// 내가 가입한 알람룸 리스트를 보여주는 창
public class MyAlarmRoomListFragment extends Fragment implements ListView.OnItemClickListener , UpdateListView{

    private ListView m_ListView;
    private ArrayList<ChatRoom> chats =new ArrayList<>();
    private AlarmRoomListAdapter adapter;
    private User myUserInfo;
    private FirebaseSystem mFirebaseSystem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_myalarmroom_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // layout xml 파일에 정의된 ListView의 객체
        m_ListView = view.findViewById(R.id.list);


        // 현재 유저 정보 받기
        myUserInfo = ((MainActivity)getActivity()).myUserInfo;
        adapter = new AlarmRoomListAdapter(getContext(), R.layout.alarmlistitem, chats);

        IntentFilter filter = new IntentFilter();
        filter.addAction("myAlarmList");
        getContext().registerReceiver(alarmListReceiver, filter);


        mFirebaseSystem = FirebaseSystem.getInstance(getContext());
        mFirebaseSystem.getMyAlarmRoomList(myUserInfo);

        // 리스트 뷰 세팅
        m_ListView.setAdapter(adapter);
        m_ListView.setOnItemClickListener(this);
        //registerForContextMenu(m_ListView);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Test", "onResume() 불려짐");
    }

    private BroadcastReceiver alarmListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("myAlarmList")){
                chats = (ArrayList<ChatRoom>)intent.getSerializableExtra("myAlarmList");
                adapter = new AlarmRoomListAdapter(getContext(), R.layout.alarmlistitem, chats);
                m_ListView.setAdapter(adapter);
                Log.i("myAlarmList22", "불려짐" + String.valueOf(chats.size()));
            }
        }
    };

    //플로팅 콘텍스트 메뉴를 생성 메소드 만들기
    // 리스트를 롱 클릭했을 때 출력이 되도록함.
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        //지금 롱 콘텍스트 메뉴에서 선택한 아이템 리스트의 위치값을 받아내기
        AdapterView.AdapterContextMenuInfo menuIn = (AdapterView.AdapterContextMenuInfo)menuInfo;

        ChatRoom chatRoom = chats.get(menuIn.position);     // 채팅 룸 객체로 받아오기
        //내가 방장이라면
        if(chatRoom.owner.equals(myUserInfo.id)){
            menu.setHeaderTitle("방장메뉴");
            menu.add(2,0,0, "삭제");
            menu.add(2,1,0, "수정");
        }else {     //내가 일반 회원이라면
            boolean isHere = false;
            menu.setHeaderTitle("회원메뉴");
            ArrayList<RoomPeople> peoples = (ArrayList<RoomPeople>) chatRoom.peoples;
            for(RoomPeople people : peoples){
                if(people.id.equals(myUserInfo.id)){
                    isHere = true;      // 지금 이 회원은 가입되어있는 상태이다.
                }
            }
            if(isHere){
                menu.add(3,1,0, "탈퇴");
            }else {
                menu.add(3,0,0, "가입");
            }
        }
    }

    /*
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = menuInfo.position;
        ChatRoom chat = chats.get(position);

        // 누른 user가 방장이면
        //방장메뉴를 출력
        if(item.getGroupId() == 2) {
            //삭제 버튼
            if(item.getItemId() == 0){
                mFirebaseSystem.deleteAlarmRoom(chat, myUserInfo);
                updateListView();
            }else if(item.getItemId() == 1) {
                Intent intent = new Intent(getContext(), AlarmUpdateActivity.class);
                intent.putExtra("chat", chat);
                startActivityForResult(intent, 100);
            }
        }else if(item.getGroupId() == 3) {  // 일반 회원메뉴
            if(item.getItemId() == 0) {
                // 가입 버튼
                mFirebaseSystem.addRoomMemberFromAlarmRoom(chat, myUserInfo);
            }else if(item.getItemId() == 1) {
                mFirebaseSystem.deleteRoomMemeberFromAlarmRoom(chat, myUserInfo);
            }
        }

        return super.onContextItemSelected(item);
    }

     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(adapter.getCount() > 0){
            ChatRoom chat = (ChatRoom)chats.get(position);

            Intent intent = new Intent(getContext(), AlarmRoomActivity.class);
            intent.putExtra("chatRoom", chat);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == Activity.RESULT_OK) {
            updateListView();
        }else if(requestCode == 200 && resultCode == Activity.RESULT_OK) {
            updateListView();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getContext().unregisterReceiver(alarmListReceiver);
    }

    public void updateListView() {mFirebaseSystem.getMyAlarmRoomList(myUserInfo);}

}
