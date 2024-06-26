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
public class SoloDialog extends AppCompatDialogFragment {

    private static final String TAG = "SoloDialog";

private TripCreationDialogInterface listener;
private EditText tripName;
    public SoloDialog() {

    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(),R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view  = inflater.inflate(R.layout.trip_creation_dialog,null);


        TextInputLayout name =view.findViewById(R.id.nameLayout);
        name.setVisibility(View.GONE);
        TextInputLayout groupCode =view.findViewById(R.id.groupCodeLayout);
        groupCode.setVisibility(View.GONE);

        tripName =view.findViewById(R.id.edt_tripName);

        builder.setView(view)
                .setTitle("Solo trip")
                .setNegativeButton("Cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    String mTripName = tripName.getText().toString();
                    listener.soloFieldVals(mTripName);
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
    public interface TripCreationDialogInterface
    {
        void soloFieldVals(String tripName);
    }
}
