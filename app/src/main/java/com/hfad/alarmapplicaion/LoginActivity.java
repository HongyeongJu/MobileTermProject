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

    String id;
    String pw;

    FirebaseSystem firebaseSystem;

    User user;

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
                id = idEditText.getText().toString();
                pw = pwEditText.getText().toString();

                if(id.equals("")){
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else if(pw.equals("")){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else if(id.equals("") && pw.equals("")){
                    Toast.makeText(getApplicationContext(), "아이디, 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseSystem.login(id, pw);
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
