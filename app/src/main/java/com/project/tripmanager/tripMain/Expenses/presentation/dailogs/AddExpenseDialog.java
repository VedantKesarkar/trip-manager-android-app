package com.project.tripmanager.tripMain.Expenses.presentation.dailogs;

import static com.project.tripmanager.tripMain.Packing.presentation.dialogs.CreateCatSelectionDialog.UNTITLED;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.project.tripmanager.R;

public class AddExpenseDialog extends DialogFragment {
    private static final String TAG = "ExpenseTypeCreationDial";
    private EditText edtExpenseName,edtAmt;
    private TextInputLayout amtLayout;
    private ExpenseAddedListener listener;

    public AddExpenseDialog() {
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view  = inflater.inflate(R.layout.add_expenses_dialog,null);
        edtExpenseName = view.findViewById(R.id.edt_expense_name);
        edtAmt = view.findViewById(R.id.edt_amount);
        amtLayout = view.findViewById(R.id.amtLayout);

        builder.setView(view)
                .setTitle("Add Expense!")
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    //do nothing
                })
                .setPositiveButton("Ok",((dialogInterface, i) -> {
                    String name = edtExpenseName.getText().toString();
                    String amt = edtAmt.getText().toString();

                    if(amt.equals(""))
                    {
                        amtLayout.setHelperText("Invalid amount");
                    }
                    else if(name.equals(""))
                    {
                        listener.onExpenseAdded(UNTITLED,Long.parseLong(amt));
                    }
                    else {
                        listener.onExpenseAdded(name,Long.parseLong(amt));
                    }
                }));
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ExpenseAddedListener) context;
        }
        catch (ClassCastException e){
            Log.d(TAG, context +"must implement interface");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener = null;
    }

    public interface ExpenseAddedListener
    {
        void onExpenseAdded(String name, long amt);
    }
}

