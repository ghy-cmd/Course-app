package com.buaacourse.tools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.buaacourse.R;
import com.buaacourse.util.RequestSingleton;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Tools extends AppCompatActivity {
    TextView mTextMonthDay;
    TextView mTextYear;
    TextView mTextLunar;
    CalendarView mCalendarView;
    CalendarLayout mCalendarLayout;
    ImageView addview;
    protected ArrayList<TodoList.todoItem> mDataset;
    boolean staus = false;
    private int mYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);
        mCalendarView = findViewById(R.id.calendarView);
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mCalendarView.setFixMode();
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
        mCalendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                ((TextView) findViewById(R.id.textView45)).setText(calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay());
            }
        });
        final String m_Text;
        addview = findViewById(R.id.add_view);
        findViewById(R.id.add_view).setOnClickListener(v -> {



            if (!staus) {
                findViewById(R.id.fragmentContainerView3).setVisibility(View.GONE);
                findViewById(R.id.newconstarin).setVisibility(View.VISIBLE);
                IconicsDrawable icon = new IconicsDrawable(this)
                        .icon(MaterialDesignIconic.Icon.gmi_check)
                        .sizeDp(24);
                addview.setImageDrawable(icon);
                staus = true;
            }else{

                JSONObject j=new JSONObject();
                if(((TextView)findViewById(R.id.password)).getText().toString().trim().equals("") ||((TextView)findViewById(R.id.password2)).getText().toString().trim().equals("") ||
                        ((TextView)findViewById(R.id.textView45)).getText().toString().trim().equals("") ){
                    DialogLoader.getInstance().showTipDialog(
                            this,
                            "提示",
                            "请输入标题时间等信息",
                            "确认");
                    return;
                }
                try {
                    j.put("title",((TextView)findViewById(R.id.password)).getText());
                    ((TextView)findViewById(R.id.password)).setText("");
                    j.put("content",((TextView)findViewById(R.id.password2)).getText());
                    ((TextView)findViewById(R.id.password2)).setText("");

                    j.put("time",((TextView)findViewById(R.id.textView45)).getText());
                    ((TextView)findViewById(R.id.textView45)).setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, "http://gswxp2.top:1000/api/tools/add", j, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (!"200".equals((String) response.get("code"))) {
                                        //TODO:toast something
                                    } else {
                                        findViewById(R.id.fragmentContainerView3).setVisibility(View.VISIBLE);
                                        findViewById(R.id.newconstarin).setVisibility(View.GONE);
                                        IconicsDrawable icon = new IconicsDrawable(Tools.this)
                                                .icon(GoogleMaterial.Icon.gmd_add)
                                                .sizeDp(24);

                                        addview.setImageDrawable(icon);
                                        staus=false;

                                        initDataSet();
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
                RequestSingleton.getInstance(Tools.this).addToRequestQueue(jsonObjectRequest);

            }
        });
        initDataSet();

    }


    private void initCalendarView() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
                getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        //mCalendarView.setSchemeDate(map);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    private void initDataSet() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        mDataset = new ArrayList<TodoList.todoItem>(10);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "http://gswxp2.top:1000/api/tools/list", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {
                                //TODO:toast something
                            } else {
                                if(((String)response.get("message")).equals("nobh")){
                                    new MaterialDialog.Builder(Tools.this)
                                            .customView(R.layout.text_input_password, true)
                                            .title("请提供北航统一身份认证信息")
                                            .positiveText("确认")
                                            .negativeText("取消")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    String name=((EditText)dialog.getWindow().findViewById(R.id.editTextTextPersonName)).getText().toString().trim();
                                                    String password=((EditText)dialog.getWindow().findViewById(R.id.editTextTextPersonName2)).getText().toString().trim();
                                                    if(password.equals("")||name.equals("")){
                                                        DialogLoader.getInstance().showTipDialog(
                                                                Tools.this,
                                                                "提示",
                                                                "请输入账号和密码",
                                                                "确认");
                                                        return;
                                                    }
                                                    JSONObject j=new JSONObject();
                                                    try {
                                                        j.put("username",name);
                                                        j.put("password",password);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                                            (Request.Method.POST, "http://gswxp2.top:1000/api/tools/submit", j, new Response.Listener<JSONObject>() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    try {
                                                                        if (!"200".equals((String) response.get("code"))) {
                                                                            //TODO:toast something
                                                                        } else {
                                                                            initDataSet();
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
                                                    RequestSingleton.getInstance(Tools.this).addToRequestQueue(jsonObjectRequest);
                                                }
                                            })
                                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    new MaterialDialog.Builder(Tools.this)
                                                            .title("是否确认不再提供统一身份认证信息")
                                                            .positiveText("确认")
                                                            .negativeText("取消").onPositive(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                                                    (Request.Method.POST, "http://gswxp2.top:1000/api/tools/deny", null, new Response.Listener<JSONObject>() {
                                                                        @Override
                                                                        public void onResponse(JSONObject response) {
                                                                            try {
                                                                                if (!"200".equals((String) response.get("code"))) {
                                                                                    //TODO:toast something
                                                                                } else {

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
                                                            RequestSingleton.getInstance(Tools.this).addToRequestQueue(jsonObjectRequest);
                                                        }
                                                    }).show();
                                                }
                                            })
                                            .show();
                                }
                                mDataset.clear();
                                JSONArray array = (JSONArray) response.get("todos");
                                for (int i = 0; i < array.length(); i++) {
                                    Log.i("123", String.valueOf(array.optJSONObject(i)));
                                    TodoList.todoItem temp = new TodoList.todoItem(
                                            (String) array.optJSONObject(i).get("title"),
                                            (String) array.optJSONObject(i).get("content"),
                                            (String) array.optJSONObject(i).get("time")
                                    );
                                    if (array.optJSONObject(i).has("url")) {
                                        temp.url = (String) array.optJSONObject(i).get("url");
                                    } else {
                                        temp.url = null;
                                    }
                                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                                    try {
                                        temp.date = ft.parse(temp.time.split("\\s+")[0]);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mDataset.add(temp);

                                }
                                Collections.sort(mDataset, (o1, o2) -> o2.date.compareTo(o1.date));
                                Map<String, Calendar> map = new HashMap<>();
                                for (TodoList.todoItem i : mDataset) {
                                    if (i.title.contains("上课")) {
                                        map.put(getSchemeCalendar(i.date.getYear() + 1900, i.date.getMonth() + 1, i.date.getDate(), 0xFF40db25, "课").toString(),
                                                getSchemeCalendar(i.date.getYear() + 1900, i.date.getMonth() + 1, i.date.getDate(), 0xFF40db25, "课"));
                                    } else {
                                        map.put(getSchemeCalendar(i.date.getYear() + 1900, i.date.getMonth() + 1, i.date.getDate(), 0xFFC75450, "作业").toString(),
                                                getSchemeCalendar(i.date.getYear() + 1900, i.date.getMonth() + 1, i.date.getDate(), 0xFFC75450, "作业"));
                                    }
                                }
                                mCalendarView.setSchemeDate(map);

                                // Set TodoAdapter as the adapter for RecyclerView.
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("todos", mDataset);

                                Tools.this.getSupportFragmentManager().beginTransaction()
                                        .setReorderingAllowed(true)
                                        .add(R.id.fragmentContainerView3, TodoList.class, bundle)
                                        .commit();

                                // END_INCLUDE(initializeRecyclerView)
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
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 3;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestSingleton.getInstance(Tools.this).addToRequestQueue(jsonObjectRequest);
    }


}