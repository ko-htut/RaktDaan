package com.cdev.raktdaan.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cdev.raktdaan.HomeActivity;
import com.cdev.raktdaan.R;
import com.cdev.raktdaan.nonactivity.RequestDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class MakeARequestFragment extends Fragment {
    private View view;
    private EditText name, age;
    private RadioGroup mRadioGroup;
    private RadioButton radioButton1,radioButton2;
    private TextView bloodGroup, bloodUnit, bloodUrgency;
    private Button button;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private boolean nameNotEmpty,ageNotEmpty;
    private String emailUser;

    private String radioText= "Male";

    public MakeARequestFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_make_arequest, container, false);


        nameNotEmpty = false;
        ageNotEmpty = false;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        button = (Button)view.findViewById(R.id.make_request_button);
        name = (EditText) view.findViewById(R.id.et_pt_name);
        age = (EditText) view.findViewById(R.id.et_pt_age);
        bloodGroup = (TextView) view.findViewById(R.id.tv_pt_bloodGroup);
        bloodUnit = (TextView) view.findViewById(R.id.tv_pt_bloodUnits);
        bloodUrgency = (TextView) view.findViewById(R.id.tv_pt_urgency);
        mRadioGroup = (RadioGroup)view.findViewById(R.id.rd_pt_sex);
        radioButton1 = (RadioButton)view.findViewById(R.id.rb1);
        radioButton2 = (RadioButton)view.findViewById(R.id.rb2);
        button.setEnabled(false);
        button.setBackgroundColor(Color.DKGRAY);


        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0)
                    nameNotEmpty = true;
                else if(s.length()==0) {
                    nameNotEmpty = false;
                    button.setEnabled(false);
                    button.setBackgroundColor(Color.DKGRAY);
                }
                if(nameNotEmpty && ageNotEmpty) {
                    button.setEnabled(true);
                    button.setBackgroundColor(Color.parseColor("#ef5350"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0)
                    ageNotEmpty = true;
                else if(s.length()==0) {
                    ageNotEmpty = false;
                    button.setEnabled(false);
                    button.setBackgroundColor(Color.DKGRAY);
                }
                if(nameNotEmpty && ageNotEmpty) {
                    button.setEnabled(true);
                    button.setBackgroundColor(Color.parseColor("#ef5350"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==R.id.rb1){
                    radioText = "Male";
                }
                else{
                    radioText = "Female";
                }
            }
        });


        bloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBloodGroup();
            }
        });
        bloodUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBloodUnit();
            }
        });
        bloodUrgency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBloodUrgency();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailUser = mFirebaseAuth.getCurrentUser().getEmail().replace(".","");
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                Date resultdate = new Date(1000*secondsToAdd(bloodUrgency.getText().toString())+System.currentTimeMillis());

                String keyTo = mDatabaseReference.child("Requests").child(emailUser).push().getKey();

                RequestDetail dataToSend = new RequestDetail(name.getText().toString().trim(),
                        age.getText().toString().trim(),radioText,bloodGroup.getText().toString(),
                        bloodUnit.getText().toString(),bloodUrgency.getText().toString(),
                        sdf.format(resultdate),emailUser,keyTo,"");

                mDatabaseReference.child("Requests").child(emailUser).child(keyTo).push().setValue(dataToSend);

                MyRequestsFragment myRequestsFragment = new MyRequestsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container_for_fragments,myRequestsFragment).commit();
                HomeActivity.toolbar.setTitle("My Requests");
                HomeActivity.navigationView.getMenu().getItem(2).setChecked(true);
            }
        });


        return view;

    }

    public void showBloodGroup() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.blood_group_dialog, null);
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

    public void showBloodUnit() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.blood_units_dialog, null);
        builder.setView(view);
        builder.setTitle("Blood Units");
        final android.support.v7.app.AlertDialog dialog = builder.create();

        final NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker3);
        np.setMaxValue(3);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);

        TextView b1 = (TextView) view.findViewById(R.id.button1);
        TextView b2 = (TextView) view.findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bloodUnit.setText(String.valueOf(np.getValue()));
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

    public long secondsToAdd(String s){
        if(s.charAt(0)=='2')
            return 24*60*60;
        else if(s.charAt(0)=='4')
            return 48*60*60;
        else
            return 7*24*60*60;
    }

    public void showBloodUrgency() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.blood_urgency_dialog, null);
        builder.setView(view);
        builder.setTitle("Blood Urgency");
        final android.support.v7.app.AlertDialog dialog = builder.create();

        TextView b1 = (TextView) view.findViewById(R.id.button1);
        TextView b2 = (TextView) view.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker4);
        np.setMaxValue(2);
        np.setMinValue(0);
        final String[] buArr = new String[]{"24 Hours", "48 Hours", "7 days   "};
        np.setDisplayedValues(buArr);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bloodUrgency.setText(buArr[np.getValue()]);
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
