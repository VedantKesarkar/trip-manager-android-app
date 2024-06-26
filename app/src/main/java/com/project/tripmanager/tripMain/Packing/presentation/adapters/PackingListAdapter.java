package com.project.tripmanager.tripMain.Packing.presentation.adapters;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.project.tripmanager.R;
import com.project.tripmanager.tripMain.Packing.dto.PackingItem;

public class PackingListAdapter extends FirestoreRecyclerAdapter<PackingItem, PackingListAdapter.PackViewHolder> {
private OnCheckListener listener;


    public PackingListAdapter(@NonNull FirestoreRecyclerOptions<PackingItem> options) {
        super(options);
    }

    public void setListener(OnCheckListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull PackViewHolder holder, int position, @NonNull PackingItem model) {
        holder.checkBox.setChecked(model.isChecked());
        holder.title.setText(model.getItemName());
    }

    @NonNull
    @Override
    public PackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.packing_list_item,parent,false);
        return new PackViewHolder(v,listener);
    }



    class PackViewHolder extends RecyclerView.ViewHolder
    {

        TextView title;
        CheckBox checkBox;
        OnCheckListener listener;
        public PackViewHolder(@NonNull View itemView, OnCheckListener onCheckListener) {
            super(itemView);

            listener = onCheckListener;
            title  = itemView.findViewById(R.id.txt_ItemName);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                int pos = getBindingAdapterPosition();
                if(pos!=NO_POSITION && listener!=null)
                {
                    listener.onItemCheckedChanged(getSnapshots().getSnapshot(pos),pos,b);
                }
            });

        }


    }


    public interface OnCheckListener
    {
        void onItemCheckedChanged(DocumentSnapshot documentSnapshot, int pos, boolean checked);
    }

}
