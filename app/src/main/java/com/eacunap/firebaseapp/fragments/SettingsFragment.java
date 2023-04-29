package com.eacunap.firebaseapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.eacunap.firebaseapp.R;
import com.eacunap.firebaseapp.activities.CompleteInfoUserActivity;
import com.eacunap.firebaseapp.activities.EditProfileActivity;
import com.eacunap.firebaseapp.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingsFragment extends Fragment {

    //view binding
    FragmentSettingsBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    String username = "", name = "", profile = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //load info user
        loadInfoUser();

        //handle loginBtn click, star edit profile screen
        binding.editRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

        binding.usernameRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "aaa", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }


    private void loadInfoUser() {

        DatabaseReference loadInfoUserReference = FirebaseDatabase.getInstance().getReference("Users");
        loadInfoUserReference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get all info of user here from snapshot
                        profile = ""+snapshot.child("profileImage").getValue();
                        name = ""+snapshot.child("name").getValue();
                        username = ""+snapshot.child("username").getValue();

                        //set data to ui
                        binding.nameTv.setText(name);
                        binding.usernameTv.setText(username);
                        try {
                            Glide.with(getContext())
                                    .load(profile)
                                    .placeholder(R.drawable.ic_person)
                                    .into(binding.profileCi);
                        }
                        catch (Exception e){
                            binding.profileCi.setImageResource(R.drawable.ic_person);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}