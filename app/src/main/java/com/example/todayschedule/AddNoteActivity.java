package com.example.todayschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.todayschedule.bean.Note;

public class AddNoteActivity extends AppCompatActivity {

    Button submitBotton;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getWindow().setBackgroundDrawableResource(R.color.note_bg);
        submitBotton = findViewById(R.id.submit_note);
        editText = findViewById(R.id.edit_note);
        submitBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Note newNote = new Note(editText.getText().toString(),TodaySchedule.UserID,0,0,0,0);
                Intent intent = new Intent();
                intent.putExtra("newNote", newNote);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}