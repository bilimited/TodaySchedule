package com.example.todayschedule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private BottomNavigationView mBottomNavigationView;
    private Fragment[]mFragments = new Fragment[4];

    //用来记录现在的Fragment。
    private static int nowFragment = 0;

    private static boolean isFirstOpen = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isFirstOpen){
            LinearLayout linearLayout = findViewById(R.id.welcome_panel);
            ImageView imageView = findViewById(R.id.welcome);
            linearLayout.bringToFront();
            Glide.with(this).load(R.drawable.logo_anim_long).into(imageView);
            Animation mShowAction = new AlphaAnimation(1,0);
            mShowAction.setDuration(500);
            mShowAction.setStartOffset(2500);
            mShowAction.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    linearLayout.setVisibility(View.GONE);
                    isFirstOpen=false;

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            linearLayout.setAnimation(mShowAction);
        }

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
         * 暂时取消了主侧边栏
         */
        //ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name,R.string.app_name);
        //drawerLayout.addDrawerListener(mActionBarDrawerToggle);
        //mActionBarDrawerToggle.syncState();

        mFragments[0] = new MainFragment();
        mFragments[1] = new ForumFragment();
        mFragments[2] = new PersonalCenterFragment();

        if(nowFragment>2||nowFragment<0){
            nowFragment=0;
        }else if(nowFragment==2){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new PersonalCenterFragment()).commit();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,mFragments[nowFragment]).commit();
    }



    //底部菜单选中事件
    private void onTabItemSelected(int id){
        Fragment fragment = null;
        switch (id){
            case R.id.bnm_item1:
                fragment = mFragments[0];
                nowFragment = 0;
                break;
            case R.id.bnm_item2:
                fragment = mFragments[1];
                nowFragment = 1;
                break;
            case R.id.bnm_item3:
                fragment = mFragments[2];
                nowFragment = 2;
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