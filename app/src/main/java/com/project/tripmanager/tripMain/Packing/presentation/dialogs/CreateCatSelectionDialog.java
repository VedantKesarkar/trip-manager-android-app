package com.project.tripmanager.tripMain.Packing.presentation.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.project.tripmanager.R;

public class CreateCatSelectionDialog extends DialogFragment {

    private CatSelectionDialogInterface listener;
    private static final String TAG = "CreateCatSelectionDialog";
    public static final String UNTITLED = "Untitled";

    public CreateCatSelectionDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view  = inflater.inflate(R.layout.cat_selection_dialog,null);
        TextView edt_title =view.findViewById(R.id.edt_title);
        builder.setView(view)
                .setTitle("Give a title ")
                .setNegativeButton("Cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    if(edt_title.getText().toString().trim().equals(""))
                    {
                        listener.catSelectionTitle(UNTITLED);
                    }
                    else {
                        listener.catSelectionTitle(edt_title.getText().toString());
                    }

                });

        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CatSelectionDialogInterface) context;
        }
        catch (ClassCastException e){
            Log.d(TAG, context +"must implement interface");
        }

    }

    public interface CatSelectionDialogInterface
    {
        void catSelectionTitle(String title);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
