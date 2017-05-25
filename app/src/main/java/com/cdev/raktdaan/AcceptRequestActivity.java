package com.cdev.raktdaan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cdev.raktdaan.Fragments.ArrayListDetail;
import com.cdev.raktdaan.nonactivity.RequestDetail;
import com.cdev.raktdaan.nonactivity.UserDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AcceptRequestActivity extends AppCompatActivity {

    private RequestDetail userToAccept;
    private Intent intent;
    private TextView tvBloodGroup, tvName, tvBloodUnits, tvTime, tvGender, tvAge;
    private Button buttonAccept;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference databaseReference;

    private UserDetail myDetail;
    private String myEmail;
    private RequestDetail actualDetail;
    private String childOfChild;
    private String myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_request);

        tvBloodGroup = (TextView) findViewById(R.id.ac_bg);
        tvName = (TextView) findViewById(R.id.ac_name);
        tvBloodUnits = (TextView) findViewById(R.id.ac_bloodUnits);
        tvTime = (TextView) findViewById(R.id.ac_time);
        tvGender = (TextView) findViewById(R.id.ac_gender);
        tvAge = (TextView) findViewById(R.id.ac_age);
        buttonAccept = (Button) findViewById(R.id.ac_buttonAccept);

        mFirebaseAuth = FirebaseAuth.getInstance();
        myEmail = mFirebaseAuth.getCurrentUser().getEmail().replace(".","");
        myName = mFirebaseAuth.getCurrentUser().getDisplayName();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users").child(myEmail).child("Details");

        buttonAccept.setEnabled(false);
        buttonAccept.setBackgroundColor(Color.parseColor("#546E7A"));

        intent = getIntent();
        userToAccept = new RequestDetail(intent.getStringExtra("name"),intent.getStringExtra("age")
        ,intent.getStringExtra("gender"),intent.getStringExtra("bg"),intent.getStringExtra("bu"),
                intent.getStringExtra("urgency"),intent.getStringExtra("time"),intent.getStringExtra("email")
        ,intent.getStringExtra("key"));

        tvBloodGroup.setText(userToAccept.getBloodGroup());
        tvName.setText(userToAccept.getName());
        tvBloodUnits.setText("Blood units : "+userToAccept.getBloodUnits());
        tvTime.setText("Valid till : "+userToAccept.getTime());
        tvGender.setText("Gender : "+userToAccept.getGender());
        tvAge.setText("Age : "+ userToAccept.getAge());

        databaseReference = mFirebaseDatabase.getReference().child("Requests").
                child(userToAccept.getEmail()).child(userToAccept.getKey());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    childOfChild = data.getKey();
                    userToAccept.setAccepted(data.getValue(RequestDetail.class).getAccepted());

                    boolean alreadyAccepted = false;
                    for(ArrayListDetail checker:userToAccept.getAccepted()){
                        if(checker.getEmail().equals(myEmail)){
                            alreadyAccepted = true;
                            break;
                        }
                    }
                    if(!alreadyAccepted) {
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren())
                                    myDetail = child.getValue(UserDetail.class);
                                buttonAccept.setEnabled(true);
                                buttonAccept.setBackgroundColor(Color.parseColor("#009688"));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else{
                        buttonAccept.setText("REQUEST ALREADY ACCEPTED");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userToAccept.getAccepted().add(new ArrayListDetail(myEmail,myName,myDetail.getMobileNumber()));
                databaseReference.child(childOfChild).setValue(userToAccept);
                finish();
            }
        });
    }
}
