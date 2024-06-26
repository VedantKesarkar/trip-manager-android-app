package com.project.tripmanager.tripMain.ExpenseReports.presentation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.tripmanager.R;
import com.project.tripmanager.databinding.FragmentMyReportBinding;
import com.project.tripmanager.tripMain.ExpenseReports.domain.ReportViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyReportFragment extends Fragment {
    private FragmentMyReportBinding binding;
    private ReportViewModel reportViewModel;
    private String[] types;
    private Integer[] allTimeList;
    private Integer[] dailyList;
    private static final String TAG = "MyReportFragment";
    public MyReportFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyReportBinding.inflate(inflater,container,false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reportViewModel = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);
        reportViewModel.getExpenseTypes().observe(getViewLifecycleOwner(), new Observer<String[]>() {
            @Override
            public void onChanged(String[] res) {
                types = res;
                Log.d(TAG, Arrays.toString(res));
            }
        });

        reportViewModel.getMemberDailyTypeExpenses().observe(getViewLifecycleOwner(), new Observer<Integer[]>() {
            @Override
            public void onChanged(Integer[] res) {
                dailyList = res;
                Log.d(TAG, Arrays.toString(res));
                setUpPieChartToday();
            }
        });
        reportViewModel.getMemberTotalTypeExpenses().observe(getViewLifecycleOwner(), new Observer<Integer[]>() {
            @Override
            public void onChanged(Integer[] res) {
                allTimeList = res;
                Log.d(TAG, Arrays.toString(dailyList));
                setUpPieChartTotal();
            }
        });

        reportViewModel.getTotalMemberExpense().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.txtTotal.setText("Total Expenses: "+integer);
            }
        });

        reportViewModel.getDailyMemberExpense().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.txtToday.setText("Expenses today: "+integer);
            }
        });
    }

    private void setUpPieChartToday() {
        List<PieEntry> entries= new ArrayList<>();
        for(int i = 0; i < types.length; i ++ )
        {
            entries.add(new PieEntry(dailyList[i],types[i]));
        }
        PieDataSet dataSet = new PieDataSet(entries,"All time expenses");
        dataSet.setFormSize(20f);
        //Value color
        dataSet.setValueTextSize(20f);
        dataSet.setValueTextColor(ContextCompat.getColor(requireActivity(),R.color.black));
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData data = new PieData(dataSet);
        binding.memberDailyPieChart.setData(data);
        binding.memberDailyPieChart.animateY(1000);
        //Label color and size are changed with the widget
        binding.memberDailyPieChart.setEntryLabelColor(ContextCompat.getColor(requireActivity(),R.color.black));
        binding.memberDailyPieChart.setEntryLabelTextSize(17f);
        binding.memberDailyPieChart.getDescription().setEnabled(false);

        binding.memberDailyPieChart.getLegend().setEnabled(false);

        binding.memberDailyPieChart.invalidate();
    }

    public void setUpPieChartTotal()
    {
        List<PieEntry> entries= new ArrayList<>();
        for(int i = 0; i < types.length; i ++ )
        {
            entries.add(new PieEntry(allTimeList[i],types[i]));
        }
        PieDataSet dataSet = new PieDataSet(entries,"All time expenses");
        dataSet.setFormSize(20f);
        //Value color
        dataSet.setValueTextSize(20f);
        dataSet.setValueTextColor(ContextCompat.getColor(requireActivity(),R.color.black));
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        binding.memberTotalPieChart.setData(data);
        binding.memberTotalPieChart.animateY(1000);
        //Label color and size are changed with the widget
        binding.memberTotalPieChart.setEntryLabelColor(ContextCompat.getColor(requireActivity(),R.color.black));
        binding.memberTotalPieChart.setEntryLabelTextSize(17f);
        binding.memberTotalPieChart.getDescription().setEnabled(false);

        binding.memberTotalPieChart.getLegend().setEnabled(false);

        binding.memberTotalPieChart.invalidate();
    }
}