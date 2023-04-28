package com.eacunap.firebaseapp.BottomSheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eacunap.firebaseapp.activities.CodeEmailActivity;
import com.eacunap.firebaseapp.activities.CompleteInfoUserActivity;
import com.eacunap.firebaseapp.databinding.CodeSheetBottomBinding;
import com.eacunap.firebaseapp.databinding.OptionPictureBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class OptionPictureBottomSheet extends BottomSheetDialogFragment {

    public static OptionPictureBottomSheet newInstance() {
        OptionPictureBottomSheet bottomSheetOption = new OptionPictureBottomSheet();
        return bottomSheetOption;
    }

    //Binding
    OptionPictureBottomSheetBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = OptionPictureBottomSheetBinding.inflate(inflater, container, false);

        //Handle click to resend code.
        binding.galleryRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery();
            }
        });

        //handle click log out, start Verification code screen
        binding.cameraRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera();
            }
        });

        return binding.getRoot();
    }

    private void gallery() {

        ((CompleteInfoUserActivity)getActivity()).selectPhotoGallery();

    }

    private void camera() {

        ((CompleteInfoUserActivity)getActivity()).takePhoto();

    }

}
