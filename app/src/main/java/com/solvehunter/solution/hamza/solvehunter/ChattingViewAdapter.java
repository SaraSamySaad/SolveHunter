package com.solvehunter.solution.hamza.solvehunter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Created by C.M on 23/10/2018.
 */

public class ChattingViewAdapter extends RecyclerView.Adapter<ChattingViewAdapter.ChattingViewHolder>   {
    private Context mCtx;
    private List<DialogesData> dialogesDataList;
    private FirebaseAuth mAuth;


    public ChattingViewAdapter(Context mCtx, List<DialogesData> dialogesDataList) {
        this.mCtx = mCtx;
        this.dialogesDataList = dialogesDataList;
    }

    @NonNull
    @Override
    public ChattingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialoge_message_item, null);
        return new ChattingViewAdapter.ChattingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingViewHolder holder, int position) {
        final DialogesData dialogesData = dialogesDataList.get(dialogesDataList.size()-1-position);
        mAuth = FirebaseAuth.getInstance();
        if (dialogesData.getSenderID().toString().equals(mAuth.getCurrentUser().getUid().toString())){
            holder.bodySender.setText(dialogesData.getBody());
            holder.reciver.setVisibility(View.GONE);

        }
        else
        {
            holder.bodyReciver.setText(String.valueOf(dialogesData.getBody()));
            holder.sender.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return dialogesDataList.size();
    }

    class ChattingViewHolder extends RecyclerView.ViewHolder{
        TextView bodySender;
        TextView bodyReciver;
        CardView reciver;
        CardView sender;
        public ChattingViewHolder(View itemView) {
            super(itemView);
            bodySender=itemView.findViewById(R.id.message_body_sender);
            bodyReciver=itemView.findViewById(R.id.message_body_reciver);
            reciver=itemView.findViewById(R.id.reciver);
            sender=itemView.findViewById(R.id.sender);
        }
    }
}
