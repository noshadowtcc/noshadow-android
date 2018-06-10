package com.noshadow.app.features;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noshadow.app.R;
import com.noshadow.app.model.JobObject;

import java.util.List;

public class BackgrounditemsAdapter extends RecyclerView.Adapter<BackgroundItemViewHolder> {

    private Context context;

    private List<JobObject> items;


    public BackgrounditemsAdapter(Context context, List<JobObject> items) {
        this.context = context;
        this.items = items;
    }


    public void setItems(List<JobObject> items) {
        this.items = items;
    }

    @Override
    public BackgroundItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.background_item, parent, false);
        return new BackgroundItemViewHolder(parent.getContext(), v);
    }

    @Override
    public void onBindViewHolder(BackgroundItemViewHolder holder, int position) {
        JobObject model = items.get(position);

        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}