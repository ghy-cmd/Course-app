package com.buaacourse.auth;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.view.Gravity;

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

public class Login extends AppCompatActivity {

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
        setContentView(R.layout.activity_log);
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

    public void check(View view) {
        String email = ((TextView)findViewById(R.id.emailadress)).getText().toString();
        String password = ((TextView)findViewById(R.id.password)).getText().toString();
        if(email.equals("")){
            XToast.error(XUI.getContext(),"请输入邮箱").show();
            return;
        }else if(password.equals("")){
            XToast.error(XUI.getContext(),"请输入密码").show();
            return;
        }
        HashMap<String, String> playload = new HashMap<>();
        playload.put("email",email);
        playload.put("password",password);
        JSONObject j = new JSONObject(playload);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,"http://gswxp2.top:1000/api/auth/login",j,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("try", response.toString());
                try {
                    if(!response.getString("code").equals("200")){
                        XToast.error(XUI.getContext(),response.getString("message")).show();
                    }else{
                        Login.this.finish();
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
        }
        );
        queue.add(jsonObjectRequest);
    }

    public void goRegister(View view) {
        Log.i("123","123");
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        finish();
    }

    public void goReset(View view) {
        Intent intent = new Intent(this, Reset.class);
        startActivity(intent);
        finish();
    }

    public void goBack(View view) {
        this.finish();
    }
    // end callWebService()
}