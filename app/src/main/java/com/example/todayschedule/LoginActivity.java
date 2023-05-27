package com.example.todayschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todayschedule.bean.User_Table;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class LoginActivity extends AppCompatActivity {


    TextView LoginTitle;
    EditText AccountText;
    EditText PasswordText;

    //登陆按钮、注册按钮、密码找回按钮
    Button LoginButton;
    TextView RegisterButton;
    TextView FindPasswordButton;

    //眼睛按钮
    TextView ShowButton;
    //密码可见状态
    boolean ShowPassword;

    RadioButton autologin;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        sharedPreferences = getSharedPreferences("global",MODE_PRIVATE);
        String lastAccount = sharedPreferences.getString("lastAccount","");
        if(!lastAccount.equals("")){
            AccountText.setText(lastAccount);
        }

        /**
         * 登陆监听
         */
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //账号(Account)、密码(Password)
                final String Account = AccountText.getText().toString().trim();
                final String Password = PasswordText.getText().toString().trim();
                if (TextUtils.isEmpty(Account)) {
                    Toast.makeText(LoginActivity.this, "请填写手机号码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(LoginActivity.this, "请填写密码", Toast.LENGTH_SHORT).show();
                } else {
                    BmobQuery<User_Table> bmobQuery = new BmobQuery<>();
                    bmobQuery.findObjects(new FindListener<User_Table>(){
                        @Override
                        public void done(List<User_Table> object, BmobException e) {
                            if (e == null) {
                                //判断信号量，若查找结束count和object长度相等，则没有查找到该账号
                                int count=0;
                                for (User_Table user_table : object) {
                                    if (user_table.getAccount().equals(Account)) {
                                        //已查找到该账号，检测密码是否正确
                                        if (user_table.getPassword().equals(Password)) {
                                            //密码正确，跳转（Home是登陆后跳转的页面）
                                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                            TodaySchedule.LoggedAccount = user_table.getAccount();
                                            TodaySchedule.UserID = user_table.getObjectId();

                                            /**
                                             * 判断管理员逻辑
                                             * TODO: 添加管理员专用登录界面
                                             */
                                            BmobQuery<User_Table> bmobQuery = new BmobQuery<User_Table>();
                                            bmobQuery.getObject(TodaySchedule.UserID, new QueryListener<User_Table>() {
                                                @Override
                                                public void done(User_Table object,BmobException e) {
                                                    if(e==null){
                                                        TodaySchedule.IsAdmin = object.getAdmin();
                                                    }
                                                }
                                            });

                                            SharedPreferences.Editor editor = sharedPreferences.edit();

                                            editor.putString("lastAccount",user_table.getAccount());
                                            if(autologin.isChecked()){
                                                editor.putString("lastPassword",user_table.getPassword());
                                            }else {
                                                editor.putString("lastPassword","");
                                            }
                                            editor.commit();

                                            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            //startActivity(intent);
                                            finish();
                                            break;
                                        }else {
                                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                    count++;
                                }
                                if (count >= object.size()){
                                    Toast.makeText(LoginActivity.this,"该账号不存在",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(LoginActivity.this,"该账号不存在",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        //跳转到注册界面
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    //View初始化
    public void init(){
        //Login标题(LoginTitle)、账号(AccountText)、密码(PasswordText)
        AccountText = findViewById(R.id.AccountText);
        PasswordText = findViewById(R.id.PasswordText);

        //登录按钮(Login)、跳到注册按钮(Register)、跳到密码找回按钮(FindPassword)
        LoginButton = findViewById(R.id.LoginButton);
        RegisterButton = findViewById(R.id.RegisterButton);

        autologin = findViewById(R.id.auto_login);

    }
}