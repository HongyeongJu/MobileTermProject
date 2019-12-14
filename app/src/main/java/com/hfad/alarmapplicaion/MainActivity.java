package com.hfad.alarmapplicaion;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);     // 툴바 설정
        setSupportActionBar(toolbar);

        //Toast.makeText(getApplicationContext(), "유저아이디: " + myUserInfo.id, Toast.LENGTH_SHORT).show();

        if(!Util.isRunningSessionService(getApplicationContext())){
            // 유저 메인 정보를 가져오고 이를 바탕으로 Session 서비스 생성
            myUserInfo = (User)getIntent().getSerializableExtra("user");
            Intent startSessionServiceIntent = new Intent(this, SessionService.class);
            startSessionServiceIntent.putExtra("user", myUserInfo);
            startService(startSessionServiceIntent);
        }
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

    //메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // 메뉴 클릭했을 때 로그아웃 구현


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                // 세션 서비스를 종료시킨다.
                Intent stopSessionServiceIntent = new Intent(this, SessionService.class);
                stopService(stopSessionServiceIntent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}