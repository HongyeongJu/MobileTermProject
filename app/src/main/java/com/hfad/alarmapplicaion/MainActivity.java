package com.hfad.alarmapplicaion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.alarmapplicaion.DatabaseSystem.FirebaseSystem;
import com.hfad.alarmapplicaion.model.User;
import com.hfad.alarmapplicaion.service.SessionService;
import com.hfad.alarmapplicaion.ui.main.SectionsPagerAdapter;



public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
    FirebaseSystem mFirebaseSystem;

    public User myUserInfo;
    TextView nav_header_id_text;
    TextView nav_header_point_text;
    TextView nav_header_total_point_text;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseSystem = FirebaseSystem.getInstance(getApplicationContext());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.addOnTabSelectedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);     // 툴바 설정
        setSupportActionBar(toolbar);

        //App Bar의 좌측 영역쪽에 Drawer를 open 하기 위한 Icon 추가
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher); 이미지버튼을 통한 네비게이션 드러우러

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView)findViewById(R.id.nvView);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.closed);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //Toast.makeText(getApplicationContext(), "유저아이디: " + myUserInfo.id, Toast.LENGTH_SHORT).show();
        myUserInfo = (User)getIntent().getSerializableExtra("user");
        if(!Util.isRunningSessionService(getApplicationContext())){
            // 유저 메인 정보를 가져오고 이를 바탕으로 Session 서비스 생성
            Intent startSessionServiceIntent = new Intent(this, SessionService.class);
            startSessionServiceIntent.putExtra("user", myUserInfo);
            startService(startSessionServiceIntent);
        }


        // 네비게이션 드러우러에 있는 Header값 처리하기
        View nav_header_view = navigationView.getHeaderView(0);

        nav_header_id_text = (TextView)nav_header_view.findViewById(R.id.nav_user_id);
        nav_header_point_text = (TextView)nav_header_view.findViewById(R.id.nav_user_point);
        nav_header_total_point_text = (TextView)nav_header_view.findViewById(R.id.nav_user_total_point);
        ImageView nav_header_image = (ImageView)nav_header_view.findViewById(R.id.nv_image);

        nav_header_total_point_text.setText(String.valueOf(myUserInfo.totalPoint));
        nav_header_id_text.setText(myUserInfo.id);
        nav_header_point_text.setText(String.valueOf(myUserInfo.point));
        if(!myUserInfo.gender){
            nav_header_image.setImageResource(R.drawable.man);
        }else{
            nav_header_image.setImageResource(R.drawable.woman);
        }

        // 네비게이션드러우러에서 선택했을 때 처리해주기.
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_logout:
                        // 세션 서비스를 종료시킨다.
                        Intent stopSessionServiceIntent = new Intent(getApplicationContext(), SessionService.class);
                        stopService(stopSessionServiceIntent);
                        finish();
                        break;

                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction("changeUserState");
        registerReceiver(receiver, filter);

        mFirebaseSystem.setChangeStateUserListener(myUserInfo);

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


    // Drawer가 선택되어있을 때 이전 버튼을 눌렀을 때 Drawer 닫기
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    // 파이어 베이스로 User 값이 수정되었을 때 다시 갱신해서 받아오기.
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("changeUserState")) {
                User myUserInfo = (User)intent.getSerializableExtra("changeUserState");
                nav_header_id_text.setText(myUserInfo.id);
                nav_header_point_text.setText(String.valueOf(myUserInfo.point));
                nav_header_total_point_text.setText(String.valueOf(myUserInfo.totalPoint));
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        mFirebaseSystem.deleteChangeStateUserListener(myUserInfo);
    }


}