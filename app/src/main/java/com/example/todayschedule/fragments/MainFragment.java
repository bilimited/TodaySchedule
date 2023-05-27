package com.example.todayschedule.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todayschedule.EditActivity;
import com.example.todayschedule.NoticeActivity;
import com.example.todayschedule.R;
import com.example.todayschedule.ScheduleActivity;
import com.example.todayschedule.TodaySchedule;
import com.example.todayschedule.bean.Image;
import com.example.todayschedule.bean.Notice;
import com.example.todayschedule.tool.Base64Coder;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View theview;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        theview = inflater.inflate(R.layout.fragment_main, container, false);
        init();
        return theview;
    }

    LinearLayout container;

    public void init(){
        container = theview.findViewById(R.id.notice_board);
        CardView cardView = theview.findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        reflesh();
    }

    private void loadNotice(){
        container.removeAllViews();

        BmobQuery<Notice> bmobQuery = new BmobQuery<>();

        bmobQuery.findObjects(new FindListener<Notice>() {
            @Override
            public void done(List<Notice> list, BmobException e) {
                if(e==null){
                    for(int i =list.size()-1;i>=0;i--){
                        addNotice(list.get(i));
                    }
                }else {
                    Toast.makeText(getContext(), "发生异常:"+e.getErrorCode(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addNotice(Notice notice){
        if(notice==null){
            return;
        }
        final View v = LayoutInflater.from(getContext()).inflate(R.layout.card_notice, null);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                intent.putExtra("Notice",notice);
                startActivity(intent);
            }
        });
        TextView title = v.findViewById(R.id.title);
        TextView content = v.findViewById(R.id.content);
        ImageView imageView = v.findViewById(R.id.notice_img);
        title.setText(notice.getTitle());
        content.setText(notice.getContent());
        if(notice.getImgID()!=null&&!notice.getImgID().equals("")){
            Base64Coder.LoadImage(getActivity(),notice.getImgID(),imageView);
        }else {
            imageView.setMaxWidth(0);
        }

        container.addView(v);
    }

    /**
     * 由于多线程问题，这个也是摆设
     */
    private void reflesh(){
        TextView addButton = theview.findViewById(R.id.addNew);
        if(TodaySchedule.isAdmin()){
            addButton.setText("添加");
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), EditActivity.class);
                    intent.putExtra("isNotice",true);
                    startActivity(intent);
                }
            });
        }else{
            addButton.setText("");
        }

        loadNotice();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}