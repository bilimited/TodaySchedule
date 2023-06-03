package com.example.todayschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SetBgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_bg);
        getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));


    }
}