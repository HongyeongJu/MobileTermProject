package com.hfad.alarmapplicaion.DatabaseSystem;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.alarmapplicaion.MainActivity;
import com.hfad.alarmapplicaion.model.ChatRoom;
import com.hfad.alarmapplicaion.model.User;

// Firebase에 대한 접근과 이에 대한 함수 제공.
public class FirebaseSystem  {

    private static FirebaseDatabase mFirebaseDatabase;      // 파이어베이스 객체
    private static DatabaseReference mUsersDatabaseReference;       // User테이블 객체
    private static DatabaseReference mChatRoomDatabaseReference;        // 채팅방 객체
    private static DatabaseReference mShopDatabaseReference;        // 상점 테이블 객체
    private static FirebaseSystem firebaseSystem;           // 싱글톤 패턴으로 현재 클래스 객체
    private static Context mContext;

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
                            intent.putExtra("user", user);
                            mContext.startActivity(intent);


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
    private boolean isId;
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



    public void addChatRoom(final ChatRoom chatRoom){

        mChatRoomDatabaseReference.push().setValue(chatRoom);
    }

}
