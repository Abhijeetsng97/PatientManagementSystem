package com.abhijeet.patientbillingsoftware.Util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.abhijeet.patientbillingsoftware.Activities.HomeActivity;
import com.abhijeet.patientbillingsoftware.Activities.PatientActivity;
import com.abhijeet.patientbillingsoftware.Activities.ReportActivity;
import com.abhijeet.patientbillingsoftware.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by abhij on 19-03-2018.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";
    private static String type;
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx
            ,String tye){
        type = tye;
        Log.d(TAG,"bottom nav helper setup method");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.ic_home:
                        Intent i1 = new Intent(context, HomeActivity.class);//ACTIVITY_NUM = 0
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("type", type);
                        i1.putExtras(bundle1);
                        context.startActivity(i1);
                        break;
                    case R.id.ic_patient:
                        Intent i2 = new Intent(context, PatientActivity.class);//ACTIVITY_NUM = 1
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("type", type);
                        i2.putExtras(bundle2);;
                        context.startActivity(i2);
                        break;
                    case R.id.ic_report:
                        Intent i3 = new Intent(context, ReportActivity.class);//ACTIVITY_NUM = 2
                        Bundle bundle3 = new Bundle();
                        bundle3.putString("type", type);
                        i3.putExtras(bundle3);
                        context.startActivity(i3);
                        break;
                }
                return false;
            }
        });
    }
}
