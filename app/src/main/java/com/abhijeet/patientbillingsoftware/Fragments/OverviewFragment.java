package com.abhijeet.patientbillingsoftware.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhijeet.patientbillingsoftware.R;
import com.abhijeet.patientbillingsoftware.Util.Ward;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by abhij on 19-03-2018.
 */

public class OverviewFragment extends Fragment {

    private ArcProgress arcProgress,arcProgress1,arcProgress2;
    private int valIcu,valGen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        getVal("icu");
        getVal("gen");

        arcProgress = view.findViewById(R.id.arc_progress);
        arcProgress1 = view.findViewById(R.id.arc_progress1);
        arcProgress2 = view.findViewById(R.id.arc_progress2);

        arcProgress.setBottomText("BEDS OCCUPIED");
        arcProgress.setMax(200);
        arcProgress.setProgress(valIcu + valGen);
        arcProgress.setFinishedStrokeColor(Color.parseColor("#FF6F00"));
        arcProgress.setUnfinishedStrokeColor(Color.parseColor("#FFECB3"));
        arcProgress.setTextSize(250);
        arcProgress.setBottomTextSize(40);
        arcProgress.setTextColor(Color.parseColor("#BF360C"));

        arcProgress1.setBottomText("IN ICU");
        arcProgress1.setMax(100);
        arcProgress1.setProgress(valIcu);
        arcProgress1.setFinishedStrokeColor(Color.parseColor("#FF6F00"));
        arcProgress1.setUnfinishedStrokeColor(Color.parseColor("#FFECB3"));
        arcProgress1.setTextSize(150);
        arcProgress1.setBottomTextSize(30);
        arcProgress1.setTextColor(Color.parseColor("#BF360C"));

        arcProgress2.setBottomText("IN GENERAL");
        arcProgress2.setMax(100);
        arcProgress2.setProgress(valGen);
        arcProgress2.setFinishedStrokeColor(Color.parseColor("#FF6F00"));
        arcProgress2.setUnfinishedStrokeColor(Color.parseColor("#FFECB3"));
        arcProgress2.setTextSize(150);
        arcProgress2.setBottomTextSize(30);
        arcProgress2.setTextColor(Color.parseColor("#BF360C"));
        return view;
    }

    public void getVal(final String wardType){
        Log.d("asd","ad");
        FirebaseDatabase mFD = FirebaseDatabase.getInstance();
        DatabaseReference mDF= mFD.getReference();
        mDF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Ward ward = new Ward();
                int i = 0;
                for (DataSnapshot ds : dataSnapshot.child("Ward").child(wardType).getChildren()) {
                    ward.setPos(ds.getValue(Ward.class).getPos());
                    if(ward.getPos().contains("occupied")){
                        i++;
                        Log.d("asd",Integer.toString(i));
                    }
                }
                if(wardType.contains("icu")){
                    arcProgress1.setProgress(i);
                }
                else{
                    arcProgress2.setProgress(i);
                    arcProgress.setProgress((arcProgress1.getProgress()+arcProgress2.getProgress())/2);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

