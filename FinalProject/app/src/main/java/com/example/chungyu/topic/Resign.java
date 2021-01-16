package com.example.chungyu.topic;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Resign extends Dialog {

    private EditText resign_user_input;
    private EditText resign_passwd_input;
    private EditText resign_repasswd_input;
    private Button resign_btn_check;
    private Button resign_btn_cancel;
    private Button resign_btn_resign;
    private TextView resign_txv_check;
    private TextView resign_txv_msg;
    private View.OnClickListener onNegateClickListener;
    //private PositiveListener onCheckListener;
    private PositiveListener onPositiveClickListener;
    String showUri = "http://cs208.csie.ncyu.edu.tw/login.php";
    com.android.volley.RequestQueue requestQueueDownload;

    public Resign(@NonNull Context context) {
        super(context);
    }

    public Resign(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resign);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        resign_user_input = (EditText) findViewById(R.id.resign_user_input);
        resign_passwd_input = (EditText) findViewById(R.id.resign_passwd_input);
        resign_repasswd_input = (EditText) findViewById(R.id.resign_repasswd_input);
        resign_txv_check = (TextView) findViewById(R.id.resign_txv_check);
        resign_txv_msg = (TextView) findViewById(R.id.resign_txv_msg);
        resign_btn_check = (Button) findViewById(R.id.resign_btn_check);
        resign_btn_cancel = (Button) findViewById(R.id.resign_btn_cancel);
        resign_btn_resign = (Button) findViewById(R.id.resign_btn_resign);

        resign_btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect(showUri, new Resign_Callback() {
                    @Override
                    public void onSuccess(ArrayList<String> user, ArrayList<String> pwd) {
                        boolean flag = false;
                        int i;
                        resign_txv_check.setTextColor(Color.RED);
                        for (i = 0; i<user.size(); i++) {
                            if (String.valueOf(resign_user_input.getText()).equals(user.get(i))) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            resign_txv_check.setText("此帳號尚未註冊");
                            resign_txv_check.setTextColor(Color.GREEN);
                        } else {
                            resign_txv_check.setText("此帳號已被註冊");
                            resign_txv_check.setTextColor(Color.RED);
                        }
                    }
                });
            }
        });
        resign_btn_cancel.setOnClickListener(onNegateClickListener);
        resign_btn_resign.setOnClickListener(new View.OnClickListener() {
            boolean isSuccess = false;
            @Override
            public void onClick(View v) {
                connect(showUri, new Resign_Callback() {
                    @Override
                    public void onSuccess(ArrayList<String> user, ArrayList<String> pwd) {
                        boolean flag = false;
                        int i;
                        for (i = 0; i<user.size(); i++) {
                            if (String.valueOf(resign_user_input.getText()).equals(user.get(i))) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            isSuccess = true;
                            if (!resign_repasswd_input.getText().toString().equals(resign_passwd_input.getText().toString())) {
                                resign_txv_msg.setText("密碼不同，請再輸入一次");
                                resign_txv_msg.setTextColor(Color.RED);
                                isSuccess = false;
                            }
                        } else {
                            resign_txv_msg.setText("此帳號已被註冊");
                            resign_txv_msg.setTextColor(Color.RED);
                        }
                    }
                });
                if (onPositiveClickListener != null) {
                    if (isSuccess) {
                        //postToServer(uploadUri);
                    }
                    onPositiveClickListener.onClick(isSuccess,resign_user_input,resign_passwd_input);
                }
            }
        });
        requestQueueDownload = Volley.newRequestQueue(getContext());
    }

    public void setOnPositiveListener(PositiveListener onPositiveClickListener) {
        this.onPositiveClickListener = onPositiveClickListener;
    }

    public void setOnNegateListener(View.OnClickListener onNegateClickListener) {
        this.onNegateClickListener = onNegateClickListener;
    }

    public interface PositiveListener {
        public void onClick(boolean flag,EditText user,EditText pwd);
    }

    public void connect(String address, final Resign_Callback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST
                ,address, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    JSONArray data = response.getJSONArray("data");

                    ArrayList<String> User = new ArrayList<String>();
                    ArrayList<String> Pwd =new ArrayList<String>();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jasondata = data.getJSONObject(i);
                        String user = jasondata.getString("name");
                        String pwd = jasondata.getString("passwd");

                        User.add(user);
                        Pwd.add(pwd);
                    }
                    callback.onSuccess(User,Pwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }
        });
        requestQueueDownload.add(jsonObjectRequest);
    }
}
