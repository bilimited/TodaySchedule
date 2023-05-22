package com.example.todayschedule.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todayschedule.EditActivity;
import com.example.todayschedule.PostActivity;
import com.example.todayschedule.R;
import com.example.todayschedule.bean.Course;
import com.example.todayschedule.bean.Post;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ForumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForumFragment newInstance(String param1, String param2) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View theview;
    LinearLayout container;
    BottomSheetDialog bottomSheetDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        theview = inflater.inflate(R.layout.fragment_forum, container, false);
        init();
        return theview;
    }

    @Override
    public void onStart() {
        super.onStart();
        reflesh();
    }

    public void init(){
        container = theview.findViewById(R.id.container);
        ImageView add = theview.findViewById(R.id.forum_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtra("isPost",true);
                startActivity(intent);
            }
        });

    }


    public void loadPost(){



        ArrayList<Post> postList = new ArrayList<>(); //课程列表
        BmobQuery<Post> bmobQuery = new BmobQuery<>();

        bmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if(e==null){
                    for(int i =list.size()-1;i>=0;i--){
                        addPost(list.get(i));
                    }
                }else {
                    Toast.makeText(getContext(), "发生异常:"+e.getErrorCode(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void addPost(Post post){
        final View v = LayoutInflater.from(getContext()).inflate(R.layout.card_post, null);
        TextView username = v.findViewById(R.id.post_username);
        TextView userinfo = v.findViewById(R.id.post_userinfo);
        TextView content = v.findViewById(R.id.post_content);
        TextView likes = v.findViewById(R.id.like_number);
        TextView comments = v.findViewById(R.id.comment_number);
        TextView date = v.findViewById(R.id.date);
        ImageView like = v.findViewById(R.id.like);

        /*
        限制预览的内容长度
         */
        if(post.getContent().length()>=100){
            content.setText(post.getContent().substring(0,100)+"...");
        }else {
            content.setText(post.getContent());
        }

        username.setText(post.getAuthor());
        userinfo.setText(post.getAuthorinfo());
        likes.setText(String.valueOf(post.getLikes()));
        comments.setText(String.valueOf(post.getComments()));
        date.setText(post.getCreatedAt());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.putExtra("post",post);
                startActivity(intent);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like(post);
                likes.setText(String.valueOf(post.getLikes()));
            }
        });
        container.addView(v);
    }

    public void reflesh(){
        container.removeAllViews();
        loadPost();
    }

    public void like(Post post){
        Post temp = new Post(post.getObjectId());
        temp.setLikes(post.getLikes()+1);
        post.setLikes(post.getLikes()+1);
        temp.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e!=null){
                    Toast.makeText(getActivity(), "发生异常:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}