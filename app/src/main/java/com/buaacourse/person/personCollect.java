package com.buaacourse.person;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.buaacourse.R;
import com.buaacourse.course.personQuestion;
import com.buaacourse.util.RequestSingleton;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class personCollect extends AppCompatActivity {
    RecyclerView recyclerView;
    Context context = this;
    private ArrayList<moment> moments;
    ConstraintLayout empty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_collect);
        recyclerView = findViewById(R.id.recycler_moment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        empty=findViewById(R.id.empty_collect);
        initData();
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

    public class Moment2Adapter extends RecyclerView.Adapter<Moment2Adapter.ViewHolder> {
        private Context context;
        private ArrayList<moment> momentArrayList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private TextView time;
            private TextView title;
            private TextView content;
            private ImageView logo;
            private ConstraintLayout item;

            public ViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.textView51);
                time = view.findViewById(R.id.textView52);
                title = view.findViewById(R.id.textView53);
                content = view.findViewById(R.id.textView54);
                logo = view.findViewById(R.id.radiusImageView4);
                item = view.findViewById(R.id.l_moment);
            }

            public ImageView getLogo() {
                return logo;
            }

            public TextView getName() {
                return name;
            }

            public TextView getTitle() {
                return title;
            }

            public TextView getContent() {
                return content;
            }

            public TextView getTime() {
                return time;
            }

            public ConstraintLayout getItem() {
                return item;
            }
        }

        public Moment2Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            context = viewGroup.getContext();
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.person_moment_item, viewGroup, false);
            return new ViewHolder(view);
        }

        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            moment f = momentArrayList.get(position);
            viewHolder.getName().setText(f.name);
            viewHolder.getTitle().setText(f.title);
            viewHolder.getContent().setText(f.content);
            Glide.with(context).load(f.logo).into(viewHolder.getLogo());
            String s;
            switch (f.operation) {
                case "发布问题":
                    s = "提问于 " + f.time;
                    break;
                case "发布分享":
                    s = "分享于 " + f.time;
                    break;
                case "发布回复":
                    s = "回复于 " + f.time;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + f.operation);
            }
            viewHolder.getTime().setText(s);

            viewHolder.getItem().setOnClickListener(v -> {
                Intent i = new Intent(context, personQuestion.class);
                i.putExtra("forumId", f.postid);
                startActivity(i);
            });
        }

        public int getItemCount() {
            return momentArrayList.size();
        }

        public Moment2Adapter(ArrayList<moment> dataSet) {
            momentArrayList = dataSet;
        }

    }

    public class moment {
        String name;
        String logo;
        String operation;
        String time;
        String title;
        String content;
        String postid;
    }

    public void initData() {
        moments = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "http://gswxp2.top:1000/api/person/collection", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {
                                //TODO:toast something
                            } else {
                                System.out.println(response);
                                moments.clear();
                                JSONArray news = (JSONArray) response.get("news");
                                for (int i = 0; i < news.length(); i++) {
                                    moment f = new moment();
                                    f.name = (String) news.optJSONObject(i).get("name");
                                    f.logo = (String) news.optJSONObject(i).get("logo");
                                    f.operation = (String) news.optJSONObject(i).get("operation");
                                    f.time = (String) news.optJSONObject(i).get("time");
                                    f.title = (String) news.optJSONObject(i).get("title");
                                    f.content = (String) news.optJSONObject(i).get("content");
                                    f.postid = (String) news.optJSONObject(i).get("postid");
                                    moments.add(f);
                                }
                                if (news.length()==0){
                                    recyclerView.setVisibility(View.GONE);
                                    empty.setVisibility(View.VISIBLE);
                                }
                                Moment2Adapter momentAdapter = new Moment2Adapter(moments);
                                recyclerView.setAdapter(momentAdapter);
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
}