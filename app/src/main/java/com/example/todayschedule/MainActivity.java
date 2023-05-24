package com.example.todayschedule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todayschedule.bean.Image;
import com.example.todayschedule.fragments.ForumFragment;
import com.example.todayschedule.fragments.MainFragment;
import com.example.todayschedule.fragments.PersonalCenterFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private BottomNavigationView mBottomNavigationView;
    private Fragment[]mFragments = new Fragment[4];

    TextView nav_title;
    TextView nav_subtitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!TodaySchedule.isLogged()){
            nav_title.setText("未登录");
            nav_subtitle.setText("登录以变得更强");
        }else {
            nav_title.setText("早上好,"+TodaySchedule.LoggedAccount);
            nav_subtitle.setText("😄");
        }

        //不知道为什么登录之后背景颜色会变黑。 暴力修复。
        drawerLayout.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void init(){

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        //mBottomNavigationView.getMaxItemCount()

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onTabItemSelected(item.getItemId());
                return true;
            }
        });

        drawerLayout = findViewById(R.id.main_drawer);
        navigationView = findViewById(R.id.main_nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        LinearLayout nav_header = (LinearLayout) navigationView.getHeaderView(0);
        nav_title = (TextView) nav_header.getChildAt(0);
        nav_subtitle = (TextView) nav_header.getChildAt(1);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nm_item1:{
                        Intent intent = new Intent(MainActivity.this,SelectPicActivity.class);
                        startActivity(intent);
                    }
                    case R.id.nm_item2:{
                        Toast.makeText(MainActivity.this, "在做了在做了", Toast.LENGTH_SHORT).show();
                    }
                    case R.id.nm_quit:{
                        Toast.makeText(MainActivity.this, "在做了在做了", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
        nav_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TodaySchedule.isLogged()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("确认退出?");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            TodaySchedule.logout();
                            //loadData();//重置数据
                            dialog.dismiss();
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }

                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }else {
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }

            }
        });

        mFragments[0] = new MainFragment();
        mFragments[1] = new ForumFragment();
        mFragments[2] = new PersonalCenterFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,mFragments[0]).commit();
    }

    //底部菜单选中事件
    private void onTabItemSelected(int id){
        Fragment fragment = null;
        switch (id){
            case R.id.bnm_item1:
                fragment = mFragments[0];
                break;
            case R.id.bnm_item2:
                fragment = mFragments[1];
                break;
            case R.id.bnm_item3:
                fragment = mFragments[2];
                break;
        }
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment).commit();
        }
    }

    //ToolBar相关
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    //ToolBar点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}