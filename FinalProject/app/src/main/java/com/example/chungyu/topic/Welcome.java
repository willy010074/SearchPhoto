package com.example.chungyu.topic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class Welcome extends Activity /*implements View.OnTouchListener*/ {

    private ConstraintLayout constraintLayout;
    private FlickerTextView txv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        constraintLayout = (ConstraintLayout) findViewById(R.id.Welcome);
        //constraintLayout.setOnTouchListener(this);
        txv = (FlickerTextView) findViewById(R.id.textView4);

        new Handler().postDelayed(new Runnable(){
            public void run()
            {
                Intent it = new Intent(getApplication(),Sign_or_resign.class);
                startActivity(it);
                finish();
                //execute the task
            }
        },3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Hiiiiiiiiiiiiiiiiiiii!!!!!!!!!!!!");
            }
        }
    }
}
