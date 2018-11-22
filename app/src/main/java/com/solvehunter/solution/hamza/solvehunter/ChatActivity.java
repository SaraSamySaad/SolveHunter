package com.solvehunter.solution.hamza.solvehunter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private List<DialogesData> dialogesDataList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;
    EditText messageBody;
    ProgressBar loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.chatting);
        messageBody=findViewById(R.id.message_body);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        dialogesDataList = new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();
        loader=findViewById(R.id.loader);
        getData();


    }
    public void getData(){
        loader.setVisibility(View.VISIBLE);
        myRef.child("Dialoges").child(getIntent().getExtras().getString("chatId")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialogesDataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DialogesData dialogesData = snapshot.getValue(DialogesData.class);
                    dialogesData.setId(snapshot.getKey());
                    dialogesDataList.add(dialogesData);
                }
                loader.setVisibility(View.GONE);
                ChattingViewAdapter adapter=new ChattingViewAdapter(ChatActivity.this,dialogesDataList);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onResume() {
        super.onResume();
        messageBody.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (messageBody.getRight() - messageBody.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        myRef.child("Dialoges").child(getIntent().getExtras().getString("chatId")).push()
                                .setValue(new DialogesData("false",messageBody.getText().toString(),mAuth.getCurrentUser().getUid()))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Log.e("idmessage",getIntent().getExtras().getString("senderId"));

                                        myRef.child("Doctors").child(getIntent().getExtras().getString("senderId")).child("dialoges")
                                                .child(getIntent().getExtras().getString("senderId")+mAuth.getCurrentUser().getUid())
                                                .setValue(new LastMessage(messageBody.getText().toString(),mAuth.getCurrentUser().getUid()))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("dialoges")
                                                                .child(getIntent().getExtras().getString("senderId")+mAuth.getCurrentUser().getUid())
                                                                .setValue(new LastMessage(messageBody.getText().toString(),getIntent().getExtras().getString("senderId")))
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        messageBody.setText("");
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
