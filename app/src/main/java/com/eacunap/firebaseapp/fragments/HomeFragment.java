package com.eacunap.firebaseapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.eacunap.firebaseapp.R;
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

        return binding.getRoot();    }
}