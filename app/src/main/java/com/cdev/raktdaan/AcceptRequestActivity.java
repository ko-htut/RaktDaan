package com.cdev.raktdaan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

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

    private UserDetail myDetail;
    private String myEmail;

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
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users").child(myEmail).child("Details");


        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren())
                    myDetail = child.getValue(UserDetail.class);
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        intent = getIntent();
        userToAccept = new RequestDetail(intent.getStringExtra("name"),intent.getStringExtra("age")
        ,intent.getStringExtra("gender"),intent.getStringExtra("bg"),intent.getStringExtra("bu"),
                intent.getStringExtra("urgency"),intent.getStringExtra("time"),intent.getStringExtra("email")
        ,intent.getStringExtra("key"),intent.getStringExtra("accepted"));

        tvBloodGroup.setText(userToAccept.getBloodGroup());
        tvName.setText(userToAccept.getName());
        tvBloodUnits.setText("Blood units : "+userToAccept.getBloodUnits());
        tvTime.setText("Valid till : "+userToAccept.getTime());
        tvGender.setText("Gender : "+userToAccept.getGender());
        tvAge.setText("Age : "+ userToAccept.getAge());


    }
}
