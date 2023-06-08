package com.example.todayschedule;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todayschedule.bean.Course;
import com.example.todayschedule.bean.Note;
import com.example.todayschedule.bean.User_Info;


import com.example.todayschedule.fragments.MainFragment;
import com.example.todayschedule.tool.Base64Coder;
import com.example.todayschedule.tool.DatabaseHelper;
import com.example.todayschedule.tool.DragViewUtil;
import com.example.todayschedule.tool.NoteDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xujiaji.happybubble.Auto;
import com.xujiaji.happybubble.BubbleDialog;
import com.xujiaji.happybubble.BubbleLayout;
import com.xujiaji.happybubble.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 不建议改与Note更新相关的代码。。。
 */
public class ScheduleActivity extends AppCompatActivity {

    //星期几
    private RelativeLayout day;

    //SQLite Helper类
    private DatabaseHelper databaseHelper = new DatabaseHelper
            (this, "database.db", null, 1);

    //被点击的View
    View ClickedView;
    int currentCoursesNumber = 0;
    int maxCoursesNumber = 0;

    ImageView AddCourse;
    ImageView leftMenu;
    TextView nav_title;
    TextView nav_subtitle;
    ImageView portrait;

    ConstraintLayout noteContainer;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule);

        init();


}

    private void setBackground(int bgid){
        ImageView background = findViewById(R.id.bg);
        if(bgid<6){
            background.setImageResource(MainFragment.bg_list[bgid]);
        }else {
            background.setImageURI(TodaySchedule.background_url);
        }
    }

    private void init(){

        /**
         * 设置ToolBar相关的代码
         */
        DrawerLayout drawerLayout = findViewById(R.id.drawer);

        AddCourse = findViewById(R.id.addCourse);

        navigationView = findViewById(R.id.nav_view);
        ImageView addNote = findViewById(R.id.addNote);
        noteContainer = findViewById(R.id.note_container);
        Toolbar toolbar = findViewById(R.id.toolbar);
        /**
         * 添加返回图标部分
         */
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        //getSupportActionBar().setHomeButtonEnabled(true);

        /**
         * 添加三道杠图标部分
         */
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        ConstraintLayout nav_header = (ConstraintLayout) navigationView.getHeaderView(0);
        nav_title = (TextView) nav_header.findViewById(R.id.nav_title);
        nav_subtitle = (TextView) nav_header.findViewById(R.id.nav_subtitle);
        portrait = (ImageView)nav_header.findViewById(R.id.shapeableImageView);

        ScrollView scrollView = findViewById(R.id.scrollView2);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        AddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleActivity.this, AddCourseActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        /**
         * navigationView点击事件
         */
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nm_to_json:{
                        dataToJson();
                        break;
                    }
                    case R.id.nm_from_json:{
                        Intent intent = new Intent(ScheduleActivity.this,InputJsonActivity.class);
                        startActivityForResult(intent,4);
                        break;
                    }
                    case R.id.nm_clear:{
                        AlertDialog.Builder builder=new AlertDialog.Builder(ScheduleActivity.this);
                        builder.setMessage("确认清除所有便签?");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                                clearNote();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        break;
                    }
                }
                return false;
            }
        });
        nav_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TodaySchedule.isLogged()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(ScheduleActivity.this);
                    builder.setMessage("确认退出?");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            TodaySchedule.logout();
                            //loadData();//重置数据
                            dialog.dismiss();
                            Intent intent = new Intent(ScheduleActivity.this,LoginActivity.class);
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
                    Intent intent = new Intent(ScheduleActivity.this,LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
                Intent intent = new Intent(ScheduleActivity.this,AddNoteActivity.class);
                startActivityForResult(intent,TodaySchedule.REQUEST_ADD_NOTE);
            }
        });
        setBackground(TodaySchedule.background_id);
        loadNote();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!TodaySchedule.isLogged()){
            nav_title.setText("未登录");
            nav_subtitle.setText("登录以变得更强");
        }else {
            User_Info.findUserInfo(TodaySchedule.UserID, new FindListener<User_Info>() {
                @Override
                public void done(List<User_Info> list, BmobException e) {
                    if(e==null&&!list.isEmpty()){
                        User_Info user_info = list.get(0);
                        nav_title.setText(user_info.getNickName());
                        nav_subtitle.setText("id:"+TodaySchedule.LoggedAccount);
                        Base64Coder.LoadProtrait(ScheduleActivity.this,user_info.getPortraitID(),portrait);
                    }
                }
            });
        }


        if(TodaySchedule.isLogged()){
            bmob_loadData();
        }else {
            loadData();
        }
    }

    //从数据库加载数据
    @SuppressLint("Range")
    private void loadData() {
        clearItemCourseView();
        ArrayList<Course> coursesList = new ArrayList<>(); //课程列表
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from courses", null);
        if (cursor.moveToFirst()) {
            do {
                coursesList.add(new Course(
                        cursor.getString(cursor.getColumnIndex("course_name")),
                        cursor.getString(cursor.getColumnIndex("teacher")),
                        cursor.getString(cursor.getColumnIndex("class_room")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getInt(cursor.getColumnIndex("class_start")),
                        cursor.getInt(cursor.getColumnIndex("class_end"))));
            } while(cursor.moveToNext());
        }
        cursor.close();

        //使用从数据库读取出来的课程信息来加载课程表视图
        for (Course course : coursesList) {
            createLeftView(course);
            createItemCourseView(course);
        }
    }


    private int[] cls_colors = {
            R.color.cls_1,
            R.color.cls_2,
            R.color.cls_3,
            R.color.cls_4,
            R.color.cls_5,
            R.color.cls_6,
            R.color.cls_7,
            R.color.cls_8,
    };

    @ColorRes
    private int getCourseColor(Course course){
        int seed = (course.getDay()*10+course.getStart())%8;
        return cls_colors[seed];
    }

    //将数据从云端同步到本地
    private void bmob_loadData(){
        clearItemCourseView();
        ArrayList<Course> coursesList = new ArrayList<>(); //课程列表
        BmobQuery<Course> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<Course>() {
            @Override
            public void done(List<Course> list, BmobException e) {
                if(e==null){
                    for(Course course : list){
                        if(course.getUserid().equals(TodaySchedule.UserID)){
                            //coursesList.add(course);
                            createLeftView(course);
                            createItemCourseView(course);
                        }
                    }
                }else {
                    Toast.makeText(ScheduleActivity.this,"读取课程时发生错误："+e.getErrorCode(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //保存数据到数据库
    private void saveData(Course course) {
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL
                ("insert into courses(course_name, teacher, class_room, day, class_start, class_end) " + "values(?, ?, ?, ?, ?, ?)",
                        new String[] {course.getCourseName(),
                                course.getTeacher(),
                                course.getClassRoom(),
                                course.getDay()+"",
                                course.getStart()+"",
                                course.getEnd()+""}
                );
    }

    private void bmob_saveData(Course course){
        course.setUserid(TodaySchedule.UserID);
        course.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e!=null){
                    Toast.makeText(ScheduleActivity.this, "添加课程失败:"+e.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteData(Course course){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from courses where course_name = ? and day =? and class_start=? and class_end=?",
                new String[]{course.getCourseName(),
                        String.valueOf(course.getDay()),
                        String.valueOf(course.getStart()),
                        String.valueOf(course.getEnd())});
        Toast.makeText(ScheduleActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
    }

    private void bmob_deleteData(Course course){
        course.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e!=null){
                    Toast.makeText(ScheduleActivity.this, "无法删除课程:"+e.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bmob_changeData(Course course){
        course.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e!=null){
                    Toast.makeText(ScheduleActivity.this, "无法更新课程:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("Range")
    private void dataToJson(){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        if(TodaySchedule.isLogged()){
            ArrayList<Course> coursesList = new ArrayList<>(); //课程列表
            BmobQuery<Course> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("userid",TodaySchedule.UserID);
            bmobQuery.findObjects(new FindListener<Course>() {
                @Override
                public void done(List<Course> list, BmobException e) {
                    if(e==null){
                        String result = gson.toJson(list);
                        Intent intent = new Intent(ScheduleActivity.this,ShowJsonActivity.class);
                        intent.putExtra("json",result);
                        Toast.makeText(ScheduleActivity.this, "导出成功！", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }else {
                        Toast.makeText(ScheduleActivity.this,"读取课程时发生错误："+e.getErrorCode(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else {
            ArrayList<Course> coursesList = new ArrayList<>(); //课程列表
            SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from courses", null);
            if (cursor.moveToFirst()) {
                do {
                    coursesList.add(new Course(
                            cursor.getString(cursor.getColumnIndex("course_name")),
                            cursor.getString(cursor.getColumnIndex("teacher")),
                            cursor.getString(cursor.getColumnIndex("class_room")),
                            cursor.getInt(cursor.getColumnIndex("day")),
                            cursor.getInt(cursor.getColumnIndex("class_start")),
                            cursor.getInt(cursor.getColumnIndex("class_end"))));
                } while(cursor.moveToNext());
            }
            cursor.close();

            String result = gson.toJson(coursesList);
            Intent intent = new Intent(ScheduleActivity.this,ShowJsonActivity.class);
            intent.putExtra("json",result);
            Toast.makeText(ScheduleActivity.this, "导出成功！", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }

    private void fromJson(String json,boolean isSync){
        Gson gson = new Gson();
        Course[] courses;
        try {
            courses = gson.fromJson(json,Course[].class);
            for(Course course:courses){
                createLeftView(course);
                createItemCourseView(course);
            }
            Toast.makeText(this, "导入成功!", Toast.LENGTH_SHORT).show();
            if(isSync){
                if(!TodaySchedule.isLogged()){
                    Toast.makeText(this, "你并未登录!", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    for (Course course:courses){
                        Course course1 = new Course(
                                course.getCourseName(),
                                course.getTeacher(),
                                course.getClassRoom(),
                                course.getDay(),
                                course.getStart(),
                                course.getEnd()
                        );
                        bmob_saveData(course1);
                    }
                }
            }else {
                clearData();
                for (Course course:courses){
                    saveData(course);
                }
            }
        }catch (Exception e){
            Log.e("test",e.getMessage());
            Log.e("test", String.valueOf(e.getCause()));
            for(StackTraceElement s: e.getStackTrace()){
                Log.e("test", String.valueOf(s));
            }

            Toast.makeText(this, "解析失败，请检查数据格式是否有误!", Toast.LENGTH_SHORT).show();
        }

    }

    //创建"第几节数"视图
    private void createLeftView(Course course) {
        int endNumber = course.getEnd();
        if (endNumber > maxCoursesNumber) {
            for (int i = 0; i < endNumber-maxCoursesNumber; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.left_view, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110,180);
                view.setLayoutParams(params);

                TextView text = view.findViewById(R.id.class_number_text);
                text.setText(String.valueOf(++currentCoursesNumber));

                LinearLayout leftViewLayout = findViewById(R.id.left_view_layout);
                leftViewLayout.addView(view);
            }
            maxCoursesNumber = endNumber;
        }
    }

    //获得控件里面的星期几控件
    private RelativeLayout getViewDay(int day){
        int dayId = 0;
        switch (day) {
            case 1: dayId = R.id.monday; break;
            case 2: dayId = R.id.tuesday; break;
            case 3: dayId = R.id.wednesday; break;
            case 4: dayId = R.id.thursday; break;
            case 5: dayId = R.id.friday; break;
            case 6: dayId = R.id.saturday; break;
            case 7: dayId = R.id.weekday; break;
        }
        return findViewById(dayId);
    }

    //创建单个课程视图
    private void createItemCourseView(final Course course) {
        int getDay = course.getDay();
        if ((getDay < 1 || getDay > 7) || course.getStart() > course.getEnd())
            Toast.makeText(this, "星期几没写对,或课程结束时间比开始时间还早~~", Toast.LENGTH_LONG).show();
        else {

            day = getViewDay(getDay);

            int height = 180;
            final View v = LayoutInflater.from(this).inflate(R.layout.card_course, null); //加载单个课程布局
            v.setY(height * (course.getStart()-1)); //设置开始高度,即第几节课开始
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,(course.getEnd()-course.getStart()+1)*height - 8); //设置布局高度,即跨多少节课
            v.setLayoutParams(params);
            TextView text = v.findViewById(R.id.text_view);
            text.setText(course.getCourseName() + "\n" + course.getTeacher() + "\n" + course.getClassRoom()); //显示课程名
            CardView cardView = v.findViewById(R.id.course_card);
            cardView.setCardBackgroundColor(getResources().getColor(getCourseColor(course)));
            day.addView(v);

            //查看课程
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClickedView=view;
                    Intent intent = new Intent(ScheduleActivity.this, SeeCourseActivity.class);
                    intent.putExtra("seeCourse", course);
                    startActivityForResult(intent, 1);
                }
            });
        }
    }

    //清除课程
    private void clearItemCourseView(){
        for(int i =1;i<=7;i++){
            day = getViewDay(i);
            day.removeAllViews();
        }
    }

    private void initNote(Note note){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        note.setPosx(screenWidth/2);
        note.setPosy(screenHeight/2);
    }

    /**
     * 可能是最复杂的一个添加view方法
     * @param note
     * @return
     */
    private View addNote(Note note){
        View v = LayoutInflater.from(this).inflate(R.layout.card_note, null);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(150,150);
        v.setLayoutParams(params);
        v.setX(note.getPosx());
        v.setY(note.getPosy());
        DragViewUtil.registerDragAction(v, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
                BubbleLayout bl = new BubbleLayout(ScheduleActivity.this);
                //这个方法是不是在侮辱我智商
                bl.setBubbleColor(Color.rgb(255,246,224));
                NoteDialog noteDialog = new NoteDialog(ScheduleActivity.this);
                noteDialog.setNote(note)
                        //.addContentView(LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.bubble_view, null))
                        .setBubbleLayout(bl)
                        .setClickedView(v)
                        .autoPosition(Auto.LEFT_AND_RIGHT)
                        .show();
                noteDialog.setDelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delNote(note);
                        noteDialog.cancel();
                    }
                });
            }
        });

        noteContainer.addView(v);
        return v;
    }

    private void delNote(Note note){
        Note t = new Note(note);
        t.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e!=null){
                    Toast.makeText(ScheduleActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        noteArrayList.get(note).setVisibility(View.GONE);
        noteArrayList.remove(note);
    }

    private void clearNote(){
        for(Note note:noteArrayList.keySet()){
            Note t = new Note(note);
            t.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e!=null){
                        Toast.makeText(ScheduleActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            noteArrayList.get(note).setVisibility(View.GONE);
        }
        noteArrayList.clear();
    }

    private HashMap<Note,View> noteArrayList = new HashMap<>();

    private void loadNote(){
        if(TodaySchedule.isLogged()){
            noteContainer.removeAllViews();
            noteArrayList.clear();
            BmobQuery<Note> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("userid",TodaySchedule.UserID);
            bmobQuery.findObjects(new FindListener<Note>() {
                @Override
                public void done(List<Note> list, BmobException e) {
                    for(Note note:list){
                        noteArrayList.put(note,addNote(note));
                    }
                }
            });
        }
    }

    private void updateNote(){
        for(Note note:noteArrayList.keySet()){
            Note newNote = new Note(note);
            View tempv = noteArrayList.get(note);
            newNote.setPosx(tempv.getX());
            newNote.setPosy(tempv.getY());
            newNote.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e!=null){
                        Toast.makeText(ScheduleActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void clearData(){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("Delete from courses where 1=1");
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateNote();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        /*
            resultCode 通过在开始Activity时调用StartActivityForResult()设置
         */
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Course course = (Course) data.getSerializableExtra("course");
            //创建课程表左边视图(节数)
            createLeftView(course);
            //创建课程表视图
            createItemCourseView(course);
            //存储数据到数据库
            if(TodaySchedule.isLogged()){
                bmob_saveData(course);
            }else {
                saveData(course);
            }
        }

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Course PreCourse = (Course) data.getSerializableExtra("PreCourse");
            boolean isDelete = data.getBooleanExtra("isDelete", true);


            if (isDelete) {
                /*
                * 删除课程
                * */
                ClickedView.setVisibility(View.GONE);//先隐藏
                day = getViewDay(PreCourse.getDay());
                day.removeView(ClickedView);//再移除课程视图
                if(TodaySchedule.isLogged()){
                    Course tmp = new Course(PreCourse);
                    bmob_deleteData(tmp);
                }else {
                    deleteData(PreCourse);
                }
            } else {
                /*
                * 修改课程
                * */
                Intent intent = new Intent(ScheduleActivity.this, AddCourseActivity.class);
                intent.putExtra("ReviseCourse", PreCourse);
                intent.putExtra("isRevise", true);
                startActivityForResult(intent, 2);
            }
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            Course PreCourse = (Course) data.getSerializableExtra("PreCourse");
            Course newCourse = (Course) data.getSerializableExtra("newCourse");

            ClickedView.setVisibility(View.GONE);//先隐藏
            day = getViewDay(PreCourse.getDay());
            day.removeView(ClickedView);//再移除课程视图

            //创建课程表左边视图(节数)
            createLeftView(newCourse);
            //创建课程表视图
            createItemCourseView(newCourse);
            if(!TodaySchedule.isLogged()){
                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                sqLiteDatabase.execSQL("update courses set " +
                                "course_name = ?,teacher = ?,class_room=? ,day=? ,class_start=? ,class_end =?" +
                                "where course_name = ? and day =? and class_start=? and class_end=?",
                        new String[]{newCourse.getCourseName(),
                                newCourse.getTeacher(),
                                newCourse.getClassRoom(),
                                String.valueOf(newCourse.getDay()),
                                String.valueOf(newCourse.getStart()),
                                String.valueOf(newCourse.getEnd()),
                                PreCourse.getCourseName(),
                                String.valueOf(PreCourse.getDay()),
                                String.valueOf(PreCourse.getStart()),
                                String.valueOf(PreCourse.getEnd())});
            }else{
                Course course1 = new Course(
                        newCourse.getCourseName(),
                        newCourse.getTeacher(),
                        newCourse.getClassRoom(),
                        newCourse.getDay(),
                        newCourse.getStart(),
                        newCourse.getEnd(),
                        newCourse.getUserid()
                );
                course1.setObjectId(newCourse.getObjectId());
                bmob_changeData(course1);

            }

        }

        if(requestCode == TodaySchedule.REQUEST_PUT_JSON && resultCode == Activity.RESULT_OK && data != null){
            fromJson(data.getStringExtra("json"),data.getBooleanExtra("isSave",false));
        }

        if(requestCode == TodaySchedule.REQUEST_ADD_NOTE && resultCode == Activity.RESULT_OK && data != null){
            Note note = (Note) data.getSerializableExtra("newNote");
            initNote(note);

            //addNote(note);
            note.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e!=null){
                        Toast.makeText(ScheduleActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        //updateNote();
                        loadNote();
                        //noteArrayList.put(note,addNote(note));
                    }
                }
            });
        }

    }

}