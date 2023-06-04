package com.example.todayschedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

public class SetBgActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 千万不要改cardview的数量...
     */
    private CardView[] cardViews = new CardView[7];
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_bg);
        sharedPreferences = getSharedPreferences("global",MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true);
        cardViews[0] = findViewById(R.id.selbg1);
        cardViews[1] = findViewById(R.id.selbg2);
        cardViews[2] = findViewById(R.id.selbg3);
        cardViews[3] = findViewById(R.id.selbg4);
        cardViews[4] = findViewById(R.id.selbg5);
        cardViews[5] = findViewById(R.id.selbg6);
        cardViews[6] = findViewById(R.id.custombg);
        for(CardView cardView : cardViews){
            cardView.setOnClickListener(this);
        }
    }
    @Override
    public void onClick(View v) {
        int i = 0;
        switch (v.getId()){
            case R.id.selbg1:{
                i++;
            }
            case R.id.selbg2:{
                i++;
            }
            case R.id.selbg3:{
                i++;
            }
            case R.id.selbg4:{
                i++;
            }
            case R.id.selbg5:{
                i++;
            }
            case R.id.selbg6:{
                i++;
            }
            case R.id.custombg:{
                i++;
                TodaySchedule.background_id = 7-i;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("bg",String.valueOf(TodaySchedule.background_id));
                if(i==1){
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, TodaySchedule.REQUEST_SELECT_PIC);
                }else{
                    finish();
                }

                break;
            } default:{
                break;
            }
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TodaySchedule.REQUEST_SELECT_PIC) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                TodaySchedule.background_url = uri;
                finish();
                //Base64Coder.getBase64FromFile(this,uri);
            }
        }
    }
}