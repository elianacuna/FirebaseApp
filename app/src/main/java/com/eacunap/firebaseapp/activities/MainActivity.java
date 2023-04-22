package com.eacunap.firebaseapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.eacunap.firebaseapp.BottomSheet.CodeBottomSheet;
import com.eacunap.firebaseapp.R;
import com.eacunap.firebaseapp.databinding.ActivityMainBinding;
import com.eacunap.firebaseapp.databinding.ActivitySplashBinding;

public class MainActivity extends AppCompatActivity {

    //binding
    private ActivityMainBinding binding;

    CodeBottomSheet codeBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logInMb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {



                return false;
            }
        });


        //handle loginBtn click, star login screen
        binding.logInMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        //handle registerBtn click, star register screen
        binding.registerMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
    }
}