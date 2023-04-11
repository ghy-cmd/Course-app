package com.buaacourse.course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
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
import com.buaacourse.person.PersonFans;
import com.buaacourse.util.RequestSingleton;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CourseInfo extends AppCompatActivity {
    protected RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    String url = "";

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected CourseInfo.LayoutManagerType mCurrentLayoutManagerType;
    private static final String TAG = "RecyclerViewCourseInfo";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected IndexAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
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
        getinfo();
        mRecyclerView = findViewById(R.id.indexlist);
        mLayoutManager = new LinearLayoutManager(this);

        mCurrentLayoutManagerType = CourseInfo.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (CourseInfo.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
    }

    private void getinfo() {
        JSONObject j = new JSONObject();
        try {
            j.put("id", getIntent().getStringExtra("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, "http://gswxp2.top:1000/api/course/detail", j,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<Index> mDataset = new ArrayList<>();
                            mDataset.clear();
                            ((TextView) findViewById(R.id.coursetitle)).setText(response.getString("name"));
                            ((TextView) findViewById(R.id.courseresource)).setText(response.getString("resource"));
                            if (response.getString("resource").equals("")) {
                                ((TextView) findViewById(R.id.courseresource)).setText("暂无其他资源");
                            } else {
                                url = response.getString("resource");
                                ((TextView) findViewById(R.id.courseresource)).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                                ((TextView) findViewById(R.id.courseresource)).setOnClickListener(v -> {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    Uri uri = Uri.parse(url);
                                    intent.setData(uri);
                                    CourseInfo.this.startActivity(intent);
                                });
                            }
                            JSONArray people = (JSONArray) response.get("index");
                            for (int i = 0; i < people.length(); i++) {
                                Index f = new Index(people.getString(i));
                                mDataset.add(f);
                            }
                            mAdapter = new IndexAdapter(mDataset);
                            mRecyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public static class Index {

        public String url;

        Index(String url) {
            this.url = url;
        }


    }

    public void setRecyclerViewLayoutManager(CourseInfo.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(this, 2);
                mCurrentLayoutManagerType = CourseInfo.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = CourseInfo.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    public static class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.ViewHolder> {
        private static final String TAG = "TodoAdapter";
        private Context context;
        private ArrayList<Index> mDataSet;

        // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final ImageView image;

            public ViewHolder(View v) {
                super(v);
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    }
                });
                image = v.findViewById(R.id.imageView17);
            }

            public ImageView getImage() {
                return image;
            }
        }
        // END_INCLUDE(recyclerViewSampleViewHolder)

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
         */
        public IndexAdapter(ArrayList<Index> dataSet) {
            mDataSet = dataSet;
        }

        // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
        // Create new views (invoked by the layout manager)
        @Override
        public IndexAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            context = viewGroup.getContext();
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.indexitem, viewGroup, false);
            return new IndexAdapter.ViewHolder(v);
        }
        // END_INCLUDE(recyclerViewOnCreateViewHolder)

        // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(IndexAdapter.ViewHolder viewHolder, final int position) {
            Log.d(TAG, "Element " + position + " set.");

            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            Log.i("123", String.valueOf(mDataSet.get(position)));
            viewHolder.getImage().setOnClickListener(v -> {
                final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                ImageView imgView = new ImageView(context);
                imgView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                Glide.with(context).load(mDataSet.get(position).url).into(imgView);
                dialog.setContentView(imgView);
                dialog.show();
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            });
            Glide.with(context).load(mDataSet.get(position).url).into(viewHolder.getImage());
        }
        // END_INCLUDE(recyclerViewOnBindViewHolder)

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

    }
}