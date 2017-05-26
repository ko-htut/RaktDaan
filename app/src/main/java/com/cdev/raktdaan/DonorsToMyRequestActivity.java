package com.cdev.raktdaan;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cdev.raktdaan.Fragments.ArrayListDetail;
import com.cdev.raktdaan.Fragments.MyRequestsFragment;
import com.cdev.raktdaan.nonactivity.DonorsAdapter;
import com.cdev.raktdaan.nonactivity.RequestDetail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DonorsToMyRequestActivity extends AppCompatActivity {

    private ListView listViewDonors;
    private RequestDetail requestDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors_to_my_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Request Detail");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setResult(0);

        requestDetail = MyRequestsFragment.list.get(getIntent().getIntExtra("position",0));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(DonorsToMyRequestActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
                } else {
                    builder = new AlertDialog.Builder(DonorsToMyRequestActivity.this);
                }
                builder.setTitle("Accept Request")
                        .setMessage("Are you sure you want to delete this request?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests").child(requestDetail.getEmail()).child(requestDetail.getKey());

                                databaseReference.removeValue();

                                setResult(1);
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .show();
            }
        });

        listViewDonors = (ListView) findViewById(R.id.listview_donors);
        LayoutInflater myinflater = getLayoutInflater();
        ViewGroup myHeader = (ViewGroup)myinflater.inflate(R.layout.accepted_requests_header, listViewDonors, false);
        listViewDonors.addHeaderView(myHeader, null, false);

        ArrayList<ArrayListDetail> toSendArraylist =  requestDetail.getAccepted();
        DonorsAdapter donorsAdapter = new DonorsAdapter(this,R.layout.donor_card,new ArrayList<ArrayListDetail>(toSendArraylist.subList(1,toSendArraylist.size())));
        listViewDonors.setAdapter(donorsAdapter);
    }


}
