package com.buaacourse.course;


import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.buaacourse.R;
import com.buaacourse.util.RequestSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * TODO: document your custom view class.
 */
public class ScoreBar extends FrameLayout {

    private String courseId;

    public ScoreBar(Context context) {
        super(context);
        this.initComponent(context);
    }

    public ScoreBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initComponent(context);
    }

    public ScoreBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initComponent(context);
    }

    private void initComponent(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.component_score_bar, this);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public void initData(String courseId) {
        this.courseId = courseId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "http://gswxp2.top:1000/api/course/" + courseId + "/overview", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("123",response.toString());
                            if (!"200".equals((String) response.get("code"))) {
                                //TODO:toast something
                            } else {
                                ((TextView) findViewById(R.id.textView6)).setText(String.valueOf((Math.floor((Double) response.get("totalscore")*10))/10));
                                ((RatingBar) findViewById(R.id.ratingBar)).setRating(((Double)response.get("totalscore")).floatValue());
                                ((RatingBar) findViewById(R.id.ratingBar2)).setRating(((Double)response.get("contentscore")).floatValue());
                                ((RatingBar) findViewById(R.id.ratingBar3)).setRating(((Double) response.get("evalscore")).floatValue());
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
        RequestSingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

    }
    public void initData2(Double score1,Double score2,Double score3){
        ((TextView) findViewById(R.id.textView6)).setText(String.valueOf((Math.floor(score1*10))/10));

        ((RatingBar) findViewById(R.id.ratingBar)).setRating(score1.floatValue());
        ((RatingBar) findViewById(R.id.ratingBar2)).setRating(score2.floatValue());
        ((RatingBar) findViewById(R.id.ratingBar3)).setRating(score3.floatValue());
    }
}