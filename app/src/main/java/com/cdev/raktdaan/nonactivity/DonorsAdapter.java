package com.cdev.raktdaan.nonactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdev.raktdaan.Fragments.ArrayListDetail;
import com.cdev.raktdaan.R;

import java.util.ArrayList;

/**
 * Created by ash on 26/05/17.
 */

public class DonorsAdapter extends ArrayAdapter<ArrayListDetail> {

    public DonorsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<ArrayListDetail> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.donor_card,parent,false);
        }
        TextView donorName = (TextView) convertView.findViewById(R.id.donor_name);
        TextView donorContactNo = (TextView)convertView.findViewById(R.id.donor_contactNo);
        ImageView messageButton = (ImageView) convertView.findViewById(R.id.imageview_message);
        ImageView callButton = (ImageView) convertView.findViewById(R.id.imageViewCall);

        final ArrayListDetail detail = getItem(position);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+ detail.getMobileNumber()));
                getContext().startActivity(callIntent);
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                        + detail.getMobileNumber())));
            }
        });

        donorName.setText(detail.getName());
        donorContactNo.setText(detail.getMobileNumber());
        return convertView;

    }
}
