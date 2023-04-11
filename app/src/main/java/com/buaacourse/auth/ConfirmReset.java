package com.buaacourse.auth;


import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ConfirmReset extends AppCompatActivity {
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reset);
        Bundle bundle=this.getIntent().getExtras();
        email=bundle.getString("email");
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

    public void confirm(View view) {
        String newPasswd = ((TextView) findViewById(R.id.editTextTextPassword4)).getText().toString();
        String confirmPasswd = ((TextView) findViewById(R.id.password)).getText().toString();
        if(newPasswd.equals("")){
            error("请输入新密码");
        }else if(confirmPasswd.equals("")){
            error("请确认新密码");
        }else{
            if(!newPasswd.equals(confirmPasswd)){
                error("两次输入结果不一致");
            }else{
                RequestQueue queue = Volley.newRequestQueue(this);
                HashMap<String, String> playload = new HashMap<>();
                playload.put("email",email);
                playload.put("password",newPasswd);
                JSONObject j = new JSONObject(playload);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST,"http://gswxp2.top:1000/api/auth/reset",j,new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("try", response.toString());
                                try {
                                    if(!response.getString("code").equals("200")){
                                        XToast.error(XUI.getContext(),response.getString("message")).show();
                                    }else{
                                        XToast.success(XUI.getContext(),"密码重置成功").show();
                                        finish();
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
        }
    }

    public void goBack(View view) {
        this.finish();
    }
    public void error(String message){
        XToast.error(XUI.getContext(),message).show();
    }
    public void success(){
        XToast.success(XUI.getContext(),"您已经成功重置密码").show();
    }

}