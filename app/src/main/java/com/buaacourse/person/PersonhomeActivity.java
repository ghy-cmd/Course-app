package com.buaacourse.person;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.buaacourse.R;
import com.buaacourse.home.Home;
import com.buaacourse.util.RequestSingleton;
import com.bumptech.glide.Glide;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.strategy.InputInfo;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PersonhomeActivity extends AppCompatActivity {
    TextView followNum;
    TextView followText;
    TextView fansNum;
    TextView fansText;
    TextView collectNum;
    TextView collectText;
    RadiusImageView logo;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch aSwitch;
    TextView name;
    TextView signiture;
    Context context = this;
    String nameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personhome);
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
        followNum = findViewById(R.id.textView41);
        followText = findViewById(R.id.textView44);
        fansNum = findViewById(R.id.textView39);
        fansText = findViewById(R.id.textView43);
        collectNum = findViewById(R.id.textView42);
        collectText = findViewById(R.id.textView45);
        logo = findViewById(R.id.radiusImageView2);
        aSwitch = findViewById(R.id.switch1);
        name = findViewById(R.id.textView36);
        findViewById(R.id.constraintLayout6).setOnClickListener(v -> {
            Intent i = new Intent(this, PersonInfo.class);
            startActivity(i);
        });
        findViewById(R.id.constraintLayout12).setOnClickListener(v -> {DialogLoader.getInstance().showTipDialog(this,"关于我们","航课app\nmade by gsw,ghy,zsz,zhy","确认");});
        findViewById(R.id.constraintLayout14).setOnClickListener(v -> {
            DialogLoader.getInstance().showInputDialog(
                    this,
                    R.drawable.info,
                    "问题反馈",
                    "",
                    new InputInfo(InputType.TYPE_CLASS_TEXT,
                            "请输入反馈内容"),
                    null,
                    "确认",
                    (dialog, which) -> {
                        KeyboardUtils.hideSoftInput(dialog);
                        dialog.dismiss();
                    },
                    "取消",
                    (dialog, which) -> {
                        KeyboardUtils.hideSoftInput(dialog);
                        dialog.dismiss();
                    });
        });
        findViewById(R.id.constraintLayout7).setOnClickListener(v -> {
            DialogLoader.getInstance().showConfirmDialog(context, "确认退出？", "确认",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                    (Request.Method.POST, "http://gswxp2.top:1000/api/auth/logout", null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if (!"200".equals((String) response.get("code"))) {
                                                    //TODO:toast something
                                                } else {
                                                    finish();
                                                    dialog.dismiss();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("NetworkError", error.toString());
                                        }
                                    });
                            RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                        }
                    }, "取消");
        });

        signiture = findViewById(R.id.textView37);
//        aSwitch.setChecked(true);
        followNum.setOnClickListener(v -> {
            jumpFollow();
        });
        followText.setOnClickListener(v -> {
            jumpFollow();
        });
        fansNum.setOnClickListener(v -> {
            jumpFans();
        });
        fansText.setOnClickListener(v -> {
            jumpFans();
        });
        collectNum.setOnClickListener(v -> {
            jumpCollections();
        });
        collectText.setOnClickListener(v -> {
            jumpCollections();
        });
        logo.setOnClickListener(v -> {
            jumpMoments();
        });
        initData();
        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            JSONObject j = new JSONObject();
            try {
                if (isChecked) {
                    j.put("operation", "open");
                } else {
                    j.put("operation", "close");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, "http://gswxp2.top:1000/api/person/privacy", j, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (!"200".equals((String) response.get("code"))) {
                                    //TODO:toast something
                                } else {
                                    initData();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("NetworkError", error.toString());
                        }
                    });
            RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

        });
    }

    public void jumpFollow() {
        Intent intent = new Intent(this, PersonFollow.class);
        startActivity(intent);
    }

    public void jumpFans() {
        Intent intent = new Intent(this, PersonFans.class);
        startActivity(intent);
    }

    public void jumpCollections() {
        Intent intent = new Intent(this, personCollect.class);
        startActivity(intent);
    }

    public void jumpMoments() {
        Intent intent = new Intent(this, personMoments.class);
        intent.putExtra("name", nameUser);
        startActivity(intent);
    }

    public void initData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "http://gswxp2.top:1000/api/person/self", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {
                                //TODO:toast something
                            } else {
                                nameUser = (String) response.get("name");
                                name.setText((String) response.get("name"));
                                signiture.setText((String) response.get("signiture"));
                                Glide.with(context).load((String) response.get("logo")).into(logo);
                                followNum.setText(String.valueOf((int) response.get("watches")));
                                fansNum.setText(String.valueOf((int) response.get("watched")));
                                collectNum.setText(String.valueOf((int) response.get("collections")));
                                aSwitch.setChecked((Boolean) response.get("privacy"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("NetworkError", error.toString());
                    }
                });
        RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}