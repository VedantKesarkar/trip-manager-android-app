package com.project.tripmanager.tripMain.Packing.presentation.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.project.tripmanager.R;
import com.project.tripmanager.tripMain.Packing.dto.CategorySubType;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private static ArrayList<CategorySubType> list = new ArrayList<>();
    private final Context context;
    private final OnTagClick onTagClick;

    public TagAdapter(ArrayList<CategorySubType> list, Context context, OnTagClick onTypeClick) {
        this.onTagClick = onTypeClick;
        TagAdapter.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.category_sub_type,null,false);
        return new ViewHolder(view, onTagClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.icon.setImageResource(list.get(position).getImg());
            holder.descp.setText(list.get(position).getDescp());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        ImageView icon;
        TextView descp;

        MaterialCardView card;
        OnTagClick onTagClick;

        public ViewHolder(@NonNull View itemView, OnTagClick onTypeClick) {
            super(itemView);
            this.onTagClick = onTypeClick;
            card = itemView.findViewById(R.id.subType_item);
            card.setOnClickListener(this);
            icon = itemView.findViewById(R.id.subType_imgView);
            descp = itemView.findViewById(R.id.subType_txt);
        }

        @Override
        public void onClick(View view) {
            onTagClick.onTagSelected(getBindingAdapterPosition());

        }
    }

    public interface OnTagClick
    {
        void onTagSelected(int pos) ;
    }
}
