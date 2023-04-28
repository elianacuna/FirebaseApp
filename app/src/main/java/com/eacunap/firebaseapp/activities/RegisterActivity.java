package com.eacunap.firebaseapp.activities;

import static com.eacunap.firebaseapp.R.string.message_email_1;
import static com.eacunap.firebaseapp.R.string.message_email_2;
import static com.eacunap.firebaseapp.R.string.message_email_3;
import static com.eacunap.firebaseapp.R.string.message_email_4;
import static com.eacunap.firebaseapp.R.string.message_email_5;
import static com.eacunap.firebaseapp.R.string.message_email_6;
import static com.eacunap.firebaseapp.R.string.message_email_7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.eacunap.firebaseapp.R;
import com.eacunap.firebaseapp.databinding.ActivityRegisterBinding;
import com.eacunap.firebaseapp.sendCodeEmail.JavaMailAPI;
import com.eacunap.firebaseapp.utils.SignUp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    //variables for the mail
    String verification = "";
    String  file_1 = "";
    String  file_2 = "";
    String  file_3 = "";
    String  file_4 = "";
    String  file_5 = "";
    String  file_6 = "";
    String  file_7 = "";

    //Binding
    private ActivityRegisterBinding binding;

    //IA for the designs
    String IA = "";

    //Variables
    String email = "", name = "", password = "";
    int years;

    //Firebase auth
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Init Firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        page_design();

    }

    private void page_design() {
        pagePrincipal();
        pageEmail();
        pageName();
        pageBirthday();
        pagePassword();
        pagePolicies();
    }

    private void pagePrincipal() {
        //handle nextBtn click, star Email screen
        binding.nextMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.principalRl.setVisibility(View.GONE);
                binding.emailRl.setVisibility(View.VISIBLE);

                IA = "email";
            }
        });
        //handle back click, star Main screen
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    private void pageEmail(){
        //handle emailBtn click, star name screen
        binding.emailMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = binding.emailEt.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    binding.description1.setTextColor(getResources().getColor(R.color.error));
                    binding.description1.setText(R.string.error_email);

                }else {
                    binding.emailRl.setVisibility(View.GONE);
                    binding.nameRl.setVisibility(View.VISIBLE);

                    IA = "name";
                }
            }
        });
        //handle back click, star Main screen
        binding.back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //This line is used to return the previous info
        SignUp.email(binding);
    }

    private void pageName(){

        //handle nameBtn click, star birthday screen
        binding.nameMb.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               name = binding.nameEt.getText().toString();

               if (!name.isEmpty()){

                   binding.birthdayRl.setVisibility(View.VISIBLE);
                   binding.nameRl.setVisibility(View.GONE);

                   IA = "age";

               } else{
                   binding.description2.setText(R.string.error_name);
                   binding.description2.setTextColor(getResources().getColor(R.color.error));
               }
           }
       });
        //handle back click, star Main screen
        binding.back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //This line is used to return the previous info
        SignUp.name(binding);
    }

    private void pageBirthday() {

        //handle birthdayMb click, star password screen
        binding.birthdayMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (years < 5){
                    binding.description3.setText(R.string.error_age);
                    binding.description3.setTextColor(getResources().getColor(R.color.error));

                }
                else {
                    binding.birthdayRl.setVisibility(View.GONE);
                    binding.passwordRl.setVisibility(View.VISIBLE);
                    IA = "password";

                    binding.description3.setText(R.string.choose_your_date_of_birth);
                    binding.description3.setTextColor(Color.GRAY);
                }
            }
        });
        //handle back click, star Main screen
        binding.back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Age DatePicker
        binding.ageDp.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int yearOfBirth, int monthOfBirth, int dayOfBirth) {

                yearOfBirth = binding.ageDp.getYear();
                monthOfBirth = binding.ageDp.getMonth();
                dayOfBirth = binding.ageDp.getDayOfMonth();

                Calendar calendarMin = Calendar.getInstance();
                calendarMin.add(Calendar.MONTH, 0);
                calendarMin.add(Calendar.DAY_OF_MONTH, 0);
                binding.ageDp.setMaxDate(calendarMin.getTimeInMillis() - 1000);

                Calendar birthDate = new GregorianCalendar(yearOfBirth, monthOfBirth, dayOfBirth);
                Calendar now = Calendar.getInstance();

                long ageInDays = (now.getTimeInMillis() - birthDate.getTimeInMillis())
                        / 1000 / 60 / 60 / 24;

                years = Double.valueOf(ageInDays / 365.25d).intValue();
                if (years == 0){
                    binding.ageTxt.setText("0 " + getString(R.string.years_old));
                }
                else if (years == 1){
                    binding.ageTxt.setText("1 " + getString(R.string.year_old));
                }
                else {
                    binding.ageTxt.setText(years +"\t\t" + getString(R.string.years_old));

                }
            }
        });


    }

    private void pagePassword() {

        //handle nameBtn click, star birthday screen
        binding.passwordMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = binding.passwordEt.getText().toString();
                if (!password.isEmpty()){
                    if (password.length() >= 8){
                        binding.passwordRl.setVisibility(View.GONE);
                        binding.privacyRl.setVisibility(View.VISIBLE);

                        IA = "privacy";

                    }else {
                        binding.description4.setText(R.string.description_4);
                        binding.description4.setTextColor(getResources().getColor(R.color.error));                    }
                }else {
                    binding.description4.setText(R.string.description_4);
                    binding.description4.setTextColor(getResources().getColor(R.color.error));
                }
            }
        });
        //handle back click, star Main screen
        binding.back4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //This line is used to return the previous info
        binding.passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = binding.passwordEt.getText().toString();

                SignUp.password_check(password, binding, getApplicationContext());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void pagePolicies() {
        //Handle click, create account and go to confirm email
        binding.signUpMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.agreePrivacyCB.isChecked() && binding.agreeTermsCB.isChecked()){

                    createAccount();
                }
            }
        });
        //handle back click, star Main screen
        binding.back5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.privacyRl.setVisibility(View.GONE);
                binding.creatingRl.setVisibility(View.VISIBLE);
            }
        });
    }

    private void createAccount() {
        binding.creatingRl.setVisibility(View.VISIBLE);
        binding.privacyRl.setVisibility(View.GONE);

        //Create user in firebase auth
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //account creation success, now add in firebase realtime database
                updateUserInfo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Message: account creating failed
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInfo() {
        long timestamp = System.currentTimeMillis();

        //get current user id, since user is registered so we can get now
        String id = firebaseAuth.getUid();

        //setup data to add in database
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("auth", email);
        hashMap.put("name", name);
        hashMap.put("password", password);
        hashMap.put("birthday", "");
        hashMap.put("profileImage", "");
        hashMap.put("username", "");
        hashMap.put("cover", "");

        //set data to database
        DatabaseReference updateUserInfoDB = FirebaseDatabase.getInstance().getReference("Users");
        updateUserInfoDB.child(id).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendMail();

                //set data to db
                HashMap<String, Object> hashMap_IA = new HashMap<>();
                hashMap_IA.put("confirm_auth", "no_confirmed");

                DatabaseReference  IAReference = FirebaseDatabase.getInstance().getReference("IAUser");
                IAReference.child(firebaseAuth.getUid()).updateChildren(hashMap_IA)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                startActivity(new Intent(getApplicationContext(), CodeEmailActivity.class));
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //data failed adding to db
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (IA != null && IA.equalsIgnoreCase("email")){
            binding.emailRl.setVisibility(View.GONE);
            binding.principalRl.setVisibility(View.VISIBLE);

            IA = "main";

        } else if (IA != null && IA.equalsIgnoreCase("name")){
            binding.nameRl.setVisibility(View.GONE);
            binding.emailRl.setVisibility(View.VISIBLE);

            IA = "email";

        } else if (IA != null && IA.equalsIgnoreCase("age")){
            binding.birthdayRl.setVisibility(View.GONE);
            binding.nameRl.setVisibility(View.VISIBLE);

            IA = "name";

        }  else if (IA != null && IA.equalsIgnoreCase("password")){
            binding.passwordRl.setVisibility(View.GONE);
            binding.birthdayRl.setVisibility(View.VISIBLE);

            IA = "age";

        }  else if (IA != null && IA.equalsIgnoreCase("privacy")){
            binding.privacyRl.setVisibility(View.GONE);
            binding.passwordRl.setVisibility(View.VISIBLE);

            IA = "password";

        } else if (IA != null && IA.equalsIgnoreCase("main")){

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        } else {

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }
    }

    private void sendMail(){

        file_1 = getApplicationContext().getString(message_email_1);
        file_2 = getApplicationContext().getString(message_email_2);
        file_3 = getApplicationContext().getString(message_email_3);
        file_4 = getApplicationContext().getString(message_email_4);
        file_5 = getApplicationContext().getString(message_email_5);
        file_6 = getApplicationContext().getString(message_email_6);
        file_7 = getApplicationContext().getString(message_email_7);

        email = binding.emailEt.getText().toString();

        verification = generateString(6);


        //setup data to add in db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code_verification", verification);
        hashMap.put("email", email);

        DatabaseReference  IAReference = FirebaseDatabase.getInstance().getReference("IAUser");
        IAReference.child(firebaseAuth.getUid()).setValue(hashMap)
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
        Snackbar.make(binding.privacyRl, R.string.send_email, Snackbar.LENGTH_LONG)
                .show();

        javaMailAPI.execute();
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

    private void dialogFinish() {

        final AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this)
                .setTitle("Do you want to stop creating \nyour account")
                .setMessage("If you stop now, you'll lose any \nprogress you've made.")
                .setPositiveButton("Continue creating account", null)
                .setNegativeButton("Stop creating account", null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        positiveButton.setTextColor(Color.GRAY);
        positiveButton.setTextSize(12);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        negativeButton.setTextColor(Color.BLUE);
        negativeButton.setTextSize(12);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                dialog.dismiss();

            }
        });

    }

}