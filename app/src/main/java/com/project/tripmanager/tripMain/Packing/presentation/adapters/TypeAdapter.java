package com.project.tripmanager.tripMain.Packing.presentation.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.project.tripmanager.R;
import com.project.tripmanager.tripMain.Packing.dto.CategoryType;

import java.util.ArrayList;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> implements SubTypeAdapter.OnTypeClick {

    private final ArrayList<CategoryType> list;
    Context context;
    private final OnCatClick onCatClick;
    static boolean onClick = false;
    static int pPos = -1;


    public TypeAdapter(ArrayList<CategoryType> list, Context context, OnCatClick onCatClick) {
        this.list = list;
        this.context = context;
        this.onCatClick = onCatClick;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.category_type,null,false);
        return new ViewHolder(view,onCatClick);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.title.setText(list.get(position).getTitle());
            SubTypeAdapter subTypeAdapter = new SubTypeAdapter(list.get(position).getCategoryList(),context,this);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            holder.recyclerView.setAdapter(subTypeAdapter);
            subTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onTypeSelected(int pos, boolean click, View view) {
            onClick = click;
            onCatClick.onCatSelected(pPos,pos,view);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {

       RecyclerView recyclerView;
       TextView title;
       OnCatClick onCatClick;
        public ViewHolder(@NonNull View itemView,OnCatClick onCatClick) {
            super(itemView);
            this.onCatClick = onCatClick;

            recyclerView = itemView.findViewById(R.id.rv_child);
            recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {


                    if(e.getAction()==MotionEvent.ACTION_UP)
                    {
                        pPos = getBindingAdapterPosition();

                    }
                    return false;


                }

                @Override
                public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {


                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });

            title = itemView.findViewById(R.id.txt_catType);
        }



    }

    public interface OnCatClick
    {

        void onCatSelected(int tPos, int sPos, View view);
    }
}
