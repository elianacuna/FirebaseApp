package com.eacunap.firebaseapp.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eacunap.firebaseapp.BottomSheet.CodeBottomSheet;
import com.eacunap.firebaseapp.BottomSheet.OptionPictureBottomSheet;
import com.eacunap.firebaseapp.R;
import com.eacunap.firebaseapp.databinding.ActivityCodeEmailBinding;
import com.eacunap.firebaseapp.databinding.ActivityCompleteInfoUserBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.HashMap;
import java.util.List;

public class CompleteInfoUserActivity extends AppCompatActivity {

    //binding
    private ActivityCompleteInfoUserBinding binding;

    //BottomSheet
    OptionPictureBottomSheet optionPictureBottomSheet;

    //Select photo
    private Uri imageUri = null;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    String username = "", imageProfile = "", name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompleteInfoUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //load info user
        loadInfoUser();


        //skip
        binding.skipMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.selectProfileRl.setVisibility(View.GONE);
                binding.usernameRl.setVisibility(View.GONE);
            }
        });
        binding.skip2Mb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.usernameRl.setVisibility(View.GONE);
                binding.welcomeRl.setVisibility(View.VISIBLE);
                homeActivity();
            }
        });

        //Handle username tn, check if this field is not empty.
        binding.usernameMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = binding.usernameEt.getText().toString().trim();
                if (!username.isEmpty()){

                    updateUsernameAndSearchUsername(username);
                }
                else{
                    Toast.makeText(CompleteInfoUserActivity.this, R.string.error_username, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //check if you have permissions to access gallery or camera and if ProfileIv has a selected image
        binding.pictureMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    saveProfile();
                } else {
                    optionPictureBottomSheet = OptionPictureBottomSheet.newInstance();
                    optionPictureBottomSheet.show(getSupportFragmentManager(), optionPictureBottomSheet.getTag());
                    verify_permission_camara();
                }
            }
        });
        binding.doneMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    saveProfile();
                } else {
                    optionPictureBottomSheet = OptionPictureBottomSheet.newInstance();
                    optionPictureBottomSheet.show(getSupportFragmentManager(), optionPictureBottomSheet.getTag());
                    verify_permission_camara();
                }
            }
        });

        //select photo from gallery or take photo
        binding.changePictureMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionPictureBottomSheet = OptionPictureBottomSheet.newInstance();
                optionPictureBottomSheet.show(getSupportFragmentManager(), optionPictureBottomSheet.getTag());
                verify_permission_camara();
            }
        });
    }

    private void homeActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        },5500);//5500 means 5.5 seconds

    }

    private void updateUsernameAndSearchUsername(String username) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //data will be available on dataSnapshot.getValue();
                        if(dataSnapshot.exists()) {
                            //username already exist
                            binding.errorUsername.setText("The username " + username + " is not available");
                            binding.errorUsername.setVisibility(View.VISIBLE);
                        } else {

                            //username no exist
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("username", username);

                            DatabaseReference reference_create_user = FirebaseDatabase.getInstance().getReference("Users");
                            reference_create_user.child(firebaseAuth.getUid()).updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            binding.welcomeRl.setVisibility(View.VISIBLE);
                                            binding.usernameRl.setVisibility(View.GONE);
                                            homeActivity();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CompleteInfoUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }


    //here we are updating and uploading to the DB the profile image of the user
    private void saveProfile() {
        //Image path and name, use uid to replace previous
        String filePath = "ProfileImages/"+name+"/"+firebaseAuth.getUid();

        //storage reference
        StorageReference referenceStorage = FirebaseStorage.getInstance().getReference(filePath);
        referenceStorage.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isSuccessful());
                        String uploadedImageUri = ""+uriTask.getResult();
                        updateImageDatabase(uploadedImageUri);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CompleteInfoUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateImageDatabase(String uploadedImageUri){
        //setup data to add in db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("profileImage", uploadedImageUri);

        DatabaseReference reference_create_user = FirebaseDatabase.getInstance().getReference("Users");
        reference_create_user.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        HashMap<String, Object> hashMap2 = new HashMap<>();
                        hashMap2.put("image1", uploadedImageUri);
                        DatabaseReference reference_update_uploads = FirebaseDatabase.getInstance().getReference("UploadsPhotos");
                        reference_update_uploads.child(firebaseAuth.getUid())
                                .setValue(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        binding.usernameRl.setVisibility(View.VISIBLE);
                                        binding.selectProfileRl.setVisibility(View.GONE);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //category add failed
                                        Toast.makeText(CompleteInfoUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CompleteInfoUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    //Gallery and Take Photo
    public void takePhoto() {
        //intent to take photo
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Pick");//image title
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Image Description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        camaraActivityResultLauncher.launch(intent);
        optionPictureBottomSheet.dismiss();
    }

    public void selectPhotoGallery() {
        //intent to pick from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
        optionPictureBottomSheet.dismiss();
    }

    private void verify_permission_camara(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(CompleteInfoUserActivity.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
    private ActivityResultLauncher<Intent> camaraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //used to handle result of camera inter
                    //get uri of image
                    if (result.getResultCode() == Activity.RESULT_OK){
                        binding.profileIv.setImageURI(imageUri);
                        binding.relative.setVisibility(View.GONE);
                        binding.relative2.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(CompleteInfoUserActivity.this, R.string.cancelled, Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //used to handle result of gallery inter
                    //get uri of image
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imageUri = data.getData();
                        binding.profileIv.setImageURI(imageUri);
                        binding.relative.setVisibility(View.GONE);
                        binding.relative2.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(CompleteInfoUserActivity.this, R.string.cancelled , Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void loadInfoUser() {

        DatabaseReference loadInfoUserReference = FirebaseDatabase.getInstance().getReference("Users");
        loadInfoUserReference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get all info of user here from snapshot
                        imageProfile = ""+snapshot.child("profileImage").getValue();
                        name = ""+snapshot.child("name").getValue();

                        //set data to ui
                        binding.description1.setText(getApplicationContext().getString(R.string.welcome_1) + "\t\t" + name);

                        try {
                            Glide.with(getApplicationContext())
                                    .load(imageProfile)
                                    .placeholder(R.drawable.ic_person)
                                    .into(binding.profileCi);
                        }
                        catch (Exception e){
                            binding.profileCi.setImageResource(R.drawable.ic_person);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CompleteInfoUserActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}