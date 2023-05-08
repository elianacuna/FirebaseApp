package com.eacunap.firebaseapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.eacunap.firebaseapp.R;
import com.eacunap.firebaseapp.activities.PostsActivity;
import com.eacunap.firebaseapp.databinding.FragmentFriendsBinding;
import com.eacunap.firebaseapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    //binding
    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        //handle addIv click, star  screen
        binding.addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getContext(), binding.addIv);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());

                popup.setForceShowIcon(true);

                //registering popup with onMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.posts:
                                startActivity(new Intent(getContext(), PostsActivity.class));
                                return true;

                            case R.id.story:
                                Toast.makeText(getContext(), "b", Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.reels:
                                Toast.makeText(getContext(), "c", Toast.LENGTH_SHORT).show();
                                return true;

                            default:
                                return false;

                        }

                    }
                });


                popup.show();//showing popup menu
            }
        });

        return binding.getRoot();    }

}