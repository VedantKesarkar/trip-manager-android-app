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

import com.project.tripmanager.R;

public class ExpenseTypeCreationDialog extends DialogFragment {
    private static final String TAG = "ExpenseTypeCreationDial";
    private EditText edtType;
    private  ExpenseTypeCreationInterface listener;

    public ExpenseTypeCreationDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view  = inflater.inflate(R.layout.expense_type_creation_dialog,null);
        edtType = view.findViewById(R.id.edt_expense_type);
        builder.setView(view)
                .setTitle("Create Expense Type")
                .setNegativeButton("Cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("Ok",((dialogInterface, i) -> {
                    String typeName = edtType.getText().toString();
                    if(typeName.equals(""))listener.onExpenseTypeCreated(UNTITLED);
                    else listener.onExpenseTypeCreated(typeName);
                }));
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ExpenseTypeCreationInterface) context;
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

    public interface ExpenseTypeCreationInterface
    {
        void onExpenseTypeCreated(String typeName);
    }
}
