package com.laurensius_dede_suhardiman.smartairport;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laurensius_dede_suhardiman.smartairport.adapter.TourismAdapter;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;
import com.laurensius_dede_suhardiman.smartairport.listener.CustomListener;
import com.laurensius_dede_suhardiman.smartairport.model.PhoneDirectory;
import com.laurensius_dede_suhardiman.smartairport.model.Tourism;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TourismAttraction extends AppCompatActivity {

    RecyclerView.LayoutManager mLayoutManager;
    List<Tourism> listTourism = new ArrayList<>();
    private RecyclerView rvTourism;
    private TourismAdapter tourismAdapter= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism);


        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_tourism_guide));

        rvTourism = (RecyclerView)findViewById(R.id.rv_tourism);
        rvTourism.setAdapter(null);
        rvTourism.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(TourismAttraction.this);
        rvTourism.setLayoutManager(new GridLayoutManager(this,2));
        tourismAdapter= new TourismAdapter(listTourism,TourismAttraction.this);
        tourismAdapter.notifyDataSetChanged();
        rvTourism.setAdapter(tourismAdapter);
        rvTourism.addOnItemTouchListener(new CustomListener(this, new CustomListener.OnItemClickListener() {
            @Override
            public void onItemClick(View childVew, int childAdapterPosition) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TourismAttraction.this,
                            new String[]{Manifest.permission.CALL_PHONE},100);
                    return;
                }
                Tourism tourism = tourismAdapter.getItem(childAdapterPosition);
                Intent intent = new Intent(TourismAttraction.this,DirectionMap.class);
                intent.putExtra("destinationObject", tourism);
                intent.putExtra("object_type", "tourism");
                startActivity(intent);
            }
        }));


        requestTourism();
    }

    public void requestTourism(){
        Random random = new Random();
        int rnd = random.nextInt(999999 - 99) + 99;
        String tag_req_tourism = getResources().getString(R.string.tag_req_tourism_list);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_get_tourism))
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_req_tourism);
    }
    //
    public void parseData(JSONObject responseJsonObj){
        Log.d(getResources().getString(R.string.debug_tag),responseJsonObj.toString());
        try{
            String severity = responseJsonObj.getString(getResources().getString(R.string.json_key_severity));
            JSONObject content = responseJsonObj.getJSONObject(getResources().getString(R.string.json_key_content));
            if(severity.equals(getResources().getString(R.string.success))){
                JSONArray jsonArrayTourism = content.getJSONArray(getResources().getString(R.string.json_key_tourism));
                if(jsonArrayTourism.length() > 0){
                    for(int x=0;x<jsonArrayTourism.length();x++){
                        JSONObject objTourism = jsonArrayTourism.getJSONObject(x);
                        listTourism.add(new Tourism(
                                objTourism.getString(getResources().getString(R.string.tourism_tourism_id)),
                                objTourism.getString(getResources().getString(R.string.tourism_name)),
                                objTourism.getString(getResources().getString(R.string.tourism_city_district)),
                                objTourism.getString(getResources().getString(R.string.tourism_latitude)),
                                objTourism.getString(getResources().getString(R.string.tourism_longitude)),
                                objTourism.getString(getResources().getString(R.string.tourism_description)),
                                objTourism.getString(getResources().getString(R.string.tourism_category)),
                                objTourism.getString(getResources().getString(R.string.tourism_address)),
                                objTourism.getString(getResources().getString(R.string.tourism_phone)),
                                objTourism.getString(getResources().getString(R.string.tourism_image))
                        ));
                    }
                }
            }
        }catch (JSONException e){

        }
        tourismAdapter.notifyDataSetChanged();
    }
}
