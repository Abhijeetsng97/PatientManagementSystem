package com.abhijeet.patientbillingsoftware.Activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.abhijeet.patientbillingsoftware.R;
import com.abhijeet.patientbillingsoftware.Util.Bills;
import com.abhijeet.patientbillingsoftware.Util.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by abhij on 20-03-2018.
 */

public class ReportGenerationActivity extends AppCompatActivity {

    private String type, UID;
    private TextView rName,rStatus;
    private String name,uid,time,ward,wardNum,dismissTime,billTotal,paid;
    private DatabaseReference mDF;
    private FirebaseDatabase mFD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_generation);
        Toolbar Toolbar = findViewById(R.id.toolbarreport);
        setSupportActionBar(Toolbar);
        Toolbar.setTitle("Report Generation");

        Bundle extras = getIntent().getExtras();
        type = extras.getString("type");
        UID = extras.getString("id");
        rName = findViewById(R.id.report_name);
        rStatus = findViewById(R.id.report_status);
        mFD = FirebaseDatabase.getInstance();
        mDF = mFD.getReference();

        generateLayout();
    }

    public void generateLayout(){
        mDF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("patient").getChildren()) {
                    Patient temp = new Patient();
                    temp.setName(ds.getValue(Patient.class).getName());
                    temp.setBillTotal(ds.getValue(Patient.class).getBillTotal());
                    temp.setDismissTime(ds.getValue(Patient.class).getDismissTime());
                    temp.setId(ds.getValue(Patient.class).getId());
                    temp.setPaid(ds.getValue(Patient.class).getPaid());
                    temp.setTime(ds.getValue(Patient.class).getTime());
                    temp.setWard(ds.getValue(Patient.class).getWard());
                    temp.setWardNum(ds.getValue(Patient.class).getWardNum());
                    if(temp.getId().contentEquals(UID)) {
                        name=temp.getName();
                        uid=temp.getId();
                        time=temp.getTime();
                        ward=temp.getWard();
                        wardNum=temp.getWardNum();
                        dismissTime=temp.getDismissTime();
                        billTotal=temp.getBillTotal();
                        paid=temp.getPaid();
                        rName.setText("Name: " + temp.getName());
                        if(!temp.getDismissTime().contentEquals("null")){
                            rStatus.setText("Patient discharged on: " + temp.getDismissTime());
                        }
                        else{
                            rStatus.setText("Patient is in: " + temp.getWard() + " - " +
                                temp.getWardNum());
                        }
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void GenerateReport(View view)  {
        String data =
                "HM HOSPITALS GURUGRAM\n"+
                "Name: "+name+"\n"+
                        "Hospital id: "+uid+"\n"+
                        "Admit date and time: "+time+"\n"+
                        "Admit ward: "+ward+"\n"+
                        "Admit ward number: "+wardNum+"\n"+
                        "Discharge time: "+dismissTime+"\n"+
                        "Bill total: "+billTotal+"\n"+
                        "Bill paid: "+paid+"\n"
                ;

        data+="\n\nBills Summary"+"\n";
        getBill(data);
    }

    public void getBill(final String dataa){
        mDF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String data = "";
                for (DataSnapshot ds : dataSnapshot.child("patient").child(UID).child("Bills").getChildren()) {
                    Bills temp = new Bills();
                    temp.setName(ds.getValue(Bills.class).getName());
                    temp.setAmount(ds.getValue(Bills.class).getAmount());
                    temp.setPaid(ds.getValue(Bills.class).getPaid());
                    data+=temp.getName()+"  "+temp.getAmount()+"  "+temp.getPaid()+"\n";
                }
                Log.d("asd",dataa+data);
                try {
                    File root = new File(Environment.getExternalStorageDirectory(), "Notes");
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File gpxfile = new File(root, name+".txt");
                    FileWriter writer = new FileWriter(gpxfile);
                    writer.append(dataa+data);
                    writer.flush();
                    writer.close();
                    Toast.makeText(ReportGenerationActivity.this, "Saved "+gpxfile, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ReportGenerationActivity.this, ReportActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
