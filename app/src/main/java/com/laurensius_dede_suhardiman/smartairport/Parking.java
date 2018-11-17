package com.laurensius_dede_suhardiman.smartairport;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laurensius_dede_suhardiman.smartairport.adapter.ParkingAreaAdapter;
import com.laurensius_dede_suhardiman.smartairport.adapter.RouteAdapter;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;
import com.laurensius_dede_suhardiman.smartairport.model.ParkingArea;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Parking extends AppCompatActivity {

    RecyclerView.LayoutManager mLayoutManager;
    List<ParkingArea> listParkingArea = new ArrayList<>();
    private RecyclerView rvParking;
    private ParkingAreaAdapter parkingAreaAdapter= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_parking_guide));

        rvParking = (RecyclerView)findViewById(R.id.rv_parking);
        rvParking.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(Parking.this);
        rvParking.setLayoutManager(mLayoutManager);
        parkingAreaAdapter= new ParkingAreaAdapter(listParkingArea,Parking.this);
        parkingAreaAdapter.notifyDataSetChanged();
        rvParking.setAdapter(parkingAreaAdapter);
        requestParkingArea();

    }


    public void requestParkingArea(){
        Random random = new Random();
        int rnd = random.nextInt(999999 - 99) + 99;
        String tag_req_parking_area = getResources().getString(R.string.tag_req_parking_area_list);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_get_parking_area))
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_req_parking_area);
    }
    //
    public void parseData(JSONObject responseJsonObj){
        Log.d(getResources().getString(R.string.debug_tag),responseJsonObj.toString());
        try{
            String severity = responseJsonObj.getString(getResources().getString(R.string.json_key_severity));
            JSONObject content = responseJsonObj.getJSONObject(getResources().getString(R.string.json_key_content));
            if(severity.equals(getResources().getString(R.string.success))){
                JSONArray jsonArrayParking = content.getJSONArray(getResources().getString(R.string.json_key_parking));
                if(jsonArrayParking.length() > 0){
                    for(int x=0;x<jsonArrayParking.length();x++){
                        JSONObject objParkingArea = jsonArrayParking.getJSONObject(x);
                        listParkingArea.add(new ParkingArea(
                                objParkingArea.getString(getResources().getString(R.string.parking_parking_id)),
                                objParkingArea.getString(getResources().getString(R.string.parking_name)),
                                objParkingArea.getString(getResources().getString(R.string.parking_description)),
                                objParkingArea.getString(getResources().getString(R.string.parking_status_car)),
                                objParkingArea.getString(getResources().getString(R.string.parking_status_motorcycle)),
                                objParkingArea.getString(getResources().getString(R.string.parking_latitude)),
                                objParkingArea.getString(getResources().getString(R.string.parking_longitude))
                        ));
                    }
                }
            }
        }catch (JSONException e){

        }
        parkingAreaAdapter.notifyDataSetChanged();
    }
}
