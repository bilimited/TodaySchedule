package com.example.todayschedule;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todayschedule.bean.Comment;
import com.example.todayschedule.bean.Image;
import com.example.todayschedule.bean.Post;
import com.example.todayschedule.fragments.EditCommentFragment;
import com.example.todayschedule.tool.Base64Coder;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class PostActivity extends AppCompatActivity {

    Post post;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ConstraintLayout constraintLayout = findViewById(R.id.activity_post_layout);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.white));

        Toolbar toolbar = findViewById(R.id.toolbar);

        TextView username = findViewById(R.id.post_username);
        TextView userinfo = findViewById(R.id.post_userinfo);
        TextView content = findViewById(R.id.post_content);
        TextView likes = findViewById(R.id.like_number);
        TextView date = findViewById(R.id.date);
        TextView delete = findViewById(R.id.delete_post);
        TextView comments = findViewById(R.id.comment_number);
        ImageView img = findViewById(R.id.image);

        post = (Post) getIntent().getSerializableExtra("post");

        username.setText(post.getAuthor());
        userinfo.setText(post.getAuthorinfo());
        content.setText(post.getContent());
        likes.setText(String.valueOf(post.getLikes()));
        date.setText(post.getCreatedAt());
        comments.setText(String.valueOf(post.getComments()));

        if(post.getImgID()!=null&&!post.getImgID().equals("")){
            Base64Coder.LoadImage(this,post.getImgID(),img);
        }
        if(post.getAuthorid().equals(TodaySchedule.UserID)||TodaySchedule.isAdmin()){
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(PostActivity.this);
                    builder.setMessage("确认删除?");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            /*
                            先删除帖子的评论，再删除帖子
                             */
                            BmobQuery<Comment> commentBmobQuery = new BmobQuery<>();
                            commentBmobQuery.addWhereEqualTo("to_post",post.getObjectId());
                            commentBmobQuery.findObjects(new FindListener<Comment>() {
                                @Override
                                public void done(List<Comment> list, BmobException e) {
                                    if(e==null){
                                        for(Comment comment:list){
                                            Comment temp = new Comment(comment.getObjectId());
                                            temp.delete(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    Toast.makeText(PostActivity.this, "评论删除错误:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }else {
                                        Toast.makeText(PostActivity.this, "评论查找错误:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            Post post1=new Post(post.getObjectId());

                            post1.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e!=null){
                                        Toast.makeText(PostActivity.this, "帖子删除错误:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("test",e.getMessage());
                                    }else {
                                        Toast.makeText(PostActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });



                            dialog.dismiss();
                            finish();
                        }

                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
        }else {
            delete.setText("");
        }

        ImageView imageView = findViewById(R.id.comment_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditCommentFragment editCommentFragment = new EditCommentFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Post",post);
                editCommentFragment.setArguments(bundle);
                editCommentFragment.show(getSupportFragmentManager(),"EditCommentFragment");
            }
        });

        container = findViewById(R.id.comment_container);
        load_comment();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void load_comment(){
        BmobQuery<Comment> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.addWhereEqualTo("to_post",post.getObjectId());
        commentBmobQuery.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if(e==null){
                    for(Comment comment:list){
                        add_comment(comment);
                    }
                }else {
                    Toast.makeText(PostActivity.this, "发生异常:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void add_comment(Comment comment){
        final View v = LayoutInflater.from(PostActivity.this).inflate(R.layout.card_comment, null);
        TextView username = v.findViewById(R.id.post_username);
        TextView content = v.findViewById(R.id.post_content);
        TextView date = v.findViewById(R.id.date);

        username.setText(comment.getAuthor());
        content.setText(comment.getContent());
        date.setText(comment.getCreatedAt());

        container.addView(v);
    }


}