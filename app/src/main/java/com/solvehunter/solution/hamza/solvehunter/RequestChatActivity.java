package com.solvehunter.solution.hamza.solvehunter;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RequestChatActivity extends AppCompatActivity {
    CheckBox acceptPay;
    TextView problemHint;
    Button sendRequest;
    ProgressBar loader;
    private FirebaseAuth mAuth;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef =database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_chat);
        acceptPay=findViewById(R.id.accept_payment);
        problemHint=findViewById(R.id.problem_hint);
        sendRequest=findViewById(R.id.send_request);
        loader=findViewById(R.id.loader);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        acceptPay.setText("by select this you agree to pay "+getIntent().getExtras().getString("price")+" to doctor");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                if(problemHint.getText().toString().isEmpty()){
                    Toast.makeText(RequestChatActivity.this,"please write problem hint",Toast.LENGTH_LONG).show();
                }
                else{
                    if (!acceptPay.isChecked()){
                        Toast.makeText(RequestChatActivity.this,"you must accept payment for chatting",Toast.LENGTH_LONG).show();
                    }
                    else{
                        {
                            myRef.child("ChatRequests").child(getIntent().getExtras().getString("docId")).push()
                                    .setValue(new RequestData(getIntent().getExtras().getString("docId"),mAuth.getCurrentUser().getUid(),problemHint.getText().toString()))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("dialoges").child(getIntent().getExtras().getString("docId")+mAuth.getCurrentUser().getUid())
                                                    .setValue(new DialogesListData(getIntent().getExtras().getString("docId"),problemHint.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    myRef.child("Dialoges").child(getIntent().getExtras().getString("docId")+mAuth.getCurrentUser().getUid()).push()
                                                            .setValue(new DialogesData("false",problemHint.getText().toString(),mAuth.getCurrentUser().getUid()));
                                                    loader.setVisibility(View.GONE);
                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(RequestChatActivity.this);
                                                    builder1.setMessage("Doctor recived your requset and he/she will communicate with you at the earliest depending on the doctor queue");
                                                    builder1.setPositiveButton(
                                                            "ok",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                    finish();
                                                                }
                                                            });
                                                    AlertDialog alert11 = builder1.create();
                                                    alert11.show();
                                                    alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor( R.color.colorPrimaryDark));
                                                }
                                            });
                                        }
                                    });

                        }

                    }

                }
            }
        });
    }
}
