package com.abhijeet.patientbillingsoftware.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.abhijeet.patientbillingsoftware.Fragments.OverviewFragment;
import com.abhijeet.patientbillingsoftware.Fragments.ProfileFragment;
import com.abhijeet.patientbillingsoftware.Fragments.VerifyFragment;
import com.abhijeet.patientbillingsoftware.R;
import com.abhijeet.patientbillingsoftware.Util.BottomNavigationViewHelper;
import com.abhijeet.patientbillingsoftware.Util.SectionPagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by abhij on 19-03-2018.
 */

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = HomeActivity.this;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle extras = getIntent().getExtras();
        type = extras.getString("type");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i = new Intent(HomeActivity.this, AddPatientActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            i.putExtras(bundle);
            startActivity(i);
            finish();
            }
        });
        setupBottomNavigationView();
        setupViewPager();
    }

    /**
     * responsible for adding 3 tabs in home uptop
     */
    public void setupViewPager(){
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OverviewFragment());
        adapter.addFragment(new ProfileFragment());
        if(type.contentEquals("admin")){
            adapter.addFragment(new VerifyFragment());
        }
        ViewPager viewPager = (ViewPager)findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_overview);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_profile);
        if(type.contentEquals("admin")){
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_verify);
        }
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
