package com.solvehunter.solution.hamza.solvehunter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    TextView newHere;
    EditText email;
    EditText password;
    Button signIn;
    private FirebaseAuth mAuth;
    ProgressBar signInProgress;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = pref.edit();
        newHere=findViewById(R.id.new_here);
        email=findViewById(R.id.sign_in_email);
        password=findViewById(R.id.sign_in_password);
        signIn=findViewById(R.id.sign_in);
        mAuth = FirebaseAuth.getInstance();
        signInProgress=findViewById(R.id.sign_in_loader);
    }

    @Override
    protected void onResume() {
        super.onResume();
        newHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInProgress.setVisibility(View.VISIBLE);
                if (email.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
                    Toast.makeText(SignInActivity.this, "enter your mail & paseword", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                editor.putString("userID",mAuth.getCurrentUser().getUid());
                                                editor.putString("name",dataSnapshot.child("name").getValue().toString());
                                                editor.putString("phone",dataSnapshot.child("phone").getValue().toString());
                                                editor.putString("image",dataSnapshot.child("image").getValue().toString());
                                                editor.commit();
                                                signInProgress.setVisibility(View.GONE);
                                                Intent intent = new Intent(SignInActivity.this,UserHomeActivity.class);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }

                                        });
                                    }
                                    else{
                                        signInProgress.setVisibility(View.GONE);
                                        Toast.makeText(SignInActivity.this, "wrong data cheek your email & paseword", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
    }
}
