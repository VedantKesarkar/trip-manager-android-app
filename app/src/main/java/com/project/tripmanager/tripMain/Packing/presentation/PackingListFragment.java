package com.project.tripmanager.tripMain.Packing.presentation;

import static com.project.tripmanager.tripMain.Packing.repo.UserPackingRepo.PRIVATE_PACKING_LIST;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.project.tripmanager.R;
import com.project.tripmanager.databinding.FragmentPackingListBinding;
import com.project.tripmanager.tripMain.Packing.domain.CategoriesViewModel;
import com.project.tripmanager.tripMain.Packing.domain.GlobalPackingViewModel;
import com.project.tripmanager.tripMain.Packing.domain.TagsViewModel;
import com.project.tripmanager.tripMain.Packing.dto.PackingItem;
import com.project.tripmanager.tripMain.Packing.presentation.adapters.PackingListAdapter;
import com.project.tripmanager.tripMain.Packing.presentation.dialogs.PackingItemDialog;
import com.project.tripmanager.tripMain.Packing.presentation.dialogs.PickDocumentDialog;

import java.util.ArrayList;
import java.util.Objects;

public class PackingListFragment extends Fragment implements PackingListAdapter.OnCheckListener {

private FragmentPackingListBinding binding;
private final FirebaseFirestore db = FirebaseFirestore.getInstance();
private static final String TAG = "PackingListFragment";
private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
private  ListenerRegistration listenerRegistration;
private String groupCode="",catId="", tag="",email;

private final ArrayList<String> categorySelected = new ArrayList<>();

private PackingListAdapter adapter;
private TagsViewModel tagsViewModel;
private CategoriesViewModel categoriesViewModel;
private GlobalPackingViewModel globalPackingViewModel;
private boolean isDocTag = false;

    public PackingListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPackingListBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tagsViewModel = new ViewModelProvider(requireActivity()).get(TagsViewModel.class);
        categoriesViewModel = new ViewModelProvider(requireActivity()).get(CategoriesViewModel.class);
        categoriesViewModel.getCategoriesSelectedLive().observe(getViewLifecycleOwner(), arrayList -> {

            categorySelected.clear();
            categorySelected.addAll(arrayList);
            Toast.makeText(requireActivity(), "Observe", Toast.LENGTH_SHORT).show();;
        });
        globalPackingViewModel = new ViewModelProvider(requireActivity()).get(GlobalPackingViewModel.class);


        binding.fbAddItem.setOnClickListener(v-> {
            if(!isDocTag)
                showItemCreationDialog();
            else {
                createSelectFromListDialog();
            }
        });

        tagsViewModel.getTag().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tag = s;
                if(s.equals("Documents"))
                {
                    isDocTag = true;
                }
                else {
                    isDocTag = false;
                }
            }
        });


        tagsViewModel.getItemPathRef().observe(getViewLifecycleOwner(), packingItemReference -> {
            groupCode = packingItemReference.getCode();
            tag = packingItemReference.getTag();
            catId = packingItemReference.getCatId();
            if(!groupCode.equals("") && !tag.equals("") && !catId.equals(""))
            {
                 email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
                if(email!=null)
                {
                    Log.d(TAG, email);
                    tagsViewModel.attachListener(requireActivity(),email,groupCode,catId,tag);
                    setUpRV();
                }

            }
        });

        tagsViewModel.getListener().observe(getViewLifecycleOwner(), l -> listenerRegistration = l);

    }


    private void showItemCreationDialog() {

        if(!tag.equals(""))
        {
            PackingItemDialog creationDialog = new PackingItemDialog();
            creationDialog.show(requireActivity().getSupportFragmentManager(),"Create Item ");
//            globalPackingViewModel.setCatSelected(categorySelected);
           globalPackingViewModel.setTag(tag);
        }


    }

    private void setUpRV() {
        DocumentReference catListRef =  db.collection(groupCode).document(email).collection(PRIVATE_PACKING_LIST).document(catId);
        CollectionReference itemRef = catListRef.collection(tag);
        FirestoreRecyclerOptions<PackingItem> options = new FirestoreRecyclerOptions.Builder<PackingItem>()
                .setQuery(itemRef, PackingItem.class)
                .build();
        adapter = new PackingListAdapter(options);
        adapter.setListener(this);
        binding.rvItemList.setAdapter(adapter);
        binding.rvItemList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rvItemList.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rvItemList.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.gardient_divider)));
        binding.rvItemList.addItemDecoration(dividerItemDecoration);

        adapter.startListening();
    }


    private void createSelectFromListDialog() {
        PickDocumentDialog pickDocumentDialog = new PickDocumentDialog();
        pickDocumentDialog.show(requireActivity().getSupportFragmentManager(),"Select document ");
        globalPackingViewModel.setTag(tag);
    }




    @Override
    public void onItemCheckedChanged(DocumentSnapshot documentSnapshot, int pos, boolean checked) {
        PackingItem item = documentSnapshot.toObject(PackingItem.class);
        assert item != null;
        item.setChecked(checked);
        Toast.makeText(requireActivity(), "Checked: "+checked, Toast.LENGTH_SHORT).show();
        tagsViewModel.updateItem(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter!=null)
            adapter.stopListening();
        if(listenerRegistration!=null)
        {
            listenerRegistration.remove();
            Log.d(TAG, "Listener removed");
        }

    }
}