package com.abhijeet.patientbillingsoftware.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.abhijeet.patientbillingsoftware.R;
import com.abhijeet.patientbillingsoftware.Util.BottomNavigationViewHelper;
import com.abhijeet.patientbillingsoftware.Util.ArrayListAdapter;
import com.abhijeet.patientbillingsoftware.Util.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/**
 * Created by abhij on 19-03-2018.
 */

public class ReportActivity extends AppCompatActivity {

    private static final String TAG = "ReportActivity";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = ReportActivity.this;

    private ListView listView;
    private ArrayList<Patient> dataList;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Bundle extras = getIntent().getExtras();
        type = extras.getString("type");

        setupBottomNavigationView();

        listView = findViewById(R.id.list_view_report);
        makeList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ReportActivity.this, ReportGenerationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", dataList.get(i).getId());
                bundle.putString("type", type);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    public void makeList() {
        dataList = new ArrayList<>();
        FirebaseDatabase mFD = FirebaseDatabase.getInstance();
        DatabaseReference mDF = mFD.getReference();
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
                    dataList.add(temp);
                }
                ArrayListAdapter adapter = new ArrayListAdapter(ReportActivity.this, dataList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Setup for Bottom Navigation View Setup
     */
    public void setupBottomNavigationView(){
        Log.d(TAG,"Setup for Bottom Navigation");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottonNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx, type);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
