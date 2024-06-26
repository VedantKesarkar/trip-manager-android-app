package com.project.tripmanager.newTrip.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.tripmanager.Core;
import com.project.tripmanager.R;
import com.project.tripmanager.auth.presentation.AuthActivity;
import com.project.tripmanager.databinding.ActivityStartNewTripBinding;
import com.project.tripmanager.newTrip.domain.mViewModel;
import com.project.tripmanager.tripMain.TripMainActivity;

import java.util.Objects;

public class StartNewTrip extends AppCompatActivity implements CreationDialog.TripCreationDialogInterface, JoiningDialog.TripCreationDialogInterface, SoloDialog.TripCreationDialogInterface {

    ActivityStartNewTripBinding binding;
    private final Core core = new Core();
    private mViewModel viewModel;
    FirebaseAuth mAuth;
    private boolean solo,creator;




    private int c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartNewTripBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();
        binding.btnCreateTrip.setVisibility(View.INVISIBLE);
        setTitle(getResources().getString(R.string.start_new_trip));
        setSupportActionBar(binding.toolbar);
        binding.screen.setVisibility(View.INVISIBLE);
        binding.lottieLoading.setVisibility(View.VISIBLE);


        viewModel = new ViewModelProvider(this).get(mViewModel.class);
        viewModel.getSuccess().observe(this, res -> {
            if(res)
            {
                Toast.makeText(StartNewTrip.this, "Success !!", Toast.LENGTH_SHORT).show();
                //navigate
                startActivity(new Intent(StartNewTrip.this, TripMainActivity.class));
                finish();
            }
            else {
                Toast.makeText(StartNewTrip.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });




        binding.cardOne.setOnClickListener(v -> {
            binding.cardOne.getBackground().setTint(getResources().getColor(R.color.dark_pink,getTheme()));
            binding.cardTwo.getBackground().setTint(getResources().getColor(R.color.pink,getTheme()));
            if(c==0)
            {
                solo = true;
                binding.btnCreateTrip.setVisibility(View.VISIBLE);
            }
            else
            {
               creator=true;
               binding.btnCreateTrip.setVisibility(View.VISIBLE);
            }

        } );
        binding.cardTwo.setOnClickListener(v->{

            binding.cardOne.getBackground().setTint(getResources().getColor(R.color.pink,getTheme()));
            if(c==0)
            {
                solo = false;
                binding.btnCreateTrip.setVisibility(View.INVISIBLE);
                joinOrCreate();
                c++;
            }
            else
            {
                creator=false;
                binding.cardTwo.getBackground().setTint(getResources().getColor(R.color.dark_pink,getTheme()));
                binding.btnCreateTrip.setVisibility(View.VISIBLE);
            }
        });

        binding.btnCreateTrip.setOnClickListener(v->
        {
            if(solo)
            {
               createSoloDialog();
            }
            else if(creator)
            {
                createGroupDialog();
            }
            else
            {
                joinGroupDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null)
        {
            startActivity(new Intent(StartNewTrip.this, AuthActivity.class));
            finish();
        }
        else  {
           viewModel.setUser(user.getEmail());
        }
        viewModel.getUserStatus().observe(this, in -> {
            if(in)
            {
                startActivity(new Intent(StartNewTrip.this,TripMainActivity.class));
                finish();
            }
            else
            {
                binding.screen.setVisibility(View.VISIBLE);
                binding.btnCreateTrip.setVisibility(View.INVISIBLE);
                binding.lottieLoading.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void createSoloDialog() {
        SoloDialog soloDialog = new SoloDialog();
        soloDialog.show(getSupportFragmentManager(),"Go solo");
    }

    private void joinGroupDialog() {
        JoiningDialog joiningDialog = new JoiningDialog();
        joiningDialog.show(getSupportFragmentManager(),"Join group");
    }

    private void createGroupDialog() {
        CreationDialog creationDialog = new CreationDialog();
        creationDialog.show(getSupportFragmentManager(),"Create group");
    }

    @SuppressLint("SetTextI18n")
    private void joinOrCreate() {
        binding.imgOne.setImageResource(R.drawable.baseline_group_add_24);
        binding.imgTwo.setImageResource(R.drawable.baseline_person_add_24);
        binding.txtOne.setText("Create group");
        binding.txtTwo.setText("Join group");

    }


    @Override
    public void onBackPressed() {

        if(c!=0)
        {
            reset();
        }
        else {
            //Close application
            finishAffinity();
            System.exit(0);
        }

    }

    @SuppressLint("SetTextI18n")
    private void reset() {
        binding.btnCreateTrip.setVisibility(View.INVISIBLE);
        binding.cardOne.getBackground().setTint(getResources().getColor(R.color.pink,getTheme()));
        binding.cardTwo.getBackground().setTint(getResources().getColor(R.color.pink,getTheme()));
        binding.imgOne.setImageResource(R.drawable.baseline_person_24);
        binding.imgTwo.setImageResource(R.drawable.baseline_groups_24);
        binding.txtOne.setText("Go solo");
        binding.txtTwo.setText("Group");
        c = 0;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.log_out,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_logOut)
        {
            createSignOutDialog();
        }
        return super.onOptionsItemSelected(item);
    }



    private void createSignOutDialog() {
        AlertDialog.Builder logOutDialog = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        logOutDialog.setTitle("Log out ?");
        logOutDialog.setMessage("You will be signed out of your account");
        logOutDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
            mAuth.signOut();
            startActivity(new Intent(StartNewTrip.this,AuthActivity.class));
            finish();
        });
        logOutDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
            //do nothing
        });
        logOutDialog.create().show();
    }


    @Override
    public void fieldValues(String username, String tripName,String amt) {
        if(mAuth.getCurrentUser()!=null)
        {
            String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            viewModel.creator(email,username,tripName,amt);

        }

    }

    @Override
    public void fieldVal(String username, String groupCode) {
        if(mAuth.getCurrentUser()!=null)
        {
            String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            viewModel.joiner(email,username,Integer.parseInt(groupCode));

        }
    }

    @Override
    public void soloFieldVals(String tripName) {
        if(mAuth.getCurrentUser()!=null)
        {
            String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();


        }
    }

//    public void saveData()
//    {
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean(STATUS,true);
//        editor.putString(GROUP_CODE,core.getGroupCode());
//        editor.apply();
//
//
//    }
//
//    public boolean loadData()
//    {
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
//        groupCode = sharedPreferences.getString(GROUP_CODE,"");
//        if(!groupCode.equals(""))
//        {
//            core.setGroupCode(groupCode);
//        }
//        status = sharedPreferences.getBoolean(STATUS,false);
//        return status;
//    }
}