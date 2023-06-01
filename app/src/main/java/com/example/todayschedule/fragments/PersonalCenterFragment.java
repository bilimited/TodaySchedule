package com.example.todayschedule.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todayschedule.AboutActivity;
import com.example.todayschedule.LoginActivity;
import com.example.todayschedule.MainActivity;
import com.example.todayschedule.R;
import com.example.todayschedule.RegisterActivity;
import com.example.todayschedule.SettingActivity;
import com.example.todayschedule.TodaySchedule;
import com.example.todayschedule.bean.User_Info;
import com.example.todayschedule.tool.Base64Coder;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalCenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalCenterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PersonalCenterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalCenterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalCenterFragment newInstance(String param1, String param2) {
        PersonalCenterFragment fragment = new PersonalCenterFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        theview = inflater.inflate(R.layout.fragment_personal_center, container, false);
        return theview;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        TextView userid = theview.findViewById(R.id.userid);
        TextView info = theview.findViewById(R.id.desc);
        ImageView imageView = theview.findViewById(R.id.pc_portrait);
        LinearLayout container = theview.findViewById(R.id.container);
        RelativeLayout btn1 = theview.findViewById(R.id.btn1);
        RelativeLayout btn3 = theview.findViewById(R.id.btn3);
        RelativeLayout btn4 = theview.findViewById(R.id.btn4);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TodaySchedule.isLogged()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setMessage("确认退出?");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            TodaySchedule.logout();
                            //loadData();//重置数据
                            dialog.dismiss();
                            Intent intent = new Intent(getContext(), LoginActivity.class);
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
                    Toast.makeText(getContext(), "你好像...并没有登录", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });
        final View v = LayoutInflater.from(getContext()).inflate(R.layout.card_login_or_register, null);
        TextView btnl = v.findViewById(R.id.btn_login);
        Button btnr = v.findViewById(R.id.btn_register);
        btnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });

        btnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        container.addView(v);
        if(TodaySchedule.isLogged()){
            for(int i = 0;i<container.getChildCount();i++){
                container.getChildAt(i).setVisibility(View.VISIBLE);
            }
            v.setVisibility(imageView.GONE);
            User_Info.findUserInfo(TodaySchedule.UserID, new FindListener<User_Info>() {
                @Override
                public void done(List<User_Info> list, BmobException e) {
                    if(e==null&&!list.isEmpty()){
                        User_Info user_info = list.get(0);
                        userid.setText(user_info.getNickName());
                        info.setText("id:"+TodaySchedule.LoggedAccount);
                        Base64Coder.LoadProtrait(getActivity(),user_info.getPortraitID(),imageView);
                    }

                }
            });
        }else {
            for(int i = 0;i<container.getChildCount();i++){
                container.getChildAt(i).setVisibility(View.GONE);
            }
            v.setVisibility(View.VISIBLE);
            userid.setText("你没有登录");
            info.setText("快去登录");


        }


    }

}