package com.abhijeet.patientbillingsoftware.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abhijeet.patientbillingsoftware.Login.Login;
import com.abhijeet.patientbillingsoftware.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by abhij on 19-03-2018.
 */

public class ProfileFragment extends Fragment {

    private TextView empName;
    private EditText email;
    private Button signOut, resetPassword;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        empName = view.findViewById(R.id.emp_name);
        empName.setText(mAuth.getCurrentUser().getEmail().split("@")[0]);
        email = view .findViewById(R.id.input_email_reset);
        signOut = view.findViewById(R.id.sign_out);
        resetPassword = view.findViewById(R.id.reset_password);
        init();

        return view;
    }

    public  void init(){
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent i = new Intent(getContext(), Login.class);
                startActivity(i);
            }
        });
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(),"Password Reset Email sent",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getContext(),"Wrong Email",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
    }
}

