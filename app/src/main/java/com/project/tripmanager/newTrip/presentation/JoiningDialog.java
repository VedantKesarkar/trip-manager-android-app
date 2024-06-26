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

public class JoiningDialog extends AppCompatDialogFragment {

    //TODO implement form validation
    private static final String TAG = "JoiningDialog";
private TripCreationDialogInterface listener;
private EditText name;
    private EditText groupCode;
    public JoiningDialog() {

    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(),R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view  = inflater.inflate(R.layout.trip_creation_dialog,null);
        name =view.findViewById(R.id.edt_name);
        TextInputLayout tripName = view.findViewById(R.id.tripNameLayout);
        groupCode =view.findViewById(R.id.edt_groupCode);
        tripName.setVisibility(View.GONE);

        builder.setView(view)
                .setTitle("Join group")
                .setNegativeButton("Cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    String uName = name.getText().toString();
                    String code = groupCode.getText().toString();
                    listener.fieldVal(uName,code);
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
        void fieldVal(String username,String groupCode);
    }
}
