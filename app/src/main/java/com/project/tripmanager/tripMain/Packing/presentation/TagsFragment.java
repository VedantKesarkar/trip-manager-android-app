package com.project.tripmanager.tripMain.Packing.presentation;

import static com.project.tripmanager.tripMain.TripMainActivity.IS_GLOBAL_NAV;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.tripmanager.R;
import com.project.tripmanager.databinding.FragmentTagsBinding;
import com.project.tripmanager.tripMain.Packing.domain.GlobalPackingInteractionVM;
import com.project.tripmanager.tripMain.Packing.domain.TagsViewModel;
import com.project.tripmanager.tripMain.Packing.dto.CategorySubType;
import com.project.tripmanager.tripMain.Packing.presentation.adapters.TagAdapter;

import java.util.ArrayList;


public class TagsFragment extends Fragment implements TagAdapter.OnTagClick {

    FragmentTagsBinding binding;
    private TagAdapter adapter;
    private TagsViewModel tagsViewModel;
    private GlobalPackingInteractionVM interactionVM;
    private NavController navController;
    private final ArrayList<CategorySubType> list = new ArrayList<>();
    public static final String TAG = "tag";


    public TagsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTagsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.main_navHost_fragment);
        list.clear();
        list.add(new CategorySubType("Documents", R.drawable.img_docs));
//       list.add(new CategorySubType("Appliances", R.drawable.img_devices));
        list.add(new CategorySubType("Health", R.drawable.img_health));
        list.add(new CategorySubType("Clothes", R.drawable.img_clothes));
        list.add(new CategorySubType("Other", R.drawable.img_other));
        adapter = new TagAdapter(list,requireActivity(),this);
        binding.rvTagsList.setAdapter(adapter);
        binding.rvTagsList.setLayoutManager(new GridLayoutManager(requireActivity(),2));



        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.action_tagsFragment_to_packingFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    @Override
    public void onTagSelected(int pos) {
        tagsViewModel = new ViewModelProvider(requireActivity()).get(TagsViewModel.class);
        interactionVM = new ViewModelProvider(requireActivity()).get(GlobalPackingInteractionVM.class);
        String tag = list.get(pos).getDescp();
        tagsViewModel.setTag(tag);
        interactionVM.setTag(tag);
        assert getArguments() != null;
        if(!getArguments().getBoolean(IS_GLOBAL_NAV))
        {
            navController.navigate(R.id.action_tagsFragment_to_packingListFragment);
        }
        else
        {
            navController.navigate(R.id.action_tagsFragment_to_globalListFragment);
            //Toast.makeText(requireActivity(), "Navigate to global list", Toast.LENGTH_SHORT).show();
        }
    }



}