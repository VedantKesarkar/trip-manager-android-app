package com.project.tripmanager.tripMain.Packing.presentation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.card.MaterialCardView;
import com.project.tripmanager.R;
import com.project.tripmanager.databinding.FragmentCategoriesBinding;
import com.project.tripmanager.tripMain.Packing.domain.CategoriesViewModel;
import com.project.tripmanager.tripMain.Packing.dto.CategorySubType;
import com.project.tripmanager.tripMain.Packing.dto.CategoryType;
import com.project.tripmanager.tripMain.Packing.presentation.adapters.TypeAdapter;
import com.project.tripmanager.tripMain.Packing.presentation.dialogs.CreateCatSelectionDialog;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment implements TypeAdapter.OnCatClick {

    FragmentCategoriesBinding binding;
    private final ArrayList<CategoryType> categoryTypes = new ArrayList<>();
    private final ArrayList<CategorySubType> length = new ArrayList<>();
    private final ArrayList<CategorySubType> transport = new ArrayList<>();
    private final ArrayList<CategorySubType> weather = new ArrayList<>();
    private final ArrayList<CategorySubType> activities = new ArrayList<>();
    private CategoriesViewModel catViewModel;


    private final ArrayList<String> catSelected = new ArrayList<>();
    private static final String TAG = "CategoriesFragment";
    private NavController navController;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(inflater,container,false);
        catSelected.clear();
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        length.add(new CategorySubType("Short",R.drawable.img_work));
        length.add(new CategorySubType("Medium",R.drawable.img_backpack));
        length.add(new CategorySubType("Long",R.drawable.img_luggage));

        weather.add(new CategorySubType("Warm",R.drawable.img_warm));
        weather.add(new CategorySubType("Cold",R.drawable.img_cold));
        weather.add(new CategorySubType("Rainy",R.drawable.img_water_drop));

        transport.add(new CategorySubType("Car",R.drawable.img_car));
        transport.add(new CategorySubType("Flight",R.drawable.img_flight));
        transport.add(new CategorySubType("Public",R.drawable.img_public));

        activities.add(new CategorySubType("Casual",R.drawable.img_walk));
        activities.add(new CategorySubType("Sightseeing",R.drawable.img_photo));
        activities.add(new CategorySubType("Outdoor",R.drawable.img_hiking));

        categoryTypes.add(new CategoryType("Length of trip", length));
        categoryTypes.add(new CategoryType("Mode of transport", transport));
        categoryTypes.add(new CategoryType("Weather there", weather));
        categoryTypes.add(new CategoryType("Activities", activities));

        TypeAdapter typeAdapter = new TypeAdapter(categoryTypes, requireActivity(), this);
        binding.rvParent.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rvParent.setAdapter(typeAdapter);
        typeAdapter.notifyDataSetChanged();

        catViewModel = new ViewModelProvider(requireActivity()).get(CategoriesViewModel.class);


        binding.fbCreateCatList.setOnClickListener(v->{
            //navigation
            if(catSelected.size()<3)
            {
                Toast.makeText(requireActivity(), "Select at least 3 ", Toast.LENGTH_SHORT).show();

            }
            else
            {
                CreateCatSelectionDialog creationDialog = new CreateCatSelectionDialog();
                creationDialog.show(requireActivity().getSupportFragmentManager(),"Create Category Selection");
                catViewModel.setCategoriesSelectedLive(catSelected);
                Log.d(TAG, "CatSelected: "+catSelected);

            }


        });
    }

    @Override
    public void onCatSelected(int tPos, int sPos,View view) {

            String type = categoryTypes.get(tPos).getCategoryList().get(sPos).getDescp();
            boolean selected = !categoryTypes.get(tPos).getCategoryList().get(sPos).isSelected();

            if(selected && catSelected.size()<=5)
            {
                //Toast.makeText(requireActivity(), type + " selected!", Toast.LENGTH_SHORT).show();
                MaterialCardView cv = (MaterialCardView) view;
                categoryTypes.get(tPos).getCategoryList().get(sPos).setSelected(true);
                cv.getBackground().setTint(getResources().getColor(R.color.dark_pink,requireActivity().getTheme()));
                catSelected.add(type);
            }
            else if(selected)
            {
                Toast.makeText(requireActivity(), "Select any 6", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //Toast.makeText(requireActivity(), type + " removed!", Toast.LENGTH_SHORT).show();
                categoryTypes.get(tPos).getCategoryList().get(sPos).setSelected(false);
                MaterialCardView cv = (MaterialCardView) view;
                cv.getBackground().setTint(getResources().getColor(R.color.pink,requireActivity().getTheme()));
                catSelected.remove(type);
            }



    }


}