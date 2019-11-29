package com.hfad.alarmapplicaion;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.alarmapplicaion.model.User;
import com.hfad.alarmapplicaion.service.SessionService;
import com.hfad.alarmapplicaion.ui.main.SectionsPagerAdapter;



public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();

    public User myUserInfo;

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.addOnTabSelectedListener(this);



        //Toast.makeText(getApplicationContext(), "유저아이디: " + myUserInfo.id, Toast.LENGTH_SHORT).show();

        // 유저 메인 정보를 가져오고 이를 바탕으로 Session 서비스 생성
        myUserInfo = (User)getIntent().getSerializableExtra("user");
        Intent startSessionServiceIntent = new Intent(this, SessionService.class);
        startSessionServiceIntent.putExtra("user", myUserInfo);
        startService(startSessionServiceIntent);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}