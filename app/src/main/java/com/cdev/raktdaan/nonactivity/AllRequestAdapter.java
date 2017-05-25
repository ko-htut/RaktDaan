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

import com.cdev.raktdaan.Fragments.ArrayListDetail;
import com.cdev.raktdaan.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Created by ash on 24/05/17.
 */

public class AllRequestAdapter extends ArrayAdapter<RequestDetail> {

    private String myEmail;

    public AllRequestAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<RequestDetail> objects) {
        super(context, resource, objects);
        myEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.requests_card,parent,false);
        }
        TextView bloodGroup = (TextView)convertView.findViewById(R.id.tvRC_bloodGroup);
        TextView name = (TextView)convertView.findViewById(R.id.tvRC_name);
        TextView time = (TextView)convertView.findViewById(R.id.tvRC_validTill);
        TextView age = (TextView)convertView.findViewById(R.id.tvRC_age);
        TextView accepted = (TextView) convertView.findViewById(R.id.tvRC_accepted);

        RequestDetail detail = getItem(position);
        bloodGroup.setText(detail.getBloodGroup());
        name.setText(detail.getName());
        time.setText("Valid Till : "+detail.getTime());
        age.setText("Age : "+detail.getAge());

        boolean foundAccepted = false;

        for(ArrayListDetail arrayListDetail:detail.getAccepted()){
            if(arrayListDetail.getEmail().equals(myEmail)){
                foundAccepted = true;
                accepted.setVisibility(View.VISIBLE);
                break;
            }
        }

        return convertView;

    }
}
