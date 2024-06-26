package com.project.tripmanager.tripMain.Expenses.presentation;

import static com.project.tripmanager.tripMain.Expenses.repo.AddExpensesRepo.EXPENSES;
import static com.project.tripmanager.tripMain.Expenses.repo.ExpenseTypeRepo.EXPENSE_LIST;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.tripmanager.R;
import com.project.tripmanager.databinding.FragmentAddExpensesBinding;
import com.project.tripmanager.tripMain.Expenses.domain.AddExpensesViewModel;
import com.project.tripmanager.tripMain.Expenses.dto.ExpenseModel;
import com.project.tripmanager.tripMain.Expenses.presentation.adapters.AddExpenseAdapter;
import com.project.tripmanager.tripMain.Expenses.presentation.dailogs.AddExpenseDialog;

import java.util.Objects;


public class AddExpensesFragment extends Fragment {

    FragmentAddExpensesBinding binding;
    private NavController navController;
    private AddExpensesViewModel addExpensesViewModel;
    private AddExpenseAdapter adapter;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String email,groupCode, typeId;
    public AddExpensesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddExpensesBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        addExpensesViewModel = new ViewModelProvider(requireActivity()).get(AddExpensesViewModel.class);
        addExpensesViewModel.getPathRef().observe(getViewLifecycleOwner(), expensePathRef -> {
            if(expensePathRef.getGroupCode()!=null &&expensePathRef.getTypeId()!=null)
            {
                groupCode = expensePathRef.getGroupCode();
                typeId = expensePathRef.getTypeId();
                setUpRv();
            }
        });
        binding.fbAdd.setOnClickListener(view1 -> createAddExpenseDialog());

    }

    private void createAddExpenseDialog() {
        AddExpenseDialog dialog = new AddExpenseDialog();
        dialog.show(requireActivity().getSupportFragmentManager(),"Add Expenses!");
    }

    private void setUpRv() {

           if(mAuth.getCurrentUser()!=null)
           {
               email = mAuth.getCurrentUser().getEmail();
               CollectionReference expenseRef = db.collection(groupCode).document(email).collection(EXPENSE_LIST)
                                                .document(typeId).collection(EXPENSES);
               FirestoreRecyclerOptions<ExpenseModel> options = new FirestoreRecyclerOptions.Builder<ExpenseModel>()
                               .setQuery(expenseRef,ExpenseModel.class)
                               .build();
               adapter = new AddExpenseAdapter(options);
               binding.rvExpensesList.setAdapter(adapter);
               binding.rvExpensesList.setLayoutManager(new LinearLayoutManager(requireActivity()));
               binding.rvExpensesList.setHasFixedSize(true);

               Drawable dividerDrawable = ContextCompat.getDrawable(requireActivity(), R.drawable.dashed_divider);
               DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL);
               assert dividerDrawable != null;
               dividerItemDecoration.setDrawable(dividerDrawable);
               binding.rvExpensesList.addItemDecoration(dividerItemDecoration);

               adapter.startListening();
           }




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter!=null)
        {
            adapter.stopListening();
        }
    }
}