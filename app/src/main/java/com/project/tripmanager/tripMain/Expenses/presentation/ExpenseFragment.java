package com.project.tripmanager.tripMain.Expenses.presentation;

import static com.project.tripmanager.tripMain.Expenses.repo.ExpenseTypeRepo.EXPENSE_LIST;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.tripmanager.R;
import com.project.tripmanager.databinding.FragmentExpenseBinding;
import com.project.tripmanager.tripMain.Expenses.domain.AddExpensesViewModel;
import com.project.tripmanager.tripMain.Expenses.domain.ExpenseTypeViewModel;
import com.project.tripmanager.tripMain.Expenses.dto.ExpenseTypeModel;
import com.project.tripmanager.tripMain.Expenses.presentation.adapters.ExpenseTypeAdapter;
import com.project.tripmanager.tripMain.Expenses.presentation.dailogs.ExpenseTypeCreationDialog;

import java.util.Objects;


public class ExpenseFragment extends Fragment implements ExpenseTypeAdapter.ExpenseTypeClickListener {

FragmentExpenseBinding binding;
private NavController navController;
private ExpenseTypeViewModel expenseTypeViewModel;
private AddExpensesViewModel addExpensesViewModel;
private ExpenseTypeAdapter adapter;
private final FirebaseFirestore db = FirebaseFirestore.getInstance();
private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
private String email,groupCode;
    private static final String TAG = "ExpenseFragment";

    public ExpenseFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExpenseBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        if(groupCode!=null)setUpRv();
        addExpensesViewModel = new ViewModelProvider(requireActivity()).get(AddExpensesViewModel.class);
        expenseTypeViewModel = new ViewModelProvider(requireActivity()).get(ExpenseTypeViewModel.class);
        expenseTypeViewModel.setGroupCode();
        expenseTypeViewModel.getGroupCode().observe(getViewLifecycleOwner(), s -> {
            if(groupCode==null)
            {
                groupCode = s;
                Log.d(TAG, "change bro");
                setUpRv();
            }

        });
        binding.fbCreate.setOnClickListener(view1 -> createTypeDialog());

    }

    private void setUpRv() {
        if(mAuth.getCurrentUser()!=null && groupCode!=null)
        {
            email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            assert email != null;
            CollectionReference expenseTypesRef  = db.collection(groupCode).document(email).collection(EXPENSE_LIST);
            FirestoreRecyclerOptions<ExpenseTypeModel> options = new FirestoreRecyclerOptions.Builder<ExpenseTypeModel>()
                    .setQuery(expenseTypesRef,ExpenseTypeModel.class)
                    .build();
            adapter = new ExpenseTypeAdapter(options);
            adapter.setListener(this);
            binding.rvExpenseTypes.setAdapter(adapter);
            binding.rvExpenseTypes.setLayoutManager(new LinearLayoutManager(requireActivity()));
            binding.rvExpenseTypes.setHasFixedSize(true);
            adapter.startListening();
        }

    }

    private void createTypeDialog() {
        ExpenseTypeCreationDialog dialog = new ExpenseTypeCreationDialog();
        dialog.show(requireActivity().getSupportFragmentManager(),"Create Expense Type" );
    }

    @Override
    public void onExpenseTypeClicked(DocumentSnapshot docSnapshot, int pos) {

        addExpensesViewModel.setTypeId(docSnapshot.getId());
        addExpensesViewModel.setGroupCode(groupCode);
        navController.navigate(R.id.action_expenseFragment_to_addExpensesFragment);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter!=null)
            adapter.stopListening();

    }
}