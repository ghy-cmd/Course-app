package com.buaacourse.person;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.buaacourse.R;
import com.buaacourse.util.GlideEngine;
import com.buaacourse.util.RequestSingleton;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.Utils;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PersonInfo extends AppCompatActivity {
    private List<LocalMedia> mSelectList = new ArrayList<>();
    private Spinner spinner;
    String major = "计算机学院";
    Boolean change = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        spinner = findViewById(R.id.spinner_system);
        String[] items = {"计算机学院", "软件学院", "电子信息工程学院", "网络空间安全学院", "人工智能学院"};
        WidgetUtils.initSpinnerStyle(spinner, items);
        spinner.setBackground(getDrawable(R.drawable.editsharp));
        spinner.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.button_mi)));
        //spinner.setPopupBackgroundDrawable(getDrawable(R.drawable.editsharp));
        final float scale = this.getResources().getDisplayMetrics().density;
        spinner.setDropDownVerticalOffset((int) (63 * scale + 0.5f));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(spinner.getContext(), R.layout.xui_layout_spinner_selected_item, R.id.spinner_item, items);
        adapter.setDropDownViewResource(R.layout.xui_layout_spinner_drop_down_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                major = items[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        findViewById(R.id.radiusImageView4).setOnClickListener(v -> {
            PictureSelector.create(this)

                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(GlideEngine.createGlideEngine())
                    .minSelectNum(1)
                    .selectionMode(PictureConfig.SINGLE)
                    .enableCrop(true)
                    .compress(true)
                    .circleDimmedLayer(true)
                    .previewEggs(true)
                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                        @Override
                        public void onResult(List<LocalMedia> result) {
                            Log.i("info", String.valueOf(result.get(0)));
                            Glide.with(PersonInfo.this)
                                    .load(result.get(0).getCutPath())
                                    .override(200, 200)
                                    .centerCrop()
                                    .placeholder(R.drawable.picture_image_placeholder)
                                    .into((RadiusImageView) findViewById(R.id.radiusImageView4));
                            Boolean change = true;

                        }

                        @Override
                        public void onCancel() {
                        }
                    });
        });
        findViewById(R.id.button4).setOnClickListener(v -> {
            JSONObject j = new JSONObject();
            if (((TextView) findViewById(R.id.name)).getText().toString().trim().equals("")) {
                DialogLoader.getInstance().showTipDialog(
                        this,
                        "提示",
                        "请输入姓名",
                        "确认");
                return;
            }
            try {
                j.put("name", ((TextView) findViewById(R.id.name)).getText().toString().trim());
                j.put("signiture", ((TextView) findViewById(R.id.signiture)).getText());
                j.put("major", major);
                if (change) {
                    j.put("logo", "");
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    BitmapDrawable bd = (BitmapDrawable) ((RadiusImageView) findViewById(R.id.radiusImageView4)).getDrawable();
                    Bitmap bitmap = bd.getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] bytes = baos.toByteArray();
                    j.put("logo", Base64.encodeToString(bytes, Base64.DEFAULT));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, "http://gswxp2.top:1000/api/person/update", j, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (!"200".equals((String) response.get("code"))) {

                                } else {
                                    finish();
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "http://gswxp2.top:1000/api/person/self", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {
                                //TODO:toast something
                            } else {
                                String n = (String) response.get("name");
                                ((TextView) findViewById(R.id.name)).setText(n);
                                String s = (String) response.get("signiture");
                                if(s.equals("这个人很懒，什么也没写")) {
                                    ((EditText) findViewById(R.id.signiture)).setText("");
                                }
                                else{
                                    ((EditText) findViewById(R.id.signiture)).setText(s);
                                }
                                for(int i=0;i<items.length;i++){
                                    if(items[i].equals((String)response.get("major"))){
                                        spinner.setSelection(i);
                                    }
                                }
                                Glide.with(PersonInfo.this).load((String) response.get("logo")).into((RadiusImageView) findViewById(R.id.radiusImageView4));
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


}