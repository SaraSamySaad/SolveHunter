package com.solvehunter.solution.hamza.solvehunter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ImageView userImage;
    TextView userName;
    TextView userEmail;
    TextView userPhone;
    ProgressBar postsLoader;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Button logOut;
    List<UserHomePosts> userPostsList;
    RecyclerView recyclerView;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView noPosts;
    ImageView editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userImage=findViewById(R.id.user_image);
        userName=findViewById(R.id.user_name);
        noPosts=findViewById(R.id.no_posts);
        userPhone=findViewById(R.id.user_phone);
        userEmail=findViewById(R.id.user_mail);
        recyclerView=findViewById(R.id.user_posts_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsLoader=findViewById(R.id.user_posts_loader);
        userPostsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        logOut=findViewById(R.id.log_out);
        editProfile=findViewById(R.id.edit_profile);
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userName.setText(pref.getString("name",""));
        userPhone.setText(pref.getString("phone",""));
        userEmail.setText(mAuth.getCurrentUser().getEmail());
        if(!pref.getString("image","").equals("")){
            Glide.with(ProfileActivity.this).load(pref.getString("image","")).into(userImage);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                editor = pref.edit();
                editor.remove("userID");
                editor.apply();
                Intent i = new Intent(ProfileActivity.this, SignInActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        myRef.child("acceptedPosts").orderByChild("uploadedById").equalTo(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userPostsList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserHomePosts userHomePosts = snapshot.getValue(UserHomePosts.class);
                            userHomePosts.setId(snapshot.getKey());
                            userPostsList.add(userHomePosts);
                        }
                        if(userPostsList.isEmpty()){
                            noPosts.setVisibility(View.VISIBLE);
                        }
                        postsLoader.setVisibility(View.GONE);
                        userHomePostsAdapter adapter=new userHomePostsAdapter(ProfileActivity.this,userPostsList);
                        recyclerView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ProfileActivity.this, EditeProfileActivity.class);
                startActivity(mainIntent);
            }
        });
    }
}
