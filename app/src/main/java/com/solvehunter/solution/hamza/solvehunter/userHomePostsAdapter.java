package com.solvehunter.solution.hamza.solvehunter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by C.M on 16/10/2018.
 */

public class userHomePostsAdapter extends RecyclerView.Adapter<userHomePostsAdapter.userHomeViewHolder> {


    private Context mCtx;
    private List<UserHomePosts> userHomePostsList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;

    public userHomePostsAdapter(Context mCtx, List<UserHomePosts> userHomePostsList) {
        this.mCtx = mCtx;
        this.userHomePostsList = userHomePostsList;
    }

    @NonNull
    @Override
    public userHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.user_home_item, null);
        return new userHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final userHomeViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        final UserHomePosts userHomePosts = userHomePostsList.get(userHomePostsList.size()-1- position);
        holder.body.setText(userHomePosts.getBody());
        holder.likes.setText(String.valueOf(userHomePosts.getLikes()));
        holder.comments.setText("comments (" + userHomePosts.getComments() + ")");
        //get uploaded by data
        myRef.child("Users").child(userHomePosts.getUploadedById()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){
                    myRef.child("Doctors").child(userHomePosts.getUploadedById()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            holder.userName.setText(dataSnapshot.child("name").getValue().toString());

                            if(!dataSnapshot.child("image").getValue().equals("")){
                                Glide.with(mCtx).load(dataSnapshot.child("image").getValue()).into(holder.userImage);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    holder.userName.setText(dataSnapshot.child("name").getValue().toString());
                    if(!dataSnapshot.child("image").getValue().equals("")){
                        Glide.with(mCtx).load(dataSnapshot.child("image").getValue()).into(holder.userImage);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child("Likes").child(userHomePosts.getId()).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    for (Drawable drawable : holder.likes.getCompoundDrawables()) {
                        if (drawable != null) {
                            DrawableCompat.setTint(drawable, ContextCompat.getColor(mCtx, R.color.colorAccent));
                        }
                    }
                }
                else
                {
                    for (Drawable drawable : holder.likes.getCompoundDrawables()) {
                        if (drawable != null) {
                            DrawableCompat.setTint(drawable, ContextCompat.getColor(mCtx, R.color.colorPrimaryDark));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("likesError",String.valueOf(userHomePosts.getLikes()));
                myRef.child("Likes").child(userHomePosts.getId()).child(mAuth.getCurrentUser().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue()==null){
                                    //add user id in fire base like & likes++
                                    myRef.child("Likes").child(userHomePosts.getId()).child(mAuth.getCurrentUser().getUid()).setValue("true")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    myRef.child("acceptedPosts").child(userHomePosts.getId()).child("likes").setValue(userHomePosts.getLikes()+1)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    userHomePosts.setLikes(userHomePosts.getLikes()+1);
                                                                    holder.likes.setText(String.valueOf(userHomePosts.getLikes()));
                                                                    for (Drawable drawable : holder.likes.getCompoundDrawables()) {
                                                                        if (drawable != null) {
                                                                            DrawableCompat.setTint(drawable, ContextCompat.getColor(mCtx, R.color.colorPrimaryDark));
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                }
                                            });
                                }
                                // else for unlike & likes--
                                else {
                                    myRef.child("Likes").child(userHomePosts.getId()).child(mAuth.getCurrentUser().getUid())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            myRef.child("acceptedPosts").child(userHomePosts.getId()).child("likes").setValue(userHomePosts.getLikes()-1)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            userHomePosts.setLikes(userHomePosts.getLikes()-1);
                                                            holder.likes.setText(String.valueOf(userHomePosts.getLikes()));
                                                            for (Drawable drawable : holder.likes.getCompoundDrawables()) {
                                                                if (drawable != null) {
                                                                    DrawableCompat.setTint(drawable, ContextCompat.getColor(mCtx, R.color.colorAccent));
                                                                }
                                                            }
                                                        }
                                                    });
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        });
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(mCtx, AllCommentsActivity.class);
                mainIntent.putExtra("postBody",userHomePosts.getBody());
                mainIntent.putExtra("postUploadedId",userHomePosts.getUploadedById());
                mainIntent.putExtra("postId",userHomePosts.getId());
                mCtx.startActivity(mainIntent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return userHomePostsList.size();
    }

    class userHomeViewHolder extends RecyclerView.ViewHolder {

        TextView body;
        TextView likes;
        TextView comments;
        TextView userName;
        ImageView userImage;

        public userHomeViewHolder(View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.body);
            likes = itemView.findViewById(R.id.likes);
            comments = itemView.findViewById(R.id.comments);
            userName = itemView.findViewById(R.id.user_name);
            userImage = itemView.findViewById(R.id.user_image);
        }
    }

}
