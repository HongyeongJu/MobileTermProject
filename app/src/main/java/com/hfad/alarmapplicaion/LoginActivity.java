package com.hfad.alarmapplicaion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.model.User;

public class LoginActivity extends AppCompatActivity {

    ImageView loginButton;
    ImageView signupButton;
    EditText idEditText;        // id 에디트 텍스트
    EditText pwEditText;        // password 에디트 텍스트

    FirebaseSystem firebaseSystem;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        // 만약 로그인 되고 있다면.
        if(Util.isRunningSessionService(getApplicationContext())){
            /*
            Intent intent = new Intent(LoginActivity.this,  MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

             */
            Intent intent = new Intent();
            intent.setAction("startMainActivity");

            sendBroadcast(intent);
        }

        loginButton = (ImageView)findViewById(R.id.loginbtn);
        signupButton=  (ImageView)findViewById(R.id.btnRegister);
        idEditText = (EditText)findViewById(R.id.user_id);
        pwEditText = (EditText)findViewById(R.id.user_password);

        firebaseSystem = FirebaseSystem.getInstance(getApplicationContext());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseSystem.login(idEditText.getText().toString(), pwEditText.getText().toString());

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

    @Override
    protected void onResume() {
        super.onResume();

    }

}
