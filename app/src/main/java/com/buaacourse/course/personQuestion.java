package com.buaacourse.course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.buaacourse.R;
import com.buaacourse.util.RequestSingleton;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.strategy.impl.MaterialDialogStrategy;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;

import io.noties.markwon.Markwon;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;
import io.noties.markwon.ext.latex.JLatexMathPlugin;

import com.buaacourse.person.personMoments;

public class personQuestion extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected ArrayList<forum> forums;
    protected RelativeLayout relativeLayout;
    protected BottomSheetDialog bottomSheetDialog;
    EditText editText2;
    ImageView replySend;
    //    String courseId;
    String forumId;
    Boolean editStyle = false;
    Boolean accepted = false;
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
//        courseId = i.getStringExtra("courseId");
        forumId = i.getStringExtra("forumId");
//        courseId = "3";
        //forumId = "4";
        setContentView(R.layout.activity_person_question);
        recyclerView = findViewById(R.id.recyclerView);
        relativeLayout = findViewById(R.id.relativeggg);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        initDataset2();
        View view = View.inflate(this, R.layout.dialog_input, null);
        ImageView iv_dialod_close = (ImageView) view.findViewById(R.id.imageview_comment);
        iv_dialod_close.setOnClickListener(v -> bottomSheetDialog.dismiss());
        editText2 = view.findViewById(R.id.editTextTextMultiLine4);
        replySend = view.findViewById(R.id.reply_send);
        bottomSheetDialog = new BottomSheetDialog(this, R.style.dialog);
        bottomSheetDialog.setContentView(view);
        relativeLayout.setOnClickListener(v -> {
            editText2.setText("");
            bottomSheetDialog.show();
        });
        final Markwon markwon = Markwon.create(this);
        final MarkwonEditor editor = MarkwonEditor.create(markwon);
        editText2.addTextChangedListener((MarkwonEditorTextWatcher.withPreRender(
                editor,
                Executors.newCachedThreadPool(),
                editText2)));
        bottomSheetDialog.setOnCancelListener(v -> {
            editStyle = false;
        });
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

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private ArrayList<forum> forumArrayList;
        private Context context;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView name;
            private final TextView time;
            private final RadiusImageView logo;
            private final ButtonView like;
            Boolean incollection;
            private final TextView content;
            private final TextView comment1;
            private final TextView comment2;
            //            private final TextView addComment;
            private final ConstraintLayout constraintLayout;
            private final ConstraintLayout constraintLayout1;
            private final EditText editText1;
            private final ButtonView postComment;
            private final ImageView imageView;
            private final Button button;
            Boolean addC = false;
            private final ButtonView up_p;
            private final ButtonView down_p;
            private final TextView rate;
            private final TextView title;
            private final ButtonView delete;
            private final ImageView imageView_reply;
            private final TextView replysNum;
            private final ButtonView edit;
            private final ButtonView accept;


            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                name = (TextView) view.findViewById(R.id.textView16);
                time = (TextView) view.findViewById(R.id.textView17);
                content = (TextView) view.findViewById(R.id.textView18);
                comment1 = (TextView) view.findViewById(R.id.textView19);
                comment2 = (TextView) view.findViewById(R.id.textView20);
//                addComment = (TextView) view.findViewById(R.id.textView23);
                constraintLayout = view.findViewById(R.id.constrainLayout10);//查看更多回复
                constraintLayout1 = view.findViewById(R.id.constraintLayout11);//添加评论
                editText1 = view.findViewById(R.id.editTextTextMultiLine3);//添加comment
                postComment = view.findViewById(R.id.buttonView_comment);
                imageView = view.findViewById(R.id.imageview_comment);//删除键
                logo = view.findViewById(R.id.image_fornum);
                like = view.findViewById(R.id.likeFornum);
                button = view.findViewById(R.id.button10);
                up_p = view.findViewById(R.id.up_p);
                down_p = view.findViewById(R.id.down_p);
                rate = view.findViewById(R.id.rate);
                title = view.findViewById(R.id.forum_title);
                delete = view.findViewById(R.id.deleteFornum);
                imageView_reply = view.findViewById(R.id.imageView_reply);
                replysNum = view.findViewById(R.id.textView23);
                edit = view.findViewById(R.id.editFornum);
                accept = view.findViewById(R.id.accept);
                imageView.setOnClickListener(v -> {
                    constraintLayout1.setVisibility(View.GONE);
//                    addComment.setVisibility(View.VISIBLE);
                    personQuestion.this.relativeLayout.setVisibility(View.VISIBLE);
                    if (!addC)
                        constraintLayout.setVisibility(View.VISIBLE);
                });
            }

            public RadiusImageView getLogo() {
                return logo;
            }

            public TextView getName() {
                return name;
            }

            public TextView getTime() {
                return time;
            }

            public void setIncollection(Boolean incollection) {
                this.incollection = incollection;
            }

            public ButtonView getLike() {
                return like;
            }

            public TextView getContent() {
                return content;
            }

            public TextView getComment1() {
                return comment1;
            }

            public TextView getComment2() {
                return comment2;
            }

            public ConstraintLayout getConstraintLayout() {
                return constraintLayout;
            }

            public ConstraintLayout getConstraintLayout1() {
                return constraintLayout1;
            }

            public ButtonView getPostComment() {
                return postComment;
            }

            public EditText getEditText1() {
                return editText1;
            }

            public Button getButton() {
                return button;
            }

            public ButtonView getDown_p() {
                return down_p;
            }

            public ButtonView getUp_p() {
                return up_p;
            }

            public TextView getRate() {
                return rate;
            }

            public TextView getTitle() {
                return title;
            }

            public ButtonView getDelete() {
                return delete;
            }

            public ImageView getImageView_reply() {
                return imageView_reply;
            }

            public TextView getReplysNum() {
                return replysNum;
            }

            public ButtonView getEdit() {
                return edit;
            }

            public ButtonView getAccept() {
                return accept;
            }
        }

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used
         *                by RecyclerView.
         */
        public RecyclerAdapter(ArrayList<forum> dataSet) {
            forumArrayList = dataSet;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            context = viewGroup.getContext();
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.fornum_row_item, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
//            viewHolder.getTextView().setText(localDataSet[position]);
            forum f = forumArrayList.get(position);
            final Markwon markwon =Markwon.builder(context)
                    .usePlugin(JLatexMathPlugin.create(viewHolder.getContent().getTextSize()))
                    .build();
            viewHolder.addC = f.comments.size() <= 2;
            if (f.comments.size() > 0) {
                StringBuilder s1 = new StringBuilder();
                s1.append(f.comments.get(0).name).append("：").append(f.comments.get(0).content);
                viewHolder.getComment1().setText(s1);
                viewHolder.getComment1().setVisibility(View.VISIBLE);
                if (f.comments.size() > 1) {
                    StringBuilder s2 = new StringBuilder();
                    s2.append(f.comments.get(1).name).append("：").append(f.comments.get(1).content);
                    viewHolder.getComment2().setText(s2);
                    viewHolder.getComment2().setVisibility(View.VISIBLE);
                    viewHolder.getConstraintLayout().setVisibility(View.VISIBLE);
                    viewHolder.getConstraintLayout().setOnClickListener(v -> {
                        viewHolder.getConstraintLayout().setVisibility(View.GONE);
//                        viewHolder.getAddComment().setVisibility(View.VISIBLE);
                        for (int i = 2; i < f.comments.size(); i++) {
                            s2.append("\n").append(f.comments.get(i).name).append("：").append(f.comments.get(i).content);
                        }
                        viewHolder.getComment2().setText(s2);
                        viewHolder.addC = true;
                    });
                }
            }
            viewHolder.getButton().setOnClickListener(v -> {
//                viewHolder.getAddComment().setVisibility(View.GONE);

                viewHolder.getConstraintLayout1().setVisibility(View.VISIBLE);
                viewHolder.getConstraintLayout().setVisibility(View.GONE);
                personQuestion.this.relativeLayout.setVisibility(View.GONE);
//                viewHolder.addC = true;
            });
            viewHolder.getName().setText(f.name);
            viewHolder.getTime().setText(f.time);
            viewHolder.getRate().setText(String.valueOf(f.rate));
            viewHolder.getTitle().setText(f.title);
            RadiusImageView imageView = viewHolder.getLogo();
            Glide.with(context).load(f.logo).into(imageView);
            imageView.setOnClickListener(v -> {
                Intent intent = new Intent(context, personMoments.class);
                intent.putExtra("name", f.name);
                startActivity(intent);
            });
            markwon.setMarkdown(viewHolder.getContent(), f.content);
            viewHolder.setIncollection(f.incollection);
            String s = (forumArrayList.size() - 1) + " Replies";
            viewHolder.getReplysNum().setText(s);
            if (position == 0) {
                if (f.incollection) {
                    viewHolder.getLike().setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_after, 0, 0, 0);
                } else {
                    viewHolder.getLike().setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_before, 0, 0, 0);
                }
            } else {
//                viewHolder.getLike().setWidth(5);
                viewHolder.getLike().setVisibility(View.GONE);
//                viewHolder.getTitle().setVisibility(View.GONE);
                viewHolder.getTitle().setHeight(0);
                viewHolder.getImageView_reply().setVisibility(View.GONE);
                viewHolder.getReplysNum().setVisibility(View.GONE);
            }
            if (position == 1 && f.accept != null && f.accept) {
                accepted = true;
            } else if (position == 1) {
                accepted = false;
            }
            if (f.self) {
                viewHolder.getEdit().setVisibility(View.VISIBLE);
                viewHolder.getDelete().setVisibility(View.VISIBLE);
                if (position != 0 && type.equals("post_question")) {
                    if (accepted) {
                        if (position == 1) {
                            viewHolder.getAccept().setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct, 0, 0, 0);
                        } else {
                            viewHolder.getAccept().setVisibility(View.GONE);
                        }
                    } else {
                        viewHolder.getAccept().setCompoundDrawablesWithIntrinsicBounds(R.drawable.accept_before, 0, 0, 0);
                    }
                } else {
                    viewHolder.getAccept().setVisibility(View.GONE);
                }
            } else {
                viewHolder.getEdit().setVisibility(View.GONE);
                viewHolder.getDelete().setVisibility(View.GONE);
                viewHolder.getAccept().setVisibility(View.GONE);
                if (accepted) {
                    if (position == 1) {
                        viewHolder.getAccept().setVisibility(View.VISIBLE);
                        viewHolder.getAccept().setCompoundDrawablesWithIntrinsicBounds(R.drawable.correct, 0, 0, 0);
                    }
                }
            }
            viewHolder.getAccept().setOnClickListener(v -> {
                JSONObject j = new JSONObject();
                try {
                    j.put("id", f.id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, "http://gswxp2.top:1000/api/forum/accept", j, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (!"200".equals((String) response.get("code"))) {
                                        //TODO:toast something
                                    } else {
                                        initDataset2();
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
            viewHolder.getLike().setOnClickListener(v -> {
                HashMap<String, String> playload = new HashMap<>();
                playload.put("id", f.id);
                JSONObject j = new JSONObject(playload);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, "http://gswxp2.top:1000/api/forum/collect", j, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (!"200".equals((String) response.get("code"))) {
                                        //TODO:toast something
                                    } else {
                                        System.out.println(response);
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
                if (!viewHolder.incollection) {
                    viewHolder.getLike().setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_after, 0, 0, 0);
                    viewHolder.incollection = true;
                } else {
                    viewHolder.getLike().setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_before, 0, 0, 0);
                    viewHolder.incollection = false;
                }
            });
            viewHolder.getDelete().setOnClickListener(v -> {
                DialogLoader.getInstance().showConfirmDialog(context, "确认删除？", "确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String, String> playload = new HashMap<>();
                                playload.put("id", f.id);
                                if (viewHolder.getAbsoluteAdapterPosition() == 0) {
                                    playload.put("type", "post");
                                } else {
                                    playload.put("type", "reply");
                                }
                                JSONObject j = new JSONObject(playload);
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                        (Request.Method.POST, "http://gswxp2.top:1000/api/forum/delete", j, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    if (!"200".equals((String) response.get("code"))) {
                                                        //TODO:toast something
                                                    } else {
                                                        initDataset2();
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
            if (f.selfoperation != null && f.selfoperation.equals("up")) {
                viewHolder.getUp_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.up_ed, 0, 0, 0);
                viewHolder.getDown_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.down, 0, 0, 0);
            } else if (f.selfoperation != null && f.selfoperation.equals("down")) {
                viewHolder.getUp_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.up, 0, 0, 0);
                viewHolder.getDown_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_ed, 0, 0, 0);
            } else {
                viewHolder.getUp_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.up, 0, 0, 0);
                viewHolder.getDown_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.down, 0, 0, 0);
            }
            viewHolder.getUp_p().setOnClickListener(v -> {
                if (f.selfoperation != null && f.selfoperation.equals("up")) {
                    viewHolder.getUp_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.up, 0, 0, 0);
                    viewHolder.getDown_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.down, 0, 0, 0);
                    f.selfoperation = "";
                } else {
                    viewHolder.getUp_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.up_ed, 0, 0, 0);
                    viewHolder.getDown_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.down, 0, 0, 0);
                    f.selfoperation = "up";
                }
                HashMap<String, String> playload = new HashMap<>();
                playload.put("id", f.id);
                if (position == 0) {
                    playload.put("type", "post");
                } else {
                    playload.put("type", "reply");
                }
                playload.put("operation", "up");
                JSONObject j = new JSONObject(playload);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, "http://gswxp2.top:1000/api/forum/rate", j, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (!"200".equals((String) response.get("code"))) {
                                        //TODO:toast something
                                    } else {
                                        initDataset2();
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
            viewHolder.getDown_p().setOnClickListener(v -> {
                if (f.selfoperation != null && f.selfoperation.equals("down")) {
                    viewHolder.getUp_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.up, 0, 0, 0);
                    viewHolder.getDown_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.down, 0, 0, 0);
                    f.selfoperation = "";
                } else {
                    viewHolder.getUp_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.up, 0, 0, 0);
                    viewHolder.getDown_p().setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_ed, 0, 0, 0);
                    f.selfoperation = "down";
                }

                HashMap<String, String> playload = new HashMap<>();
                playload.put("id", f.id);
                if (position == 0) {
                    playload.put("type", "post");
                } else {
                    playload.put("type", "reply");
                }
                playload.put("operation", "down");
                JSONObject j = new JSONObject(playload);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, "http://gswxp2.top:1000/api/forum/rate", j, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (!"200".equals((String) response.get("code"))) {
                                        //TODO:toast something
                                    } else {
                                        initDataset2();
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
            viewHolder.getPostComment().setOnClickListener(v -> {
                HashMap<String, String> playload = new HashMap<>();
                playload.put("content", viewHolder.getEditText1().getText().toString());
                playload.put("type", "comment");
                playload.put("relateid", f.id);
                playload.put("operation", "new");
                playload.put("title", "");
                if (position == 0) {
                    playload.put("relatetype", "post");
                } else {
                    playload.put("relatetype", "reply");
                }
                JSONObject j = new JSONObject(playload);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, "http://gswxp2.top:1000/api/forum/content", j, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (!"200".equals((String) response.get("code"))) {
                                        //TODO:toast something
                                    } else {
                                        initDataset2();
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
            viewHolder.getEdit().setOnClickListener(v -> {
                if (position == 0) {
                    Intent intent = new Intent(context, PostDiscuss.class);
                    if (type.equals("post_discuss")) {
                        intent.putExtra("type", 1);
                    }
//                    intent.putExtra("type", 2);
                    intent.putExtra("content", f.content);
                    intent.putExtra("title", f.title);
                    intent.putExtra("forumId", f.id);
                    startActivity(intent);
                } else {
                    editStyle = true;
                    bottomSheetDialog.show();
                    editText2.setText(f.content);
                }
            });
            replySend.setOnClickListener(v -> {
                HashMap<String, String> playload = new HashMap<>();
                playload.put("content", editText2.getText().toString());
                playload.put("type", "reply");
                playload.put("title", "");
                playload.put("relateid", String.valueOf(forumId));
                if (editStyle) {
                    playload.put("operation", "modify");
                    playload.put("id", f.id);
                } else {
                    playload.put("operation", "new");
                }
                playload.put("relatetype", "post");
                JSONObject j = new JSONObject(playload);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, "http://gswxp2.top:1000/api/forum/content", j, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (!"200".equals((String) response.get("code"))) {
                                        //TODO:toast something
                                    } else {
                                        bottomSheetDialog.dismiss();
                                        initDataset2();
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
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return forumArrayList.size();
        }
    }

    public class forum {
        String id;
        String name;
        String logo;
        String time;
        String content = "空";
        String title;//
        int rate;
        Boolean self;
        Boolean incollection;//
        String selfoperation;
        String type;
        Boolean accept;
        ArrayList<comment> comments = new ArrayList<>();

        public forum(String id, String name, String logo, String time, String content,
                     String title, int rate, Boolean self,
                     Boolean incollection,
                     String selfoperation) {
            this.id = id;
            this.name = name;
            this.logo = logo;
            this.time = time;
            this.content = content;
            this.title = title;
            this.rate = rate;
            this.self = self;
            this.incollection = incollection;
            this.selfoperation = selfoperation;
            this.type = type;
        }
    }

    public class comment {
        String name;
        String content;
        String time;
        String id;

        public comment(String name,
                       String content,
                       String time,
                       String id) {
            this.name = name;
            this.content = content;
            this.time = time;
            this.id = id;
        }
    }

    public void initDataset() {
        forums = new ArrayList<>();
        forum item = new forum("1", "1", null, "11", "**Hello there!**", "tiezi1", 200, true, true, "");
        comment comment1 = new comment("ghy", "哈哈hahahhaahhahahahahahahahahahahahahhahahahahahhhhahahhahahaa", "2021.12.4", "1");
        comment comment2 = new comment("hhh", "heihei", null, "2");
        item.comments.add(comment1);
        item.comments.add(comment2);
        item.comments.add(comment1);
        forums.add(item);
        item = new forum("1", "2", null, "12", "```\n" + "/spotPosts//复用发现页接口\n" + "```\n" + "\n", "reply1", 123, false, null, "down");
        item.name = "2";
        item.time = "22";
        item.incollection = true;

        forums.add(item);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(forums);
        recyclerView.setAdapter(recyclerAdapter);
//        initDataset2();
    }

    public void initDataset2() {
        forums = new ArrayList<>();
        HashMap<String, String> playload = new HashMap<>();
        playload.put("id", forumId);
        JSONObject j = new JSONObject(playload);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "http://gswxp2.top:1000/api/forum/detail", j, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {

                                //TODO:toast something
                            } else {
                                forums.clear();
                                type = (String) response.get("type");
                                forum nf = new forum((String) response.get("id"), (String) response.get("name"), (String) response.get("logo"), (String) response.get("time"),
                                        (String) response.get("content"), (String) response.get("title"),
                                        (int) response.get("rate"), (Boolean) response.get("self"),
                                        (Boolean) response.get("incollection"), (String) response.get("selfoperation"));
                                JSONArray comments = (JSONArray) response.get("comments");
                                for (int j = 0; j < comments.length(); j++) {
                                    comment cccc = new comment((String) comments.optJSONObject(j).get("name"), (String) comments.optJSONObject(j).get("content"),
                                            (String) comments.optJSONObject(j).get("time"), (String) comments.optJSONObject(j).get("id"));
                                    nf.comments.add(cccc);
                                }
                                forums.add(nf);
                                JSONArray array = (JSONArray) response.get("reply");
                                for (int i = 0; i < array.length(); i++) {
                                    nf = new forum((String) array.optJSONObject(i).get("id"), (String) array.optJSONObject(i).get("name"), (String) array.optJSONObject(i).get("logo"),
                                            (String) array.optJSONObject(i).get("time"), (String) array.optJSONObject(i).get("content"),
                                            null, (int) array.optJSONObject(i).get("rate"), (Boolean) array.optJSONObject(i).get("self"),
                                            false, (String) array.optJSONObject(i).get("selfoperation"));
                                    nf.accept = (Boolean) array.optJSONObject(i).get("accept");
                                    comments = (JSONArray) array.getJSONObject(i).get("comments");
                                    for (int j = 0; j < comments.length(); j++) {
                                        comment cccc = new comment((String) comments.optJSONObject(j).get("name"), (String) comments.optJSONObject(j).get("content"),
                                                (String) comments.optJSONObject(j).get("time"), (String) comments.optJSONObject(j).get("id"));
                                        nf.comments.add(cccc);
                                    }
                                    forums.add(nf);
                                }
                                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(forums);
                                recyclerView.setAdapter(recyclerAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        personQuestion.this.finish();
                        Log.e("NetworkError", error.toString());
                    }
                });
        RequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDataset2();
    }
}