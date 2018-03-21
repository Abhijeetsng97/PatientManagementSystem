package com.abhijeet.patientbillingsoftware.Util;

import android.content.Context;
import android.graphics.Color;
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
 * Created by abhij on 20-03-2018.
 */

public class ListVerifyAdapter extends ArrayAdapter<Users> {
    Context context;
    public ListVerifyAdapter(Context context, ArrayList<Users> data) {
        super(context, 0, data);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Users user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_verify_item, parent,
                    false);
        }
        TextView lvName =  convertView.findViewById(R.id.emp_name);
        ImageView lvImage = convertView.findViewById(R.id.textEmp);

        lvName.setText(user.name);

        Random random = new Random();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(user.name.substring(0,1).toUpperCase(),
                        Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        lvImage.setImageDrawable(drawable);
        return convertView;
    }
}
