package com.abhijeet.patientbillingsoftware.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.transition.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhijeet.patientbillingsoftware.R;
import com.abhijeet.patientbillingsoftware.Util.FirebaseMethods;
import com.abhijeet.patientbillingsoftware.Util.Users;
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

import org.w3c.dom.Text;

import static android.view.View.GONE;

/**
 * Created by abhij on 17-03-2018.
 */

public class Register extends AppCompatActivity {
    private static final String TAG = ".Register";
    private EditText memail,mpassword,musername;
    private TextView loginback;
    private Button registerBtn;
    private Context mContext = Register.this;
    private ProgressBar pBar;
    private FirebaseMethods firebaseMethods;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;
    private String append = "",username,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseMethods = new FirebaseMethods(mContext);

        initWidgets();
        setupFirebaseAuth();
    }

    public void initWidgets(){
        memail = findViewById(R.id.input_email);
        mpassword = findViewById(R.id.input_password);
        musername = findViewById(R.id.input_username);
        registerBtn = findViewById(R.id.btn_register);
        loginback = findViewById(R.id.link_login);
        pBar = findViewById(R.id.progressBar);
        pBar.setVisibility(View.GONE);
    }

    public void Loginback(View view){
        Intent i = new Intent(Register.this, Login.class);
        startActivity(i);
        finish();
    }

    private boolean checkInputs(String email, String username, String password){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(email.equals("") || username.equals("") || password.equals("")){
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void RegisterUser(View view){
        email = memail.getText().toString();
        username = musername.getText().toString();
        password = mpassword.getText().toString();

        if(checkInputs(email, username, password)){
            pBar.setVisibility(View.VISIBLE);
            firebaseMethods.registerNewUser(email, password, username);
        }
        pBar.setVisibility(GONE);
        mAuth.signOut();
    }

    public void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    userID = mAuth.getCurrentUser().getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //1st check: Make sure the username is not already in use
                            if(firebaseMethods.checkIfUsernameExists(username, dataSnapshot)){
                                append = myRef.push().getKey().substring(0,7);
                            }
                            username = username + append;

                            //add new user to the database
                            firebaseMethods.addNewUser("notdone",username,"emp",userID);
                            Toast.makeText(mContext, "Signup successful. Admin Verification " +
                                    "Left.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Register.this, Login.class);
                            startActivity(i);
                            finish();
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
