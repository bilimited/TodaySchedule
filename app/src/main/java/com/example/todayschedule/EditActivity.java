package com.example.todayschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.todayschedule.bean.Comment;
import com.example.todayschedule.bean.Image;
import com.example.todayschedule.bean.Notice;
import com.example.todayschedule.bean.Post;
import com.example.todayschedule.tool.ArtificialIdiot;
import com.example.todayschedule.tool.Base64Coder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditActivity extends AppCompatActivity {

    private EditText edit_title,edit_content;

    /**
     * isPost,isNotice:是Post还是Notice
     * 离谱代码，但我懒得改了
     */
    private boolean isPost = false;
    private boolean isNotice = false;

    /**
     * TODO:草稿标题和内容
     */
    private static String draft_title;
    private static String draft_content;

    //用来预览的图片
    ImageView sel_img;
    //选择图片的Uri
    Uri sel_uri;





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

        sel_img = findViewById(R.id.select_img);
        sel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, TodaySchedule.REQUEST_SELECT_PIC);
            }
        });

        /**
         * 点击提交事件。
         */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TodaySchedule.isLogged()){
                    Toast.makeText(EditActivity.this, "请先登录!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String base64 = Base64Coder.getBase64FromFile(EditActivity.this,sel_uri);
                if(base64!=null){
                    Toast.makeText(EditActivity.this, "Base64 length:"+base64.length(), Toast.LENGTH_SHORT).show();
                }


                if(sel_uri!=null){
                    Image image = new Image();
                    image.setBase64(base64);
                    image.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e!=null){
                                Toast.makeText(EditActivity.this, "图片上传失败:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }else {
                                save(image.getObjectId());
                            }
                        }
                    });
                }else {
                    save(null);
                }

            }
        });

    }

    private void save(String imgID){
        if(isPost){
            Post post = new Post("<h1>"+
                            edit_title.getText().toString()+
                            "</h1>"
                            +edit_content.getText().toString(),
                    0,
                    TodaySchedule.LoggedAccount,
                    "无",
                    TodaySchedule.UserID);
            if(ArtificialIdiot.isCallingXiaoAi(post.getContent())){
                post.setComments(1);
            }
            post.setImgID(imgID);
            post.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){
                        Toast.makeText(EditActivity.this, "发表成功!", Toast.LENGTH_SHORT).show();
                        if(ArtificialIdiot.isCallingXiaoAi(post.getContent())){
                            ArtificialIdiot.getAIResponce(post.getContent(),post.getObjectId());
                        }
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
            notice.setImgID(imgID);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TodaySchedule.REQUEST_SELECT_PIC) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                sel_uri = uri;
                sel_img.setImageURI(uri);
                //Base64Coder.getBase64FromFile(this,uri);
            }
        }
    }
}