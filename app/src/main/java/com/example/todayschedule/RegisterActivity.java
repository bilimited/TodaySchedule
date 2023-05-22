package com.example.todayschedule;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todayschedule.bean.User_Table;
import com.example.todayschedule.tool.Check;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity extends AppCompatActivity {
    TextView RegisterTitle; //注册标题
    EditText AccountText;   //账号
    EditText PasswordText;  //密码
    EditText SMS_Code;      //验证码

    TextView LoginButton;   //回到登录按钮
    Button RegisterButton;  //注册按钮
    Button GetCode;         //获取验证码按钮
    Button Cheat;

    TextView ShowButton;    //小眼睛按钮
    boolean ShowPassword;   //密码可见状态（初始不可见）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        //获取验证码
        GetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取客户端输入的账号
                final String Account = AccountText.getText().toString().trim();
                //isEmpty()方法判断是否为空
                if (TextUtils.isEmpty(Account)){
                    Toast.makeText(RegisterActivity.this,"请填写手机号码",Toast.LENGTH_SHORT).show();
                }else if (Check.PhoneCheck(Account.trim()) != true){
                    Toast.makeText(RegisterActivity.this,"请填写正确的手机号码",Toast.LENGTH_SHORT).show();
                }else {
                    BmobQuery<User_Table> bmobQuery = new BmobQuery<>();
                    bmobQuery.findObjects(new FindListener<User_Table>() {
                        @Override
                        public void done(List<User_Table> object, BmobException e) {
                            if (e == null) {
                                int count=0;    //判断是否查询到尾
                                for (User_Table user_table : object) {
                                    if (user_table.getAccount().equals(Account)){
                                        Toast.makeText(RegisterActivity.this,"该账号已注册过",Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    count++;
                                }
                                //查询到尾，说明没有重复账号
                                if (count == object.size()){
                                    SendSMS(Account);
                                }
                            }else {
                                SendSMS(Account);
                            }
                        }
                    });
                }
            }
        });

        /**
         * 注册
         */
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //账号(Account)、密码(Password)
                final String Account = AccountText.getText().toString().trim();
                final String Password = PasswordText.getText().toString().trim();
                if (TextUtils.isEmpty(Password)){
                    Toast.makeText(RegisterActivity.this,"请填写密码",Toast.LENGTH_SHORT).show();
                }else if (Password.length()<6){
                    Toast.makeText(RegisterActivity.this,"密码不得少于6位数",Toast.LENGTH_SHORT).show();
                }else if (Password.length()>16){
                    Toast.makeText(RegisterActivity.this,"密码不得多于16位数",Toast.LENGTH_SHORT).show();
                }else if (Check.PasswordCheck(Password) != true){
                    Toast.makeText(RegisterActivity.this,"密码最少包含3个字母",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(SMS_Code.getText().toString().trim())){
                    Toast.makeText(RegisterActivity.this,"请填写验证码",Toast.LENGTH_SHORT).show();
                } else {
                    //短信验证码效验
                    BmobSMS.verifySmsCode(Account, SMS_Code.getText().toString().trim(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //将用户信息存储到Bmob云端数据
                                final User_Table user = new User_Table();
                                user.setAccount(Account);
                                user.setPassword(Password);
                                user.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            //注册成功，回到登录页面
                                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else {
                                            Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                SMS_Code.setText("");
                                Toast.makeText(RegisterActivity.this,"验证码错误"+e.getErrorCode(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        //返回登陆界面
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShowPassword == false) {
                    //密码不可见-->>密码可见
                    PasswordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    PasswordText.setSelection(PasswordText.getText().toString().length());
                    ShowPassword = true;
                }else {
                    //密码可见-->>密码不可见
                    PasswordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    PasswordText.setSelection(PasswordText.getText().toString().length());
                    ShowPassword = false;
                }
            }
        });

        Cheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //账号(Account)、密码(Password)
                final String Account = AccountText.getText().toString().trim();
                final String Password = PasswordText.getText().toString().trim();
                if (TextUtils.isEmpty(Password)){
                    Toast.makeText(RegisterActivity.this,"请填写密码",Toast.LENGTH_SHORT).show();
                }else if (Password.length()<6){
                    Toast.makeText(RegisterActivity.this,"密码不得少于6位数",Toast.LENGTH_SHORT).show();
                }else if (Password.length()>16){
                    Toast.makeText(RegisterActivity.this,"密码不得多于16位数",Toast.LENGTH_SHORT).show();
                }else if (Check.PasswordCheck(Password) != true){
                    Toast.makeText(RegisterActivity.this,"密码最少包含3个字母",Toast.LENGTH_SHORT).show();
                }else {
                    //将用户信息存储到Bmob云端数据
                    final User_Table user = new User_Table();
                    user.setAccount(Account);
                    user.setPassword(Password);
                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                //注册成功，回到登录页面
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void init(){
        //注册标题(Title)、账号(Account)、密码(Password)、验证码(SMS_Code)
        RegisterTitle = findViewById(R.id.RegisterTitle);
        AccountText = findViewById(R.id.AccountText);
        PasswordText = findViewById(R.id.PasswordText);
        SMS_Code = findViewById(R.id.SMS_Code);
        Cheat = findViewById(R.id.ForceRegister);

        //回到登录按钮(Login)、注册按钮(Register)、验证码获取按钮(GetCode)
        LoginButton = findViewById(R.id.LoginButton);
        RegisterButton = findViewById(R.id.RegisterButton);
        GetCode = findViewById(R.id.GetCode);

        //将密码文本初始设置为不可见状态
        ShowButton = findViewById(R.id.ShowButton);
        ShowPassword = false;

        //设置标题字体样式(方舒整体 常规)
        //RegisterTitle.setTypeface(Typeface.createFromAsset(getAssets(),"font/FZSTK.TTF"));
        //设置按钮文本字体样式(方舒整体 常规)
        //RegisterButton.setTypeface(Typeface.createFromAsset(getAssets(),"font/FZSTK.TTF"));
        //设置背景图片透明度(0~255，值越小越透明)
        RegisterButton.getBackground().setAlpha(100);
    }

    /**
     * 发送验证码
     * @param account：输入的手机号码
     *  SMS 为Bmob短信服务自定义的短信模板名字
     */
    private void SendSMS(String account){
        BmobSMS.requestSMSCode(account, "今日课表短信模板", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    Toast.makeText(RegisterActivity.this,"验证码已发送",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this,"发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        /**
         * 设置按钮60s等待
         * onTick()方法——>>计时进行时的操作
         *      ：显示倒计时，同时设置按钮不可点击
         * onFinish()方法——>>计时完成时的操作
         *      ：刷新原文本，同时设置按钮可以点击
         */
        CountDownTimer timer =new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                GetCode.setEnabled(false);
                GetCode.setText("重新获取("+millisUntilFinished/1000+"s)");
            }

            @Override
            public void onFinish() {
                GetCode.setEnabled(true);
                GetCode.setText("获取验证码");
            }
        }.start();
    }
}
