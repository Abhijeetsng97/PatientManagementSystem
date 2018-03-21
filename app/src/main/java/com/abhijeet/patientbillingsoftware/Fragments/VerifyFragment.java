package com.abhijeet.patientbillingsoftware.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.abhijeet.patientbillingsoftware.Activities.BillingActivity;
import com.abhijeet.patientbillingsoftware.Activities.PatientActivity;
import com.abhijeet.patientbillingsoftware.R;
import com.abhijeet.patientbillingsoftware.Util.ArrayListAdapter;
import com.abhijeet.patientbillingsoftware.Util.ListVerifyAdapter;
import com.abhijeet.patientbillingsoftware.Util.Patient;
import com.abhijeet.patientbillingsoftware.Util.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abhij on 20-03-2018.
 */

public class VerifyFragment extends Fragment {

    private ListView lvVerify;
    private ArrayList<Users> dataList;
    private FirebaseDatabase mFD;
    private DatabaseReference mDF;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify, container, false);


        mFD = FirebaseDatabase.getInstance();
        mDF = mFD.getReference();

        lvVerify = view.findViewById(R.id.listViewVerify);
        makeList();
        lvVerify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("Verify Employee")
                        .setMessage("Are you sure you want to verify this employee?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                verify(dataList.get(i).getUid());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        return view;
    }

    public void verify(final String UID){
        mDF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("user").getChildren()) {
                    Users temp = new Users();
                    temp.setName(ds.getValue(Users.class).getName());
                    temp.setAuth(ds.getValue(Users.class).getAuth());
                    temp.setType(ds.getValue(Users.class).getType());
                    temp.setUid(ds.getValue(Users.class).getUid());
                    if(temp.getUid().contentEquals(UID)) {
                        temp.setAuth("done");
                        mDF.child("user")
                                .child(UID)
                                .setValue(temp);
                        Toast.makeText(getContext(),
                                "Auth for "+temp.getName()+" done",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                ListVerifyAdapter adapter = new ListVerifyAdapter(getContext(), dataList);
                lvVerify.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void makeList(){
        dataList = new ArrayList<>();
        mDF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("user").getChildren()) {
                    Users temp = new Users();
                    temp.setName(ds.getValue(Users.class).getName());
                    temp.setAuth(ds.getValue(Users.class).getAuth());
                    temp.setType(ds.getValue(Users.class).getType());
                    temp.setUid(ds.getValue(Users.class).getUid());
                    if(temp.getAuth().contentEquals("notdone") &&
                            temp.getType().contentEquals("emp")) {
                        dataList.add(temp);
                    }
                }
                ListVerifyAdapter adapter = new ListVerifyAdapter(getContext(), dataList);
                lvVerify.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
