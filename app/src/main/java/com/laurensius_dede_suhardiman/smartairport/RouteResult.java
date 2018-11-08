package com.laurensius_dede_suhardiman.smartairport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laurensius_dede_suhardiman.smartairport.adapter.RouteAdapter;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;
import com.laurensius_dede_suhardiman.smartairport.model.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RouteResult extends AppCompatActivity {


    RecyclerView.LayoutManager mLayoutManager;
    List<Route> listRoute= new ArrayList<>();
    private RecyclerView rvRouteResult;
    private RouteAdapter routeAdapter= null;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_result);

        rvRouteResult = (RecyclerView)findViewById(R.id.rv_route_result);
        rvRouteResult.setAdapter(null);
        rvRouteResult.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(RouteResult.this);
        rvRouteResult.setLayoutManager(mLayoutManager);
        routeAdapter= new RouteAdapter(listRoute);
        routeAdapter.notifyDataSetChanged();
        rvRouteResult.setAdapter(routeAdapter);
        
        Intent i = getIntent();
        String origin_id = i.getStringExtra("origin_id");
        String destination_id = i.getStringExtra("destination_id");

        requestRoute(origin_id,destination_id);
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
