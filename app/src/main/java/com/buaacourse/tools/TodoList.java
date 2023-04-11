package com.buaacourse.tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buaacourse.R;

import java.util.ArrayList;
import java.util.Date;


public class TodoList extends Fragment {
    protected RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    ArrayList<todoItem> mDataset;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    protected TodoList.LayoutManagerType mCurrentLayoutManagerType;
    private static final String TAG = "RecyclerViewTodoList";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected TodoAdapter mAdapter;

    public TodoList() {
        super(R.layout.fragment_todo_list);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = requireArguments();
        mDataset= (ArrayList<todoItem>) bundle.getSerializable("todos");
        mRecyclerView = view.findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(getContext());

        mCurrentLayoutManagerType = TodoList.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (TodoList.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mRecyclerView.setAdapter(new TodoAdapter(mDataset));
    }

        public static class todoItem {
        String title;
        String content;
        String time;
        String url;
        Date date;

        public todoItem(String title, String content, String time) {
            this.title = title;
            this.content = content;
            this.time = time;
        }

        @Override
        public String toString() {
            return "todoItem{" +
                    "title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", time='" + time + '\'' +
                    ", url='" + url + '\'' +
                    ", date=" + date +
                    '}';
        }
    }
    public void setRecyclerViewLayoutManager(TodoList.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getContext(), 2);
                mCurrentLayoutManagerType = TodoList.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getContext());
                mCurrentLayoutManagerType = TodoList.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }
    public static class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
        private static final String TAG = "TodoAdapter";
        private Context context;
        private ArrayList<todoItem> mDataSet;

        // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView title;
            private final TextView content;
            private final TextView ddl;

            public ViewHolder(View v) {
                super(v);
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    }
                });
                title = v.findViewById(R.id.textView36);
                content = v.findViewById(R.id.textView41);
                ddl = v.findViewById(R.id.textView40);
            }

            public TextView getTitle() {
                return title;
            }

            public TextView getContent() {
                return content;
            }

            public TextView getDdl() {
                return ddl;
            }
        }
        // END_INCLUDE(recyclerViewSampleViewHolder)

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
         */
        public TodoAdapter(ArrayList<TodoList.todoItem> dataSet) {
            mDataSet = dataSet;
        }

        // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
        // Create new views (invoked by the layout manager)
        @Override
        public TodoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            context = viewGroup.getContext();
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_todo_item, viewGroup, false);
            return new TodoAdapter.ViewHolder(v);
        }
        // END_INCLUDE(recyclerViewOnCreateViewHolder)

        // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(TodoAdapter.ViewHolder viewHolder, final int position) {
            Log.d(TAG, "Element " + position + " set.");

            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            Log.i("123", String.valueOf(mDataSet.get(position)));
            viewHolder.getTitle().setText(mDataSet.get(position).title);
            viewHolder.getContent().setText(mDataSet.get(position).content);
            viewHolder.getDdl().setText(mDataSet.get(position).time);
            viewHolder.getContent().setPaintFlags(0);
            if(mDataSet.get(position).url!=null){
                viewHolder.getContent().getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
                viewHolder.itemView.setOnClickListener(v -> {
                    Intent intent =new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(mDataSet.get(position).url);
                    intent.setData(uri);
                    context.startActivity(intent);
                });
            }

        }
        // END_INCLUDE(recyclerViewOnBindViewHolder)

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
    }
}