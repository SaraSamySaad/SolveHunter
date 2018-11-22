package com.solvehunter.solution.hamza.solvehunter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C.M on 21/10/2018.
 */

public class ChatTabFreagment extends android.support.v4.app.Fragment{

    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;
    private List<DialogesListData> dialogesListDataList;
    ProgressBar loader;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_tab,container,false);
        recyclerView = view.findViewById(R.id.chats);
        loader=view.findViewById(R.id.loader);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dialogesListDataList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        getData();
        return view;
    }

    public void getData(){
        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("dialoges").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loader.setVisibility(View.VISIBLE);
                dialogesListDataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DialogesListData dialogesListData = snapshot.getValue(DialogesListData.class);
                    dialogesListData.setId(snapshot.getKey());
                    dialogesListDataList.add(dialogesListData);
                }
                ChatListAdapter adapter=new ChatListAdapter(getContext(),dialogesListDataList);
                recyclerView.setAdapter(adapter);
                loader.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
