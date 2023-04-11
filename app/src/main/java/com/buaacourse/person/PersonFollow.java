package com.buaacourse.person;

import androidx.annotation.NonNull;
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
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PersonFollow extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected ArrayList<followPerson> followPeople;
    ConstraintLayout empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_follow);
        recyclerView = findViewById(R.id.folloeRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        empty=findViewById(R.id.empty_follow);
        initData();
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
    }

    public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {
        private Context context;
        private ArrayList<followPerson> followPersonArrayList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private TextView signiture;
            private RadiusImageView imageView;
            private ButtonView status1;
            private ButtonView status2;

            public ViewHolder(@NonNull View view) {
                super(view);
                name = view.findViewById(R.id.textView38);
                signiture = view.findViewById(R.id.textView40);
                imageView = view.findViewById(R.id.radiusImageView3);
                status1 = view.findViewById(R.id.follow_btn);
                status2 = view.findViewById(R.id.followed_btn);
            }

            public TextView getName() {
                return name;
            }

            public TextView getSigniture() {
                return signiture;
            }

            public RadiusImageView getImageView() {
                return imageView;
            }

            public ButtonView getStatus1() {
                return status1;
            }

            public ButtonView getStatus2() {
                return status2;
            }
        }

        public FollowAdapter(ArrayList<followPerson> dataSet) {
            followPersonArrayList = dataSet;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            context = viewGroup.getContext();
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.person_follow_item, viewGroup, false);
            return new ViewHolder(view);
        }

        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            followPerson f = followPersonArrayList.get(position);
            viewHolder.getName().setText(f.name);
            viewHolder.getSigniture().setText(f.signiture);
            Glide.with(context).load(f.logo).into(viewHolder.getImageView());
            viewHolder.getStatus2().setVisibility(View.VISIBLE);
            viewHolder.getStatus2().setText(f.status);
            viewHolder.getStatus2().setOnClickListener(v -> {
                DialogLoader.getInstance().showConfirmDialog(context, "确定不再关注 " + f.name + " 吗？", "确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JSONObject j = new JSONObject();
                                try {
                                    j.put("name", f.name);
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
                                dialog.dismiss();
                            }
                        },
                        "取消");
            });
            viewHolder.getImageView().setOnClickListener(v -> {
                Intent intent = new Intent(PersonFollow.this,personMoments.class);
                intent.putExtra("name", f.name);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return followPersonArrayList.size();
        }
    }

    public class followPerson {
        String name;
        String logo;
        String signiture;
        String status;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void initData() {
        followPeople = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "http://gswxp2.top:1000/api/person/watchlist", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {
                                //TODO:toast something
                            } else {
                                followPeople.clear();
                                JSONArray people = (JSONArray) response.get("people");
                                for (int i = 0; i < people.length(); i++) {
                                    followPerson f = new followPerson();
                                    f.name = (String) people.optJSONObject(i).get("name");
                                    f.logo = (String) people.optJSONObject(i).get("logo");
                                    f.signiture = (String) people.optJSONObject(i).get("signiture");
                                    f.status = (String) people.optJSONObject(i).get("status");
                                    followPeople.add(f);
                                }
                                if (people.length()==0){
                                    recyclerView.setVisibility(View.GONE);
                                    empty.setVisibility(View.VISIBLE);
                                }
                                FollowAdapter followAdapter = new FollowAdapter(followPeople);
                                recyclerView.setAdapter(followAdapter);
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