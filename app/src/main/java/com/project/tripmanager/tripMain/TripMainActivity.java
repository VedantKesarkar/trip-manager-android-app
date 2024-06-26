package com.project.tripmanager.tripMain;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.project.tripmanager.R;
import com.project.tripmanager.databinding.ActivityTripMainBinding;
import com.project.tripmanager.tripMain.Expenses.domain.AddExpensesViewModel;
import com.project.tripmanager.tripMain.Expenses.domain.ExpenseTypeViewModel;
import com.project.tripmanager.tripMain.Expenses.presentation.dailogs.AddExpenseDialog;
import com.project.tripmanager.tripMain.Expenses.presentation.dailogs.ExpenseTypeCreationDialog;
import com.project.tripmanager.tripMain.Packing.domain.CategoriesViewModel;
import com.project.tripmanager.tripMain.Packing.domain.GlobalPackingInteractionVM;
import com.project.tripmanager.tripMain.Packing.domain.GlobalPackingViewModel;
import com.project.tripmanager.tripMain.Packing.domain.TagsViewModel;
import com.project.tripmanager.tripMain.Packing.presentation.dialogs.CreateCatSelectionDialog;
import com.project.tripmanager.tripMain.Packing.presentation.dialogs.PackingItemDialog;
import com.project.tripmanager.tripMain.Packing.presentation.dialogs.PickDocumentDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TripMainActivity extends AppCompatActivity implements CreateCatSelectionDialog.CatSelectionDialogInterface, PackingItemDialog.PackingItemDialogInterface, ExpenseTypeCreationDialog.ExpenseTypeCreationInterface, AddExpenseDialog.ExpenseAddedListener, PickDocumentDialog.DocSelectListener
{

    ActivityTripMainBinding binding;
    private static final String TAG = "TripMainActivity";
    public static final String IS_GLOBAL_NAV = "Is global Nav";
    private NavController navController;
    private CategoriesViewModel catViewModel;
    private TagsViewModel tagsViewModel;
    private GlobalPackingInteractionVM interactionVM;
    private AlertDialog pickAlertDialog;

    private GlobalPackingViewModel globalPackingViewModel;
    private ExpenseTypeViewModel expenseTypeViewModel;
    private AddExpensesViewModel addExpensesViewModel;
    boolean avail = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTripMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_navHost_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController);

        binding.bottomNavigationView.setOnItemSelectedListener(item->
        {
            NavigationUI.onNavDestinationSelected(item,navController);
            return true;

        });
        catViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        tagsViewModel = new ViewModelProvider(this).get(TagsViewModel.class);
        globalPackingViewModel = new ViewModelProvider(this).get(GlobalPackingViewModel.class);
        interactionVM = new ViewModelProvider(this).get(GlobalPackingInteractionVM.class);
        expenseTypeViewModel = new ViewModelProvider(this).get(ExpenseTypeViewModel.class);
        addExpensesViewModel = new ViewModelProvider(this).get(AddExpensesViewModel.class);

        catViewModel.getCategoriesSelectedLive().observe(this, arrayList -> {
            if(arrayList.size()!=0 )
            {
                globalPackingViewModel.checkGlobalListAvail(arrayList);
                interactionVM.setCatList(arrayList);
            }

        });
        globalPackingViewModel.isGlobalListAvail().observe(this, avail -> {
            if(avail)
            {
                Log.d(TAG, "A list is present!");
                createPickFromGlobalListDialog();
            }
        });
    }

    @Override
    public void catSelectionTitle(String title) {
        catViewModel.setTitle(title);
        if(avail)
        {
            pickAlertDialog.show();
            avail = false;
        }
        else
            navController.navigate(R.id.action_categoriesFragment2_to_packingFragment);

    }

    private void createPickFromGlobalListDialog() {
        avail = true;
        AlertDialog.Builder pickDialog = new AlertDialog.Builder(TripMainActivity.this,R.style.AlertDialogTheme);
        pickDialog.setTitle("Look at global List");
        pickDialog.setMessage("Shows lists with items based on selected category combination");
        pickDialog.setPositiveButton("OK", (dialogInterface, i) -> {
            //Navigate to select tag
            Bundle bundleNav = new Bundle();
            bundleNav.putBoolean(IS_GLOBAL_NAV,true);
            navController.navigate(R.id.action_categoriesFragment2_to_tagsFragment,bundleNav);
        });
        pickDialog.setNegativeButton("NO",(dialogInterface, i)->
                navController.navigate(R.id.action_categoriesFragment2_to_packingFragment));
        pickDialog.setCancelable(false);
         pickAlertDialog =pickDialog.create();
        pickAlertDialog.setCanceledOnTouchOutside(false);

    }

    @Override
    public void onItemCreated(String title) {
        tagsViewModel.setItemName(title);
        globalPackingViewModel.setItemName(title);
    }

    @Override
    public void onExpenseTypeCreated(String typeName) {
        String date = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
        expenseTypeViewModel.createExpenseType(typeName,date);
    }

    @Override
    public void onExpenseAdded(String name, long amt) {
        addExpensesViewModel.addExpense(name,amt);
    }

    @Override
    public void onDocSelected(String docName) {
        tagsViewModel.setItemName(docName);
        globalPackingViewModel.setItemName(docName);
    }
}