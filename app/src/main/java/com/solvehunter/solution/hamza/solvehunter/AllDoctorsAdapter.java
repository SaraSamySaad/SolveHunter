package com.solvehunter.solution.hamza.solvehunter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by C.M on 21/10/2018.
 */

public class AllDoctorsAdapter extends RecyclerView.Adapter<AllDoctorsAdapter.AllDoctorsViewHolder>  {
    private Context mCtx;
    private List<AllDoctorsData> allDoctorsDataList;
    private FirebaseAuth mAuth;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef =database.getReference();

    public AllDoctorsAdapter(Context mCtx, List<AllDoctorsData> allDoctorsDataList) {
        this.mCtx = mCtx;
        this.allDoctorsDataList = allDoctorsDataList;

    }
    @NonNull
    @Override
    public AllDoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.all_doctors_item, null);
        return new AllDoctorsAdapter.AllDoctorsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllDoctorsViewHolder holder, int position) {
        final AllDoctorsData allDoctorsData = allDoctorsDataList.get(position);
        holder.docName.setText(allDoctorsData.getName());
        holder.docPrice.setText(String.valueOf(allDoctorsData.getChatPrice()));

        if(!allDoctorsData.getImage().equals("")){
            Glide.with(mCtx).load(allDoctorsData.getImage()).into(holder.docImage);
        }

        mAuth = FirebaseAuth.getInstance();
        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Query query = myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("dialoges").child(allDoctorsData.getId());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()==null){

                            Intent mainIntent = new Intent(mCtx, RequestChatActivity.class);
                            mainIntent.putExtra("price",String.valueOf(allDoctorsData.getChatPrice()));
                            mainIntent.putExtra("docId",allDoctorsData.getId());
                            mCtx.startActivity(mainIntent);
                        }
                        else {
                            Toast.makeText(mCtx,"you sent an request wait for doctor replay",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return allDoctorsDataList.size();
    }

    class AllDoctorsViewHolder extends RecyclerView.ViewHolder{

        TextView docName;
        TextView docPrice;
        TextView request;
        ImageView docImage;
        public AllDoctorsViewHolder(View itemView) {
            super(itemView);
            docName = itemView.findViewById(R.id.doctor_name);
            docPrice = itemView.findViewById(R.id.doctor_chat_price);
            request = itemView.findViewById(R.id.request);
            docImage=itemView.findViewById(R.id.doctor_image);
        }


    }
}
