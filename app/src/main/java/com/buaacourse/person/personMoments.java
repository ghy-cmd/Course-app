package com.buaacourse.person;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
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
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.strategy.impl.MaterialDialogStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class personMoments extends AppCompatActivity {
    String otherName;
    RecyclerView recyclerView;
    TextView name;
    ImageView logo;
    TextView signiture;
    TextView followNum;
    TextView fansNum;
    TextView collectNum;
    Context context = this;
    Boolean self = false;
    ConstraintLayout empty;
    ConstraintLayout full;
    private ArrayList<moment> moments;
    ButtonView follow;
    ButtonView followed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_moments);
        Intent i = getIntent();
        otherName = i.getStringExtra("name");
        name = findViewById(R.id.textView36);
        logo = findViewById(R.id.radiusImageView2);
        signiture = findViewById(R.id.textView37);
        followNum = findViewById(R.id.textView41);
        fansNum = findViewById(R.id.textView39);
        collectNum = findViewById(R.id.textView42);
        empty = findViewById(R.id.c_empty);
        full = findViewById(R.id.constraintLayout16);
        recyclerView = findViewById(R.id.recycler_moment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        follow = findViewById(R.id.follow_btn2);
        followed = findViewById(R.id.followed_btn2);
        DialogLoader.getInstance().setIDialogStrategy(new MaterialDialogStrategy());
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
        follow.setOnClickListener(v -> {
            JSONObject j = new JSONObject();
            try {
                j.put("name", otherName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, "http://gswxp2.top:1000/api/person/watch", j, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (!"200".equals((String) response.get("code"))) {
                                    //TODO:toast something
                                } else {
                                    initData1();
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
        followed.setOnClickListener(v -> {
            DialogLoader.getInstance().showConfirmDialog(context, "确定不再关注 " + otherName + " 吗？", "确认",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            JSONObject j = new JSONObject();
                            try {
                                j.put("name", otherName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                    (Request.Method.POST, "http://gswxp2.top:1000/api/person/watch", j, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if (!"200".equals((String) response.get("code"))) {
                                                    //TODO:toast something
                                                } else {
                                                    initData1();
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
                            dialog.dismiss();
                        }
                    },
                    "取消");
        });
        initData1();
    }

    public void initData1() {
        //先获取一遍自己信息
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "http://gswxp2.top:1000/api/person/self", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {
                                //TODO:toast something
                            } else {
                                if (otherName.equals((String) response.get("name"))) {
                                    name.setText((String) response.get("name"));
                                    signiture.setText((String) response.get("signiture"));
                                    Glide.with(context).load((String) response.get("logo")).into(logo);
                                    followNum.setText(String.valueOf((int) response.get("watches")));
                                    fansNum.setText(String.valueOf((int) response.get("watched")));
                                    collectNum.setText(String.valueOf((int) response.get("collections")));
                                    self = true;
                                    empty.setVisibility(View.GONE);
                                    full.setVisibility(View.VISIBLE);
                                    initMomentData();
                                } else {
                                    initData2();
                                }
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

    public void initData2() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("name", otherName);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "http://gswxp2.top:1000/api/person/other", j, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {
                                //TODO:toast something
                            } else {
                                name.setText((String) response.get("name"));
                                signiture.setText((String) response.get("signiture"));
                                Glide.with(context).load((String) response.get("logo")).into(logo);
                                followNum.setText(String.valueOf((int) response.get("watches")));
                                fansNum.setText(String.valueOf((int) response.get("watched")));
                                collectNum.setText(String.valueOf((int) response.get("collections")));
                                self = (Boolean) response.get("privacy");
                                if ("互相关注".equals(response.get("status"))) {
                                    followed.setVisibility(View.VISIBLE);
                                    followed.setText("互相关注");
                                    follow.setVisibility(View.GONE);
                                } else if ("已关注".equals(response.get("status"))) {
                                    followed.setVisibility(View.VISIBLE);
                                    followed.setText("已关注");
                                    follow.setVisibility(View.GONE);
                                } else if ("没关注".equals(response.get("status"))) {
                                    followed.setVisibility(View.GONE);
                                    follow.setVisibility(View.VISIBLE);
                                }
                                if (self) {
                                    empty.setVisibility(View.GONE);
                                    full.setVisibility(View.VISIBLE);
                                    initMomentData();
                                }
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

    public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.ViewHolder> {
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

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
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

        public MomentAdapter(ArrayList<moment> dataSet) {
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

    public void initMomentData() throws JSONException {
        moments = new ArrayList<>();
        JSONObject j = new JSONObject();
        j.put("operation", "other");
        j.put("name", otherName);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "http://gswxp2.top:1000/api/person/news", j, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {
                                //TODO:toast something
                            } else {
                                moments.clear();
                                System.out.println(response);
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
                                if (news.length() == 0) {
                                    recyclerView.setVisibility(View.GONE);
                                    empty.setVisibility(View.VISIBLE);
                                }
                                MomentAdapter momentAdapter = new MomentAdapter(moments);
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

    @Override
    protected void onResume() {
        super.onResume();
        initData1();
    }
}