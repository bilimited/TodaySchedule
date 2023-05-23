package com.example.todayschedule;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.todayschedule.bean.Course;
import com.example.todayschedule.tool.DatabaseHelper;

public class SeeCourseActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = new DatabaseHelper
            (this, "database.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_course);
        Intent intent = getIntent();
        final Course seeCourse = (Course) intent.getSerializableExtra("seeCourse");
        final TextView seeCourseName = (TextView) findViewById(R.id.see_course_name);
        final TextView seeDay = (TextView) findViewById(R.id.see_week);
        final TextView seeStart = (TextView) findViewById(R.id.see_classes_begin);
        final TextView seeEnd = (TextView) findViewById(R.id.see_classes_ends);
        final TextView seeTeacher = (TextView) findViewById(R.id.see_teacher_name);
        final TextView seeClassRoom = (TextView) findViewById(R.id.see_class_room);
        seeCourseName.setText(seeCourse.getCourseName());
        seeDay.setText(String.valueOf(seeCourse.getDay()));
        seeStart.setText(String.valueOf(seeCourse.getStart()));
        seeEnd.setText(String.valueOf(seeCourse.getEnd()));
        seeTeacher.setText(seeCourse.getTeacher());
        seeClassRoom.setText(seeCourse.getClassRoom());

        Button delBtn = (Button)findViewById(R.id.btn_copy);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeeCourseActivity.this, ScheduleActivity.class);
                intent.putExtra("PreCourse", seeCourse);
                intent.putExtra("isDelete",true);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        //修改按钮被按下时
        Button ReviseBtn = (Button)findViewById(R.id.btn_return);
        ReviseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeeCourseActivity.this, ScheduleActivity.class);
                intent.putExtra("PreCourse", seeCourse);
                intent.putExtra("isDelete",false);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("SeeCourseActivity", "修改的返回来了");
    }
}
