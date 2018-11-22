package com.solvehunter.solution.hamza.solvehunter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class AllDoctorsTabFragment extends android.support.v4.app.Fragment {
    RecyclerView recyclerView;
    List<AllDoctorsData> allDoctorsDataList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;
    ProgressBar loader;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_doctors_tab,container,false);
        recyclerView = view.findViewById(R.id.all_doctors);
        loader=view.findViewById(R.id.all_doctors_loader);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allDoctorsDataList = new ArrayList<>();
        getData();
        return view;
    }
    public void getData(){
        myRef.child("Doctors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loader.setVisibility(View.VISIBLE);
                allDoctorsDataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AllDoctorsData allDoctorsData = snapshot.getValue(AllDoctorsData.class);
                    allDoctorsData.setId(snapshot.getKey());
                    allDoctorsDataList.add(allDoctorsData);
                }
                AllDoctorsAdapter adapter=new AllDoctorsAdapter(getContext(),allDoctorsDataList);
                recyclerView.setAdapter(adapter);
                loader.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
