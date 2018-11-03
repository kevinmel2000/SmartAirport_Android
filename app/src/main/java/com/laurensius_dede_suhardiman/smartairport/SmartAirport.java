package com.laurensius_dede_suhardiman.smartairport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.laurensius_dede_suhardiman.smartairport.fragment.FragmentHome;

public class SmartAirport extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_airport);

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
//                        selectedFragment = new FragmentKeranjang();
                        break;
                    case R.id.navigation_notifications:
//                        if(sharedPreferences.getString("sf_datauser",null) != null){
//                            Toast.makeText(KuninganFoodMarket.this,"Udah login bos",Toast.LENGTH_LONG).show();
//                            selectedFragment = new FragmentAkun();
//                        }else{
//                            Intent i = new Intent(SmartAirport.this, Login.class);
//                            startActivity(i);
//                            finish();
//                        }
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

}
