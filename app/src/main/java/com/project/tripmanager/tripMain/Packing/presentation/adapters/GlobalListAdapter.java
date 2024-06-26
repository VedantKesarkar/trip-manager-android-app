package com.project.tripmanager.tripMain.Packing.presentation.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tripmanager.R;
import com.project.tripmanager.tripMain.Packing.dto.GlobalListItem;

public class GlobalListAdapter extends ListAdapter<GlobalListItem, GlobalListAdapter.GlobalViewHolder> {

private GlobalListItemClickListener listener;
    public GlobalListAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setListener(GlobalListItemClickListener listener) {
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<GlobalListItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<GlobalListItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull GlobalListItem oldItem, @NonNull GlobalListItem newItem) {
            return oldItem.getItemName().equals(newItem.getItemName())&&
                    oldItem.getCount().equals(newItem.getCount());
        }

        @Override
        public boolean areContentsTheSame(@NonNull GlobalListItem oldItem, @NonNull GlobalListItem newItem) {
            return oldItem.getItemName().equals(newItem.getItemName())&&
                    oldItem.getCount().equals(newItem.getCount());
        }
    };

    @NonNull
    @Override
    public GlobalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.global_list_item, parent, false);
        return new GlobalViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GlobalViewHolder holder, int position) {
        holder.getName().setText(getItem(position).getItemName());
        holder.getCount().setText(String.valueOf(getItem(position).getCount()));
    }

    public static class GlobalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name, count;
        GlobalListItemClickListener listener;
        public GlobalViewHolder(@NonNull View itemView, GlobalListItemClickListener listener) {
            super(itemView);
            name  = itemView.findViewById(R.id.txt_itemName);
            count  = itemView.findViewById(R.id.txt_count);
            itemView.setOnClickListener(this);
            this.listener = listener;
        }

        public TextView getName() {
            return name;
        }

        public TextView getCount() {
            return count;
        }

        @Override
        public void onClick(View view) {
            if(getBindingAdapterPosition()!=RecyclerView.NO_POSITION)
                listener.onGlobalListItemClick(getBindingAdapterPosition(),view);
        }
    }

    public interface GlobalListItemClickListener
    {
        void onGlobalListItemClick(int pos, View view);
    }

}

