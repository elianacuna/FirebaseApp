package com.eacunap.firebaseapp.activities;

import static com.eacunap.firebaseapp.R.string.message_email_1;
import static com.eacunap.firebaseapp.R.string.message_email_2;
import static com.eacunap.firebaseapp.R.string.message_email_3;
import static com.eacunap.firebaseapp.R.string.message_email_4;
import static com.eacunap.firebaseapp.R.string.message_email_5;
import static com.eacunap.firebaseapp.R.string.message_email_6;
import static com.eacunap.firebaseapp.R.string.message_email_7;

import static java.lang.String.format;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.eacunap.firebaseapp.BottomSheet.CodeBottomSheet;
import com.eacunap.firebaseapp.R;
import com.eacunap.firebaseapp.databinding.ActivityCodeEmailBinding;
import com.eacunap.firebaseapp.sendCodeEmail.JavaMailAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

public class CodeEmailActivity extends AppCompatActivity {

    //variables for the mail
    String verification = "";
    String  file_1 = "";
    String  file_2 = "";
    String  file_3 = "";
    String  file_4 = "";
    String  file_5 = "";
    String  file_6 = "";
    String  file_7 = "";
    String email = "", name = "", newEmail = "";

    Context context;

    //binding
    private ActivityCodeEmailBinding binding;

    //BottomSheet
    CodeBottomSheet codeBottomSheet;

    //firebase auth
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    //IA
    String IA = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCodeEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplicationContext();

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //In this part of the code we display the bottom sheet so that the user can choose other options.
        binding.notCodeMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                codeBottomSheet = CodeBottomSheet.newInstance();
                codeBottomSheet.show(getSupportFragmentManager(), codeBottomSheet.getTag());
            }
        });

        //load user information for the emailTxt
        loadInfoUser();

        //Settings of code email
        codeEmail();
        changeEmail();
    }

    //load info user
    private void loadInfoUser() {

        DatabaseReference loadInfoUserReference = FirebaseDatabase.getInstance().getReference("Users");
        loadInfoUserReference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get all info of user here from snapshot
                        email = ""+snapshot.child("auth").getValue();
                        name = ""+snapshot.child("name").getValue();

                        //set data to ui
                        binding.emailTxt.setText(getApplicationContext().getString(R.string.description_code) + email);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    //verification of the code
    public void codeEmail() {
        //load user information bottom changeMb
        binding.codeMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference sendCodeReference = FirebaseDatabase.getInstance().getReference("IAUser");
                sendCodeReference.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //calling the code from the database
                        String codeDb = ""+snapshot.child("code_verification").getValue();
                        String code = binding.codeEt.getText().toString().trim();

                        //checking if the EditText Code field is empty or not
                        if (!code.isEmpty()){

                            //If the field is not empty, check if the code is correct.
                            if (codeDb.equals(code)){

                                //If the codeDb is correct to the entered code, send the following command to the DB
                                //setup data to add in db
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("confirm_auth", "auth_confirmed");
                                hashMap.put("star_type", "email");

                                DatabaseReference updateIAReference = FirebaseDatabase.getInstance().getReference("IAUser");
                                updateIAReference.child(firebaseAuth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        startActivity(new Intent(getApplicationContext(), CompleteInfoUserActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //error update
                                        Toast.makeText(CodeEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                //If the code entered is not the same as the one in the database, send this message
                                Toast.makeText(CodeEmailActivity.this, R.string.code_no_correct, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            //If the EditText field is empty, send this message
                            Toast.makeText(CodeEmailActivity.this, R.string.enter_the_code, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //error checking code
                    }
                });
            }
        });
    }


    //handle back click, star change email screen
    public void emailChange() {
        //configuration of the "I did not receive the code"
        binding.codeRl.setVisibility(View.GONE);
        binding.changeRl.setVisibility(View.VISIBLE);
        codeBottomSheet.dismiss();

        IA = "change";

    }

    private void changeEmail() {
        binding.newEmailMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newEmail = binding.newEmailEt.getText().toString();

                //check if the EditTextNewEmail is not empty
                if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){

                    binding.description2.setTextColor(getResources().getColor(R.color.error));
                    binding.description2.setText(R.string.error_email);

                } else {
                    //get all info of user here from snapshot
                    DatabaseReference changeEmailReference = FirebaseDatabase.getInstance().getReference("Users");
                    changeEmailReference.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String email = ""+snapshot.child("auth").getValue();
                            String password = ""+snapshot.child("password").getValue();

                            //Get auth credentials from the user for re-authentication
                            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task){
                                    //Now change your email address
                                    newEmail = binding.newEmailEt.getText().toString().trim();

                                    //if the field is not empty, update the email address
                                    firebaseUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //Update new email address
                                            if (task.isSuccessful()){
                                                binding.changeRl.setVisibility(View.GONE);
                                                binding.codeRl.setVisibility(View.VISIBLE);

                                                codeResendChangeEmail();


                                                newEmail = binding.newEmailEt.getText().toString();

                                                //set data to DB
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("auth", newEmail);

                                                //setup data to add in db
                                                DatabaseReference updateNewEmailReference = FirebaseDatabase.getInstance().getReference("Users");
                                                updateNewEmailReference.child(firebaseAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        //email address updated successfully
                                                        Toast.makeText(CodeEmailActivity.this, R.string.email_successfully, Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //error update db
                                                        Toast.makeText(CodeEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(CodeEmailActivity.this, "An error occurred, please try again later", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //error updating new email address
                                            Toast.makeText(CodeEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //In case the re-authentication fails
                                    Toast.makeText(CodeEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });

    }

    //handle click log out, start Verification code screen
    public void logOut() {
        //Log out of account
        firebaseAuth.signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }


    //Send code to email address
    private void codeResendChangeEmail() {
        file_1 = getApplicationContext().getString(message_email_1);
        file_2 = getApplicationContext().getString(message_email_2);
        file_3 = getApplicationContext().getString(message_email_3);
        file_4 = getApplicationContext().getString(message_email_4);
        file_5 = getApplicationContext().getString(message_email_5);
        file_6 = getApplicationContext().getString(message_email_6);
        file_7 = getApplicationContext().getString(message_email_7);


        String newEmail = binding.newEmailEt.getText().toString();

        verification = generateString(6);


        //setup data to add in db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code_verification", verification);

        DatabaseReference  IAReference = FirebaseDatabase.getInstance().getReference("IAUser");
        IAReference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        String sendMessage = file_1+name+"!"+"\n"+file_2+file_3+verification+"\n"+file_4+file_5+file_6+file_7;
        String sendSubject = "[Quiker] Please verify your device";

        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,newEmail,sendSubject,sendMessage);
        Snackbar.make(binding.codeRl, R.string.send_email, Snackbar.LENGTH_LONG)
                .show();

        javaMailAPI.execute();
    }

    public void resendCode() {
        file_1 = getApplicationContext().getString(message_email_1);
        file_2 = getApplicationContext().getString(message_email_2);
        file_3 = getApplicationContext().getString(message_email_3);
        file_4 = getApplicationContext().getString(message_email_4);
        file_5 = getApplicationContext().getString(message_email_5);
        file_6 = getApplicationContext().getString(message_email_6);
        file_7 = getApplicationContext().getString(message_email_7);


        verification = generateString(6);


        //setup data to add in db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code_verification", verification);

        DatabaseReference  IAReference = FirebaseDatabase.getInstance().getReference("IAUser");
        IAReference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        String sendMessage = file_1+name+"!"+"\n"+file_2+file_3+verification+"\n"+file_4+file_5+file_6+file_7;
        String sendSubject = "[Quiker] Please verify your device";

        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,email,sendSubject,sendMessage);
        Snackbar.make(binding.codeRl, R.string.send_email, Snackbar.LENGTH_LONG)
                .show();

        javaMailAPI.execute();
        codeBottomSheet.dismiss();
    }

    private String generateString(int length){
        char[] chars = "0123456789".toCharArray();
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<length; i++){
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        if (IA != null && IA.equalsIgnoreCase("change")){
            binding.changeRl.setVisibility(View.GONE);
            binding.codeRl.setVisibility(View.VISIBLE);

            IA = "code";

        } else if (IA != null && IA.equalsIgnoreCase("code")){

            finish();

        } else {

            finish();
        }
    }
}