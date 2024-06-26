package com.project.tripmanager.newTrip.presentation;

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
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.project.tripmanager.R;

//TODO implement form validation
public class CreationDialog extends AppCompatDialogFragment {

    private static final String TAG = "TripCreationDialog";

private TripCreationDialogInterface listener;
private EditText name,tripName,budget;
    public CreationDialog() {

    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(),R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view  = inflater.inflate(R.layout.trip_creation_dialog,null);
        name =view.findViewById(R.id.edt_name);
        tripName =view.findViewById(R.id.edt_tripName);
        budget =view.findViewById(R.id.edt_budget);
        TextInputLayout groupCode =view.findViewById(R.id.groupCodeLayout);
        groupCode.setVisibility(View.GONE);

        builder.setView(view)
                .setTitle("Create group")
                .setNegativeButton("Cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    String uName = name.getText().toString();
                    String uTripName = tripName.getText().toString();
                    String amt = budget.getText().toString();
                    if(amt.equals(""))amt = "0";
                    listener.fieldValues(uName,uTripName,amt);
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (TripCreationDialogInterface) context;
        }
        catch (ClassCastException e){
            Log.d(TAG, context +"must implement interface");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;

    }

    public interface TripCreationDialogInterface
    {
        void fieldValues(String username,String tripName,String amt);
    }
}
