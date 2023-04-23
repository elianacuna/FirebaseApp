package com.eacunap.firebaseapp.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.eacunap.firebaseapp.R;
import com.eacunap.firebaseapp.databinding.ActivityRegisterBinding;

public class SignUp extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void name(ActivityRegisterBinding binding){
        binding.nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.description1.setText(R.string.description_2);
                binding.description1.setTextColor(Color.GRAY);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public static void email(ActivityRegisterBinding binding) {
        binding.emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.description3.setText(R.string.description_1);
                binding.description3.setTextColor(Color.GRAY);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public static void password_check(String password, ActivityRegisterBinding binding, Context context){
        //at least 6 characters
        if (password.length() >= 8){
            binding.chip1.setTextColor(context.getResources().getColor(R.color.color_text_chip));
            binding.chip1.setChipBackgroundColor(context.getResources().getColorStateList(R.color.color_chip));
        }else {
            binding.chip1.setTextColor(context.getResources().getColor(R.color.error_chip_text_color));
            binding.chip1.setChipBackgroundColor(context.getResources().getColorStateList(R.color.error_chip_color));
        }

        //for number
        if (password.matches("(.*[0-9].*)")){
            binding.chip2.setTextColor(context.getResources().getColor(R.color.color_text_chip));
            binding.chip2.setChipBackgroundColor(context.getResources().getColorStateList(R.color.color_chip));
        }else {
            binding.chip2.setTextColor(context.getResources().getColor(R.color.error_chip_text_color));
            binding.chip2.setChipBackgroundColor(context.getResources().getColorStateList(R.color.error_chip_color));
        }

        //for uppercase
        if (password.matches("(.*[A-Z].*)")){
            binding.chip3.setTextColor(context.getResources().getColor(R.color.color_text_chip));
            binding.chip3.setChipBackgroundColor(context.getResources().getColorStateList(R.color.color_chip));
        }else {
            binding.chip3.setTextColor(context.getResources().getColor(R.color.error_chip_text_color));
            binding.chip3.setChipBackgroundColor(context.getResources().getColorStateList(R.color.error_chip_color));
        }

        //for symbol
        if (password.matches("^(?=.*[_.()&%=+#@]).*$")){
            binding.chip4.setTextColor(context.getResources().getColor(R.color.color_text_chip));
            binding.chip4.setChipBackgroundColor(context.getResources().getColorStateList(R.color.color_chip));
        }else {
            binding.chip4.setChipBackgroundColor(context.getResources().getColorStateList(R.color.error_chip_color));
            binding.chip4.setTextColor(context.getResources().getColor(R.color.error_chip_text_color));
        }

        //for lowercase letter
        if (password.matches("(.*[a-z].*)")){
            binding.chip5.setTextColor(context.getResources().getColor(R.color.color_text_chip));
            binding.chip5.setChipBackgroundColor(context.getResources().getColorStateList(R.color.color_chip));
        }else {
            binding.chip5.setTextColor(context.getResources().getColor(R.color.error_chip_text_color));
            binding.chip5.setChipBackgroundColor(context.getResources().getColorStateList(R.color.error_chip_color));
        }

    }


}

