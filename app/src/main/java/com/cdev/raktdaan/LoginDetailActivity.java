package com.cdev.raktdaan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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

    private String EmailUser;
    private String gender = "Male";

    //Backend
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private TextView bloodGroup;
    private RadioGroup mRadioGroup;
    private RadioButton radioButton1, radioButton2;
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

        bloodGroup = (TextView) findViewById(R.id.textView_bloodGroup);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_gender);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton_male);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton_female);
        etDOB = (EditText) findViewById(R.id.etDOB);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etAddress = (EditText) findViewById(R.id.etAddress);

        bloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBloodGroup();
            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radioButton_male) {
                    gender = "Male";
                } else {
                    gender = "Female";
                }
            }
        });

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

//        //BE
//        UserDetail newUser = new UserDetail(spinnerBloodGroup.getSelectedItem().toString()+spinnerRhFactor.getSelectedItem().toString(),
//                spinnerGender.getSelectedItem().toString(),etDOB.getText().toString().trim(),
//                etMobileNumber.getText().toString().trim(),etAddress.getText().toString().trim());
//        mDatabaseReference.child("users").child(EmailUser).child("Details").push().setValue(newUser);
//        mDatabaseReference.child("signedInUsers").child(EmailUser).push().setValue("1");
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

    public void showBloodGroup() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.blood_group_dialog, null);
        builder.setView(view);
        builder.setTitle("Blood Group");
        final android.support.v7.app.AlertDialog dialog = builder.create();

        TextView b1 = (TextView) view.findViewById(R.id.button1);
        TextView b2 = (TextView) view.findViewById(R.id.button2);

        final NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker1);
        np.setMinValue(0);
        np.setMaxValue(3);
        final String[] bgArr = new String[]{"A", "B", "O", "AB"};
        np.setDisplayedValues(bgArr);
        np.setWrapSelectorWheel(false);
        final NumberPicker np2 = (NumberPicker) view.findViewById(R.id.numberPicker2);
        np2.setMaxValue(1);
        np2.setMinValue(0);
        final String[] rfArr = new String[]{"+", "-"};
        np2.setDisplayedValues(rfArr);
        np2.setWrapSelectorWheel(false);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bloodGroup.setText(bgArr[np.getValue()] + rfArr[np2.getValue()]);
                dialog.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
