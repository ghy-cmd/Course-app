package com.buaacourse.auth;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.buaacourse.R;
import com.buaacourse.person.PersonInfo;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    static {
        XToast.Config.get()
                .setAlpha(200)
                .setToastTypeface(XUI.getDefaultTypeface())
                .setGravity(Gravity.CENTER)
                .allowQueue(false);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.titleBar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void sendVerify(View view) {
        final TextView textView = (TextView) findViewById(R.id.emailadress);
        Log.i("try", textView.getText().toString());
        if(textView.getText().toString().equals("")){
            XToast.error(XUI.getContext(),"请输入邮箱").show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> playload = new HashMap<>(1);
        playload.put("email", textView.getText().toString());
        JSONObject j = new JSONObject(playload);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "http://gswxp2.top:1000/api/auth/register/captcha", j, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("try", response.toString());
                        try {
                            if(!response.getString("code").equals("200")){
                                XToast.error(XUI.getContext(),response.getString("message")).show();
                            }else{
                                XToast.success(XUI.getContext(),"已发送验证码").show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("try", error.toString());

                    }
                });

        queue.add(jsonObjectRequest);
    }

    public void sendLogRequest(View view) {
        Intent i=new Intent(this,Login.class);
        startActivity(i);
        this.finish();
    }

    public void sendRegisterRequest(View view) {
        Log.i("register","123");
        final TextView emailText = (TextView) findViewById(R.id.emailadress);
        final TextView passwordText = (TextView) findViewById(R.id.password);
        final TextView cpatchaText = (TextView) findViewById(R.id.captcha);
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> playload = new HashMap<>(1);
        playload.put("email", emailText.getText().toString());
        playload.put("password",passwordText.getText().toString());
        playload.put("captcha",cpatchaText.getText().toString());
        JSONObject j = new JSONObject(playload);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,"http://gswxp2.top:1000/api/auth/register",j,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                Log.i("try", response.toString());
                try {
                    if(!response.getString("code").equals("200")){
                        XToast.error(XUI.getContext(),response.getString("message")).show();
                    }else{
                        XToast.success(XUI.getContext(),"成功注册").show();
                        Intent i=new Intent(Register.this, PersonInfo.class);
                        startActivity(i);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("try", error.toString());
            }
        }
        );
        queue.add(jsonObjectRequest);
    }

}