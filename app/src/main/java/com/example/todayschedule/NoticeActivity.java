package com.example.todayschedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.TextView;

import com.example.todayschedule.bean.Notice;

public class NoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        ConstraintLayout constraintLayout = findViewById(R.id.notice_activity);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.white));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true);

        TextView title = findViewById(R.id.notice_title);
        TextView author = findViewById(R.id.author);
        TextView date = findViewById(R.id.date);
        TextView content = findViewById(R.id.notice_content);

        Notice notice = (Notice) getIntent().getSerializableExtra("Notice");

        title.setText(notice.getTitle());
        author.setText(notice.getAuthor());
        date.setText(notice.getCreatedAt());
        content.setText(notice.getContent());

    }
}