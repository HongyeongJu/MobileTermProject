package com.hfad.alarmapplicaion.DatabaseSystem;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.hfad.alarmapplicaion.MainActivity;
import com.hfad.alarmapplicaion.model.ChatRoom;
import com.hfad.alarmapplicaion.model.RoomPeople;
import com.hfad.alarmapplicaion.model.Shop;
import com.hfad.alarmapplicaion.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Firebase에 대한 접근과 이에 대한 함수 제공.
public class FirebaseSystem  {

    private static FirebaseDatabase mFirebaseDatabase;      // 파이어베이스 객체
    private static DatabaseReference mUsersDatabaseReference;       // User테이블 객체
    private static DatabaseReference mChatRoomDatabaseReference;        // 채팅방 객체
    private static DatabaseReference mShopDatabaseReference;        // 상점 테이블 객체
    private static FirebaseSystem firebaseSystem;           // 싱글톤 패턴으로 현재 클래스 객체
    private static Context mContext;

    private User myUserInfo;
    private boolean isuserId = false;
    private User tempUser;      // 임시로 User데이터 조작을 위한 객체


    private FirebaseSystem(){

    }

    public static synchronized FirebaseSystem getInstance(Context context) {
        if(firebaseSystem == null){
            firebaseSystem = new FirebaseSystem();      // 현재 firebaseSystem 객체 받아온다.
            mFirebaseDatabase = FirebaseDatabase.getInstance();     // Firebase 객체 얻기
            mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");      // 주소 얻기
            mChatRoomDatabaseReference = mFirebaseDatabase.getReference().child("chatroom");
            mShopDatabaseReference = mFirebaseDatabase.getReference().child("shop");
            mContext = context;
        }

        return firebaseSystem;
    }

    // 해당 id가 서버에 등록되어 있는지 확인하는 메소드
    public boolean isId(final String id){

        isuserId = false;
        // User데이터 베이스에서
        mUsersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String postId = postSnapshot.getKey();
                    Log.d("postId", "postId : " + postId  + " id : " + id);
                    if(postId.equals(id)){
                        Log.d("postId", "같음");
                        isuserId = true;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Log.d("isuserId", String.valueOf(isuserId));
        return isuserId;
    }

// 로그인 하는 메소드
    public boolean login(String id, final String password){
        mUsersDatabaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){      // id가 있는지 없는지 확인한다.
                    // 비밀번호가 같은지 확인한다.
                    User user = dataSnapshot.getValue(User.class);
                    if(user!= null){
                        Log.d("password", user.password);
                        if(user.password.equals(password)){

                            // 유저 정보를 넘겨주면서 메인 액티비티로 이동
                            Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("user", user);
                            mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


                        }else {
                            Toast.makeText(mContext, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else { // Id가 없다면.
                    Toast.makeText(mContext, "Id가 없습니다.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return true;
    }


    private boolean isId;
    // 회원을 추가하는 메소드
    public boolean addUser(final User user)  {
        final String id = user.id;

        mUsersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)  {
                isId = false;

                for(DataSnapshot postSnapShot : dataSnapshot.getChildren())
                {
                    String postId = postSnapShot.getKey();  // id받아내기.
                    if(postId.equals(id)){      //서버에 등록된 id가 현재 만들고자하는 id랑 같을경우 예외발생
                        isId = true;        // ID가 있음.
                        Toast.makeText(mContext, "ID가 현재 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                if(!isId) {      // 아이디가 없으면 유저 아이디를 추가한다.
                    mUsersDatabaseReference.child(id).setValue(user);
                    Toast.makeText(mContext, "아이디가 등록되었습니다.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return !isId;        //성공하면 true 실패하면 false를 출력
    }


    // 알람룸 리스트를 갱신하는 메소드
    public void getAlarmRoomList(){

        /*
        mChatRoomDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ChatRoom> chats = new ArrayList<>();
                for(DataSnapshot postSnapshot :dataSnapshot.getChildren()){
                    ChatRoom chatRoom = (ChatRoom)postSnapshot.getValue(ChatRoom.class);

                    //Log.d("chatRoom", chatRoom.roomTitle);
                    chats.add(chatRoom);
                }
                Intent intent = new Intent("getAlarmList");
                intent.putExtra("alarmList", chats);
                mContext.sendBroadcast(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
        mChatRoomDatabaseReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                ArrayList<ChatRoom> chats = new ArrayList<>();
                for(MutableData postSnapShot : mutableData.getChildren()){
                    ChatRoom chatRoom = (ChatRoom)postSnapShot.getValue(ChatRoom.class);

                    // Log.d("chatRoom", chatRoom.roomTitle);
                    chats.add(chatRoom);            // 채팅 방 추가
                }
                Intent intent = new Intent("getAlarmList");
                intent.putExtra("alarmList", chats);
                mContext.sendBroadcast(intent);                 // 브로드 캐스트를 보내서 AlarmListFragment에서 리스트뷰 갱신을 하라고 명령한다.

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }


        });
    }

    // 알람룸을 추가하는 메소드
    public void addChatRoom(final ChatRoom chatRoom){
        final String id = chatRoom.roomTitle;


        mChatRoomDatabaseReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                isId = false;
                long room_number = 0;

                for(MutableData postSnapshot : mutableData.getChildren()){
                    String postId = postSnapshot.getKey();      //id 받아내기

                    //number는 각 알람방의 고유 번호이다.
                    ChatRoom chatRoom1 = postSnapshot.getValue(ChatRoom.class);
                    // 각 원소들의 number를 받아와서 . 비교를 한다.
                    if(room_number <= chatRoom1.number){
                        room_number = chatRoom1.number+1;
                    }

                    if(postId.equals(id)){ // 서버에 등록된 알람방 id가 현재 만들고자하는 id랑 같을경우 .예외발생
                        isId = true;
                        Toast.makeText(mContext, "같은 방제목을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                if(!isId){
                    chatRoom.setNumber(room_number);
                    mutableData.child(id).setValue(chatRoom);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                Toast.makeText(mContext,"방이 등록되었습니다. " ,Toast.LENGTH_SHORT).show();
                updateMyAlarmLists();
            }
        });

    }

    // 알람룸 정보를 받고 그 알람룸 정보와 지금 현재 userInfo 정보로 알람룸 참가 리스트 삭제하는 메소드
    public void deleteRoomMemeberFromAlarmRoom(final ChatRoom chatRoom, final User myUserInfo){

        String chatRoomId = chatRoom.roomTitle;
        /*
        mChatRoomDatabaseReference.child(chatRoomId).child("peoples").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String index = null;

                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    RoomPeople people = postSnapShot.getValue(RoomPeople.class);
                    if(myUserInfo.id.equals(people.id)){        // 같다면.
                        index = postSnapShot.getKey();
                    }
                }
                if(index != null){// 해당 위치에 있는 회원 삭제
                    dataSnapshot.getRef().child(index).removeValue();   // 삭제
                    Toast.makeText(mContext, "탈퇴하였습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, "회원이 없습니다. ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

         */
        mChatRoomDatabaseReference.child(chatRoomId).child("peoples").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                String index = null;

                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    RoomPeople people = postSnapShot.getValue(RoomPeople.class);
                    if(myUserInfo.id.equals(people.id)){        // 같다면.
                        index = postSnapShot.getKey();
                    }
                }
                if(index != null){// 해당 위치에 있는 회원 삭제
                    dataSnapshot.getRef().child(index).removeValue();   // 삭제
                    Toast.makeText(mContext, "탈퇴하였습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, "회원이 없습니다. ", Toast.LENGTH_SHORT).show();
                }
                updateMyAlarmLists();
            }
        });

    }

    // 알람룸 정보와 유저 정보를 받고 알람룸에 멤버를 추가하는 메소드
    public void addRoomMemberFromAlarmRoom(final ChatRoom chatRoom, final User myUserInfo){
        String chatRoomId = chatRoom.roomTitle;

        /*
        mChatRoomDatabaseReference.child(chatRoomId).child("peoples").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isHere = false;
                String lastNumStr = null;
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    RoomPeople people = postSnapShot.getValue(RoomPeople.class);
                    if(myUserInfo.id.equals(people.id)){        // 같다면? (이 방에 이미 참여하고 있다는 뜻)
                        isHere = true;
                    }
                    lastNumStr = postSnapShot.getKey();
                }
                int lastNum = Integer.valueOf(lastNumStr);

                if(isHere){
                    Toast.makeText(mContext, "이미 이 방에 참여하고 있습니다.", Toast.LENGTH_SHORT).show();
                }else if(lastNum > 8){
                    Toast.makeText(mContext, "방인원수는 8명이 제한입니다.", Toast.LENGTH_SHORT).show();
                }else {
                    lastNum++;
                    RoomPeople people = new RoomPeople(myUserInfo.id, false, myUserInfo.gender, myUserInfo.phoneNumber);
                    dataSnapshot.getRef().child(String.valueOf(lastNum)).setValue(people);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

         */

        mChatRoomDatabaseReference.child(chatRoomId).child("peoples").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                boolean isHere = false;
                String lastNumStr = null;
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    RoomPeople people = postSnapShot.getValue(RoomPeople.class);
                    if(myUserInfo.id.equals(people.id)){        // 같다면? (이 방에 이미 참여하고 있다는 뜻)
                        isHere = true;
                    }
                    lastNumStr = postSnapShot.getKey();
                }
                int lastNum = Integer.valueOf(lastNumStr);

                if(isHere){
                    Toast.makeText(mContext, "이미 이 방에 참여하고 있습니다.", Toast.LENGTH_SHORT).show();
                }else if(lastNum > 8){
                    Toast.makeText(mContext, "방인원수는 8명이 제한입니다.", Toast.LENGTH_SHORT).show();
                }else {
                    lastNum++;
                    RoomPeople people = new RoomPeople(myUserInfo.id, false, myUserInfo.gender, myUserInfo.phoneNumber);
                    dataSnapshot.getRef().child(String.valueOf(lastNum)).setValue(people);
                }
                updateMyAlarmLists();
            }
        });
    }


    // 알람방 삭제하는 메소드
    public void deleteAlarmRoom(final ChatRoom chatRoom, final User myUserInfo){
        String chatRoomId = chatRoom.roomTitle;

        /*
        mChatRoomDatabaseReference.child(chatRoomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChatRoom chat = dataSnapshot.getValue(ChatRoom.class);
                if(chat.owner.equals(myUserInfo.id)){       // 자신이 방장이면
                    dataSnapshot.getRef().removeValue();
                    Toast.makeText(mContext, "방을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, "방장만 방을 없엘 수가 있습니다. ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

         */

        mChatRoomDatabaseReference.child(chatRoomId).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                ChatRoom chat = dataSnapshot.getValue(ChatRoom.class);
                if(chat.owner.equals(myUserInfo.id)){       // 자신이 방장이면
                    dataSnapshot.getRef().removeValue();
                    Toast.makeText(mContext, "방을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, "방장만 방을 없엘 수가 있습니다. ", Toast.LENGTH_SHORT).show();
                }
                updateMyAlarmLists();
            }
        });

    }

    public void updateAlarmRoom(final ChatRoom chatRoom, final int hour, final int min, final boolean[] days){
        String chatRoomId = chatRoom.roomTitle;

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("hour", hour);
        updateMap.put("minute", min);
        updateMap.put("monday", days[0]);
        updateMap.put("tuesday", days[1]);
        updateMap.put("wednesday", days[2]);
        updateMap.put("thursday", days[3]);
        updateMap.put("friday", days[4]);
        updateMap.put("saturday", days[5]);
        updateMap.put("sunday", days[6]);
        mChatRoomDatabaseReference.child(chatRoomId).updateChildren(updateMap);
    }

    // 현재 내가 참여하고 있는 알람룸 리스트를 브로드 캐스트로 전송한다.
    // 현재 참여 하고 있는 알람룸 리스트 반환 안됨?  -> 오류 발견 :  runTransaction에서는 Toast메시지 출력 불가.
    public void getMyAlarmRoomList(final User myUserInfo){
        final ArrayList<ChatRoom> myChats = new ArrayList<ChatRoom>();
        // 예전 코드



        mChatRoomDatabaseReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            // 단순히 트랜잭션으로 데이터를 받아오면. 데이터 처리 타입 오류 발생함. 따라서 순간 입력 받기 모드로 구현함.
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                try{
                    for(MutableData postSnapshot : mutableData.getChildren()){
                        GenericTypeIndicator<List<RoomPeople>> t = new GenericTypeIndicator<List<RoomPeople>>() {};
                        ArrayList<RoomPeople> peoples = (ArrayList<RoomPeople>)postSnapshot.child("peoples").getValue(t);
                        for(RoomPeople people : peoples){
                            if(people.id.equals(myUserInfo.id)){
                                ChatRoom chat = postSnapshot.getValue(ChatRoom.class);
                                myChats.add(chat);
                            }
                        }
                    }
                    Log.d("MyAlarmListSize", String.valueOf(myChats.size()));
                    Intent intent = new Intent("myAlarmList");
                    intent.putExtra("myAlarmList", myChats);
                    mContext.sendBroadcast(intent);
                }catch(Exception e){

                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                try{
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                        GenericTypeIndicator<List<RoomPeople>> t = new GenericTypeIndicator<List<RoomPeople>>(){};      // Gerneric한 데이터를 사용해서 firebase로부터 데이터를 받으려면 사용해야됨.
                        ArrayList<RoomPeople> peoples = (ArrayList<RoomPeople>)postSnapshot.child("peoples").getValue(t);
                        for(RoomPeople people : peoples ){  //전부 검사
                            if(people.id.equals(myUserInfo.id)){        // 같다면. 채팅방의 정보를 다 넘겨준다.
                                Toast.makeText(mContext, people.id, Toast.LENGTH_SHORT).show();
                                ChatRoom chat = postSnapshot.getValue(ChatRoom.class);
                                myChats.add(chat);        // 내가 참여한 리스트에 추가한다.
                            }
                        }
                    }
                    // 브로드 케스트로 서비스에 보내고  서비스에는 데이터베이스를 업데이트를 하고 다음 알람 서비스를 업데이트를 한다.
                    Intent intent = new Intent("myAlarmList");
                    intent.putExtra("myAlarmList", myChats);
                    mContext.sendBroadcast(intent);
                }catch(Exception e){

                }

            }
        });

    }
    // 현재 Shop의 아이템의 리스트를 받아오는 메소드
    public void getShopListItem(){
        mShopDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Shop> shops = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Shop shop = postSnapshot.getValue(Shop.class);
                    shops.add(shop);
                }
                // 브로드캐스트를 사용해서 shop정보를 전달한다.
                Intent intent = new Intent("shopList");
                intent.putExtra("shopList", shops);
                mContext.sendBroadcast(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //addTurnOffListener, deleteTurnOffListener 메소드만을 위한 리스너 객체
    ChildEventListener addTurnOffListener1 = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            //Log.d("dataSnapShot" ,dataSnapshot.getRef().toString());
            // 주소값은 https://alarmapplicaion.firebaseio.com/chatroom/hjhgg/peoples 이다. 즉 people 값의 어레이리스트로 받으면 된다.
            GenericTypeIndicator<List<RoomPeople>> t = new GenericTypeIndicator<List<RoomPeople>>() {};     // 어레이리스트로 만들기 위해서
            ArrayList<RoomPeople> peoples = (ArrayList<RoomPeople>)dataSnapshot.getValue(t);
            Intent intent = new Intent("updateMemeberState");
            intent.putExtra("updateMemeberState", peoples);
            mContext.sendBroadcast(intent);
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void updateMyAlarmLists(){
        // SessionService의 updateMyAlarmList의 브로드캐스트를 호출해서 이 클래스의 getMyAlarmRoomList 함수를 가져온다.
        Intent intent = new Intent("updateMyAlarmList");
        mContext.sendBroadcast(intent);
    }

    public void addTurnOffListener(final ChatRoom chatRoom){

        String roomId = chatRoom.roomTitle;
        Log.d("roomId", roomId);
        mChatRoomDatabaseReference.child(roomId).addChildEventListener(addTurnOffListener1);
    }
    public void deleteTurnOffListener(final ChatRoom chatRoom){
        String chatRoomId = chatRoom.roomTitle;
        mChatRoomDatabaseReference.child(chatRoomId).removeEventListener(addTurnOffListener1);
    }

    // 알람 데이터베이스가 추가가 되면 리스너를 통해 수신을 해서 다른 사람들도 갱신을 할 수 있도록 한다.
    ChildEventListener addChatRoomListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            getAlarmRoomList();     // 추가되면 리스트뷰를 자동으로 갱신하도록 만듬.
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            ChatRoom chatRoom = dataSnapshot.getValue(ChatRoom.class);
            ArrayList<RoomPeople> arrayList = (ArrayList)chatRoom.peoples;

            for(RoomPeople people : arrayList){
                Log.i("peoples", String.valueOf(people.id
                ));
                Toast.makeText(mContext, people.id, Toast.LENGTH_SHORT).show();
            }
            /*
            Intent intent = new Intent("updateMyAlarmList");
            mContext.sendBroadcast(intent);

             */
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ChildEventListener changeChatRoomListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    // 전체 알람방에서 리스트의 엔티티가 추가되거나 삭제되거나 수정되었을 때 다같이 알람 내용이 바뀌도록 설정하는 리스너
    public void setAddChatRoomListener(User myUserInfo){
        this.myUserInfo = myUserInfo;
        mChatRoomDatabaseReference.addChildEventListener(addChatRoomListener);
    }
    public void deleteAddChatRoomListener(){
        mChatRoomDatabaseReference.removeEventListener(addChatRoomListener);
    }
}
