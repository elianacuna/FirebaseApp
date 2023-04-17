package com.eacunap.firebaseapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.eacunap.firebaseapp.R;
import com.eacunap.firebaseapp.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    //Binding
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //carga la animacion desde el archivo XML
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        //añade la animacion al ImageView y TextView, comienza la animacion
        binding.image.startAnimation(anim);
        binding.text.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        },2500);


    }
}