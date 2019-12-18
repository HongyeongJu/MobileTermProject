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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hfad.alarmapplicaion.AlarmRoomActivity;
import com.hfad.alarmapplicaion.AlarmSettingActivity;
import com.hfad.alarmapplicaion.AlarmUpdateActivity;
import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.MainActivity;
import com.hfad.alarmapplicaion.R;
import com.hfad.alarmapplicaion.adapter.AlarmRoomListAdapter;
import com.hfad.alarmapplicaion.model.ChatRoom;
import com.hfad.alarmapplicaion.model.RoomPeople;
import com.hfad.alarmapplicaion.model.User;

import java.util.ArrayList;


// 프레그먼트에서 서비스에 유저 객체 정보전달 시도 -> 실패 한듯??
public class AlarmListFragment extends Fragment implements ListView.OnItemClickListener , UpdateListView {


    ListView listView;      // 화면에 보여질 리스트뷰
    Button addButton;       // 추가 버튼
    FirebaseSystem mFirebaseSystem;     // 파이어 베이스 시스템 객체
    ArrayList<ChatRoom> chats = new ArrayList<>();          // 브로드 캐스트 리시버로 파이어베이스 시스템에서 intent로 받을 arrayList
    AlarmRoomListAdapter adapter;       // 리스트뷰에 적용할 어뎁터
    User myUserInfo;            // 현재 유저 정보

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_alarm_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addButton = view.findViewById(R.id.addAlarmButton);
        listView = view.findViewById(R.id.alarmList);

        myUserInfo = ((MainActivity)getActivity()).myUserInfo;      // 현재 유저 정보 받기

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AlarmSettingActivity.class);
                startActivityForResult(intent, 200);
            }
        });
        adapter = new AlarmRoomListAdapter(getContext(), R.layout.alarmlistitem, chats);


        // 브로드 캐스트 리시버를 등록해서 파이어베이스 시스템으로부터 알림방의 리스트를 받아온다.
        IntentFilter filter = new IntentFilter();
        filter.addAction("getAlarmList");
        filter.addAction("putUser");
        getContext().registerReceiver(alarmListReceiver, filter);

        //파이어베이스
        mFirebaseSystem = FirebaseSystem.getInstance(getContext());
        mFirebaseSystem.getAlarmRoomList();         // 파이어베이스로 부터 Broadcast로 알람방 리스트의 데이터를 받아오는 메소드
        mFirebaseSystem.getMyAlarmRoomList(myUserInfo);         // 현재 내가 참여한 알람방의 list를 세션 서비스에 받도록한다.

        listView.setAdapter(adapter);       // 어뎁터 설정한다.
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);



    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //mFirebaseSystem.getMyAlarmRoomList(myUserInfo);         // 현재 내가 가입한 알람룸 리스트 갱신한다.  -> 좀 아닌듯하다.

    }

    // 리스트의 개별 아이템을 선택했을 때 호출되는 메소드
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("adapterSize", String.valueOf(adapter.getCount()));
        if(adapter.getCount() >0){
            //ChatRoom chat = (ChatRoom)adapter.getItem(position);

            ChatRoom chat = (ChatRoom)chats.get(position);

            Intent intent = new Intent(getContext(), AlarmRoomActivity.class);
            intent.putExtra("chatRoom", chat);
            startActivity(intent);
        }

    }

    // 알람방 리스트를 받기위한 브로드캐스트 리시버
    private BroadcastReceiver alarmListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("getAlarmList")){
              //  Toast.makeText(getContext(),"브로드캐스트 리시버 실행", Toast.LENGTH_SHORT).show();
                chats = (ArrayList<ChatRoom>)intent.getSerializableExtra("alarmList");      // ArrayList 데이터를 받는다.
                adapter = new AlarmRoomListAdapter(getContext(), R.layout.alarmlistitem, chats);        // 새롭게 ArrayList 어뎁터를 만들어서
                listView.setAdapter(adapter);// 새롭게 listView에 어댑터를 적용한다.

                //Log.d("getAlarmList", "불려짐");
            }else if(action.equals("putUser")){
                myUserInfo = (User)intent.getSerializableExtra("user");
            }
            //Log.d("adapterSize", String.valueOf(adapter.getCount()));
        }
    };


    // 플로팅 콘텍스트 메뉴를 생성 메소드 만들기
    // 리스트를 롱 클릭했을 때 출력이 되도록한다.
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        try{
            // 지금 롱 콘텍스트 메뉴에서 선택한 아이템 리스트의 위치값을 받아내기
            AdapterView.AdapterContextMenuInfo menuIn = (AdapterView.AdapterContextMenuInfo)menuInfo;
            Log.d("Loginfo id:" , String.valueOf(menuIn.position));     // 위치값 찾아보기.

            ChatRoom chatRoom  = chats.get(menuIn.position);            // 채팅 룸의 객체로 받아오기

            //내가 방장이라면
            if(chatRoom.owner.equals(myUserInfo.id)){
                menu.setHeaderTitle("방장메뉴");
                menu.add(0,0,0,"삭제");
                menu.add(0,1,0,"수정");
            }else { // 내가 일반 회원이라면
                boolean isHere = false;
                menu.setHeaderTitle("회원메뉴");
                ArrayList<RoomPeople> peoples = (ArrayList<RoomPeople>) chatRoom.peoples;
                for(RoomPeople people : peoples){       // 가입된 회원들을 검사해서. 지금 회원이 가입을 했는지 체크
                    if(people.id.equals(myUserInfo.id)){
                        isHere = true;          // 지금 이 회원은 가입되어있는 상태이다.
                    }
                }
                if(isHere){     // 가입되어있는 상태이면 탈퇴를.
                    menu.add(1,1,0,"탈퇴");
                }else {         // 가입이 안된 상태라면 가입을 한다.
                    menu.add(1, 0, 0, "가입");
                }

            }
        }catch(Exception e){
            Intent intent = new Intent("getUser"); // 다시 자신의 유저정보를 받아온다.
            getActivity().sendBroadcast(intent);
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = menuInfo.position;       // 눌려진 위치
        ChatRoom chat = chats.get(position);

        // 누른 user가 방장이면
        // 방장메뉴를 출력
        if(item.getGroupId() == 0){
            // 삭제 버튼
            if(item.getItemId() == 0) {
                mFirebaseSystem.deleteAlarmRoom(chat, myUserInfo);
                updateListView();
            }else if(item.getItemId() == 1){        // 수정 버튼
                Intent intent = new Intent(getContext(), AlarmUpdateActivity.class);
                intent.putExtra("chat", chat);
                startActivityForResult(intent, 100);
            }
        }else if(item.getGroupId() == 1){// 일반 회원메뉴
            if(item.getItemId() == 0){      // 가입 버튼
                mFirebaseSystem.addRoomMemberFromAlarmRoom(chat, myUserInfo);
            }else if(item.getItemId()== 1){ // 탈퇴 버튼
                mFirebaseSystem.deleteRoomMemeberFromAlarmRoom(chat, myUserInfo);
            }
        }

        return super.onContextItemSelected(item);
    }

    // 수정 액티비티에서 수정이 다 끝나면 리스트뷰 갱신작업을 해준다.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            updateListView();
        }else if(requestCode == 200 && resultCode == Activity.RESULT_OK){
            updateListView();
            //Log.d("updateListView", "updateListView");
        }
    }

    public void updateListView(){
        mFirebaseSystem.getAlarmRoomList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getContext().unregisterReceiver(alarmListReceiver);
    }
}
