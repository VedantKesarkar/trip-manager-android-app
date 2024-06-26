package com.project.tripmanager.tripMain.ExpenseReports.presentation.dailogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.tripmanager.R;
import com.project.tripmanager.tripMain.ExpenseReports.domain.BudgetViewModel;

public class BudgetChangeDialog extends DialogFragment {

    private BudgetViewModel budgetViewModel;
    public BudgetChangeDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(),R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.budget_change_dialog,null);
        EditText edtAmt = view.findViewById(R.id.edt_amt_add);
        builder.setView(view);
        builder.setTitle("Increase budget by");
        builder.setPositiveButton("Ok",(dialogInterface, i) -> {
            if(!edtAmt.getText().toString().equals(""))
            {
                budgetViewModel.addToBudget(edtAmt.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel",(dialogInterface, i) -> {
            //do nothing;
        });

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.budget_change_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        budgetViewModel  = new ViewModelProvider(requireActivity()).get(BudgetViewModel.class);

    }

}
