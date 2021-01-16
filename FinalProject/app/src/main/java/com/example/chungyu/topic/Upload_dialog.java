package com.example.chungyu.topic;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Upload_dialog extends Dialog {

    private View.OnClickListener onNegateClickListener;
    private View.OnClickListener onPositiveClickListener;
    private Button btn_camera;
    private Button btn_album;

    public Upload_dialog(@NonNull Context context) {
        super(context);
    }

    public Upload_dialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_dialog);

        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_album = (Button) findViewById(R.id.btn_album);
        btn_camera.setOnClickListener(onPositiveClickListener);
        btn_album.setOnClickListener(onNegateClickListener);
    }

    public void setOnPositiveListener(View.OnClickListener onPositiveClickListener) {
        this.onPositiveClickListener = onPositiveClickListener;
    }

    public void setOnNegateListener(View.OnClickListener onNegateClickListener) {
        this.onNegateClickListener = onNegateClickListener;
    }
}
