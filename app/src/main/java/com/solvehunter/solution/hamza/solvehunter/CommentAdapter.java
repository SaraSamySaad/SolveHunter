package com.solvehunter.solution.hamza.solvehunter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by C.M on 25/10/2018.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentsViewHolder>  {

    private Context mCtx;
    private List<CommentData> commentsList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    public CommentAdapter(Context mCtx, List<CommentData> commentsList) {
        this.mCtx = mCtx;
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.comment_item, null);
        return new CommentAdapter.CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentsViewHolder holder, int position) {
        final CommentData commentData = commentsList.get( position);
        holder.commentBody.setText(commentData.getBody());
        myRef.child("Doctors").child(commentData.getAddBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("sara",commentData.getAddBy().toString());
                holder.commentedByName.setText(dataSnapshot.child("name").getValue().toString());
                if(!dataSnapshot.child("image").getValue().equals("")){
                    Glide.with(mCtx).load(dataSnapshot.child("image").getValue()).into(holder.commentedByImage);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView commentBody;
        TextView commentedByName;
        ImageView commentedByImage;
        public CommentsViewHolder(View itemView) {
            super(itemView);
            commentBody=itemView.findViewById(R.id.comment_body);
            commentedByName=itemView.findViewById(R.id.commended_by);
            commentedByImage=itemView.findViewById(R.id.commented_by_image);
        }
    }
}
