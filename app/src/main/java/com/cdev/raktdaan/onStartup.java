package com.cdev.raktdaan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by sanjay on 19/5/17.
 */

public class onStartup extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    public static final int RANDOM_CODE = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean loginStatus = sharedPreferences.getBoolean("loginStatus", false);
        if (!loginStatus) {
            mFirebaseAuth.signOut();
            Intent intent = new Intent(onStartup.this, SignInActivity.class);
            startActivityForResult(intent,RANDOM_CODE);
        } else {
            Intent intent = new Intent(onStartup.this, HomeActivity.class);
            startActivityForResult(intent,RANDOM_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            Intent intent = new Intent(onStartup.this, SignInActivity.class);
            startActivityForResult(intent, RANDOM_CODE);
        } else {
            finish();
        }
    }
}