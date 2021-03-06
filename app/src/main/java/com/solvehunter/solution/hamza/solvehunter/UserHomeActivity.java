package com.solvehunter.solution.hamza.solvehunter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class UserHomeActivity extends AppCompatActivity {

    String token ;
    List<UserHomePosts> userHomePostsList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    ProgressBar postsLoader;
    RecyclerView recyclerView;
    EditText homePost;
    private FirebaseAuth mAuth;
    public static boolean isAppRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_user_home);
        recyclerView = findViewById(R.id.user_home_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userHomePostsList = new ArrayList<>();
        postsLoader = findViewById(R.id.suser_home_loader);
        homePost = findViewById(R.id.home_post);

        isAppRunning = true;

         token = FirebaseInstanceId.getInstance().getToken();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef.child("PushTokens").child(mAuth.getCurrentUser().getUid()).setValue(token);
            }
        }, 2000);


        //To Recive notification from other app here..
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "1";
        String channel2 = "2";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    "Channel 1", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("This is BNT");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationChannel notificationChannel2 = new NotificationChannel(channel2,
                    "Channel 2", NotificationManager.IMPORTANCE_MIN);

            notificationChannel.setDescription("This is bTV");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel2);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        postsLoader.setVisibility(View.VISIBLE);
        //get posts data
        getData();
        //write post
        homePost.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    postsLoader.setVisibility(View.VISIBLE);
                    myRef.child("Pending").push().setValue(new PendingPosts(homePost.getText().toString(), mAuth.getCurrentUser().getUid()))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    postsLoader.setVisibility(View.GONE);
                                    Toast.makeText(UserHomeActivity.this, "your post is pending admain accepte", Toast.LENGTH_LONG).show();
                                    homePost.setText("");
                                }
                            });

                    InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(UserHomeActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(homePost.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }

    public void getData() {
        myRef.child("acceptedPosts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userHomePostsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserHomePosts userHomePosts = snapshot.getValue(UserHomePosts.class);
                    userHomePosts.setId(snapshot.getKey());
                    userHomePostsList.add(userHomePosts);
                    postsLoader.setVisibility(View.GONE);
                }
                userHomePostsAdapter adapter = new userHomePostsAdapter(UserHomeActivity.this, userHomePostsList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //and this to handle actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.chat) {
            Intent mainIntent = new Intent(UserHomeActivity.this, ChatUserActivity.class);
            UserHomeActivity.this.startActivity(mainIntent);
        }
        if (id == R.id.profile) {
            Intent mainIntent = new Intent(UserHomeActivity.this, ProfileActivity.class);
            UserHomeActivity.this.startActivity(mainIntent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAppRunning = false;
    }
}
