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

public class SubTypeAdapter extends RecyclerView.Adapter<SubTypeAdapter.ViewHolder> {

    private static ArrayList<CategorySubType> list = new ArrayList<>();
    static Context context;
    private final OnTypeClick onTypeClick;

    public SubTypeAdapter(ArrayList<CategorySubType> list, Context context, OnTypeClick onTypeClick) {
        this.onTypeClick = onTypeClick;
        SubTypeAdapter.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.category_sub_type,null,false);
        return new ViewHolder(view,onTypeClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.icon.setImageResource(list.get(position).getImg());
            holder.descp.setText(list.get(position).getDescp());
            boolean selected = list.get(holder.getAdapterPosition()).isSelected();

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
        OnTypeClick onTypeClick;

        public ViewHolder(@NonNull View itemView, OnTypeClick onTypeClick) {
            super(itemView);
            this.onTypeClick = onTypeClick;
            card = itemView.findViewById(R.id.subType_item);
            card.setOnClickListener(this);
            icon = itemView.findViewById(R.id.subType_imgView);
            descp = itemView.findViewById(R.id.subType_txt);
        }

        @Override
        public void onClick(View view) {
            onTypeClick.onTypeSelected(getBindingAdapterPosition(),true,view);

        }
    }

    public interface OnTypeClick
    {
        void onTypeSelected(int pos, boolean click, View view) ;
    }
}
