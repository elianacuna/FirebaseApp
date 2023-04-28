package com.eacunap.firebaseapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.eacunap.firebaseapp.R;
import com.eacunap.firebaseapp.adapters.ViewPagerAdapter;
import com.eacunap.firebaseapp.databinding.ActivityHomeBinding;
import com.eacunap.firebaseapp.fragments.FriendsFragment;
import com.eacunap.firebaseapp.fragments.HomeFragment;
import com.eacunap.firebaseapp.fragments.NotificationsFragment;
import com.eacunap.firebaseapp.fragments.SettingsFragment;
import com.eacunap.firebaseapp.fragments.StatusFragment;
import com.eacunap.firebaseapp.fragments.VideoFragment;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    //binding
    private ActivityHomeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "");
        adapter.addFragment(new FriendsFragment(), "");
        adapter.addFragment(new VideoFragment(), "");
        adapter.addFragment(new StatusFragment(), "");
        adapter.addFragment(new NotificationsFragment(), "");
        adapter.addFragment(new SettingsFragment(), "");
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        binding.tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        binding.tabLayout.getTabAt(1).setIcon(R.drawable.ic_friends);
        binding.tabLayout.getTabAt(2).setIcon(R.drawable.ic_video);
        binding.tabLayout.getTabAt(3).setIcon(R.drawable.ic_chat);
        binding.tabLayout.getTabAt(4).setIcon(R.drawable.ic_notification);
        binding.tabLayout.getTabAt(5).setIcon(R.drawable.ic_settings);

    }

}