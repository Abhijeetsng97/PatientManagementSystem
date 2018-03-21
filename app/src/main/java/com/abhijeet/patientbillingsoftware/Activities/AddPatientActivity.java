package com.abhijeet.patientbillingsoftware.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abhijeet.patientbillingsoftware.R;
import com.abhijeet.patientbillingsoftware.Util.Patient;
import com.abhijeet.patientbillingsoftware.Util.Ward;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by abhij on 19-03-2018.
 */

public class AddPatientActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton rb1;
    String rbText, currentDateandTime,type;
    private EditText pName, pWard;
    private TextView pTime;
    private Button btnAdd, btnWardNum;
    private FirebaseDatabase mFD;
    private DatabaseReference mDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        Toolbar Toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(Toolbar);
        Toolbar.setTitle("Add Patient");

        Bundle extras = getIntent().getExtras();
        type = extras.getString("type");

        radioGroup = findViewById(R.id.radioGrp);
        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    rbText = rb.getText().toString();
                }

            }
        });
        pName = findViewById(R.id.input_name);
        pTime = findViewById(R.id.time);
        pWard = findViewById(R.id.ward_number);
        btnAdd = findViewById(R.id.btn_add);
        btnWardNum = findViewById(R.id.btn_get_ward_number);

        currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        .format(new Date());
        pTime.setText("Time: "+currentDateandTime);

        mFD = FirebaseDatabase.getInstance();
        mDF = mFD.getReference();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddPatientActivity.this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    public void AddPatient(View view){
        String id = getSaltString();

        Patient p = new Patient(
                pName.getText().toString(),
                id,
                currentDateandTime,
                rbText.toLowerCase(),
                pWard.getText().toString(),
                "null",
                "0",
                "0"
        );
        mDF.child("patient").child(id).setValue(p);

        String wardname;
        if(rbText.contains("ICU")){
            wardname = "icu";
        }
        else{
            wardname = "gen";
        }
        Ward ward = new Ward(pWard.getText().toString(),"occupied");
        mDF.child("Ward").child(wardname).child(pWard.getText().toString()).setValue(ward);

        Toast.makeText(AddPatientActivity.this,"Patient Details Successfully Added",
                Toast.LENGTH_SHORT).show();

        Intent i = new Intent(AddPatientActivity.this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    public void getWardNumber(View view){
        if(rbText == null){
            return;
        }
        mDF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String wardname;
                if(rbText.contains("ICU")){
                    wardname = "icu";
                }
                else{
                    wardname = "gen";
                }
                Ward ward = new Ward();
                for (DataSnapshot ds : dataSnapshot.child("Ward").child(wardname).getChildren()) {
                    ward.setNum(ds.getValue(Ward.class).getNum());
                    ward.setPos(ds.getValue(Ward.class).getPos());
                    if(ward.getPos().contains("empty")){
                        pWard.setText(ward.getNum());
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 17) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
