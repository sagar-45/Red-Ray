package com.example.redray_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class

MainActivity2 extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView navView;
    TextView no_internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        navView = findViewById(R.id.nav_view);
        no_internet=findViewById(R.id.no_internet);
        navView.setOnNavigationItemSelectedListener(this);

        checkConnection();

        if(null==savedInstanceState) {
            loadFragment(new HomeFragment());
        }
    }

    private boolean loadFragment(Fragment fragment){

        if(fragment!= null)
        {
            getSupportFragmentManager()
                    .beginTransaction().
                    replace(R.id.nav_host_fragment,fragment).
                    commit();
            checkConnection();
            return true;
        }

        return false;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()){

            case R.id.navigation_home:
                 fragment = new HomeFragment();
                 checkConnection();
                break;

            case R.id.donor_search:
                fragment = new DonorSearchFragment();
                checkConnection();
                break;

            case R.id.help:
                fragment = new HelpFragment();
                checkConnection();
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                checkConnection();
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        if(navView.getSelectedItemId()==R.id.navigation_home) {

            new AlertDialog.Builder(MainActivity2.this)
                    .setIcon(R.drawable.ic_baseline_warning_24)
                    .setTitle(Html.fromHtml("<font color='gray'>Exit</font>"))
                    .setMessage(Html.fromHtml("<font color='gray'>Are you sure you want to exit this app</font>"))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    checkConnection();
                    dialogInterface.dismiss();
                }
            }).show();
        }
        else {
            navView.setSelectedItemId(R.id.navigation_home);
        }
    }
    public void checkConnection(){
        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
        if(null==activeNetwork){
            no_internet.setVisibility(View.VISIBLE);
        }
        if(null!=activeNetwork){
            no_internet.setVisibility(View.GONE);
        }
    }
}