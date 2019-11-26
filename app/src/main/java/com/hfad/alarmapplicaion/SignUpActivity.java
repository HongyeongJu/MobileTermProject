package com.hfad.alarmapplicaion;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.model.User;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    EditText nameEditText;
    EditText idEditText;
    EditText passwordEditText;
    ImageView btnRegister;
    FirebaseSystem mFirebaseSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameEditText = findViewById(R.id.name);
        idEditText = findViewById(R.id.user_id);
        passwordEditText = findViewById(R.id.password);
        btnRegister =  (ImageView)findViewById(R.id.btnregister);
        mFirebaseSystem = FirebaseSystem.getInstance();

        btnRegister.setOnClickListener(this);
    }
    User user1;
    @Override
    public void onClick(View v) {
        String id = idEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(v.getId() == R.id.btnregister){  // 회원 가입 버튼을 눌렀을 때
            // 아이디가 서버에 등록이 되어있지 않으면
            if(!mFirebaseSystem.isId(id)){
                User user = new User(name, id, password, 0, 0);
                mFirebaseSystem.addUser(user);  // 유저를 등록한다.
                Toast.makeText(getApplicationContext(), id + "등록완료", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(getApplicationContext(), "이미 아이디가 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
