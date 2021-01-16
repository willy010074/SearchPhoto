package com.example.chungyu.topic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class LableActivity extends AppCompatActivity {
    EditText editText;
    Button btn_no;
    Button btn_yes;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lable);

        editText = (EditText) findViewById(R.id.editText);
        btn_no = (Button) findViewById(R.id.btn_lable_no);
        btn_yes = (Button) findViewById(R.id.btn_lable_yes);
    }
}
