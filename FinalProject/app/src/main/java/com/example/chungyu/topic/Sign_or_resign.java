package com.example.chungyu.topic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Sign_or_resign extends Activity implements View.OnClickListener {

    private Button sr_btn_sign;
    private Button sr_btn_resign;
    String uploadUri="http://cs208.csie.ncyu.edu.tw/signup.php";
    String UserName = "";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_or_resign);

        sr_btn_sign = (Button) findViewById(R.id.sr_btn_sign);
        sr_btn_resign = (Button) findViewById(R.id.sr_btn_resign);
        sr_btn_sign.setOnClickListener(this);
        sr_btn_resign.setOnClickListener(this);
        onGet();

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        UserName = sharedPreferences.getString("UserName", null);
        if (UserName != null) {
            Intent it = new Intent(Sign_or_resign.this, Home.class);
            it.putExtra("name",UserName);
            startActivity(it);
            Sign_or_resign.this.finish();
        }
    }

    public void onGet() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //No Permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET}, 200);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sr_btn_sign:
                final Sign_in loginDialog = new Sign_in(this,R.style.MyDialog);

                loginDialog.setOnNegateListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginDialog.dismiss();
                    }
                });
                loginDialog.setOnPositiveListener(new Sign_in.PositiveListener() {
                    @Override
                    public void onClick(boolean isSuccess, String name) {
                        if (isSuccess) {
                            sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                            sharedPreferences.edit().putString("UserName", name).apply();
                            loginDialog.dismiss();
                            Intent it = new Intent(Sign_or_resign.this, Home.class);
                            it.putExtra("name",name);
                            startActivity(it);
                            Sign_or_resign.this.finish();
                        }
                    }
                });
                loginDialog.show();
                break;

            case R.id.sr_btn_resign:
                final Resign resignDialog = new Resign(this,R.style.MyDialog);

                resignDialog.setOnNegateListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resignDialog.dismiss();
                    }
                });
                resignDialog.setOnPositiveListener(new Resign.PositiveListener() {
                    @Override
                    public void onClick(boolean isSuccess, final EditText user, final EditText pwd) {
                        if (isSuccess) {
                            resignDialog.dismiss();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    postToServer(uploadUri,user.getText().toString(),pwd.getText().toString());
                                }
                            }).start();
                            UserName = user.getText().toString();
                            Toast.makeText(Sign_or_resign.this,UserName + "註冊成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                resignDialog.show();
                break;

        }
    }

    public void postToServer(String address, final String user, final String passwd) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
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
                params.put("name", user);
                params.put("passwd", passwd);
                return params;
            }
        };
        RequestQueue requestQueueUpload = Volley.newRequestQueue(this);
        requestQueueUpload.add(stringRequest);
    }
}
