package com.hfad.alarmapplicaion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.model.User;

public class LoginActivity extends AppCompatActivity {

    ImageView loginButton;
    ImageView signupButton;
    EditText idEditText;        // id 에디트 텍스트
    EditText pwEditText;        // password 에디트 텍스트

    FirebaseSystem firebaseSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (ImageView)findViewById(R.id.loginbtn);
        signupButton=  (ImageView)findViewById(R.id.btnRegister);
        idEditText = (EditText)findViewById(R.id.user_id);
        pwEditText = (EditText)findViewById(R.id.user_password);

        firebaseSystem = FirebaseSystem.getInstance(getApplicationContext());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = firebaseSystem.getUser(idEditText.getText().toString());

                // 등록된 유저가 없으면
                if(user == null){
                    Toast.makeText(getApplicationContext(), "등록된 유저가 아닙니다.", Toast.LENGTH_SHORT).show();
                }else {
                    // 비밀번호가 같은지 다른지 확인
                    if(user.password.equals(pwEditText.getText().toString())){// 같으면
                        Toast.makeText(getApplicationContext(), user.id +"님 접속하셨습니다." , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다. " , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
