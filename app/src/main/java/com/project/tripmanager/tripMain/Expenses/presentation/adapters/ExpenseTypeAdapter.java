package com.project.tripmanager.tripMain.Expenses.presentation.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.project.tripmanager.R;
import com.project.tripmanager.tripMain.Expenses.dto.ExpenseTypeModel;

public class ExpenseTypeAdapter extends FirestoreRecyclerAdapter<ExpenseTypeModel, ExpenseTypeAdapter.ExpenseTypeViewHolder> {

    private ExpenseTypeClickListener listener;

    public ExpenseTypeAdapter(@NonNull FirestoreRecyclerOptions<ExpenseTypeModel> options) {
        super(options);
    }

    public void setListener(ExpenseTypeClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ExpenseTypeViewHolder holder, int position, @NonNull ExpenseTypeModel model) {
        holder.typeName.setText(model.getTypeName());
        holder.date.setText(model.getCreationDate());
        holder.total.setText("Total expense: "+model.getTotalExpense());
    }

    @NonNull
    @Override
    public ExpenseTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_type_item,parent,false);
        return new ExpenseTypeViewHolder(v,listener);
    }

     class ExpenseTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView typeName,date,total;
        CardView card;
        private final ExpenseTypeClickListener listener;

        public ExpenseTypeViewHolder(@NonNull View itemView, ExpenseTypeClickListener listener) {
            super(itemView);
            typeName = itemView.findViewById(R.id.txt_typeName);
            date = itemView.findViewById(R.id.txt_date);
            total = itemView.findViewById(R.id.txt_total);
            card = itemView.findViewById(R.id.type_card);
            this.listener = listener;
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getBindingAdapterPosition();
            listener.onExpenseTypeClicked(getSnapshots().getSnapshot(pos), pos);
        }
    }

    public interface ExpenseTypeClickListener
    {
        void onExpenseTypeClicked(DocumentSnapshot docSnapshot, int pos);
    }
}
