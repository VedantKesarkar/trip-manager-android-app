package com.project.tripmanager.tripMain.Packing.presentation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.ListenerRegistration;
import com.project.tripmanager.R;
import com.project.tripmanager.databinding.FragmentGlobalListBinding;
import com.project.tripmanager.tripMain.Packing.domain.CategoriesViewModel;
import com.project.tripmanager.tripMain.Packing.domain.GlobalPackingInteractionVM;
import com.project.tripmanager.tripMain.Packing.dto.GlobalListItem;
import com.project.tripmanager.tripMain.Packing.presentation.adapters.GlobalListAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;


public class GlobalListFragment extends Fragment implements GlobalListAdapter.GlobalListItemClickListener {

  private FragmentGlobalListBinding binding;
  private ListenerRegistration listener;
  private GlobalPackingInteractionVM interactionVM;
  private CategoriesViewModel categoriesViewModel;

  private final ArrayList<View> viewList = new ArrayList<>();
    private NavController navController;
  private GlobalListAdapter adapter;
  private String  roomCode, catComboId;
  private final ArrayList<GlobalListItem> mGlobalListItems = new ArrayList<>();
  private final ArrayList<GlobalListItem> selectedListItems = new ArrayList<>();

  private boolean additionOfItems = false;

  private static final String TAG = "GlobalListFragment";


    public GlobalListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      binding = FragmentGlobalListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = Navigation.findNavController(requireActivity(), R.id.main_navHost_fragment);

    categoriesViewModel = new ViewModelProvider(requireActivity()).get(CategoriesViewModel.class);

    interactionVM = new ViewModelProvider(requireActivity()).get(GlobalPackingInteractionVM.class);
    interactionVM.findGlobalList();
    interactionVM.setContext(requireActivity());
    //interactionVM.findGlobalList();
    adapter = new GlobalListAdapter();
    adapter.setListener(this);
    binding.rvGlobal.setAdapter(adapter);
    binding.rvGlobal.setHasFixedSize(true);
    binding.rvGlobal.setLayoutManager(new LinearLayoutManager(requireActivity()));

    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rvGlobal.getContext(),
            DividerItemDecoration.VERTICAL);
    dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.gardient_divider)));
    binding.rvGlobal.addItemDecoration(dividerItemDecoration);

    interactionVM.getListener().observe(getViewLifecycleOwner(),
            listenerRegistration -> listener = listenerRegistration);

    interactionVM.getGlobalList()
            .observe(getViewLifecycleOwner(), globalListItems -> {
              if(globalListItems.size()!=0)
              {
                Log.d(TAG,"Count: "+ globalListItems.get(0).getCount());
                globalListItems.sort(Comparator.comparingLong(GlobalListItem::getCount).reversed());
                mGlobalListItems.clear();
                mGlobalListItems.addAll(globalListItems);
                adapter.submitList(globalListItems);
                adapter.notifyDataSetChanged();

              }

            });

    categoriesViewModel.getGroupCode().observe(getViewLifecycleOwner(), s -> roomCode = s);

    binding.fbAddToList.setOnClickListener(view1 -> {

      if(categoriesViewModel.getCatComboId()!=null)
      {
        catComboId = categoriesViewModel.getCatComboId();
      }
      else {
        catComboId = interactionVM.getCatComboId();
      }

       binding.fbAddToList.setEnabled(false);
       resetRecyclerView();
       //add selected items to list
       addSelectedToPackingList();
    });

    interactionVM.getAdditionOfItemsResult().observe(getViewLifecycleOwner(), result -> {
      binding.fbAddToList.setEnabled(true);
      if(result && additionOfItems) {
        Toast.makeText(requireActivity(), "Items Added to your list!", Toast.LENGTH_LONG).show();
        additionOfItems = false;
        selectedListItems.clear();
      }
      else if(!result && additionOfItems) Toast.makeText(requireActivity(), "An error occurred :(", Toast.LENGTH_SHORT).show();
    });
  }

  private void resetRecyclerView() {
    for (int i = 0; i < viewList.size(); i++) {
      viewList.get(i).setBackgroundColor(getResources().getColor(R.color.white,requireActivity().getTheme()));
    }
  }

  private void addSelectedToPackingList() {
    Log.d(TAG, "CatcomboId: "+catComboId + " roomCode: "+roomCode + "size "+selectedListItems.size());
      if(catComboId!=null && roomCode!=null  && selectedListItems.size()!=0)
      {
        interactionVM.addSelectedItemsToPackingList(roomCode,catComboId,selectedListItems);
        additionOfItems = true;

      }
      else if(selectedListItems.size()==0) Toast.makeText(requireActivity(), "Select items to add", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if(listener!=null)
    {
      listener.remove();
    }
  }

  @Override
  public void onGlobalListItemClick(int pos, View view) {
    Log.d(TAG, "Clicked : "+ pos);
    if(mGlobalListItems.size()!=0)
    {
      if(!selectedListItems.contains(mGlobalListItems.get(pos)))
      {
        view.setBackgroundColor(getResources().getColor(R.color.light_blue,requireActivity().getTheme()));
        Log.d(TAG, "Added: "+mGlobalListItems.get(pos).getItemName());
        selectedListItems.add(mGlobalListItems.get(pos));
        viewList.add(view);
      }
      else
      {
        view.setBackgroundColor(getResources().getColor(R.color.white,requireActivity().getTheme()));
        Log.d(TAG, "Removed: "+mGlobalListItems.get(pos).getItemName());
        selectedListItems.remove(mGlobalListItems.get(pos));
        viewList.remove(view);
      }
    }
  }
}