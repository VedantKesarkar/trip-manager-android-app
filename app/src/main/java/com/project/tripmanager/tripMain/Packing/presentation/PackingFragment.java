package com.project.tripmanager.tripMain.Packing.presentation;

import static com.project.tripmanager.Core.METADATA;
import static com.project.tripmanager.tripMain.Packing.repo.CategoriesRepository.PUBLIC_PACKING_LIST;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.tripmanager.R;
import com.project.tripmanager.databinding.FragmentPackingBinding;
import com.project.tripmanager.tripMain.Packing.domain.CategoriesViewModel;
import com.project.tripmanager.tripMain.Packing.domain.GlobalPackingInteractionVM;
import com.project.tripmanager.tripMain.Packing.domain.GlobalPackingViewModel;
import com.project.tripmanager.tripMain.Packing.domain.TagsViewModel;
import com.project.tripmanager.tripMain.Packing.dto.CategorySelected;
import com.project.tripmanager.tripMain.Packing.presentation.adapters.CatListAdapter;

import java.util.Objects;


public class PackingFragment extends Fragment implements CatListAdapter.CatItemClickListener, CatListAdapter.ViewMoreClickListener {


    FragmentPackingBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CatListAdapter adapter;
    private String groupCode = "";
    private NavController navController;
    private TagsViewModel tagsViewModel;
    private GlobalPackingViewModel packingViewModel;
    private GlobalPackingInteractionVM interactionVM;
    private DocumentSnapshot documentSnapshot;
    private boolean more = false;

    public PackingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPackingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.main_navHost_fragment);
        packingViewModel = new ViewModelProvider(requireActivity()).get(GlobalPackingViewModel.class);
        CategoriesViewModel catViewModel = new ViewModelProvider(requireActivity()).get(CategoriesViewModel.class);
        catViewModel.getCategoriesSelectedLive().observe(getViewLifecycleOwner(), categories ->
                        Toast.makeText(requireActivity(), "Selected: " + categories, Toast.LENGTH_SHORT).show());

        binding.fbCreate.setOnClickListener(view1 -> navController.navigate(R.id.action_packingFragment_to_categoriesFragment2));
            catViewModel.getGroupCode().observe(getViewLifecycleOwner(), s -> {
            groupCode = s;
            setUpRV();

        });

        packingViewModel.isGlobalListAvail().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean avail) {
                if(avail && documentSnapshot!=null)
                {
                    interactionVM = new ViewModelProvider(requireActivity()).get(GlobalPackingInteractionVM.class);
                    interactionVM.setCatList(Objects.requireNonNull(documentSnapshot.toObject(CategorySelected.class)).getCategories());
                    interactionVM.setCatComboId(documentSnapshot.getId());

                    // navigation to the tags fragment
                    Bundle bundleNav = new Bundle();
                    bundleNav.putBoolean(IS_GLOBAL_NAV,true);
                    navController.navigate(R.id.action_packingFragment_to_tagsFragment,bundleNav);
                }
                else if(more && !avail){
                    Toast.makeText(requireActivity(), "Lists unavailable :(", Toast.LENGTH_SHORT).show();
                    more = false;
                }

            }
        });

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finishAffinity();
                System.exit(0);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter!=null)
            adapter.stopListening();

    }

    void setUpRV() {
        CollectionReference categoryListRef = db.collection(groupCode).document(METADATA).collection(PUBLIC_PACKING_LIST);
        FirestoreRecyclerOptions<CategorySelected> options = new FirestoreRecyclerOptions.Builder<CategorySelected>()
                .setQuery(categoryListRef.orderBy("title"), CategorySelected.class)
                .build();
        adapter = new CatListAdapter(options);
        adapter.setListener(this);
        adapter.setViewMoreListener(this);
        binding.rvCategoriesList.setAdapter(adapter);
        binding.rvCategoriesList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rvCategoriesList.setHasFixedSize(true);
        adapter.startListening();
    }


    @Override
    public void onCatItemClick(DocumentSnapshot documentSnapshot, int pos) {

        packingViewModel.setCatSelected(Objects.requireNonNull(documentSnapshot.toObject(CategorySelected.class)).getCategories());

        tagsViewModel = new ViewModelProvider(requireActivity()).get(TagsViewModel.class);
        tagsViewModel.setSelection(documentSnapshot.toObject(CategorySelected.class));
        tagsViewModel.setCatId(documentSnapshot.getId());

        Bundle bundleNav = new Bundle();
        bundleNav.putBoolean(IS_GLOBAL_NAV,false);
        navController.navigate(R.id.action_packingFragment_to_tagsFragment,bundleNav);
    }

    @Override
    public void onViewGlobalList(DocumentSnapshot documentSnapshot, int pos) {
        //navigate directly to view global lists
        more = true;
        this.documentSnapshot = documentSnapshot;
        packingViewModel.checkGlobalListAvail(Objects.requireNonNull(documentSnapshot.toObject(CategorySelected.class)).getCategories());

    }
}