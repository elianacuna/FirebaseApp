package com.eacunap.firebaseapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.eacunap.firebaseapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    //Binding
    protected ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}