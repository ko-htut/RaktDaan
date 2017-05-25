package com.cdev.raktdaan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.cdev.raktdaan.nonactivity.UserDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginDetailActivity extends AppCompatActivity {

    public static final int GO_LOGIN_TO_HOME = 4;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Toolbar toolbar;
    private String[] bloodGroups;
    private String[] rhFactor;
    private String[] genders;
    private String EmailUser;

    //Backend
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private Spinner spinnerBloodGroup;
    private Spinner spinnerRhFactor;
    private Spinner spinnerGender;
    private EditText etDOB;
    private EditText etMobileNumber;
    private EditText etAddress;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_detail);

        //BackEnd
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();



        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        EmailUser = user.getEmail().replace(".","");
        toolbar = (Toolbar) findViewById(R.id.toolbarLoginDetailActivity);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle(user.getEmail());
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
                setResult(1);
                finish();
            }
        });

        etDOB = (EditText) findViewById(R.id.etDOB);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etAddress = (EditText) findViewById(R.id.etAddress);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mFirebaseAuth.signOut();
            setResult(1);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onProceed(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loginStatus", true);
        editor.commit();

        //BE
        UserDetail newUser = new UserDetail(spinnerBloodGroup.getSelectedItem().toString()+spinnerRhFactor.getSelectedItem().toString(),
                spinnerGender.getSelectedItem().toString(),etDOB.getText().toString().trim(),
                etMobileNumber.getText().toString().trim(),etAddress.getText().toString().trim());
        mDatabaseReference.child("users").child(EmailUser).child("Details").push().setValue(newUser);
        mDatabaseReference.child("signedInUsers").child(EmailUser).push().setValue("1");
        Intent intent = new Intent(LoginDetailActivity.this, HomeActivity.class);
        startActivityForResult(intent, GO_LOGIN_TO_HOME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GO_LOGIN_TO_HOME) {
            setResult(resultCode);
            finish();
        }
    }
}
