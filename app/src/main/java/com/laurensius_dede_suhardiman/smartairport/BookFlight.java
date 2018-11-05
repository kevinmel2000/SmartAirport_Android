package com.laurensius_dede_suhardiman.smartairport;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laurensius_dede_suhardiman.smartairport.adapter.RouteAdapter;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;
import com.laurensius_dede_suhardiman.smartairport.model.Airport;
import com.laurensius_dede_suhardiman.smartairport.model.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookFlight extends AppCompatActivity {

    RecyclerView.LayoutManager mLayoutManager;
    List<Route> listRoute= new ArrayList<>();
    List<Airport> listAirport = new ArrayList<>();
    private RecyclerView rvBookFlight;
    private RouteAdapter routeAdapter= null;
    private Spinner spOrigin,spDestination;
    private Button btnSearch;
    private String origin_id ;
    private String destination_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_flight);

        spOrigin = (Spinner)findViewById(R.id.sp_origin);
        spDestination = (Spinner)findViewById(R.id.sp_destination);
        btnSearch = (Button)findViewById(R.id.btn_search);

        requestAirport();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRoute(origin_id,destination_id);
            }
        });


        rvBookFlight = (RecyclerView)findViewById(R.id.rv_book_flight);
        rvBookFlight.setAdapter(null);
        rvBookFlight.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(BookFlight.this);
        rvBookFlight.setLayoutManager(mLayoutManager);
        routeAdapter= new RouteAdapter(listRoute);
        routeAdapter.notifyDataSetChanged();
        rvBookFlight.setAdapter(routeAdapter);


//
//        requestRoute();
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
                Toast.makeText(BookFlight.this,listAirport.get(position).getId(),Toast.LENGTH_LONG).show();
                origin_id = listAirport.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(BookFlight.this,listAirport.get(position).getId(),Toast.LENGTH_LONG).show();
                destination_id = listAirport.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void requestRoute(String origin,String destination){
        Random random = new Random();
        int rnd = random.nextInt(999999 - 99) + 99;
        String tag_req_route= getResources().getString(R.string.tag_req_route);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_get_routes))
                .concat(origin)
                .concat(getResources().getString(R.string.slash))
                .concat(destination)
                .concat(getResources().getString(R.string.slash))
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
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_req_route);
    }
//
    public void parseData(JSONObject responseJsonObj){
        Log.d(getResources().getString(R.string.debug_tag),responseJsonObj.toString());
        try{
            String severity = responseJsonObj.getString(getResources().getString(R.string.json_key_severity));
            JSONObject content = responseJsonObj.getJSONObject(getResources().getString(R.string.json_key_content));
            if(severity.equals(getResources().getString(R.string.success))){
                JSONArray jsonArrayRoute = content.getJSONArray(getResources().getString(R.string.json_key_routes));
                if(jsonArrayRoute.length() > 0){
                    for(int x=0;x<jsonArrayRoute.length();x++){
                        JSONObject objRoute = jsonArrayRoute.getJSONObject(x);
                        listRoute.add(new Route(
                                objRoute.getString(getResources().getString(R.string.route_route_id)),
                                objRoute.getString(getResources().getString(R.string.route_airlines_id)),
                                objRoute.getString(getResources().getString(R.string.route_flight_no)),
                                objRoute.getString(getResources().getString(R.string.route_airlines)),
                                objRoute.getString(getResources().getString(R.string.route_origin_id)),
                                objRoute.getString(getResources().getString(R.string.route_origin_name)),
                                objRoute.getString(getResources().getString(R.string.route_origin_iata)),
                                objRoute.getString(getResources().getString(R.string.route_origin_icao)),
                                objRoute.getString(getResources().getString(R.string.route_destination_id)),
                                objRoute.getString(getResources().getString(R.string.route_destination_name)),
                                objRoute.getString(getResources().getString(R.string.route_destination_iata)),
                                objRoute.getString(getResources().getString(R.string.route_destination_icao)),
                                objRoute.getString(getResources().getString(R.string.route_flight_schedule)),
                                objRoute.getString(getResources().getString(R.string.route_flight_duration_minutes)),
                                objRoute.getString(getResources().getString(R.string.route_transit)),
                                objRoute.getString(getResources().getString(R.string.route_transit_name)),
                                objRoute.getString(getResources().getString(R.string.route_transit_iata)),
                                objRoute.getString(getResources().getString(R.string.route_transit_icao))
                        ));
                    }
                }
            }
        }catch (JSONException e){

        }
        routeAdapter.notifyDataSetChanged();
    }


}
