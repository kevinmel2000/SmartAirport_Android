package com.laurensius_dede_suhardiman.smartairport;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laurensius_dede_suhardiman.smartairport.adapter.PhoneDirectoryAdapter;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;
import com.laurensius_dede_suhardiman.smartairport.listener.CustomListener;
import com.laurensius_dede_suhardiman.smartairport.model.PhoneDirectory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PhoneDir extends AppCompatActivity {

    private RecyclerView rvPhoneDirectory;
    private PhoneDirectoryAdapter phoneDirectoryAdapter = null;
    RecyclerView.LayoutManager mLayoutManager;
    List<PhoneDirectory> listPhone = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dir);

        rvPhoneDirectory = (RecyclerView)findViewById(R.id.rv_phone_directory);

        rvPhoneDirectory.setAdapter(null);
        rvPhoneDirectory.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rvPhoneDirectory.setLayoutManager(mLayoutManager);
        phoneDirectoryAdapter = new PhoneDirectoryAdapter(listPhone);
        phoneDirectoryAdapter.notifyDataSetChanged();
        rvPhoneDirectory.setAdapter(phoneDirectoryAdapter);
        rvPhoneDirectory.addOnItemTouchListener(new CustomListener(this, new CustomListener.OnItemClickListener() {
            @Override
            public void onItemClick(View childVew, int childAdapterPosition) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PhoneDir.this,
                            new String[]{Manifest.permission.CALL_PHONE},100);
                    return;
                }
                PhoneDirectory phoneDirectory = phoneDirectoryAdapter.getItem(childAdapterPosition);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phoneDirectory.getPhone()));
                startActivity(callIntent);
            }
        }));
        requestPhoneDirectory();
    }

    public void requestPhoneDirectory(){
        Random random = new Random();
        int rnd = random.nextInt(999999 - 99) + 99;
        String tag_req_phone_dir= getResources().getString(R.string.tag_req_phone_dir);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_get_phone_dir))
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_req_phone_dir);
    }

    public void parseData(JSONObject responseJsonObj){
        Log.d(getResources().getString(R.string.debug_tag),responseJsonObj.toString());
        try{
            String severity = responseJsonObj.getString(getResources().getString(R.string.json_key_severity));
            JSONObject content = responseJsonObj.getJSONObject(getResources().getString(R.string.json_key_content));
            if(severity.equals(getResources().getString(R.string.success))){
                JSONArray jsonArraySchedule = content.getJSONArray(getResources().getString(R.string.json_key_phone_dir));
                if(jsonArraySchedule.length() > 0){
                    for(int x=0;x<jsonArraySchedule.length();x++){
                        JSONObject objPhoneDir = jsonArraySchedule.getJSONObject(x);
                        listPhone.add(new PhoneDirectory(
                                objPhoneDir.getString(getResources().getString(R.string.phone_id)),
                                objPhoneDir.getString(getResources().getString(R.string.phone_name)),
                                objPhoneDir.getString(getResources().getString(R.string.phone_no)),
                                objPhoneDir.getString(getResources().getString(R.string.phone_image))
                        ));
                    }            }
            }
        }catch (JSONException e){

        }
        phoneDirectoryAdapter.notifyDataSetChanged();
    }


}
