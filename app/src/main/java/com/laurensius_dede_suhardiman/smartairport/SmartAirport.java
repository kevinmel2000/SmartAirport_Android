package com.laurensius_dede_suhardiman.smartairport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.laurensius_dede_suhardiman.smartairport.fragment.FragmentAbout;
import com.laurensius_dede_suhardiman.smartairport.fragment.FragmentAccount;
import com.laurensius_dede_suhardiman.smartairport.fragment.FragmentHome;
import com.laurensius_dede_suhardiman.smartairport.fragment.FragmentSignin;

public class SmartAirport extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;

    public static String user_name,user_email,user_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_airport);

        loadSharedPreferences();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = new FragmentHome();
                        break;
                    case R.id.navigation_about:
                        selectedFragment = new FragmentAbout();
                        break;
                    case R.id.navigation_account:
                        if(SmartAirport.user_name != null) {
                            selectedFragment = new FragmentAccount();
                        }else{
                            selectedFragment = new FragmentSignin();
                        }
                        break;
                }

                if(selectedFragment != null){
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fl_master, selectedFragment);
                    transaction.commit();
                }

                return true;
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_master, new FragmentHome());
        transaction.commit();
    }

    void loadSharedPreferences(){
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedpreferences), 0);
        editorPreferences = sharedPreferences.edit();
        user_email = sharedPreferences.getString(getResources().getString(R.string.sharedpref_user_email),null);
        user_name = sharedPreferences.getString(getResources().getString(R.string.sharedpref_user_name),null);
        user_phone = sharedPreferences.getString(getResources().getString(R.string.sharedpref_user_phone),null);
    }

}
