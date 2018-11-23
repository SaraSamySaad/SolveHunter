package com.solvehunter.solution.hamza.solvehunter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by C.M on 21/10/2018.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
    private Context mCtx;
    private List<DialogesListData> dialogesListDataList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;
    public ChatListAdapter(Context mCtx, List<DialogesListData> dialogesListDataList) {
        this.mCtx = mCtx;
        this.dialogesListDataList = dialogesListDataList;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.chat_list_item, null);
        return new ChatListAdapter.ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListViewHolder holder, int position) {
        final DialogesListData dialogesListData = dialogesListDataList.get(position);
        myRef.child("Doctors").child(dialogesListData.getReciverId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    holder.reciverName.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                    if(!dataSnapshot.child("image").getValue().equals("")){
                        Glide.with(mCtx).load(dataSnapshot.child("image").getValue()).into(holder.reciverImage);
                    }
                }
                else{
                    holder.reciverName.setText("solve hunter doctor");
                    dialogesListData.setReciverId("null");
                }



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.lastMessage.setText(String.valueOf(dialogesListData.getLastMessage()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(mCtx, ChatActivity.class);
                mainIntent.putExtra("chatId",dialogesListData.getId());
                mainIntent.putExtra("senderId",dialogesListData.getReciverId());
                mCtx.startActivity(mainIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dialogesListDataList.size();
    }

    class ChatListViewHolder extends RecyclerView.ViewHolder {
        TextView reciverName;
        TextView lastMessage;
        ImageView reciverImage;

        public ChatListViewHolder(View itemView) {
            super(itemView);
            reciverName = itemView.findViewById(R.id.reciver_name);
            lastMessage = itemView.findViewById(R.id.last_message);
            reciverImage=itemView.findViewById(R.id.reciver_image);

        }
    }
}
