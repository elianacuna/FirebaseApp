package com.eacunap.firebaseapp.BottomSheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eacunap.firebaseapp.R;
import com.eacunap.firebaseapp.activities.CodeEmailActivity;
import com.eacunap.firebaseapp.databinding.CodeSheetBottomBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Random;

public class CodeBottomSheet extends BottomSheetDialogFragment {

    public static CodeBottomSheet newInstance() {
        CodeBottomSheet bottomSheetOption = new CodeBottomSheet();
        return bottomSheetOption;
    }

    //Binding
    CodeSheetBottomBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CodeSheetBottomBinding.inflate(inflater, container, false);

        //handle back click, star change email screen
        binding.changeMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmail();
            }
        });

        //handle click log out, start Verification code screen
        binding.logOutMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        return binding.getRoot();
    }

    private void logOut() {

        ((CodeEmailActivity)getActivity()).logOut();

    }

    private void changeEmail() {

        ((CodeEmailActivity)getActivity()).emailChange();

    }

}
