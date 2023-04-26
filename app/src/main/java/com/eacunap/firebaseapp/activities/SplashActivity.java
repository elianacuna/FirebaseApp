package com.eacunap.firebaseapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.eacunap.firebaseapp.R;
import com.eacunap.firebaseapp.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    //Binding
    private ActivitySplashBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //load the animation from the XML file
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim);

        //add animation to ImageView and TextView, start animation
        binding.image.startAnimation(anim);
        binding.text.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        },2500);//2500 means 2.5 seconds
    }

    private void checkUser() {
        //get current user, if logged in
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null){
            //user not logged in
            //start main screen
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
        else {
            //user logged in check auth confirmed, same as done in login screen
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("IAUser");
            ref.child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            //get auth confirmed
                            String userType = ""+snapshot.child("confirm_auth").getValue();
                            //check auth confirmed
                            if (userType.equals("no_confirmed")){
                                //open MainActivity
                                startActivity(new Intent(SplashActivity.this, CodeEmailActivity.class));
                                finish();
                            }
                            else if (userType.equals("auth_confirmed")){
                                //open HomeActivity
                                startActivity(new Intent(SplashActivity.this, CompleteInfoUserActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });

        }
    }


}