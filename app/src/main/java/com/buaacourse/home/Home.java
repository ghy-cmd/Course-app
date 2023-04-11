package com.buaacourse.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;

import android.content.Context;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.buaacourse.R;
import com.buaacourse.auth.Login;
import com.buaacourse.course.CourseBone;
import com.buaacourse.course.personQuestion;
import com.buaacourse.tools.Tools;
import com.buaacourse.person.PersonhomeActivity;
import com.buaacourse.util.MyCookieStore;
import com.buaacourse.util.RequestSingleton;
import com.bumptech.glide.Glide;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.strategy.impl.MaterialDialogStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity {
    protected RecyclerView recyclerView;
    RecyclerView recyclerView1;
    ArrayList<home_course> homeCourses = new ArrayList<>();
    private ArrayList<moment> moments;
    TextView name;
    Context context = this;
    ImageView user;
    Boolean isLogin = false;
    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DialogLoader.getInstance().setIDialogStrategy(new MaterialDialogStrategy());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        DialogLoader.getInstance().setIDialogStrategy(new MaterialDialogStrategy());
        CookieManager manager = new CookieManager(new MyCookieStore(getApplicationContext()), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);
//        TextView textView = findViewById(R.id.textView);
//        textView.setText(message);
        name = findViewById(R.id.textView24);
        recyclerView = findViewById(R.id.courses_home);
        recyclerView1 = findViewById(R.id.moments_home);
        ImageView calender = findViewById(R.id.imageView_home2);
        user = findViewById(R.id.imageView_home);
        ButtonView search = findViewById(R.id.buttonViewSearchHome);
        search.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, SearchCourse.class);
            startActivity(intent1);
        });
        calender.setOnClickListener(v -> {
            if (isLogin) {
                Intent intent1 = new Intent(this, Tools.class);
                startActivity(intent1);
            }
        });
        user.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, Login.class);
            Intent intent2 = new Intent(this, PersonhomeActivity.class);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, "http://gswxp2.top:1000/api/person/self", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (!"200".equals((String) response.get("code"))) {
                                    if (((String) response.get("code")).equals("403")) {
                                        startActivity(intent1);
                                    }
                                    //TODO:toast something
                                } else {
                                    startActivity(intent2);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        initDataset();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "http://gswxp2.top:1000/api/person/self", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {
                                String n = "你好，" + "请登录";
                                Glide.with(context).load(getDrawable(R.drawable.img_1)).into(user);
                                name.setText(n);
                                isLogin = false;
                            } else {
                                String n = "你好，" + (String) response.get("name");
                                userName = (String) response.get("name");
                                name.setText(n);
                                Glide.with(context).load((String) response.get("logo")).into(user);
                                isLogin = true;
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
        try {
            initDataset2();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDataset();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "http://gswxp2.top:1000/api/person/self", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {
                                String n = "你好，" + "请登录";
                                Glide.with(context).load(getDrawable(R.drawable.img_1)).into(user);
                                name.setText(n);
                                isLogin = false;
                                findViewById(R.id.constraintLayout17).setVisibility(View.GONE);

                            } else {
                                String n = "你好，" + (String) response.get("name");
                                name.setText(n);
                                Glide.with(context).load((String) response.get("logo")).into(user);
                                isLogin = true;
                                findViewById(R.id.constraintLayout17).setVisibility(View.VISIBLE);
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
        initDataset();
        try {
            initDataset2();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
        private ArrayList<home_course> courseArrayList;
        private Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView name;
            private final TextView members;
            private final ImageView logo;
            private final ImageView userIcon;
            private final ConstraintLayout con1;

            public ViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.courseName);
                members = view.findViewById(R.id.home_members);
                logo = view.findViewById(R.id.ItemImage);
                userIcon = view.findViewById(R.id.imageView_user);
                con1 = view.findViewById(R.id.con1);
            }

            public TextView getName() {
                return name;
            }

            public TextView getMembers() {
                return members;
            }

            public ImageView getLogo() {
                return logo;
            }

            public ImageView getUserIcon() {
                return userIcon;
            }

            public ConstraintLayout getCon1() {
                return con1;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            home_course h = courseArrayList.get(position);
            if (position == courseArrayList.size() - 1) {
                viewHolder.getLogo().setImageResource(R.drawable.add);
                viewHolder.getName().setText("添加课程");
                viewHolder.getMembers().setVisibility(View.GONE);
                viewHolder.getUserIcon().setVisibility(View.GONE);
                viewHolder.getCon1().setOnClickListener(v -> {
                    Intent intent = new Intent(context, SearchCourse.class);
                    startActivity(intent);
                });
            } else {
                ImageView imageView = viewHolder.getLogo();
                Glide.with(context).load(h.logo).into(imageView);
                viewHolder.getName().setText(h.name);
                String s = h.member + " members";
                viewHolder.getMembers().setText(s);
                viewHolder.getCon1().setOnClickListener(v -> {
                    Intent intent = new Intent(context, CourseBone.class);
                    intent.putExtra("id", h.id);
                    startActivity(intent);
                });
            }
        }

        @Override
        public int getItemCount() {
            return courseArrayList.size();
        }

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used
         *                by RecyclerView.
         */
        public CourseAdapter(ArrayList<home_course> dataSet) {
            courseArrayList = dataSet;
        }


    }

    public class Moment3Adapter extends RecyclerView.Adapter<Moment3Adapter.ViewHolder> {
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

        public Moment3Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
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

        public Moment3Adapter(ArrayList<moment> dataSet) {
            momentArrayList = dataSet;
        }

    }


    public class home_course {
        String id;
        int member;
        String logo;
        String name;

        public home_course(String id, String name, int member, String logo) {
            this.id = id;
            this.member = member;
            this.logo = logo;
            this.name = name;
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

    public void initDataset() {
        homeCourses = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "http://gswxp2.top:1000/api/home/courses", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {
                                //TODO:toast something
                            } else {
                                homeCourses.clear();
                                System.out.println(response);
                                JSONArray courses = (JSONArray) response.get("courses");
                                for (int i = 0; i < courses.length(); i++) {
                                    home_course h = new home_course((String) courses.optJSONObject(i).get("id"),
                                            (String) courses.optJSONObject(i).get("name"),
                                            (int) courses.optJSONObject(i).get("member"),
                                            (String) courses.optJSONObject(i).get("logo"));
                                    homeCourses.add(h);
                                }
                                home_course h = new home_course("0", "0", 0, "0");
                                homeCourses.add(h);
                                CourseAdapter courseAdapter = new CourseAdapter(homeCourses);
                                recyclerView.setAdapter(courseAdapter);
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

    public void initDataset2() throws JSONException {
        moments = new ArrayList<>();
        JSONObject j = new JSONObject();
        j.put("name", userName);
        j.put("operation", "all");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "http://gswxp2.top:1000/api/person/news", j, new Response.Listener<JSONObject>() {
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
                                Moment3Adapter momentAdapter = new Moment3Adapter(moments);
                                recyclerView1.setAdapter(momentAdapter);
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