package com.laurensius_dede_suhardiman.smartairport.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.laurensius_dede_suhardiman.smartairport.AirportFacilities;
import com.laurensius_dede_suhardiman.smartairport.BookFlight;
import com.laurensius_dede_suhardiman.smartairport.FlightInfo;
import com.laurensius_dede_suhardiman.smartairport.PhoneDir;
import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.SmartTranslate;

public class FragmentHome extends Fragment {

    private LinearLayout llFlightInfo;
    private LinearLayout llBookFlight;
    private LinearLayout llTranslatorTool;
    private LinearLayout llAirportFacilities;
    private LinearLayout llPhoneDir;


    public FragmentHome() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterHome = inflater.inflate(R.layout.fragment_home, container, false);
        llFlightInfo = (LinearLayout)inflaterHome.findViewById(R.id.ll_flight_info);
        llBookFlight = (LinearLayout)inflaterHome.findViewById(R.id.ll_book_flight);
        llTranslatorTool = (LinearLayout)inflaterHome.findViewById(R.id.ll_translator_tool);
        llAirportFacilities = (LinearLayout)inflaterHome.findViewById(R.id.ll_airport_facilities);
        llPhoneDir = (LinearLayout)inflaterHome.findViewById(R.id.ll_phone_dir);
        return inflaterHome;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        llFlightInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),FlightInfo.class);
                startActivity(i);
            }
        });

        llBookFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),BookFlight.class);
                startActivity(i);
            }
        });

        llAirportFacilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),AirportFacilities.class);
                startActivity(i);
            }
        });

        llTranslatorTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),SmartTranslate.class);
                startActivity(i);
            }
        });

        llPhoneDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),PhoneDir.class);
                startActivity(i);
            }
        });
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
