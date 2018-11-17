package com.laurensius_dede_suhardiman.smartairport.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.laurensius_dede_suhardiman.smartairport.AirportFacilities;
import com.laurensius_dede_suhardiman.smartairport.BaggageTracker;
import com.laurensius_dede_suhardiman.smartairport.BookFlight;
import com.laurensius_dede_suhardiman.smartairport.EntertainmentArea;
import com.laurensius_dede_suhardiman.smartairport.FlightInfo;
import com.laurensius_dede_suhardiman.smartairport.Parking;
import com.laurensius_dede_suhardiman.smartairport.PhoneDir;
import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.SmartTranslate;
import com.laurensius_dede_suhardiman.smartairport.TourismAttraction;
import com.laurensius_dede_suhardiman.smartairport.TransportationGuide;

public class FragmentAbout extends Fragment {


    public FragmentAbout() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterAbout = inflater.inflate(R.layout.fragment_about, container, false);
        return inflaterAbout;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
