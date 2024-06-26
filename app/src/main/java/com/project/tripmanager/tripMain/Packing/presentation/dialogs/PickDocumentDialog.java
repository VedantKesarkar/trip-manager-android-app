package com.project.tripmanager.tripMain.Packing.presentation.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.project.tripmanager.R;

import java.util.ArrayList;

public class PickDocumentDialog extends DialogFragment {

    private DocSelectListener listener;
    private String selectedDoc="none";
    private View v;
    private static final String TAG = "PickDocumentDialog";

    public PickDocumentDialog() {
    }

    @NonNull
    @Override
    @SuppressLint("MissingInflatedId")
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view  = inflater.inflate(R.layout.select_doc_dialog,null);

        String[] items = {"Aadhar Card", "PAN card", "Driver's Licence","Passport","Tickets","Id card","Vaccination certificate"};
        ArrayAdapter<String> adapter = new ArrayAdapter<> (requireActivity(), android.R.layout.simple_list_item_1, items);
        ListView docListView =view.findViewById(R.id.doc_list);
        docListView.setAdapter(adapter);
        docListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(selectedDoc.equals(items[i]))
                {

                    view.setBackgroundColor(getResources().getColor(R.color.off_white,requireActivity().getTheme()));
                    selectedDoc = "none";
                }
                else {
                    if(v!=null)
                        v.setBackgroundColor(getResources().getColor(R.color.off_white,requireActivity().getTheme()));
                    selectedDoc = items[i];
                    v = view;
                    view.setBackgroundColor(getResources().getColor(R.color.light_blue,requireActivity().getTheme()));
                }




            }
        });

        builder.setView(view)
                .setTitle("Select a document ")
                .setPositiveButton("Ok",((dialogInterface, i) -> {
                    listener.onDocSelected(selectedDoc);
                }))
                .setNegativeButton("Cancel", (dialogInterface, i) -> {

                });

        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DocSelectListener) context;
        }
        catch (ClassCastException e){
            Log.d(TAG, context +"must implement interface");
        }

    }

    public interface DocSelectListener
    {
        void onDocSelected(String docName);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
