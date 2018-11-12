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
import com.laurensius_dede_suhardiman.smartairport.adapter.EntertainmentAdapter;
import com.laurensius_dede_suhardiman.smartairport.adapter.EntertainmentAdapter;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;
import com.laurensius_dede_suhardiman.smartairport.model.Entertainment;
import com.laurensius_dede_suhardiman.smartairport.model.Entertainment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntertainmentArea extends AppCompatActivity {

    private RecyclerView rvEntertainment;
    private EntertainmentAdapter phoneDirectoryAdapter = null;
    RecyclerView.LayoutManager mLayoutManager;
    List<Entertainment> listEntertainment= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment);

        rvEntertainment = (RecyclerView)findViewById(R.id.rv_entertainment);

        rvEntertainment.setAdapter(null);
        rvEntertainment.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rvEntertainment.setLayoutManager(mLayoutManager);
        phoneDirectoryAdapter = new EntertainmentAdapter(listEntertainment);
        phoneDirectoryAdapter.notifyDataSetChanged();
        rvEntertainment.setAdapter(phoneDirectoryAdapter);
        requestEntertainment();
    }

    public void requestEntertainment(){
        Random random = new Random();
        int rnd = random.nextInt(999999 - 99) + 99;
        String tag_req_entertainment = getResources().getString(R.string.tag_req_entertainment_list);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_get_entertainment))
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_req_entertainment);
    }

    public void parseData(JSONObject responseJsonObj){
        Log.d(getResources().getString(R.string.debug_tag),responseJsonObj.toString());
        try{
            String severity = responseJsonObj.getString(getResources().getString(R.string.json_key_severity));
            JSONObject content = responseJsonObj.getJSONObject(getResources().getString(R.string.json_key_content));
            if(severity.equals(getResources().getString(R.string.success))){
                JSONArray jsonArrayEntertainment = content.getJSONArray(getResources().getString(R.string.json_key_entertainment));
                if(jsonArrayEntertainment.length() > 0){
                    for(int x=0;x<jsonArrayEntertainment.length();x++){
                        JSONObject objEntertainment = jsonArrayEntertainment.getJSONObject(x);
                        listEntertainment.add(new Entertainment(
                                objEntertainment.getString(getResources().getString(R.string.entertainment_entertainment_id)),
                                objEntertainment.getString(getResources().getString(R.string.entertainment_video_title)),
                                objEntertainment.getString(getResources().getString(R.string.entertainment_description)),
                                objEntertainment.getString(getResources().getString(R.string.entertainment_yt_id))
                        ));
                    }            }
            }
        }catch (JSONException e){

        }
        phoneDirectoryAdapter.notifyDataSetChanged();
    }
    
}
