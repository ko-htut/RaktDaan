package com.cdev.raktdaan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdev.raktdaan.Fragments.MakeARequestFragment;
import com.cdev.raktdaan.Fragments.MyRequestsFragment;
import com.cdev.raktdaan.Fragments.RequestsFragment;
import com.cdev.raktdaan.nonactivity.CircleTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mFirebaseAuth;
    private ImageView mUserPhoto;
    private TextView mUserName;
    private TextView mUserEmail;
    private TextView tvNavEditProfile;
    private FirebaseUser currentUser;
    static public Toolbar toolbar;
    static public NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Requests");
        setSupportActionBar(toolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        // initialize by request fragment

        RequestsFragment requestsFragment = new RequestsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_for_fragments,requestsFragment).commit();

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        mUserPhoto = (ImageView) headerView.findViewById(R.id.firebaseuser_photo);
        mUserName = (TextView) headerView.findViewById(R.id.firebaseuser_name);
        mUserEmail = (TextView) headerView.findViewById(R.id.firebaseuser_email);
        tvNavEditProfile = (TextView) headerView.findViewById(R.id.tv_nav_edit_profile);
        Picasso.with(getApplicationContext()).load(currentUser.getPhotoUrl()).transform(new CircleTransform()).into(mUserPhoto);
        mUserName.setText(currentUser.getDisplayName());
        mUserEmail.setText(currentUser.getEmail());
        tvNavEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(0);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_requests) {
            RequestsFragment requestsFragment = new RequestsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_for_fragments,requestsFragment).commit();
            toolbar.setTitle("Requests");
        } else if (id == R.id.nav_make_a_request) {
            MakeARequestFragment makeARequestFragment = new MakeARequestFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_for_fragments,makeARequestFragment).commit();
            toolbar.setTitle("Make a Request");
        } else if (id == R.id.nav_my_requests) {
            MyRequestsFragment myRequestsFragment = new MyRequestsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_for_fragments,myRequestsFragment).commit();
            toolbar.setTitle("My Requests");
        } else if (id == R.id.nav_setting) {
            Toast.makeText(getApplicationContext(), "Open Settings Activity", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            try {
                PackageManager pm = getPackageManager();
                ApplicationInfo ai = pm.getApplicationInfo(getPackageName(), 0);
                File srcFile = new File(ai.publicSourceDir);
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("application/vnd.android.package-archive");
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(srcFile));
                startActivity(Intent.createChooser(share, "Share RaktDaan app using"));
            } catch (Exception e) {
                Log.e("ShareApp", e.getMessage());
            }
        } else if (id == R.id.nav_logout_user) {
            mFirebaseAuth.signOut();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("loginStatus", false);
            editor.commit();
            setResult(1);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
