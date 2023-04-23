package com.eacunap.firebaseapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.eacunap.firebaseapp.BottomSheet.CodeBottomSheet;
import com.eacunap.firebaseapp.databinding.ActivityCodeEmailBinding;

public class CodeEmailActivity extends AppCompatActivity {

    //binding
    private ActivityCodeEmailBinding binding;

    //BottomSheet
    CodeBottomSheet codeBottomSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCodeEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.notCodeMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                codeBottomSheet = CodeBottomSheet.newInstance();
                codeBottomSheet.show(getSupportFragmentManager(), codeBottomSheet.getTag());
            }
        });

    }
}