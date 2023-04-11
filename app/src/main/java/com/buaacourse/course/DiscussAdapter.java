package com.buaacourse.course;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.buaacourse.R;
import com.bumptech.glide.Glide;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import java.util.ArrayList;

public class DiscussAdapter extends RecyclerView.Adapter<DiscussAdapter.ViewHolder>{
    private static final String TAG = "CustomAdapter";
    private Context context;
    private ArrayList<ShortDiscuss> mDataSet;
    private boolean cliked;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            cardView = v.findViewById(R.id.cardView1);
        }

        public CardView getCardView() {
            return cardView;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public DiscussAdapter(ArrayList<ShortDiscuss> dataSet) {
        mDataSet = dataSet;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public DiscussAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        context=viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.quesiton_row_item, viewGroup, false);

        return new DiscussAdapter.ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DiscussAdapter.ViewHolder viewHolder, final int position) {
        boolean cliked=false;
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        //todo
        viewHolder.itemView.setOnClickListener(v -> {
            Intent i=new Intent(DiscussAdapter.this.context,personQuestion.class);
            i.putExtra("forumId",mDataSet.get(position).id);
            context.startActivity(i);
        });
        viewHolder.itemView.findViewById(R.id.button7).setOnClickListener(v->{
            like(viewHolder);
        });
        ((TextView) viewHolder.getCardView().findViewById(R.id.textView3)).setText(mDataSet.get(position).name);
        ((TextView) viewHolder.getCardView().findViewById(R.id.textView16)).setText(mDataSet.get(position).time);
        ((TextView) viewHolder.getCardView().findViewById(R.id.textView17)).setText(mDataSet.get(position).title);
        ((TextView) viewHolder.getCardView().findViewById(R.id.textView18)).setText(mDataSet.get(position).content);
        ((TextView) viewHolder.getCardView().findViewById(R.id.textViewxxx)).setText(String.valueOf(mDataSet.get(position).star));
        ((TextView) viewHolder.getCardView().findViewById(R.id.textView22)).setText(String.valueOf(mDataSet.get(position).follows));


        RadiusImageView imageView=(RadiusImageView)viewHolder.getCardView().findViewById(R.id.radiusImageView1);
        Glide.with(context).load(mDataSet.get(position).imageUrl).into(imageView);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    void like(ViewHolder viewHolder){
        if(!cliked) {
            viewHolder.itemView.findViewById(R.id.button7).setBackgroundResource(R.drawable.like_after);
            TextView view = ((TextView) viewHolder.getCardView().findViewById(R.id.textViewxxx));
            int star=Integer.parseInt(view.getText().toString())+1;
            view.setText(Integer.toString(star));
        }
        else{
            viewHolder.itemView.findViewById(R.id.button7).setBackgroundResource(R.drawable.like_before);
            TextView view = ((TextView) viewHolder.getCardView().findViewById(R.id.textViewxxx));
            int star=Integer.parseInt(view.getText().toString())-1;
            view.setText(Integer.toString(star));
        }
        cliked=!cliked;
    }

}
