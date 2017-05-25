package com.cdev.raktdaan.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.cdev.raktdaan.R;
import com.cdev.raktdaan.nonactivity.AllRequestAdapter;
import com.cdev.raktdaan.nonactivity.RequestDetail;
import com.cdev.raktdaan.nonactivity.UserDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.facebook.GraphRequest.TAG;


public class RequestsFragment extends Fragment {

    AllRequestAdapter allRequestAdapter;
    private ProgressBar mProgressBar;
    private RelativeLayout layoutGayab;
    private TextView noPendingRequests;
    private ArrayList<RequestDetail> arr;
    private View view;
    private ListView listView;
    private ArrayList<RequestDetail> backupList;
    private String Email;
    private UserDetail myDetail;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference getmDatabaseReference2;
    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_requests, container, false);
        listView = (ListView) view.findViewById(R.id.requestListview);

        backupList = new ArrayList<>();

        Email = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Requests");
        getmDatabaseReference2 = FirebaseDatabase.getInstance().getReference().child("users").child(Email).child("Details");

        getmDatabaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot user: dataSnapshot.getChildren())
                    myDetail = user.getValue(UserDetail.class);
                Log.d(TAG, "onDataChange: "+myDetail.getMobileNumber()+
                " "+myDetail.getBloodGroup()+" "+myDetail.getDateOfBirth()+" "+myDetail.getGender()
                +" "+myDetail.getLocalAddress());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_barRequests);
        layoutGayab = (RelativeLayout) view.findViewById(R.id.relativeGayabRequests);
        noPendingRequests = (TextView) view.findViewById(R.id.noPendingrequests);


        LayoutInflater myinflater = getActivity().getLayoutInflater();
        ViewGroup myHeader = (ViewGroup)myinflater.inflate(R.layout.listview_header, listView, false);
        Switch requestsSwitch = (Switch) myHeader.findViewById(R.id.requestsSwitch);

        listView.addHeaderView(myHeader, null, false);
        arr = new ArrayList<RequestDetail>();
        allRequestAdapter = new AllRequestAdapter(getContext(), R.layout.requests_card, arr);

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot user:dataSnapshot.getChildren()) {
                    for(DataSnapshot child:user.getChildren()) {
                        allRequestAdapter.add(child.getValue(RequestDetail.class));
                        if(child.getValue(RequestDetail.class).getBloodGroup().equals(myDetail.getBloodGroup()))
                            backupList.add(child.getValue(RequestDetail.class));
                    }
                }
                Log.d(TAG, "onDataChange: size is "+backupList.size()+" "+ arr.size()+" "+myDetail.getBloodGroup());
                if(arr.size()==0){
                    mProgressBar.setVisibility(View.GONE);
                    noPendingRequests.setVisibility(View.VISIBLE);
                }
                else{
                    layoutGayab.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView.setAdapter(allRequestAdapter);


        requestsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });


        return view;
    }

}
