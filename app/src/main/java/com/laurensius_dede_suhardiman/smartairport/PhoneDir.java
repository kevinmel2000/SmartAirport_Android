package com.laurensius_dede_suhardiman.smartairport;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.laurensius_dede_suhardiman.smartairport.adapter.PhoneDirectoryAdapter;
import com.laurensius_dede_suhardiman.smartairport.model.PhoneDirectory;

import java.util.ArrayList;
import java.util.List;

public class PhoneDir extends AppCompatActivity {

    private RecyclerView rvPhoneDirectory;
    private PhoneDirectoryAdapter phoneDirectory = null;
    RecyclerView.LayoutManager mLayoutManager;
    List<PhoneDirectory> listPhone = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dir);

        rvPhoneDirectory = (RecyclerView)findViewById(R.id.rv_phone_directory);

        rvPhoneDirectory.setAdapter(null);
        rvPhoneDirectory.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rvPhoneDirectory.setLayoutManager(mLayoutManager);
        phoneDirectory = new PhoneDirectoryAdapter(listPhone);
        phoneDirectory.notifyDataSetChanged();
        rvPhoneDirectory.setAdapter(phoneDirectory);
    }
}
