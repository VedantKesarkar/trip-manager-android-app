package com.project.tripmanager.tripMain.Packing.presentation.adapters;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.project.tripmanager.R;
import com.project.tripmanager.tripMain.Packing.dto.CategorySelected;

public class CatListAdapter extends FirestoreRecyclerAdapter<CategorySelected, CatListAdapter.CatViewHolder> {
private CatItemClickListener listener;
private ViewMoreClickListener viewMoreListener;

    public CatListAdapter(@NonNull FirestoreRecyclerOptions<CategorySelected> options) {

        super(options);
    }

    public void setListener(CatItemClickListener listener) {
        this.listener = listener;
    }

    public void setViewMoreListener(ViewMoreClickListener viewMoreListener) {
        this.viewMoreListener = viewMoreListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull CatViewHolder holder, int position, @NonNull CategorySelected model) {
        holder.title.setText(model.getTitle());
        holder.name.setText(model.getCreatedBy());
        holder.categories.setText(model.getCategories().toString());

    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_list_item,parent,false);
        return new CatViewHolder(v,listener,viewMoreListener);
    }

    class CatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        TextView title,name,categories;
        ImageView imgMore;
        CatItemClickListener listener;
        ViewMoreClickListener viewMoreListener;
        public CatViewHolder(@NonNull View itemView, CatItemClickListener catItemClickListener,ViewMoreClickListener viewMoreClickListener) {
            super(itemView);
            itemView.findViewById(R.id.catListItem).setOnClickListener(this);
            listener = catItemClickListener;
            viewMoreListener = viewMoreClickListener;
            title  = itemView.findViewById(R.id.txt_title);
            name = itemView.findViewById(R.id.txt_creator);
            categories = itemView.findViewById(R.id.txt_allCat);
            imgMore = itemView.findViewById(R.id.img_more);

            imgMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getBindingAdapterPosition();
            if(view.getId() == R.id.catListItem)
            {
                if(pos!=NO_POSITION && listener!=null)
                {

                    listener.onCatItemClick(getSnapshots().getSnapshot(pos),pos);
                }
            }
            else {
                showPopUpMenu(view);
            }
        }

        private void showPopUpMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.inflate(R.menu.cat_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.item_global_list) {
                int pos = getBindingAdapterPosition();
                viewMoreListener.onViewGlobalList(getSnapshots().getSnapshot(pos),pos);
                return true;
            }
            return false;
        }
    }


    public interface CatItemClickListener
    {
        void onCatItemClick(DocumentSnapshot documentSnapshot,int pos);
    }

    public interface ViewMoreClickListener
    {
        void onViewGlobalList(DocumentSnapshot documentSnapshot,int pos);
    }

}
