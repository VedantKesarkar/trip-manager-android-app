package com.project.tripmanager.tripMain.ExpenseReports.presentation;

import android.annotation.SuppressLint;
import android.graphics.Color;
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

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.tripmanager.R;
import com.project.tripmanager.databinding.FragmentReportBinding;
import com.project.tripmanager.tripMain.ExpenseReports.domain.BudgetViewModel;
import com.project.tripmanager.tripMain.ExpenseReports.domain.ReportViewModel;
import com.project.tripmanager.tripMain.ExpenseReports.presentation.dailogs.BudgetInfoDialog;

import java.util.ArrayList;
import java.util.List;


public class ReportFragment extends Fragment {

private FragmentReportBinding binding;
private ReportViewModel reportViewModel;
private BudgetViewModel budgetViewModel;
private Integer[] alltimeList;
private Integer[] dailyList;
private String[] members;

private NavController navController;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReportBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
       // binding.pbBudget.setProgress(50);

        binding.txtMyReport.setOnClickListener(view1 -> navController.navigate(R.id.action_ReportFragment_to_myReportFragment));

        budgetViewModel = new ViewModelProvider(requireActivity()).get(BudgetViewModel.class);
        reportViewModel = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);
        reportViewModel.setGroupCode();
        reportViewModel.getTotalTeamExpense().observe(getViewLifecycleOwner(), expense ->
                binding.txtTotal.setText("Total Expense: "+expense));

        reportViewModel.getDailyTeamExpense().observe(getViewLifecycleOwner(), expense ->
                binding.txtDaily.setText("Daily expense: "+expense));

        reportViewModel.getAvgTeamExpense().observe(getViewLifecycleOwner(), expense ->
                binding.txtAvg.setText("Average Expense: "+expense));

        reportViewModel.getAllMembers().observe(getViewLifecycleOwner(), res -> members = res);
        reportViewModel.getAllMemberTotalExpenses().observe(getViewLifecycleOwner(), res -> {
            alltimeList = res;
            setUpPieChart();
        });

        reportViewModel.getAllMemberDailyExpenses().observe(getViewLifecycleOwner(), res -> {
            dailyList = res;
            setUpBarChart();
        });

        binding.imgSettings.setOnClickListener(v -> {
            budgetViewModel.setBudgetInfo();
            createBudgetInfoDialog();
        });

    }

    private void createBudgetInfoDialog() {
        BudgetInfoDialog budgetInfoDialog = new BudgetInfoDialog();
        budgetInfoDialog.show(requireActivity().getSupportFragmentManager(),"Budget Info");
    }

    private void setUpBarChart() {
        List<BarEntry> entries= new ArrayList<>();
        for(int i = 0; i < members.length; i ++ )
        {
            entries.add(new BarEntry(i,dailyList[i],members[i]));

        }
        BarDataSet dataSet = new BarDataSet(entries,"Daily expenses");
        dataSet.setFormSize(20f);
        //Value color
        dataSet.setValueTextSize(18f);
        dataSet.setValueTextColor(ContextCompat.getColor(requireActivity(),R.color.black));
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);
        binding.allMemberDailyBarChart.setData(data);
        binding.allMemberDailyBarChart.getDescription().setEnabled(false);
        XAxis xAxis = binding.allMemberDailyBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(members));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(16f);

        binding.allMemberDailyBarChart.getLegend().setEnabled(false);

        binding.allMemberDailyBarChart.setFitBars(true);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(16f);
        binding.allMemberDailyBarChart.animateY(2000);
        binding.allMemberDailyBarChart.invalidate();
    }

    private void setUpPieChart() {

        List<PieEntry> entries= new ArrayList<>();
        for(int i = 0; i < members.length; i ++ )
        {
            entries.add(new PieEntry(alltimeList[i],members[i]));
        }
        PieDataSet dataSet = new PieDataSet(entries,"All time expenses");
        dataSet.setFormSize(20f);
        //Value color
        dataSet.setValueTextSize(20f);
        dataSet.setValueTextColor(ContextCompat.getColor(requireActivity(),R.color.black));
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        binding.allMemberTotalPieChart.setData(data);
        binding.allMemberTotalPieChart.animateY(1000);
        //Label color and size are changed with the widget
        binding.allMemberTotalPieChart.setEntryLabelColor(ContextCompat.getColor(requireActivity(),R.color.black));
        binding.allMemberTotalPieChart.setEntryLabelTextSize(18f);
        binding.allMemberTotalPieChart.getDescription().setEnabled(false);

        binding.allMemberTotalPieChart.getLegend().setEnabled(false);

        //Handle description
//        Description d = new Description();
//        d.setText("Sample data");
//        d.setTextSize(20f);

//        binding.allMemberTotalPieChart.setDescription(d);
        binding.allMemberTotalPieChart.invalidate();
    }
}