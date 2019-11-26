package com.hfad.alarmapplicaion.DatabaseSystem;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.alarmapplicaion.model.User;

// Firebase에 대한 접근과 이에 대한 함수 제공.
public class FirebaseSystem {

    private static FirebaseDatabase mFirebaseDatabase;      // 파이어베이스 객체
    private static DatabaseReference mUsersDatabaseReference;       // User테이블 객체
    private static DatabaseReference mChatRoomDatabaseReference;        // 채팅방 객체
    private static DatabaseReference mShopDatabaseReference;        // 상점 테이블 객체
    private static FirebaseSystem firebaseSystem;           // 싱글톤 패턴으로 현재 클래스 객체

    private User tempUser;      // 임시로 User데이터 조작을 위한 객체
    private FirebaseSystem(){

    }

    public static synchronized FirebaseSystem getInstance() {
        if(firebaseSystem == null){
            firebaseSystem = new FirebaseSystem();      // 현재 firebaseSystem 객체 받아온다.
            mFirebaseDatabase = FirebaseDatabase.getInstance();     // Firebase 객체 얻기
            mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");      // 주소 얻기
            mChatRoomDatabaseReference = mFirebaseDatabase.getReference().child("chatroom");
            mShopDatabaseReference = mFirebaseDatabase.getReference().child("shop");
        }

        return firebaseSystem;
    }

    // 해당 id가 서버에 등록되어 있는지 확인하는 메소드
    public boolean isId(String id){
        mUsersDatabaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tempUser = dataSnapshot.getValue(User.class);       // User 클래스의 객체 얻어오기.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(tempUser == null) return false;      // 아이디가 없음
        else{
            return true;                       // 아이디가 이미 있음
        }
    }


    public User getUser(String id){
        mUsersDatabaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tempUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return tempUser;
    }

    public boolean addUser(User user){
        String id = user.id;
        // 만들고자하는 아이디가 서버에 있는지 확인
        if(!isId(id)){  // 아이디가 서버에 없으면
            mUsersDatabaseReference.child(id).setValue(user);
            return true;        // 등록 완료

        }else {
            return false;       // 이미 아이디가 서버에 있음.
        }
    }


}
