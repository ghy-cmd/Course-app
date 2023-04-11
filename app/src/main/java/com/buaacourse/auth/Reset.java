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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.button.CountDownButton;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xui.widget.toast.XToast;


public class Reset extends AppCompatActivity {

    static CountDownButtonHelper mCountDownHelper2;
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
        setContentView(R.layout.activity_reset);
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
        CountDownButton mBtCountDown2=findViewById(R.id.button2);
        mCountDownHelper2= new CountDownButtonHelper(mBtCountDown2, 20);
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
                (Request.Method.POST, "http://gswxp2.top:1000/api/auth/reset/captcha", j, new Response.Listener<JSONObject>() {

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

    public void confirm(View view) {
        String email = ((TextView) findViewById(R.id.emailadress)).getText().toString();
        Log.i("try", email);
        String captcha = ((TextView) findViewById(R.id.captcha)).getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> playload = new HashMap<>(1);
        playload.put("email", email);
        playload.put("captcha",captcha);
        JSONObject j = new JSONObject(playload);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "http://gswxp2.top:1000/api/auth/reset/check", j, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("try", response.toString());
                        try {
                            if(response.getString("code").equals("200")){
                                System.out.println("sunccess");
                                Intent intent = new Intent(Reset.this,ConfirmReset.class);
                                intent.putExtra("email",email);
                                startActivity(intent);
                                finish();
                            }else{
                                XToast.error(XUI.getContext(),response.getString("message")).show();
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

    public void goBack(View view) {
        this.finish();
    }
}