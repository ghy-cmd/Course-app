package com.buaacourse.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.buaacourse.R;
import com.buaacourse.util.RequestSingleton;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PostComment extends AppCompatActivity {
    String courseId;
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
        Intent i = getIntent();
        courseId = i.getStringExtra("courseId");
        setContentView(R.layout.activity_post_comment);
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
        TextView textView13 = findViewById(R.id.textView13);
        TextView textView14 = findViewById(R.id.textView14);
        TextView textView15 = findViewById(R.id.textView15);
        RatingBar.OnRatingBarChangeListener listener1 = new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating == 0) {
                    textView13.setText("很差");
                } else if (rating > 0 && rating <= 1) {
                    textView13.setText("差");
                } else if (rating > 1 && rating <= 2) {
                    textView13.setText("较差");
                } else if (rating > 2 && rating <= 3) {
                    textView13.setText("一般");
                } else if (rating > 3 && rating <= 4) {
                    textView13.setText("好");
                } else if (rating > 4 && rating <= 5) {
                    textView13.setText("很好");
                }
            }
        };
        RatingBar.OnRatingBarChangeListener listener2 = new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating == 0) {
                    textView14.setText("很差");
                } else if (rating > 0 && rating <= 1) {
                    textView14.setText("差");
                } else if (rating > 1 && rating <= 2) {
                    textView14.setText("较差");
                } else if (rating > 2 && rating <= 3) {
                    textView14.setText("一般");
                } else if (rating > 3 && rating <= 4) {
                    textView14.setText("好");
                } else if (rating > 4 && rating <= 5) {
                    textView14.setText("很好");
                }
            }
        };
        RatingBar.OnRatingBarChangeListener listener3 = new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating == 0) {
                    textView15.setText("很差");
                } else if (rating > 0 && rating <= 1) {
                    textView15.setText("差");
                } else if (rating > 1 && rating <= 2) {
                    textView15.setText("较差");
                } else if (rating > 2 && rating <= 3) {
                    textView15.setText("一般");
                } else if (rating > 3 && rating <= 4) {
                    textView15.setText("好");
                } else if (rating > 4 && rating <= 5) {
                    textView15.setText("很好");
                }
            }
        };
        RatingBar ratingBar5 = findViewById(R.id.ratingBar5);
        ratingBar5.setOnRatingBarChangeListener(listener1);
        RatingBar ratingBar6 = findViewById(R.id.ratingBar6);
        ratingBar6.setOnRatingBarChangeListener(listener2);
        RatingBar ratingBar7 = findViewById(R.id.ratingBar7);
        ratingBar7.setOnRatingBarChangeListener(listener3);
        EditText text1 = findViewById(R.id.editTextTextMultiLine);
        EditText text2 = findViewById(R.id.editTextTextMultiLine2);
        ButtonView newPost = findViewById(R.id.newpost);
        CheckBox checkBox = findViewById(R.id.checkBox);
        newPost.setOnClickListener(v -> {
            JSONObject j = new JSONObject();
            try {
                j.put("courseid", courseId);
                j.put("content1", text1.getText().toString());
                j.put("content2", text2.getText().toString());
                j.put("totalscore", ratingBar5.getRating());
                j.put("contentscore", ratingBar6.getRating());
                j.put("evalscore", ratingBar7.getRating());
                j.put("anonymous", checkBox.isChecked());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            DialogLoader.getInstance().showConfirmDialog(this, "确认发布？", "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, "http://gswxp2.top:1000/api/comment/new", j, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (!"200".equals((String) response.get("code"))) {
                                    //TODO:toast something
                                    XToast.error(XUI.getContext(),response.getString("message")).show();
                                } else {
                                    System.out.println(response);
                                    PostComment.this.finish();
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
        }},"取消");
        });
    }
}