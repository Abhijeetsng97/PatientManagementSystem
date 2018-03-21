package com.abhijeet.patientbillingsoftware.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abhijeet.patientbillingsoftware.R;
import com.abhijeet.patientbillingsoftware.Util.Bills;
import com.abhijeet.patientbillingsoftware.Util.Patient;
import com.abhijeet.patientbillingsoftware.Util.Ward;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

/**
 * Created by abhij on 20-03-2018.
 */

public class BillingActivity extends AppCompatActivity{

    private TextView mName, mWard, mMoney;
    private RadioGroup radioGroup;
    private DatabaseReference mDF;
    private FirebaseDatabase mFD;
    private String UID, type;
    private int moneyremaining = -2;
    private String name,uid,time,ward,wardNum,dismissTime,billTotal,paid;
    private Vector<String> rbVector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        Toolbar Toolbar = findViewById(R.id.toolbarbilling);
        setSupportActionBar(Toolbar);
        Toolbar.setTitle("Billing Activities");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            UID = extras.getString("id");
            type = extras.getString("type");
        }

        mName = findViewById(R.id.bill_name);
        mWard = findViewById(R.id.bill_ward);
        mMoney = findViewById(R.id.bill_money);
        radioGroup = findViewById(R.id.bill_group);
        mFD = FirebaseDatabase.getInstance();
        mDF = mFD.getReference();
        rbVector = new Vector<>();

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
                        mName.setText("Name: " + temp.getName());
                        mWard.setText("Ward: " + temp.getWard().substring(0, 1).toUpperCase() +
                                temp.getWard().substring(1) + " - " + temp.getWardNum());
                        if(Integer.parseInt(temp.getBillTotal()) - Integer.parseInt(temp.getPaid())
                                == 0){
                            mMoney.setText("Nothing to pay");
                            moneyremaining = 0;
                        }
                        else{
                            mMoney.setText(
                                    "Amount to Pay: " +
                                    Integer.toString(Integer.parseInt(temp.getBillTotal())
                                            - Integer.parseInt(temp.getPaid()))
                            );
                            moneyremaining = Integer.parseInt(temp.getBillTotal())
                                    - Integer.parseInt(temp.getPaid());
                        }
                        break;
                    }
                }

                RadioGroup ll = new RadioGroup(getApplicationContext());
                ll.setOrientation(LinearLayout.VERTICAL);
                int i=1;
                for (DataSnapshot ds : dataSnapshot.child("patient").child(UID).child("Bills").getChildren()) {
                    Bills temp = new Bills();
                    temp.setName(ds.getValue(Bills.class).getName());
                    temp.setAmount(ds.getValue(Bills.class).getAmount());
                    temp.setPaid(ds.getValue(Bills.class).getPaid());
                    RadioButton rdbtn = new RadioButton(getApplicationContext());
                    rdbtn.setId(i);
                    final String buttonText = temp.getName().substring(0,1).toUpperCase()+
                            temp.getName().substring(1)+ " "+
                            temp.getAmount() + " " +
                            temp.getPaid().substring(0,1).toUpperCase();
                    rdbtn.setText(buttonText);
                    ll.addView(rdbtn);
                    final String status = temp.getPaid();
                    final  String index = Integer.toString(i);
                    rdbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status.contentEquals("no")) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BillingActivity.this);
                                alertDialogBuilder
                                        .setCancelable(false)
                                        .setMessage("Do you really want to pay for " + buttonText)
                                        .setPositiveButton("Pay",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, final int id) {
                                                        PayBill(buttonText, index);
                                                        Toast.makeText(BillingActivity.this, "Money being" +
                                                                " paid", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(BillingActivity.this,
                                                                BillingActivity.class);Bundle bundle = new Bundle();
                                                        bundle.putString("type", type);
                                                        bundle.putString("id", UID);
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                        finish();

                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                            else{
                                Toast.makeText(BillingActivity.this,"It is already paid",Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                    rbVector.add(buttonText);
                    i++;
                }
                radioGroup.addView(ll);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        radioGroup.clearCheck();
    }

    public void RaiseBill(View view){
        LayoutInflater li = LayoutInflater.from(BillingActivity.this);
        View promptsView = li.inflate(R.layout.raise_bill, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BillingActivity.this);
        alertDialogBuilder.setView(promptsView);
        final EditText billName = (EditText) promptsView
                .findViewById(R.id.billName);
        final EditText billCost = (EditText) promptsView
                .findViewById(R.id.billCost);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, final int id) {
                                if(billCost.getText().toString().contentEquals("") ||
                                        billName.getText().toString().contentEquals("")){
                                    Toast.makeText(BillingActivity.this,"Enter Details!!",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    final String n = billName.getText().toString();
                                    final String c = billCost.getText().toString();
                                    mDF.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            int num = (int)dataSnapshot.child("patient").child(uid)
                                                    .child("Bills").getChildrenCount();
                                            Bills b = new Bills(n,c,"no");
                                            mDF.child("patient").child(uid)
                                                    .child("Bills").child(Integer.toString(num+1))
                                                    .setValue(b);
                                            Patient user = new Patient(name,uid,time,ward,wardNum,dismissTime
                                                    ,Integer.toString(Integer.parseInt(billTotal)+Integer.parseInt(c))
                                                    ,paid);
                                            Map<String, Object> postValues = user.toMap();
                                            mDF.child("patient").child(uid).updateChildren(postValues);
                                            generateLayout();
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void PayBill(String rbText, String index){
        String arr[] = rbText.split(" ");
        Patient user = new Patient(name, uid, time, ward, wardNum, dismissTime, billTotal,
                Integer.toString(Integer.parseInt(paid) + Integer.parseInt(arr[1])));
        Map<String, Object> postValues = user.toMap();
        mDF.child("patient").child(uid).updateChildren(postValues);

        Bills bill = new Bills(arr[0], arr[1], "yes");
        Map<String, Object> postValues1 = bill.toMap();
        mDF.child("patient").child(uid).child("Bills").child(index).updateChildren(postValues1);
    }

    public void DischargePatient(View view){
        if(moneyremaining == 0 && dismissTime.contentEquals("null")){
            String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                    .format(new Date());
            Patient user = new Patient(name,uid,time,ward,wardNum,currentDateandTime,billTotal,paid);
            Map<String, Object> postValues = user.toMap();
            mDF.child("patient").child(uid).updateChildren(postValues);
            if(ward.contentEquals("general")){
                Ward ward1 = new Ward(wardNum,"empty");
                Map<String, Object> postValues1 = ward1.toMap();
                mDF.child("Ward").child("gen").child(wardNum).updateChildren(postValues1);
            }
            else if(ward.contentEquals("icu")){
                Ward ward1 = new Ward(wardNum,"empty");
                Map<String, Object> postValues1 = ward1.toMap();
                mDF.child("Ward").child("icu").child(wardNum).updateChildren(postValues1);
            }
            Toast.makeText(BillingActivity.this, "Patient be discharged!!",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BillingActivity.this, PatientActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(BillingActivity.this, "Patient can't be discharged bills has to " +
                    "be cleared", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BillingActivity.this, PatientActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
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
