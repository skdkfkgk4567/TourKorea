package com.example.sns_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sns_project.CallBack.RecyclerViewAdapterCallback;
import com.example.sns_project.R;
import com.example.sns_project.activity.AutoCompleteParse;
import com.example.sns_project.activity.SearchEntity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by KJH on 2017-11-06.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<SearchEntity> itemLists = new ArrayList<>();
    private RecyclerViewAdapterCallback callback;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView address;

        public CustomViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            address = itemView.findViewById(R.id.tv_address);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int ItemPosition = position;

        if( holder instanceof CustomViewHolder) {
            final CustomViewHolder viewHolder = (CustomViewHolder)holder;

            viewHolder.title.setText(itemLists.get(position).getTitle());
            viewHolder.address.setText(itemLists.get(position).getAddress());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.showToast(ItemPosition);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }

    public void setData(ArrayList<SearchEntity> itemLists) {
        this.itemLists = itemLists;
    }

    public void setCallback(RecyclerViewAdapterCallback callback) {
        this.callback = callback;
    }

    public void filter(String keyword) {
        if (keyword.length() >= 2) {
            try {
                AutoCompleteParse parser = new AutoCompleteParse(this);
                itemLists.addAll(parser.execute(keyword).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
