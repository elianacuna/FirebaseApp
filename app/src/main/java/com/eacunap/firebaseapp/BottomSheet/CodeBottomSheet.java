package com.eacunap.firebaseapp.BottomSheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eacunap.firebaseapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

public class CodeBottomSheet extends BottomSheetDialogFragment {

    public static CodeBottomSheet newInstance() {
        CodeBottomSheet bottomSheetOption = new CodeBottomSheet();
        return bottomSheetOption;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.code_sheet_bottom, container, false);

        return  view;
    }

}
