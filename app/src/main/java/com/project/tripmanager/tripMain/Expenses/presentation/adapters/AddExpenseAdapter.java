package com.project.tripmanager.tripMain.Expenses.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.project.tripmanager.R;
import com.project.tripmanager.tripMain.Expenses.dto.ExpenseModel;

public class AddExpenseAdapter extends FirestoreRecyclerAdapter<ExpenseModel, AddExpenseAdapter.AddExpenseViewholder> {


    public AddExpenseAdapter(@NonNull FirestoreRecyclerOptions<ExpenseModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AddExpenseViewholder holder, int position, @NonNull ExpenseModel model) {
        holder.name.setText(model.getName());
        holder.amt.setText(String.valueOf(model.getAmt()));
    }

    @NonNull
    @Override
    public AddExpenseViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item,parent,false);
        return new AddExpenseViewholder(view);
    }

    public static class AddExpenseViewholder extends RecyclerView.ViewHolder
    {
        private final TextView name;
        private final TextView amt;

        public AddExpenseViewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_expense_name);
            amt = itemView.findViewById(R.id.txt_expense_amt);
        }
    }
}
