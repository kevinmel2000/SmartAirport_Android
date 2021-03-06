package com.laurensius_dede_suhardiman.smartairport.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.adapter.ScheduleAdapter;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;
import com.laurensius_dede_suhardiman.smartairport.model.Schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FragmentFlightInfo extends Fragment {

    private LinearLayout llError;
    private RecyclerView rvFlightInfo;
    private ScheduleAdapter scheduleAdapter = null;
    RecyclerView.LayoutManager mLayoutManager;
    List<Schedule> listSchedule = new ArrayList<>();


    int origin_destination;

    public FragmentFlightInfo() {}


    public static FragmentFlightInfo newInstance(int sectionNumber) {
        FragmentFlightInfo fragment = new FragmentFlightInfo();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterFlightInfo = inflater.inflate(R.layout.fragment_flight_info, container, false);
        llError = (LinearLayout) inflaterFlightInfo.findViewById(R.id.ll_error);
        rvFlightInfo = (RecyclerView)inflaterFlightInfo.findViewById(R.id.rv_flight_info);
        return inflaterFlightInfo;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        llError.setVisibility(View.GONE);
        rvFlightInfo.setVisibility(View.VISIBLE);

        llError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestFlightInfo();
            }
        });

        origin_destination = getArguments().getInt("section_number");
        rvFlightInfo.setAdapter(null);
        rvFlightInfo.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvFlightInfo.setLayoutManager(mLayoutManager);
        scheduleAdapter = new ScheduleAdapter(listSchedule,origin_destination,getContext());
        scheduleAdapter.notifyDataSetChanged();
        rvFlightInfo.setAdapter(scheduleAdapter);
        requestFlightInfo();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void requestFlightInfo(){
        Random random = new Random();
        int rnd = random.nextInt(999999 - 99) + 99;
        String tag_req_flight_info = getResources().getString(R.string.tag_req_flight_info);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_status_today))
                .concat(String.valueOf(rnd))
                .concat(getResources().getString(R.string.slash));
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getResources().getString(R.string.progress_loading));
        pDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        llError.setVisibility(View.GONE);
                        rvFlightInfo.setVisibility(View.VISIBLE);
                        pDialog.dismiss();
                        Log.d(getResources().getString(R.string.debug_tag),response.toString());
                        parseData(response,origin_destination);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        llError.setVisibility(View.VISIBLE);
                        rvFlightInfo.setVisibility(View.GONE);
                        pDialog.dismiss();
//                        FlightInfo.responseJsonObj = null;
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_req_flight_info);
    }


    public void parseData(JSONObject responseJsonObj, int origin_destination){
        Log.d(getResources().getString(R.string.debug_tag),responseJsonObj.toString());
        try{
            String severity = responseJsonObj.getString(getResources().getString(R.string.json_key_severity));
            JSONObject content = responseJsonObj.getJSONObject(getResources().getString(R.string.json_key_content));
            if(severity.equals(getResources().getString(R.string.success))){
                JSONArray jsonArraySchedule = content.getJSONArray(getResources().getString(R.string.json_key_schedules));
                if(jsonArraySchedule.length() > 0){
                    if (origin_destination == 1){ // departure = origin = 1
                        for(int x=0;x<jsonArraySchedule.length();x++){
                            JSONObject objSchedule = jsonArraySchedule.getJSONObject(x);
                            if(objSchedule.getString(getResources().getString(R.string.origin_id)).equals("1")){
                                listSchedule.add(new Schedule(
                                        objSchedule.getString(getResources().getString(R.string.json_key_schedule_id)),
                                        objSchedule.getString(getResources().getString(R.string.route_id)),
                                        objSchedule.getString(getResources().getString(R.string.airlines_id)),
                                        objSchedule.getString(getResources().getString(R.string.airlines)),
                                        objSchedule.getString(getResources().getString(R.string.airlines_icon)),
                                        objSchedule.getString(getResources().getString(R.string.flight_no)),
                                        objSchedule.getString(getResources().getString(R.string.origin_id)),
                                        objSchedule.getString(getResources().getString(R.string.origin_name)),
                                        objSchedule.getString(getResources().getString(R.string.origin_iata)),
                                        objSchedule.getString(getResources().getString(R.string.origin_icao)),
                                        objSchedule.getString(getResources().getString(R.string.destination_id)),
                                        objSchedule.getString(getResources().getString(R.string.destination_name)),
                                        objSchedule.getString(getResources().getString(R.string.destination_iata)),
                                        objSchedule.getString(getResources().getString(R.string.destination_icao)),
                                        objSchedule.getString(getResources().getString(R.string.flight_schedule)),
                                        objSchedule.getString(getResources().getString(R.string.flight_duration_minutes)),
                                        objSchedule.getString(getResources().getString(R.string.transit)),
                                        objSchedule.getString(getResources().getString(R.string.transit_name)),
                                        objSchedule.getString(getResources().getString(R.string.transit_iata)),
                                        objSchedule.getString(getResources().getString(R.string.transit_icao)),
                                        objSchedule.getString(getResources().getString(R.string.flight_status))
                                ));
                            }
                        }
                    }else
                    if (origin_destination == 2){  // arrival = destination = 2
                        for(int x=0;x<jsonArraySchedule.length();x++){
                            JSONObject objSchedule = jsonArraySchedule.getJSONObject(x);
                            if(objSchedule.getString(getResources().getString(R.string.destination_id)).equals("1")){
                                listSchedule.add(new Schedule(objSchedule.getString(getResources().getString(R.string.json_key_schedule_id)),
                                        objSchedule.getString(getResources().getString(R.string.route_id)),
                                        objSchedule.getString(getResources().getString(R.string.airlines_id)),
                                        objSchedule.getString(getResources().getString(R.string.airlines)),
                                        objSchedule.getString(getResources().getString(R.string.airlines_icon)),
                                        objSchedule.getString(getResources().getString(R.string.flight_no)),
                                        objSchedule.getString(getResources().getString(R.string.origin_id)),
                                        objSchedule.getString(getResources().getString(R.string.origin_name)),
                                        objSchedule.getString(getResources().getString(R.string.origin_iata)),
                                        objSchedule.getString(getResources().getString(R.string.origin_icao)),
                                        objSchedule.getString(getResources().getString(R.string.destination_id)),
                                        objSchedule.getString(getResources().getString(R.string.destination_name)),
                                        objSchedule.getString(getResources().getString(R.string.destination_iata)),
                                        objSchedule.getString(getResources().getString(R.string.destination_icao)),
                                        objSchedule.getString(getResources().getString(R.string.flight_schedule)),
                                        objSchedule.getString(getResources().getString(R.string.flight_duration_minutes)),
                                        objSchedule.getString(getResources().getString(R.string.transit)),
                                        objSchedule.getString(getResources().getString(R.string.transit_name)),
                                        objSchedule.getString(getResources().getString(R.string.transit_iata)),
                                        objSchedule.getString(getResources().getString(R.string.transit_icao)),
                                        objSchedule.getString(getResources().getString(R.string.flight_status))
                                ));
                            }
                        }
                    }else {
                        llError.setVisibility(View.VISIBLE);
                        rvFlightInfo.setVisibility(View.GONE);
                    }
                }else{
                    llError.setVisibility(View.VISIBLE);
                    rvFlightInfo.setVisibility(View.GONE);
                }
            }else {
                llError.setVisibility(View.VISIBLE);
                rvFlightInfo.setVisibility(View.GONE);
            }
        }catch (JSONException e){
            llError.setVisibility(View.VISIBLE);
            rvFlightInfo.setVisibility(View.GONE);
        }
        scheduleAdapter.notifyDataSetChanged();
    }

}


