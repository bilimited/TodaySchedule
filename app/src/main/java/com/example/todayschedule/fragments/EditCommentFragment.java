package com.example.todayschedule.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todayschedule.R;
import com.example.todayschedule.TodaySchedule;
import com.example.todayschedule.bean.Comment;
import com.example.todayschedule.bean.Post;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class EditCommentFragment extends BottomSheetDialogFragment {

    View theview;
    Post post;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        theview = LayoutInflater.from(getContext()).inflate(R.layout.fragment_editcomment, null);
        dialog.setContentView(theview);

        init();

        return dialog;
    }

    private BottomSheetBehavior<FrameLayout> behavior;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        View bottomView = theview.findViewById(R.id.btm_sheet_comment);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomView);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void init(){
        Button btn = theview.findViewById(R.id.submit_comment);
        EditText editText = theview.findViewById(R.id.edit_comment);
        Bundle bundle = getArguments();
        if(bundle!=null){
            post = (Post) bundle.getSerializable("Post");
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TodaySchedule.isLogged()){
                    Toast.makeText(getContext(), "请先登录!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Comment comment = new Comment(
                        editText.getText().toString(),
                        TodaySchedule.LoggedAccount,
                        TodaySchedule.UserID,
                        post.getAuthorid(),
                        post.getObjectId()
                        );
                comment.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            Toast.makeText(getActivity(), "发表成功!", Toast.LENGTH_SHORT).show();

                            Post temp = new Post(post.getObjectId());
                            temp.setComments(post.getComments()+1);
                            temp.setLikes(post.getLikes());
                            post.setComments(post.getComments()+1);
                            temp.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e!=null){
                                        Toast.makeText(getActivity(), "发生错误"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            getActivity().onBackPressed();

                        }else {
                            Toast.makeText(getActivity(), "发表失败:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //behavior.setHideable(false);

    }


}
