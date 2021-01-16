package com.example.chungyu.topic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImagePage extends AppCompatActivity implements View.OnClickListener {

    private ImageLoader Loader = ImageLoader.getInstance();
    private ImageView imageView;
    private DisplayImageOptions options;
    private ImageLoaderConfiguration config;
    private int position;
    private ArrayList<String> image = new ArrayList<>();
    private Button btn;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_page);

        Intent intent = getIntent();
        position = intent.getIntExtra("id", 0);
        image = intent.getStringArrayListExtra("image");
        name = intent.getStringExtra("name");
        btn = (Button) findViewById(R.id.btn_lable);
        btn.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                Loader.displayImage(image.get(position),imageView);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_lable) {
            Intent it = new Intent(ImagePage.this,LableActivity.class);
            it.putExtra("name",name);
            startActivity(it);
        }
    }
}
