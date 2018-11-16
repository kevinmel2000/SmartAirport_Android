package com.laurensius_dede_suhardiman.smartairport;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laurensius_dede_suhardiman.smartairport.adapter.BaggageTrackingAdapter;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;
import com.laurensius_dede_suhardiman.smartairport.model.BaggageTracking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaggageTracker extends AppCompatActivity {

    private EditText etBaggageCode;
    private Button btnSearchBaggage;
    private LinearLayout llError;
    private LinearLayout llNoData;
    

    RecyclerView.LayoutManager mLayoutManager;
    List<BaggageTracking> listBaggageTracking= new ArrayList<>();
    private RecyclerView rvBaggageTracking;
    private BaggageTrackingAdapter baggageTrackingAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baggage_tracker);
        etBaggageCode = (EditText)findViewById(R.id.et_baggage_code);
        btnSearchBaggage = (Button) findViewById(R.id.btn_search_baggage);

        llError = (LinearLayout)findViewById(R.id.ll_error);
        llNoData = (LinearLayout)findViewById(R.id.ll_no_data);

        rvBaggageTracking = (RecyclerView)findViewById(R.id.rv_baggage_tracker);

        btnSearchBaggage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSearch();
            }
        });

        llError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        llNoData.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        rvBaggageTracking.setVisibility(View.VISIBLE);

        rvBaggageTracking.setAdapter(null);
        rvBaggageTracking.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(BaggageTracker.this);
        rvBaggageTracking.setLayoutManager(mLayoutManager);
        baggageTrackingAdapter = new BaggageTrackingAdapter(listBaggageTracking);
        baggageTrackingAdapter.notifyDataSetChanged();
        rvBaggageTracking.setAdapter(baggageTrackingAdapter);

    }
    
    public void validateSearch(){
        if(etBaggageCode.getText().toString().equals("")){
            Toast.makeText(BaggageTracker.this, "Please insert your baggage code", Toast.LENGTH_LONG).show();
        }else{
            requestBaggageStatus(etBaggageCode.getText().toString());    
        }
    }
    
    public void requestBaggageStatus(String code){
        Random random = new Random();
        int rnd = random.nextInt(999999 - 99) + 99;
        String tag_req_baggage_detail = getResources().getString(R.string.tag_req_baggage_detail);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_get_baggage_detail))
                .concat(code)
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
                        llError.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);
                        rvBaggageTracking.setVisibility(View.GONE);
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_req_baggage_detail);
    }
    //
    public void parseData(JSONObject responseJsonObj){
        Log.d(getResources().getString(R.string.debug_tag),responseJsonObj.toString());
        try{
            String severity = responseJsonObj.getString(getResources().getString(R.string.json_key_severity));
            JSONObject content = responseJsonObj.getJSONObject(getResources().getString(R.string.json_key_content));
            if(severity.equals(getResources().getString(R.string.success))){
                JSONArray jsonArrayBaggage = content.getJSONArray(getResources().getString(R.string.json_key_baggage));
                if(jsonArrayBaggage.length() > 0){
                    for(int x=0;x<jsonArrayBaggage.length();x++){
                        JSONObject objRoute = jsonArrayBaggage.getJSONObject(x);
                        listBaggageTracking.add(new BaggageTracking(
                                objRoute.getString(getResources().getString(R.string.baggage_baggage_id)),
                                objRoute.getString(getResources().getString(R.string.baggage_code)),
                                objRoute.getString(getResources().getString(R.string.baggage_datetime)),
                                objRoute.getString(getResources().getString(R.string.baggage_status))
                        ));
                        llError.setVisibility(View.GONE);
                        llNoData.setVisibility(View.GONE);
                        rvBaggageTracking.setVisibility(View.VISIBLE);
                    }
                }else{
                    llError.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                    rvBaggageTracking.setVisibility(View.GONE);
                }
            }else{
                llError.setVisibility(View.VISIBLE);
                llNoData.setVisibility(View.GONE);
                rvBaggageTracking.setVisibility(View.GONE);
            }
        }catch (JSONException e){
            llError.setVisibility(View.VISIBLE);
            llNoData.setVisibility(View.GONE);
            rvBaggageTracking.setVisibility(View.GONE);
        }
        baggageTrackingAdapter.notifyDataSetChanged();
    }
}
