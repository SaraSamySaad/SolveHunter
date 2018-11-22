package com.solvehunter.solution.hamza.solvehunter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef =database.getReference();
    EditText email;
    EditText password;
    EditText userName;
    EditText confirmPassword;
    EditText phoneNumber;
    Button signUp;
    ProgressBar signUpLoader;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = pref.edit();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirom_password);
        userName=findViewById(R.id.user_name);
        signUp=findViewById(R.id.sign_up);
        signUpLoader=findViewById(R.id.sign_up_loader);
        radioSexGroup = findViewById(R.id.radioSex);
        phoneNumber=findViewById(R.id.phone_number);
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = pref.edit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = findViewById(selectedId);
        mAuth = FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               signUpLoader.setVisibility(View.VISIBLE);
               int selectedId = radioSexGroup.getCheckedRadioButtonId();
               radioSexButton =  findViewById(selectedId);
               if (email.getText().toString().isEmpty()||password.getText().toString().isEmpty()||confirmPassword.getText().toString().isEmpty()||userName.getText().toString().isEmpty()||phoneNumber.getText().toString().isEmpty()){
                   signUpLoader.setVisibility(View.GONE);
                   Toast.makeText(SignUpActivity.this,"Please enter all data",Toast.LENGTH_LONG).show();
               }
               else if (!password.getText().toString().equals(confirmPassword.getText().toString())){
                   signUpLoader.setVisibility(View.GONE);
                   Toast.makeText(SignUpActivity.this,"Password & confirm password must be match",Toast.LENGTH_LONG).show();
               }
               else if(password.getText().toString().length()<6){
                   signUpLoader.setVisibility(View.GONE);
                   Toast.makeText(SignUpActivity.this,"Password must be more than 6 characters",Toast.LENGTH_LONG).show();
               }
               else{
               mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                       .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()){
                                  myRef.child("Users").child(mAuth.getCurrentUser().getUid())
                                          .setValue(new SignUpData(radioSexButton.getText().toString(),"",userName.getText().toString(),phoneNumber.getText().toString()))
                                          .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {
                                                  editor.putString("userID",mAuth.getCurrentUser().getUid());
                                                  editor.putString("name",userName.getText().toString());
                                                  editor.putString("phone",phoneNumber.getText().toString());
                                                  editor.putString("image","");
                                                  editor.commit();
                                                  signUpLoader.setVisibility(View.GONE);
                                                  Intent intent = new Intent(SignUpActivity.this,UserHomeActivity.class);
                                                  startActivity(intent);
                                              }
                                          });

                               }
                               else
                               {
                                   signUpLoader.setVisibility(View.GONE);
                                   if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                       Toast.makeText(getApplicationContext(), "you are already user", Toast.LENGTH_SHORT).show();
                                   else if (task.getException().getMessage().equals("The email address is badly formatted.")){
                                       Toast.makeText(getApplicationContext(), "The email address is badly formatted.", Toast.LENGTH_SHORT).show();
                                   }
                                   else{
                                       Toast.makeText(getApplicationContext(), "some thing wrong please try again", Toast.LENGTH_SHORT).show();
                                   }
                               }

                           }
                       });
               }
           }
       });
    }
}
