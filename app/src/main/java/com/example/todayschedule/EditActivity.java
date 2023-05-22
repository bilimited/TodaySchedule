package com.example.todayschedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todayschedule.bean.Notice;
import com.example.todayschedule.bean.Post;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class EditActivity extends AppCompatActivity {

    private boolean isPost = false;
    private boolean isNotice = false;
    private EditText edit_title,edit_content;

    private static String draft_title;
    private static String draft_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        if(getIntent().getBooleanExtra("isPost",false)){
            isPost = true;
        }else {
            if(getIntent().getBooleanExtra("isNotice",false)){
                isNotice = true;
            }
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true);

        Button submit = findViewById(R.id.submit);
        edit_title = findViewById(R.id.edit_title);
        if(isNotice){
            edit_title.setHint("标题(必填)");
        }
        edit_content = findViewById(R.id.edit_content);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TodaySchedule.isLogged()){
                    Toast.makeText(EditActivity.this, "请先登录!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isPost){
                    Post post = new Post(
                            edit_content.getText().toString(),
                            0,
                            TodaySchedule.LoggedAccount,
                            "无",
                            TodaySchedule.UserID);
                    post.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Toast.makeText(EditActivity.this, "发表成功!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(EditActivity.this, "发表失败，错误代码:"+e.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else if(isNotice) {
                    Notice notice = new Notice(
                            edit_title.getText().toString(),
                            edit_content.getText().toString(),
                            TodaySchedule.LoggedAccount,
                            TodaySchedule.UserID
                    );
                    notice.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Toast.makeText(EditActivity.this, "新闻发表成功!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(EditActivity.this, "发表失败，错误代码:"+e.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        ConstraintLayout constraintLayout = findViewById(R.id.activiey_edit_layout);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}