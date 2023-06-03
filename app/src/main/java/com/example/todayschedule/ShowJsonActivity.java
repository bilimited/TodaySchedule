package com.example.todayschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ShowJsonActivity extends AppCompatActivity {
    String copyStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_json);
        getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        copyStr = getIntent().getStringExtra("json");

        EditText editText = findViewById(R.id.showjson);
        editText.setText(copyStr);

        Button cpy = findViewById(R.id.btn_copy);
        cpy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //获取剪贴板管理器
                    ClipboardManager cm = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    }
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", copyStr);
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    Toast.makeText(ShowJsonActivity.this, "内容已复制到剪贴板", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
        Button ret = findViewById(R.id.btn_return);
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}