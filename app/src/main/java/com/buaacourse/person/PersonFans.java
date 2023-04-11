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
import com.buaacourse.util.RequestSingleton;
import com.bumptech.glide.Glide;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.strategy.impl.MaterialDialogStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PersonFans extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected ArrayList<fan> fans;
    ConstraintLayout empty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_fans);
        recyclerView = findViewById(R.id.fansRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        empty = findViewById(R.id.empty_fans);
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

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public class FanAdapter extends RecyclerView.Adapter<FanAdapter.ViewHolder> {
        private Context context;
        private ArrayList<fan> fanArrayList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private TextView signiture;
            private ImageView logo;
            private ButtonView status1;
            private ButtonView status2;

            public ViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.textView38);
                signiture = view.findViewById(R.id.textView40);
                logo = view.findViewById(R.id.radiusImageView3);
                status1 = view.findViewById(R.id.follow_btn);
                status2 = view.findViewById(R.id.followed_btn);
            }

            public TextView getSigniture() {
                return signiture;
            }

            public TextView getName() {
                return name;
            }

            public ButtonView getStatus1() {
                return status1;
            }

            public ButtonView getStatus2() {
                return status2;
            }

            public ImageView getLogo() {
                return logo;
            }
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            context = viewGroup.getContext();
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.person_follow_item, viewGroup, false);
            return new ViewHolder(view);
        }

        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            fan f = fanArrayList.get(position);
            viewHolder.getName().setText(f.name);
            viewHolder.getSigniture().setText(f.signiture);
            Glide.with(context).load(f.logo).into(viewHolder.getLogo());
            if (f.status.equals("互相关注")) {
                viewHolder.getStatus2().setVisibility(View.VISIBLE);
                viewHolder.getStatus2().setText("互相关注");
                viewHolder.getStatus1().setVisibility(View.GONE);
            } else {
                viewHolder.getStatus1().setVisibility(View.VISIBLE);
                viewHolder.getStatus1().setText("+回关");
                viewHolder.getStatus2().setVisibility(View.GONE);
            }
            viewHolder.getStatus1().setOnClickListener(v -> {
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
            });
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
            viewHolder.getLogo().setOnClickListener(v -> {
                Intent intent = new Intent(context, personMoments.class);
                intent.putExtra("name", f.name);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return fanArrayList.size();
        }

        public FanAdapter(ArrayList<fan> dataSet) {
            fanArrayList = dataSet;
        }
    }

    public class fan {
        String name;
        String logo;
        String signiture;
        String status;
    }

    public void initData() {
        fans = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "http://gswxp2.top:1000/api/person/watchedlist", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {
                                //TODO:toast something
                            } else {
                                fans.clear();
                                JSONArray people = (JSONArray) response.get("people");
                                for (int i = 0; i < people.length(); i++) {
                                    fan f = new fan();
                                    f.name = (String) people.optJSONObject(i).get("name");
                                    f.logo = (String) people.optJSONObject(i).get("logo");
                                    f.signiture = (String) people.optJSONObject(i).get("signiture");
                                    f.status = (String) people.optJSONObject(i).get("status");
                                    fans.add(f);
                                }
                                if (people.length() == 0) {
                                    recyclerView.setVisibility(View.GONE);
                                    empty.setVisibility(View.VISIBLE);
                                }
                                FanAdapter fanAdapter = new FanAdapter(fans);
                                recyclerView.setAdapter(fanAdapter);
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