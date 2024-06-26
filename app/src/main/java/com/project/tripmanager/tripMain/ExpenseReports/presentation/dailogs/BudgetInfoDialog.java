package com.project.tripmanager.tripMain.ExpenseReports.presentation.dailogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textview.MaterialTextView;
import com.project.tripmanager.R;
import com.project.tripmanager.tripMain.ExpenseReports.domain.BudgetViewModel;

public class BudgetInfoDialog extends DialogFragment {

    private BudgetViewModel budgetViewModel;
    private ProgressBar progressBar;
    private TextView txtPercent,txtSpent,txtLeft,txtOriginal;
    private Button btnChange;
    private MaterialTextView txtWarn;
    public static final String AMT_LEFT = "Amount remaining: ";
    public static final String AMT_SPENT = "Amount spent: ";
    public static final String AMT_BUDGET = "Current Budget: ";
    private static final String TAG = "BudgetInfoDialog";
    private boolean admin = false,shown=false;

    private AlertDialog dialog;
    public BudgetInfoDialog() {
    }

    @NonNull
    @Override
    @SuppressLint("MissingInflatedId")
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view  = inflater.inflate(R.layout.budget_info_dialog,null);

          txtWarn = view.findViewById(R.id.txt_warn);
         progressBar = view.findViewById(R.id.pb_budget);
         txtPercent = view.findViewById(R.id.txt_percent);
         txtSpent = view.findViewById(R.id.txt_spent);
         txtLeft = view.findViewById(R.id.txt_remaining);
         txtOriginal = view.findViewById(R.id.txt_original);
         btnChange = view.findViewById(R.id.btn_update);

        builder.setView(view);
        builder.setTitle("Budget Info");
        builder.setPositiveButton("Ok",(dialogInterface, i) -> {
            //do nothing
        });

        dialog = builder.create();
        return dialog;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          return inflater.inflate(R.layout.budget_info_dialog, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        budgetViewModel = new ViewModelProvider(requireActivity()).get(BudgetViewModel.class);
        budgetViewModel.getBudgetInfo().observe(getViewLifecycleOwner(), info -> {
            Log.d(TAG, "observe");
            if(info.isLimit())
            {

                txtWarn.setVisibility(View.VISIBLE);
                txtWarn.setText(info.getWarningMsg());

            }
            progressBar.setProgress(Integer.parseInt(info.getPercent()));
            txtPercent.setText(info.getPercent()+"%\nSpent");
            txtSpent.setText(AMT_SPENT + info.getSpent());
            txtLeft.setText(AMT_LEFT + info.getLeft());
            txtOriginal.setText(AMT_BUDGET + info.getOriginal());
        });
        btnChange.setOnClickListener(v-> {
            budgetViewModel.checkAdmin();
            shown = true;
            admin = true;
        });
        budgetViewModel.getAdmin().observe(getViewLifecycleOwner(), res -> {

            if(res && shown)
            {
                if(dialog!=null && dialog.isShowing())
                {
                    Log.d(TAG, "dismiss");
                    dialog.dismiss();
                }
                BudgetChangeDialog budgetChangeDialog = new BudgetChangeDialog();
                budgetChangeDialog.show(requireActivity().getSupportFragmentManager(),"Change budget");
                shown = false;
            }
            else {
                if(admin)
                {
                    Toast.makeText(requireActivity(), "You are not an admin!", Toast.LENGTH_SHORT).show();
                    admin = false;
                }

            }
        });
    }
}
