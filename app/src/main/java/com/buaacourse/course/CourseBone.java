package com.buaacourse.course;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.buaacourse.R;

import com.buaacourse.util.RequestSingleton;
import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.button.roundbutton.RoundDrawable;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.tabbar.TabSegment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseBone extends AppCompatActivity {
    private String courseId;
    private ViewPager viewPager;
    private MyAdapter myAdapter;
    private RoundButton button;
    private Boolean choosen;
    private ScoreBar scoreBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_bone);

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
        Intent i = getIntent();
        courseId=i.getStringExtra("id");
        TabSegment mTabSegment = findViewById(R.id.tabSegment);
        scoreBar = findViewById(R.id.scoreBar3);
        button=findViewById(R.id.button12);
        button.setOnClickListener(v -> {
            if(choosen) {
                DialogLoader.getInstance().showConfirmDialog(CourseBone.this, "确认取消选课？", "确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                choosecourse();
                                dialog.dismiss();
                            }
                        },"取消");
            }
            else{
                choosecourse();
            }

        });

        IconicsDrawable icon= new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_list)
                .sizeDp(24);
        IconicsDrawable icon2=new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_hashtag)
                .sizeDp(24);
        mTabSegment.addTab(new TabSegment.Tab(icon, icon, "", false));
        mTabSegment.addTab(new TabSegment.Tab(icon2, icon2, "", false));

        myAdapter = new MyAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(myAdapter);


        mTabSegment.setMode(TabSegment.MODE_FIXED);
        mTabSegment.setupWithViewPager(viewPager, false);
        mTabSegment.setHasIndicator(true);

        mTabSegment.setIndicatorPosition(false);
        mTabSegment.setIndicatorWidthAdjustContent(false);
        //不使用ViewPager的话，必须notifyDataChanged，否则不能正常显示
        mTabSegment.selectTab(0);

    }
    private void choosecourse(){
        HashMap<String,String> map=new HashMap<>();
        map.put("id",courseId);
        JSONObject j=new JSONObject(map);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, "http://gswxp2.top:1000/api/course/" + courseId + "/choose", j, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!"200".equals((String) response.get("code"))) {
                        //TODO:toast something
                    } else {
                        getInfo();
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
        RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scoreBar.initData(courseId);
        getInfo();
    }

    public void setButtonStyle(Boolean chosen){
        RoundDrawable d= (RoundDrawable) button.getBackground();
        if(!chosen){
            d.setBgData(ColorStateList.valueOf(getResources().getColor(R.color.text_color)));
            button.setText("加入课程");
        }
        else {
            d.setBgData(ColorStateList.valueOf(getResources().getColor(R.color.button_disabled)));
            button.setText("离开课程");
        }
    }
    public void getInfo(){
        HashMap<String,String> map=new HashMap<>();
        map.put("id",courseId);
        JSONObject j=new JSONObject(map);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, "http://gswxp2.top:1000/api/course/" + courseId + "/info", j, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!"200".equals((String) response.get("code"))) {
                        //TODO:toast something
                    } else {
                        ((TextView)findViewById(R.id.textView30)).setText((String)response.get("name"));
                        ((TextView)findViewById(R.id.textView34)).setText((String)response.get("count")+" 人已经加入课程");
                        Glide.with(getApplicationContext()).load((String)response.get("logo")).into(((ImageView)findViewById(R.id.imageView7)));
                        choosen=(Boolean)response.get("chosen");
                        setButtonStyle((Boolean)response.get("chosen"));
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
        RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
    class MyAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments = new ArrayList<>();

        public MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
            fragments.add(new CourseComment(courseId));

            fragments.add(new CourseDiscuss(courseId));

            //fragments.add(new PostDiscuss());

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "评论";
            } else {
                return "讨论";
            }
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {

            return fragments.size();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.course_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            Intent i=new Intent(this,CourseInfo.class);
            i.putExtra("id",courseId);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
