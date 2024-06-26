package com.project.tripmanager.tripMain.Info;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.google.firebase.auth.FirebaseAuth;
import com.project.tripmanager.auth.presentation.AuthActivity;
import com.project.tripmanager.databinding.FragmentInfoBinding;
import com.project.tripmanager.newTrip.presentation.StartNewTrip;
import com.project.tripmanager.tripMain.Info.domain.DetailsViewModel;
import com.project.tripmanager.tripMain.Info.domain.UserLeftInterface;


public class InfoFragment extends Fragment implements UserLeftInterface {

    private FragmentInfoBinding binding;
    private NavController navController;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInfoBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnLogout.setOnClickListener(v->{
            mAuth.signOut();
            startActivity(new Intent(requireActivity(), AuthActivity.class));
        });
        DetailsViewModel viewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        if(mAuth.getCurrentUser()!=null)
        {
            viewModel.loadInfo(mAuth.getCurrentUser().getEmail());
        }

        viewModel.getInfo().observe(getViewLifecycleOwner(), tripDetails -> {
            String tripNameAndCode = tripDetails.getTripName()+" | Room Code: "+tripDetails.getCode();
            binding.tripName.setText(tripNameAndCode);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, tripDetails.getMembers());
            binding.listView.setAdapter(adapter);
        });
        binding.btnLeave.setOnClickListener(v->{
            viewModel.deleteMember(this);

        });
    }

    @Override
    public void onLeave() {
        requireActivity().startActivity(new Intent(requireActivity(), StartNewTrip.class));
        requireActivity().finish();
    }
}