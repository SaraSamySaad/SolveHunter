package com.solvehunter.solution.hamza.solvehunter;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllCommentsActivity extends AppCompatActivity {
    TextView postBody;
    TextView uploadedByName;
    ImageView uploadedByImage;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;
    List<CommentData> commentList;
    RecyclerView recyclerView;
    ProgressBar loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comments);
        postBody =findViewById(R.id.body);
        uploadedByImage=findViewById(R.id.user_image);
        uploadedByName=findViewById(R.id.user_name);
        mAuth = FirebaseAuth.getInstance();
        postBody.setText(getIntent().getExtras().getString("postBody"));
        recyclerView = findViewById(R.id.all_comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentList = new ArrayList<>();
        loader=findViewById(R.id.loader);
    }

    @Override
    protected void onResume() {
        super.onResume();
        commentList.clear();
        loader.setVisibility(View.VISIBLE);

        myRef.child("Users").child(getIntent().getExtras().getString("postUploadedId"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()==null){
                            myRef.child("Doctors").child(getIntent().getExtras().getString("postUploadedId")).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    uploadedByName.setText(dataSnapshot.child("name").getValue().toString());
                                    if(!dataSnapshot.child("image").getValue().equals("")){
                                        Glide.with(AllCommentsActivity.this).load(dataSnapshot.child("image").getValue()).into(uploadedByImage);
                                    }

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                        else{
                            uploadedByName.setText(dataSnapshot.child("name").getValue().toString());
                            Glide.with(AllCommentsActivity.this).load(dataSnapshot.child("image").getValue()).into(uploadedByImage);
                        }
                        loader.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        myRef.child("Comments").child(getIntent().getExtras().getString("postId"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            CommentData commentData = snapshot.getValue(CommentData.class);
                            commentList.add(commentData);
                        }
                        CommentAdapter adapter=new CommentAdapter(AllCommentsActivity.this,commentList);
                        recyclerView.setAdapter(adapter);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

}

