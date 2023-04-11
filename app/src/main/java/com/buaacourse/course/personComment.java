package com.buaacourse.course;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.buaacourse.R;
import com.buaacourse.person.personMoments;
import com.bumptech.glide.Glide;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Queue;


public class personComment extends AppCompatActivity {
    ImageView imageView;
    String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_person_comment);
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
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        System.out.println(id);
        imageView = (RadiusImageView) findViewById(R.id.imageView2);
        imageView.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, personMoments.class);
            intent1.putExtra("name", name);
            startActivity(intent1);
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, "http://gswxp2.top:1000/api/comment/" + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("try", response.toString());
                            name = response.getString("name");
                            ((TextView) findViewById(R.id.textView5)).setText(response.getString("name"));
                            ((TextView) findViewById(R.id.textView10)).setText(response.getString("time"));
                            ((TextView) findViewById(R.id.textView39)).setText(response.getString("content1"));
                            ((TextView) findViewById(R.id.textView37)).setText(response.getString("content2"));
                            Glide.with(personComment.this).load(response.getString("icon")).into(imageView);
                            ((ScoreBar)findViewById(R.id.scoreBar)).initData2(response.getDouble("totalscore"),response.getDouble("contentscore"),response.getDouble("evalscore"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        queue.add(jsonObjectRequest);
    }

    public void back(View view) {
        this.finish();
    }

}