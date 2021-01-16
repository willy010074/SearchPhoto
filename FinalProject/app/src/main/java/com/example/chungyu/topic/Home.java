package com.example.chungyu.topic;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class Home extends Navigation_BaseActivity{

    private Menu drawerMenu;
    private DrawerLayout drawerLayout;
    private EditText search_input;
    private ImageView search_button;
    private ImageView update_button;
    private ViewPager myViewPager;
    private GridView photo_list;
    private Toolbar toolbar;
    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    private static String UserName = "";
    private String downloadUri = null;
    private String SearchUri = null;
    private String keyword_tmp = "";
    private ArrayList<String> image = new ArrayList<>();
    RequestQueue requestQueueImg;
    InputMethodManager imm;

    Timer timerExit = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            isExit = false;
            hasTask = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerMenu = (Menu) findViewById(R.id.group);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        search_button = (ImageView) findViewById(R.id.search_button);
        update_button = (ImageView) findViewById(R.id.update_button);
        search_input = (EditText) findViewById(R.id.search_input);
        downloadUri = "http://cs208.csie.ncyu.edu.tw/downloadImg.php";
        SearchUri = "http://cs208.csie.ncyu.edu.tw/search.php";
        Intent it = getIntent();
        UserName = it.getStringExtra("name");
        photo_list = (GridView) findViewById(R.id.photo_list);
        toolbar = (Toolbar) findViewById(R.id.ToolBar);
        setUpToolBar();
        toolbar.setBackgroundColor(Color.LTGRAY);
        CurrentMenuItem = 0;
        NV.getMenu().getItem(CurrentMenuItem).setChecked(true);//設置Navigation目前項目被選取狀態
        toolbar.setTitleTextColor(Color.BLACK);
        setUpNavigation(UserName);
        requestQueueImg = Volley.newRequestQueue(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        search_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null &&  event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    keyword_tmp = search_input.getText().toString();
                    startToSearch(SearchUri, keyword_tmp);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            photo_list.setAdapter(new ImageAdapter(Home.this,image));
                        }
                    },500);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(),0);
                }
                return false;
            }
        });

        downloadImage(downloadUri);
        new Handler().postDelayed(new Runnable(){
            public void run()
            {
                photo_list.setAdapter(new ImageAdapter(Home.this,image));
                photo_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(Home.this,ImagePage.class);
                        intent.putExtra("id",position);
                        intent.putExtra("name",UserName);
                        intent.putExtra("image",image);
                        startActivity(intent);
                    }
                });

                search_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //System.out.println(search_input.getText().toString());
                        keyword_tmp = search_input.getText().toString();
                        startToSearch(SearchUri, keyword_tmp);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                photo_list.setAdapter(new ImageAdapter(Home.this,image));
                            }
                        },500);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(),0);
                    }
                });

                update_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                });
            }
        },500);

    }

    private void refresh(){
        downloadImage(downloadUri);
        new Handler().postDelayed(new Runnable(){
            public void run()
            {
                photo_list.setAdapter(new ImageAdapter(Home.this,image));
                photo_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(Home.this,ImagePage.class);
                        intent.putExtra("id",position);
                        intent.putExtra("name",UserName);
                        intent.putExtra("image",image);
                        startActivity(intent);
                    }
                });
            }
        },500);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        else if (search_input.getText().toString().isEmpty()) {
            downloadImage(downloadUri);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    photo_list.setAdapter(new ImageAdapter(Home.this,image));
                    // 是否要退出
                    if (isExit == false) {
                        isExit = true; //記錄下一次要退出
                        Toast.makeText(Home.this, "再按一次返回退出APP", Toast.LENGTH_SHORT).show();
                        // 如果超過兩秒則恢復預設值
                        if (!hasTask) {
                            timerExit.schedule(task, 2000);
                        }
                    }
                    else {
                        Home.this.finish(); // 離開程式
                        System.exit(0);
                    }
                }
            },100);
        }
        else {
            search_input.setText("");
            downloadImage(downloadUri);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    photo_list.setAdapter(new ImageAdapter(Home.this,image));
                }
            },500);
        }

    }

   /* public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判斷是否按下Back
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (search_input.getText().toString().isEmpty()) {
                downloadImage(downloadUri);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        photo_list.setAdapter(new ImageAdapter(Home.this,image));
                        // 是否要退出
                        if (isExit == false) {
                            isExit = true; //記錄下一次要退出
                            Toast.makeText(Home.this, "再按一次返回退出APP", Toast.LENGTH_SHORT).show();
                            // 如果超過兩秒則恢復預設值
                            if (!hasTask) {
                                timerExit.schedule(task, 2000);
                            }
                        }
                        else {
                            Home.this.finish(); // 離開程式
                            System.exit(0);
                        }
                    }
                },100);
            }
            else {
                search_input.setText("");
                downloadImage(downloadUri);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        photo_list.setAdapter(new ImageAdapter(Home.this,image));
                    }
                },500);
            }
        }
        return false;
    }*/

    public void downloadImage(final String address) {
        image.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        String tmp = ServerResponse;
                        tmp = tmp.replace("[","");
                        tmp = tmp.replace("]","");
                        tmp = tmp.replace("\"","");
                        tmp = tmp.replace("\\","");

                        String[] allImagePath = tmp.split(",");
                        for(int i =0;i<allImagePath.length;i++) {
                            allImagePath[i] = "http://cs208.csie.ncyu.edu.tw/" + allImagePath[i];
                            image.add(allImagePath[i]);
                        }
                        //getImage(allImagePath);
                        //photo_list.setAdapter(new ImageAdapter(Home.this,allImagePath));
                        //img.setImageBitmap(getBitmapFromURL("UserUploadFile//test5//image//IMG_20170830_183522.jpg"));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                // Adding All values to Params.
                params.put("user", UserName);
                return params;
            }
        };
        requestQueueImg.add(stringRequest);
    }

    public void startToSearch(String address, final String keyword) {
        image.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        String tmp = ServerResponse;
                        tmp = tmp.replace("[","");
                        tmp = tmp.replace("]","");
                        tmp = tmp.replace("\"","");
                        tmp = tmp.replace("\\","");
                        tmp = tmp.replace("after_tesseract","/image");
                        tmp = tmp.replace(".txtn","\n");

                        String[] allImagePath = tmp.split("\n");
                        for(int i =0;i<allImagePath.length;i++) {
                            allImagePath[i] = "http://cs208.csie.ncyu.edu.tw/UserUploadFile/" + UserName + allImagePath[i];
                            image.add(allImagePath[i]);
                        }
                        //photo_list.setAdapter(new ImageAdapter(Home.this,allImagePath));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();
                // Adding All values to Params.
                params.put("user", UserName);
                params.put("word", keyword);
                return params;
            }
        };
        requestQueueImg.add(stringRequest);
    }


}