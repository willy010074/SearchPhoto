package com.example.chungyu.topic;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Sign_in extends Dialog {

    private EditText editText_user;
    private EditText editText_passwd;
    private Button btn_cancel;
    private Button btn_login;
    private TextView txv;
    private View.OnClickListener onNegateClickListener;
    private PositiveListener onPositiveClickListener;
    boolean isSuccess = false;
    String showUri = "http://cs208.csie.ncyu.edu.tw/login.php";
    com.android.volley.RequestQueue requestQueue;

    public Sign_in(Context context) {
        super(context);
    }

    public Sign_in(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editText_user = (EditText) findViewById(R.id.resign_user_input);
        editText_passwd = (EditText) findViewById(R.id.login_passwd_input);
        txv = (TextView) findViewById(R.id.msg);
        btn_cancel = (Button) findViewById(R.id.btn_login_cancel);
        btn_login = (Button) findViewById(R.id.resign_btn_resign);
        btn_cancel.setOnClickListener(onNegateClickListener);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect(showUri, new Sign_Callback() {
                    @Override
                    public void onSuccess(ArrayList<String> user, ArrayList<String> pwd) {
                        boolean flag = false;
                        int i;
                        for (i = 0; i<user.size(); i++) {
                            if (String.valueOf(editText_user.getText()).equals(user.get(i))) {
                                flag = true;
                                if (editText_passwd.getText().toString().equals(pwd.get(i))) {
                                    isSuccess = true;
                                    break;
                                } else {
                                    txv.setText("密碼錯誤");
                                    break;
                                }
                            }
                        }
                        if (!flag) {
                            txv.setText("此帳號尚未註冊");
                        }
                    }
                });
                if (onPositiveClickListener != null) {
                    onPositiveClickListener.onClick(isSuccess, editText_user.getText().toString());
                }
            }
        });
        requestQueue = Volley.newRequestQueue(getContext());
    }

    public void setOnPositiveListener(PositiveListener onPositiveClickListener) {

        this.onPositiveClickListener = onPositiveClickListener;
    }

    public void setOnNegateListener(View.OnClickListener onNegateClickListener) {
        this.onNegateClickListener = onNegateClickListener;
    }

    public interface PositiveListener {
        public void onClick(boolean isLogin, String username);
    }

    public void connect(String address, final Sign_Callback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST
                ,address, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
        requestQueue.add(jsonObjectRequest);
    }
}
