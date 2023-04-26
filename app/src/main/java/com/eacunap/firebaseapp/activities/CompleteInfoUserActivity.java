package com.eacunap.firebaseapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.eacunap.firebaseapp.BottomSheet.CodeBottomSheet;
import com.eacunap.firebaseapp.BottomSheet.OptionPictureBottomSheet;
import com.eacunap.firebaseapp.R;
import com.eacunap.firebaseapp.databinding.ActivityCodeEmailBinding;
import com.eacunap.firebaseapp.databinding.ActivityCompleteInfoUserBinding;

public class CompleteInfoUserActivity extends AppCompatActivity {

    //binding
    private ActivityCompleteInfoUserBinding binding;

    //BottomSheet
    OptionPictureBottomSheet optionPictureBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompleteInfoUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addPictureMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                optionPictureBottomSheet = OptionPictureBottomSheet.newInstance();
                optionPictureBottomSheet.show(getSupportFragmentManager(), optionPictureBottomSheet.getTag());
            }
        });

    }
}