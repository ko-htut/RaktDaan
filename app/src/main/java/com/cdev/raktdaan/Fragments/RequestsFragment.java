package com.cdev.raktdaan.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.cdev.raktdaan.AcceptRequestActivity;
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
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
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot user:dataSnapshot.getChildren()) {
                            for(DataSnapshot child:user.getChildren()) {
                                for(DataSnapshot next:child.getChildren()) {
                                    if (next.getValue(RequestDetail.class).getBloodGroup().equals(myDetail.getBloodGroup()))
                                        allRequestAdapter.add(next.getValue(RequestDetail.class));
                                    backupList.add(next.getValue(RequestDetail.class));
                                }
                            }
                        }
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




        listView.setAdapter(allRequestAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RequestDetail toSend = (RequestDetail) listView.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), AcceptRequestActivity.class);
                intent.putExtra("name",toSend.getName());
                intent.putExtra("age",toSend.getAge());
                intent.putExtra("bg",toSend.getBloodGroup());
                intent.putExtra("bu",toSend.getBloodUnits());
                intent.putExtra("gender",toSend.getGender());
                intent.putExtra("time",toSend.getTime());
                intent.putExtra("email",toSend.getEmail());
                intent.putExtra("key",toSend.getKey());
                intent.putExtra("accepted",toSend.getAccepted());
                intent.putExtra("urgency",toSend.getUrgency());
                startActivity(intent);
            }
        });

        requestsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    allRequestAdapter = new AllRequestAdapter(getContext(),R.layout.requests_card,backupList);
                    listView.setAdapter(allRequestAdapter);
                } else {
                    allRequestAdapter = new AllRequestAdapter(getContext(), R.layout.requests_card, arr);
                    listView.setAdapter(allRequestAdapter);
                }
            }
        });


        return view;
    }

}
