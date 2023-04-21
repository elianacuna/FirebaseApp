package com.eacunap.firebaseapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.eacunap.firebaseapp.databinding.ActivityCodeEmailBinding;

public class CodeEmailActivity extends AppCompatActivity {

    //binding
    private ActivityCodeEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCodeEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}