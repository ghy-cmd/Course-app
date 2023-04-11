package com.buaacourse.course;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.buaacourse.R;
import com.buaacourse.util.RequestSingleton;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.noties.markwon.Markwon;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;
import io.noties.markwon.ext.latex.JLatexMathPlugin;

public class PostDiscuss extends AppCompatActivity {
    private int type;
    private String courseId;
    String forumId;
    int count=0;
    String text;
    Boolean status=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if(status){
                    finish();
                }
                else{
                    ((EditText) findViewById(R.id.textView33)).setText(text);
                    ((Button) findViewById(R.id.button11)).setText("     预览帖子");
                    status=true;

                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_discuss);
        ((EditText) findViewById(R.id.textView33)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(status==false){
                    ((EditText) findViewById(R.id.textView33)).setText(text);
                    ((Button) findViewById(R.id.button11)).setText("     预览帖子");
                    status=true;
                }
                return false;
            }
        });
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        courseId = intent.getStringExtra("courseId");
        //
        forumId = intent.getStringExtra("forumId");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        if (forumId != null) {
            ((EditText) findViewById(R.id.textView29)).setText(title);
            ((EditText) findViewById(R.id.textView33)).setText(content);
        }
        //
        if (type == 1) {
            ((TextView) findViewById(R.id.textView28)).setText("分享你的收获");
            ((TextView) findViewById(R.id.textView27)).setText("扼要描述分享主题，抓住别人的眼球");
            ((TextView) findViewById(R.id.textView32)).setText("分享主要内容，内容详实有条理");
            ((EditText) findViewById(R.id.textView29)).setHint("   如：android组件中实现传参的银弹");
        }
        ((Button) findViewById(R.id.button11)).setOnClickListener(v -> post());
        // obtain Markwon instance
        final Markwon markwon = Markwon.create(this);

// create editor
        final MarkwonEditor editor = MarkwonEditor.create(markwon);

// set edit listener
        ((EditText) findViewById(R.id.textView33)).addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor));
        Toolbar myToolbar = (Toolbar) findViewById(R.id.titleBar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    void post() {
        if(status){
            status=false;
            text=((EditText) findViewById(R.id.textView33)).getText().toString();
            Log.i("123",text);
            ((Button) findViewById(R.id.button11)).setText("     确认提交");
            final Markwon markwon =Markwon.builder(this)
                    .usePlugin(JLatexMathPlugin.create(((EditText) findViewById(R.id.textView33)).getTextSize()))
                    .build();
            markwon.setMarkdown(((EditText) findViewById(R.id.textView33)),((EditText) findViewById(R.id.textView33)).getText().toString());
            return ;
        }

        String type = this.type == 1 ? "post_discuss" : "post_question";

        String title = ((EditText) findViewById(R.id.textView29)).getText().toString();
        HashMap<String, String> mymap = new HashMap<>();
        mymap.put("type", type);
        String operation;
        if (forumId != null) {
            operation = "modify";
            mymap.put("id", forumId);
        } else {
            operation = "new";
        }
        mymap.put("operation", operation);
        mymap.put("content", text);
        mymap.put("title", title);
        mymap.put("relateid", courseId);
        mymap.put("relatetype", "course");
        if(title.trim().equals("")||text.trim().equals("") ){
            DialogLoader.getInstance().showTipDialog(
                    this,
                    "提示",
                    "请输入标题或内容",
                    "确认");
            return;
        }
        DialogLoader.getInstance().showConfirmDialog(this, "确认发布？", "确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject jsonObject = new JSONObject(mymap);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, "http://gswxp2.top:1000/api/forum/content", jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (!"200".equals((String) response.get("code"))) {
                                        //TODO:toast something
                                    } else {
                                        PostDiscuss.this.finish();
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
    },"取消");
    }

}