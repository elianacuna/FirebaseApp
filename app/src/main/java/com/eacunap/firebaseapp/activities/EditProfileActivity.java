package com.eacunap.firebaseapp.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eacunap.firebaseapp.BottomSheet.ChangeProfilePictureBottomSheet;
import com.eacunap.firebaseapp.BottomSheet.OptionPictureBottomSheet;
import com.eacunap.firebaseapp.R;
import com.eacunap.firebaseapp.databinding.ActivityEditProfileBinding;
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

public class EditProfileActivity extends AppCompatActivity {

    //Binding
    private ActivityEditProfileBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //Variable for change info the user
    String username = "", imageProfile = "", name = "", coverProfile = "";

    //Variable for change info the user
    String usernameChange = "", nameChange = "";

    //Select photo
    private Uri profileUri = null;
    private Uri coverUri = null;

    //BottomSheet
    ChangeProfilePictureBottomSheet changeProfilePictureBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        changeOptionsUser();

        //load info user
        loadInfoUser();
    }

    private void changeOptionsUser() {

        //edit image profile
        binding.editMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileUri != null) {
                    saveProfile();
                } else {
                    changeProfilePictureBottomSheet = ChangeProfilePictureBottomSheet.newInstance();
                    changeProfilePictureBottomSheet.show(getSupportFragmentManager(), changeProfilePictureBottomSheet.getTag());
                    verify_permission_camara();
                }
            }
        });

        //edit image cover
        binding.edit2Mb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coverUri != null) {
                    saveProfileCover();
                } else {
                    selectPhotoGalleryCover();
                    verify_permission_camara();
                }
            }
        });

        //edit name of the user
        binding.edit3Mb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setup data to update in db
                nameChange = binding.nameEt.getText().toString();
                usernameChange = binding.usernameEt.getText().toString();

                if (!nameChange.isEmpty()){
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("name", ""+ nameChange);
                    hashMap.put("username", ""+ usernameChange);
                    //update data to db
                    DatabaseReference changeNameReference = FirebaseDatabase.getInstance().getReference("Users");
                    changeNameReference.child(firebaseAuth.getUid())
                            .updateChildren(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Profile updated...", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull  Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed to update db due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });
    }


    private void loadInfoUser() {

        DatabaseReference loadInfoUserReference = FirebaseDatabase.getInstance().getReference("Users");
        loadInfoUserReference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get all info of user here from snapshot
                        imageProfile = ""+snapshot.child("profileImage").getValue();
                        coverProfile = ""+snapshot.child("cover").getValue();
                        name = ""+snapshot.child("name").getValue();
                        username = ""+snapshot.child("username").getValue();

                        //set data to ui
                        binding.nameEt.setText(name);
                        binding.usernameEt.setText(username);
                        try {
                            Glide.with(getApplicationContext())
                                    .load(imageProfile)
                                    .placeholder(R.drawable.ic_person)
                                    .into(binding.profileIv);
                        }
                        catch (Exception e){
                            binding.profileIv.setImageResource(R.drawable.ic_person);
                        }

                        try {
                            Glide.with(getApplicationContext())
                                    .load(coverProfile)
                                    .placeholder(R.drawable.ic_person)
                                    .into(binding.coverIv);
                        }
                        catch (Exception e){
                            binding.coverIv.setImageResource(R.drawable.ic_person);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //edit image profile
    private void saveProfile() {
        //Image path and name, use uid to replace previous
        String filePathProfile = "ProfileImages/"+firebaseAuth.getUid();

        //storage reference
        StorageReference referenceStorage = FirebaseStorage.getInstance().getReference(filePathProfile);
        referenceStorage.putFile(profileUri)
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
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateImageDatabase(String uploadedImageUri){
        //setup data to add in db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("profileImage", uploadedImageUri);

        DatabaseReference changeProfileUser = FirebaseDatabase.getInstance().getReference("Users");
        changeProfileUser.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        HashMap<String, Object> hashMap2 = new HashMap<>();
                        hashMap2.put("image2", uploadedImageUri);

                        DatabaseReference changeCoverUser = FirebaseDatabase.getInstance().getReference("UploadsPhotos");
                        changeCoverUser.child(firebaseAuth.getUid())
                                .updateChildren(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Profile updated...", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //category add failed
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    //Gallery and Take Photo for profileImage
    public void takePhoto() {
        //intent to take photo
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Pick");//image title
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Image Description");
        profileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, profileUri);
        camaraActivityResultLauncher.launch(intent);
        changeProfilePictureBottomSheet.dismiss();
    }

    public void selectPhotoGallery() {
        //intent to pick from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
        changeProfilePictureBottomSheet.dismiss();
    }

    private void verify_permission_camara(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
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
                        binding.profileIv.setImageURI(profileUri);
                        binding.editMb.setText("Save");
                    }else {
                        Toast.makeText(getApplicationContext(), R.string.cancelled, Toast.LENGTH_SHORT).show();
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
                        profileUri = data.getData();
                        binding.editMb.setText("Save");
                        binding.profileIv.setImageURI(profileUri);
                    }else {
                        Toast.makeText(getApplicationContext(), R.string.cancelled , Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    //Gallery and Take Photo for profileCover
    public void selectPhotoGalleryCover() {
        //intent to pick from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncherCover.launch(intent);
    }

    private void saveProfileCover() {
        //Image path and name, use uid to replace previous
        String filePathCover = "ProfileImages/"+firebaseAuth.getUid();

        //storage reference
        StorageReference referenceStorage = FirebaseStorage.getInstance().getReference(filePathCover);
        referenceStorage.putFile(coverUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isSuccessful());
                        String uploadedImageUri = ""+uriTask.getResult();
                        updateImageDatabaseCover(uploadedImageUri);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateImageDatabaseCover(String uploadedImageUri){
        //setup data to add in db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cover", uploadedImageUri);

        DatabaseReference changeProfileUser = FirebaseDatabase.getInstance().getReference("Users");
        changeProfileUser.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        HashMap<String, Object> hashMap2 = new HashMap<>();
                        hashMap2.put("image3", uploadedImageUri);

                        DatabaseReference changeCoverUser = FirebaseDatabase.getInstance().getReference("UploadsPhotos");
                        changeCoverUser.child(firebaseAuth.getUid())
                                .updateChildren(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Profile updated...", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //category add failed
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private ActivityResultLauncher<Intent> galleryActivityResultLauncherCover = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //used to handle result of gallery inter
                    //get uri of image
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        coverUri = data.getData();
                        binding.edit2Mb.setText("Save");
                        binding.coverIv.setImageURI(coverUri);
                    }else {
                        Toast.makeText(getApplicationContext(), R.string.cancelled , Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );



}