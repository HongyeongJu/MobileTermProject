package com.hfad.alarmapplicaion;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.model.User;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    EditText nameEditText;
    EditText idEditText;
    EditText passwordEditText;
    EditText phoneEditText;
    ImageView btnRegister;
    FirebaseSystem mFirebaseSystem;
    private FirebaseDatabase mFirebaseDatabase;      // 파이어베이스 객체
    private DatabaseReference mUsersDatabaseReference;       // User테이블 객체
    RadioButton maleButton;
    RadioButton femaleButton;
    boolean gender;
    boolean checkG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameEditText = findViewById(R.id.name);
        idEditText = findViewById(R.id.user_id);
        passwordEditText = findViewById(R.id.password);
        btnRegister =  (ImageView)findViewById(R.id.btnregister);
        maleButton = (RadioButton)findViewById(R.id.malebtn);
        femaleButton = (RadioButton)findViewById(R.id.femalebtn);
        phoneEditText = (EditText)findViewById(R.id.phoneNumber);

        mFirebaseSystem = FirebaseSystem.getInstance(getApplicationContext());

        mFirebaseDatabase = FirebaseDatabase.getInstance();     // Firebase 객체 얻기
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");

        btnRegister.setOnClickListener(this);


        maleButton.setOnClickListener(this);
        femaleButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 텍스트 입력을 데이터로 받는다.
        String id = idEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        if(v.getId() == R.id.btnregister){  // 회원 가입 버튼을 눌렀을 때
            // 아이디가 서버에 등록이 되어있지 않으면
            /*
            if(!mFirebaseSystem.isId(id)){
                Log.d("Id", id);
                User user = new User(name, id, password, 0, 0);
                mFirebaseSystem.addUser(user);  // 유저를 등록한다.
                Toast.makeText(getApplicationContext(), id + "등록완료", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(getApplicationContext(), "이미 아이디가 있습니다.", Toast.LENGTH_SHORT).show();
            }

             */
            // 입력을 객체로 만든다.
            if(id.equals("") || name.equals("") || password.equals("") || phone.equals("") || checkG == false){
                Toast.makeText(getApplicationContext(), "필수항목을 확인하세요.", Toast.LENGTH_SHORT).show();
            }
            else{
                User user = new User(name, id, password, 0, 0, gender, phone);
                mFirebaseSystem.addUser(user);
                finish();
            }

        }else if(v.getId() == R.id.malebtn){        // 남성은 false
            gender = false;
            checkG = true;
        }else if(v.getId() == R.id.femalebtn)       // 여성은 true
        {
            gender = true;
            checkG = true;
        }
    }

}
