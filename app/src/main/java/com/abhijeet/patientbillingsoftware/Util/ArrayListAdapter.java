package com.abhijeet.patientbillingsoftware.Util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhijeet.patientbillingsoftware.Activities.PatientActivity;
import com.abhijeet.patientbillingsoftware.R;
import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by abhij on 19-03-2018.
 */

public class ArrayListAdapter extends ArrayAdapter<Patient> {
    Context context;
    public ArrayListAdapter(Context context, ArrayList<Patient> data) {
        super(context, 0, data);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Patient user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent,
                    false);
        }
        TextView lvName =  convertView.findViewById(R.id.patient_name);
        TextView lvStatus =  convertView.findViewById(R.id.patient_status);
        ImageView lvImage = convertView.findViewById(R.id.textPatient);

        if(context instanceof PatientActivity) {
            lvName.setText(user.name);
            lvStatus.setText(user.ward.substring(0, 1).toUpperCase() + user.ward.substring(1) +
                    " - " + user.wardNum);
        }
        else{
            lvName.setText(user.name);
            if(user.getDismissTime().contentEquals("null")){
                lvStatus.setText("Not Discharged");
            }
            else
                lvStatus.setText("Discharged on "+user.getDismissTime().substring(0,10));
        }

        Random random = new Random();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(user.name.substring(0,1).toUpperCase(),
                        Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        lvImage.setImageDrawable(drawable);
        return convertView;
    }
}
