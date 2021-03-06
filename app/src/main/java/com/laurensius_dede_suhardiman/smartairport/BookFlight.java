package com.laurensius_dede_suhardiman.smartairport;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;
import com.laurensius_dede_suhardiman.smartairport.model.Airport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookFlight extends AppCompatActivity {

    private Spinner spOrigin,spDestination;
    List<Airport> listAirport = new ArrayList<>();
    private Button btnSearch;

    private String origin_id, destination_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_flight);


        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_search_flight));

        spOrigin = (Spinner)findViewById(R.id.sp_origin);
        spDestination = (Spinner)findViewById(R.id.sp_destination);
        btnSearch = (Button)findViewById(R.id.btn_search);

        requestAirport();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(origin_id.equals(destination_id)){
                    new AlertDialog.Builder(BookFlight.this)
                            .setTitle("Notification")
                            .setMessage("Departure and arrival cannot be the same airport")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    spOrigin.requestFocus();
                                }}).show().
                            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));

                }else{
                    Intent i = new Intent(BookFlight.this,RouteResult.class);
                    i.putExtra("origin_id",origin_id);
                    i.putExtra("destination_id",destination_id);
                    startActivity(i);
                }
            }
        });
    }


    public void requestAirport(){
        Random random = new Random();
        int rnd = random.nextInt(999999 - 99) + 99;
        String tag_req_airport_list = getResources().getString(R.string.tag_req_airport_list);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_get_airport))
                .concat(String.valueOf(rnd))
                .concat(getResources().getString(R.string.slash));
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.progress_loading));
        pDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d(getResources().getString(R.string.debug_tag),response.toString());
                        parseDataAirport(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        new AlertDialog.Builder(BookFlight.this)
                                .setTitle("Whooops . . .")
                                .setMessage("Something went wrong. Please try again!")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        requestAirport();
                                    }}).show().
                                getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));

                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_req_airport_list);
    }

    public void parseDataAirport(JSONObject responseJsonObj){
        Log.d(getResources().getString(R.string.debug_tag),responseJsonObj.toString());
        try{
            String severity = responseJsonObj.getString(getResources().getString(R.string.json_key_severity));
            JSONObject content = responseJsonObj.getJSONObject(getResources().getString(R.string.json_key_content));
            if(severity.equals(getResources().getString(R.string.success))){
                JSONArray jsonArrayRoute = content.getJSONArray(getResources().getString(R.string.json_key_airport));
                if(jsonArrayRoute.length() > 0){
                    List<String> spinnerArray = new ArrayList<>();
                    for(int x=0;x<jsonArrayRoute.length();x++){
                        JSONObject objRoute = jsonArrayRoute.getJSONObject(x);
                        listAirport.add(new Airport(
                                objRoute.getString(getResources().getString(R.string.airport_id)),
                                objRoute.getString(getResources().getString(R.string.airport_name)),
                                objRoute.getString(getResources().getString(R.string.airport_address)),
                                objRoute.getString(getResources().getString(R.string.airport_iata)),
                                objRoute.getString(getResources().getString(R.string.airport_icao))
                        ));
                        spinnerArray.add(
                                objRoute.getString(getResources().getString(R.string.airport_name))
                                + "\n"
                                + objRoute.getString(getResources().getString(R.string.airport_address))
                        );
                    }
                    ArrayAdapter<String> airportAdapter = new ArrayAdapter(
                            BookFlight.this,
                            R.layout.custom_spinner_item,spinnerArray);
                    spOrigin.setAdapter(airportAdapter);
                    spDestination.setAdapter(airportAdapter);
                }
            }
        }catch (JSONException e){

        }

        spOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                origin_id = listAirport.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                destination_id = listAirport.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




}
