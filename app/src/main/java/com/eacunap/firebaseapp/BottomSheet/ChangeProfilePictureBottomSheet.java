package com.eacunap.firebaseapp.BottomSheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eacunap.firebaseapp.activities.CompleteInfoUserActivity;
import com.eacunap.firebaseapp.activities.EditProfileActivity;
import com.eacunap.firebaseapp.databinding.OptionPictureBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChangeProfilePictureBottomSheet extends BottomSheetDialogFragment {

    public static ChangeProfilePictureBottomSheet newInstance() {
        ChangeProfilePictureBottomSheet bottomSheetOption = new ChangeProfilePictureBottomSheet();
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

        ((EditProfileActivity)getActivity()).selectPhotoGallery();

    }

    private void camera() {

        ((EditProfileActivity)getActivity()).takePhoto();

    }

}
