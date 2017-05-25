package com.cdev.raktdaan.nonactivity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cdev.raktdaan.R;

import java.util.ArrayList;

public class MyRequestAdapter extends ArrayAdapter<RequestDetail> {

    public MyRequestAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<RequestDetail> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.my_requests_card,parent,false);
        }
        TextView bloodGroup = (TextView)convertView.findViewById(R.id.CardBG);
        TextView name = (TextView)convertView.findViewById(R.id.CardName);
        TextView bloodUnits = (TextView)convertView.findViewById(R.id.CardUnits);
        TextView time = (TextView)convertView.findViewById(R.id.CardTime);

        RequestDetail detail = getItem(position);
        bloodGroup.setText(detail.getBloodGroup());
        name.setText(detail.getName());
        bloodUnits.setText("Blood Units : "+detail.getBloodUnits());
        time.setText("Valid Till : "+detail.getTime());


        return convertView;

    }

}
