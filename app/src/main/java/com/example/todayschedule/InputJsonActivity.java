package com.example.todayschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class InputJsonActivity extends AppCompatActivity {

    EditText editText;
    RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_json);
        getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        Button button = findViewById(R.id.submit_json_button);
        editText = findViewById(R.id.showjson);
        radioButton = findViewById(R.id.radio_isSave);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = editText.getText().toString();
                Intent intent = new Intent(InputJsonActivity.this, ScheduleActivity.class);
                intent.putExtra("json", json);
                intent.putExtra("isSave",radioButton.isChecked());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
}