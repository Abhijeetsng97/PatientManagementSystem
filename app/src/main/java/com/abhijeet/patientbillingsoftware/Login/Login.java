package com.abhijeet.patientbillingsoftware.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhijeet.patientbillingsoftware.Activities.HomeActivity;
import com.abhijeet.patientbillingsoftware.R;
import com.abhijeet.patientbillingsoftware.Util.FirebaseMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by abhij on 17-03-2018.
 */

public class Login extends AppCompatActivity {
    private static final String TAG = ".Login";
    private EditText email,password;
    private Button loginBtn;
    private TextView signUp;
    private Context mContext = Login.this;
    private ProgressBar pBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(mContext);
        init();
        setupFirebaseAuth();
    }

    public void init(){
        email = findViewById(R.id.input_email);
        email.setText("abhijeet.singh1997@gmail.com");
        password = findViewById(R.id.input_password);
        password.setText("qwerty@123");
        loginBtn = findViewById(R.id.btn_login);
        signUp = findViewById(R.id.link_signup);
        pBar = findViewById(R.id.progressBar);
        pBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
    }

    public void SignUp(View view){
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
        finish();
    }

    public boolean isStringNull(String s) {
        if(s.contentEquals(""))
            return true;
        return false;
    }

    public  void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String type = firebaseMethods.getType(mAuth.getCurrentUser().getUid()
                                    ,dataSnapshot);
                            Log.d(TAG, type);
                            if(type.contentEquals("notdef")){
                                Toast.makeText(mContext,"<- Some Error Contact Admin ->",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else if(type.contentEquals("admin")){
                                Intent intent = new Intent(Login.this,
                                        HomeActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("type", "admin");
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }
                            else if(type.contentEquals("emp")){
                                Boolean auth = firebaseMethods.getAuthType(mAuth.getCurrentUser().getUid()
                                        ,dataSnapshot);
                                if(auth){
                                    Intent intent = new Intent(Login.this,
                                            HomeActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("type", "emp");
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(mContext,"Verification Not Done",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void Login(View view){
        String in_email = email.getText().toString();
        String in_password = password.getText().toString();
        if(isStringNull(in_email) || isStringNull(in_password)){
            Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
        }else{
            pBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(in_email, in_password)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            if (task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:success", task.getException());
                                Toast.makeText(mContext, getString(R.string.auth_success), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(mContext,getString(R.string.auth_failed),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
        pBar.setVisibility(View.GONE);
        //mAuth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

