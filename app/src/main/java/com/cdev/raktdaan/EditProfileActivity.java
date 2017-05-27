package com.cdev.raktdaan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cdev.raktdaan.nonactivity.UserDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sanjay on 26/5/17.
 */

public class EditProfileActivity  extends AppCompatActivity{
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private String myEmail;
    private TextView tvBloodGroup;
    private RadioGroup radioGroup;
    private RadioButton rbMale, rbFemale;
    private EditText etDOB,etMobileNumber, etAddress;
    private UserDetail oldUserDetail;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbarLoginDetailActivity);
        toolbar.setTitle("Edit Profile");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myEmail = mFirebaseAuth.getCurrentUser().getEmail().replace(".", "");
        mDatabaseReference = mFirebaseDatabase.getReference().child("users").child(myEmail).child("Details");

        tvBloodGroup = (TextView) findViewById(R.id.textView_bloodGroup);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_gender);
        rbMale = (RadioButton) findViewById(R.id.radioButton_male);
        rbFemale = (RadioButton) findViewById(R.id.radioButton_female);
        etDOB = (EditText) findViewById(R.id.etDOB);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etAddress = (EditText) findViewById(R.id.etAddress);
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    oldUserDetail = child.getValue(UserDetail.class);
                }
                tvBloodGroup.setText(oldUserDetail.getBloodGroup());
                if (oldUserDetail.getGender().equals("Male")) {
                    rbMale.setChecked(true);
                    rbFemale.setChecked(false);
                } else {
                    rbMale.setChecked(false);
                    rbFemale.setChecked(true);
                }
                etDOB.setText(oldUserDetail.getDateOfBirth());
                etMobileNumber.setText(oldUserDetail.getMobileNumber());
                etAddress.setText(oldUserDetail.getLocalAddress());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
