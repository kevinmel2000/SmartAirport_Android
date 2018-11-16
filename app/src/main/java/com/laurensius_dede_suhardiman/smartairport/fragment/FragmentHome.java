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

public class FragmentHome extends Fragment {

    private LinearLayout llFlightInfo;
    private LinearLayout llBookFlight;
    private LinearLayout llTranslatorTool;
    private LinearLayout llAirportFacilities;
    private LinearLayout llBaggageTracking;
    private LinearLayout llTransportationGuide;
    private LinearLayout llPhoneDir;
    private LinearLayout llParking;
    private LinearLayout llTourismAttraction;
    private LinearLayout llEntertainment;
    private LinearLayout llBIJBNews;


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
        llBaggageTracking = (LinearLayout)inflaterHome.findViewById(R.id.ll_baggage_tracking);
        llTransportationGuide = (LinearLayout)inflaterHome.findViewById(R.id.ll_transportation_guide);
        llPhoneDir = (LinearLayout)inflaterHome.findViewById(R.id.ll_phone_dir);
        llParking = (LinearLayout)inflaterHome.findViewById(R.id.ll_parking);
        llTourismAttraction = (LinearLayout)inflaterHome.findViewById(R.id.ll_tourism_attraction);
        llEntertainment = (LinearLayout)inflaterHome.findViewById(R.id.ll_entertainment);
        llBIJBNews =(LinearLayout)inflaterHome.findViewById(R.id.ll_bijb_news);
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

        llTransportationGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),TransportationGuide.class);
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


        llBaggageTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),BaggageTracker.class);
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

        llParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),Parking.class);
                startActivity(i);
            }
        });

        llTourismAttraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),TourismAttraction.class);
                startActivity(i);
            }
        });

        llEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),EntertainmentArea.class);
                startActivity(i);
            }
        });

        llBIJBNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.url_bijb_news)));
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
