package com.buaacourse.course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.buaacourse.R;
import com.buaacourse.auth.Reset;
import com.buaacourse.util.RequestSingleton;
import com.xuexiang.xui.widget.button.ButtonView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class CourseDiscuss extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }


    protected CourseDiscuss.LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected DiscussAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<ShortDiscuss> mDataset;
    private String forumId;
    private LayoutInflater inflater;
    private ViewGroup container;

    public CourseDiscuss(String forumId){
        this.forumId=forumId;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initDataset();
    }
    public void onResume() {
        super.onResume();
        initDataset();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater=inflater;
        View rootView = inflater.inflate(R.layout.fragment_course_discuss, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = rootView.findViewById(R.id.recyclerview1);
        ButtonView mButton = rootView.findViewById(R.id.post);
        mButton.setOnClickListener(v -> {
            if(rootView.findViewById(R.id.pops).getVisibility()==View.GONE){
                rootView.findViewById(R.id.pops).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.pops).bringToFront();
            }else{
                rootView.findViewById(R.id.pops).setVisibility(View.GONE);
            }
        });
        rootView.findViewById(R.id.button8).setOnClickListener(v->{
            Intent intent = new Intent(getActivity(),PostDiscuss.class);
            intent.putExtra("type",0);
            intent.putExtra("courseId",forumId);
            startActivity(intent);
            rootView.findViewById(R.id.pops).setVisibility(View.GONE);
        });
        rootView.findViewById(R.id.button9).setOnClickListener(v->{
            Intent intent = new Intent(getActivity(),PostDiscuss.class);
            intent.putExtra("type",1);
            intent.putExtra("courseId",forumId);
            startActivity(intent);
            rootView.findViewById(R.id.pops).setVisibility(View.GONE);
        });
        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = CourseDiscuss.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (CourseDiscuss.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);


        return rootView;
    }
    public void setRecyclerViewLayoutManager(CourseDiscuss.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = CourseDiscuss.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = CourseDiscuss.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mDataset = new ArrayList<ShortDiscuss>(10);
        HashMap<String,String> req=new HashMap<>(0);
        req.put("id",forumId);
        JSONObject j=new JSONObject(req);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "http://gswxp2.top:1000/api/forum/list", j, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!"200".equals((String) response.get("code"))) {
                                //TODO:toast something
                            } else {
                                mDataset.clear();
                                JSONArray array = (JSONArray) response.get("posts");
                                for (int i = 0; i < array.length(); i++) {
                                    mDataset.add(new ShortDiscuss(
                                            (String) array.optJSONObject(i).get("name"),
                                            (String) array.optJSONObject(i).get("logo"),
                                            (String) array.optJSONObject(i).get("content"),
                                            (String) array.optJSONObject(i).get("title"),
                                            ((Integer)array.optJSONObject(i).get("star")).intValue(),
                                            ((Integer)array.optJSONObject(i).get("follows")).intValue(),
                                            (String) array.optJSONObject(i).get("time"),
                                                (String) array.optJSONObject(i).get("id")

                                            ));
                                }
                                mAdapter = new DiscussAdapter(mDataset);
                                // Set CustomAdapter as the adapter for RecyclerView.
                                mRecyclerView.setAdapter(mAdapter);
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
        RequestSingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

}