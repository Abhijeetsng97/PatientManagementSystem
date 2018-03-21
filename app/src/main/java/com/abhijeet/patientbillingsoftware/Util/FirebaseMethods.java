package com.abhijeet.patientbillingsoftware.Util;

/**
 * Created by abhij on 18-03-2018.
 */

import android.util.Log;

import com.abhijeet.patientbillingsoftware.R;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by abhij on 11-03-2018.
 */

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    private Context context;
    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    public FirebaseMethods(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public boolean checkIfUsernameExists(String usrname, DataSnapshot dataSnapshot){
        Users user = new Users();

        for (DataSnapshot ds: dataSnapshot.child("user").child(userID).getChildren()) {
            user.setName(ds.getValue(Users.class).getName());
            Log.d(TAG, "checkIfUsernameExists: username: " + user.getName());
            if (user.getName().equals(usrname)) {
                Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + user.getName());
                return true;
            }
        }
        return false;
    }

    public String getType(String UID, DataSnapshot dataSnapshot){
        Users user = new Users();
        int i = 0;
        for (DataSnapshot ds: dataSnapshot.child("user").getChildren()) {i++;
            user.setUid(ds.getValue(Users.class).getUid());
            user.setType(ds.getValue(Users.class).getType());
            if (user.getUid().equals(UID)) {
                return user.getType();
            }
        }
        return "notdef";
    }

    public Boolean getAuthType(String UID, DataSnapshot dataSnapshot){
        Users user = new Users();
        int i = 0;
        for (DataSnapshot ds: dataSnapshot.child("user").getChildren()) {i++;
            user.setUid(ds.getValue(Users.class).getUid());
            user.setAuth(ds.getValue(Users.class).getAuth());
            if (user.getUid().equals(UID) && user.getAuth().equals("done")) {
                return true;
            }
        }
        return false;
    }

    public void registerNewUser(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();

                        } else if (task.isSuccessful()) {
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }

                    }
                });
    }

    public void addNewUser(String auth, String name, String type, String uid){

        Users user = new Users(auth, name, type, uid);
        myRef.child("user")
                .child(userID)
                .setValue(user);
    }
}
