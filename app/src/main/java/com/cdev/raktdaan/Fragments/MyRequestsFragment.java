package com.cdev.raktdaan.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdev.raktdaan.DonorsToMyRequestActivity;
import com.cdev.raktdaan.R;
import com.cdev.raktdaan.nonactivity.MyRequestAdapter;
import com.cdev.raktdaan.nonactivity.RequestDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyRequestsFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private View view;
    private ListView listView;
    private MyRequestAdapter myRequestAdapter;
    static  public  ArrayList<RequestDetail> list;
    private String Email;

    private RelativeLayout layoutGayab;
    private TextView noRequestFound;
    private ProgressBar myRequestProgressBar;

    public MyRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_requests, container, false);

        listView = (ListView) view.findViewById(R.id.myRequestListView);
        layoutGayab = (RelativeLayout)view.findViewById(R.id.relativeGayab);
        noRequestFound = (TextView)view.findViewById(R.id.noRequestFound);
        myRequestProgressBar = (ProgressBar)view.findViewById(R.id.myRequestProgressBar);


        mFirebaseAuth = FirebaseAuth.getInstance();
        Email = mFirebaseAuth.getCurrentUser().getEmail().replace(".","");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Requests").child(Email);

        list = new ArrayList<>();
        myRequestAdapter = new MyRequestAdapter(getContext(),R.layout.my_requests_card,list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DonorsToMyRequestActivity.class);
                intent.putExtra("position", position);
                startActivityForResult(intent, 501);
            }
        });

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot user:dataSnapshot.getChildren()) {
                    for(DataSnapshot next:user.getChildren())
                        myRequestAdapter.add(next.getValue(RequestDetail.class));

                }
                if(list.size()==0){
                    myRequestProgressBar.setVisibility(View.GONE);
                    noRequestFound.setVisibility(View.VISIBLE);
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
        listView.setAdapter(myRequestAdapter);


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==501){
                final ArrayList<RequestDetail> nayaArrayList = new ArrayList<>();
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot user:dataSnapshot.getChildren()) {
                            for(DataSnapshot next:user.getChildren())
                                nayaArrayList.add(next.getValue(RequestDetail.class));

                        }
                        if(nayaArrayList.size()==0){
                            myRequestProgressBar.setVisibility(View.GONE);
                            listView.setVisibility(View.GONE);
                            layoutGayab.setVisibility(View.VISIBLE);
                            noRequestFound.setVisibility(View.VISIBLE);
                        }
                        else{
                            layoutGayab.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                        }

                        list = nayaArrayList;
                        myRequestAdapter = new MyRequestAdapter(getContext(),R.layout.my_requests_card,nayaArrayList);

                        listView.setAdapter(myRequestAdapter);
                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
    }
}
