package com.laurensius_dede_suhardiman.smartairport;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;
import com.laurensius_dede_suhardiman.smartairport.model.Route;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BookingProcess extends AppCompatActivity {

    Intent intent;
    Route route;
    
    private TextView tvOriginIataIcao,tvDestinationIataIcao,tvSchedule,tvDuration,tvOrigin,tvDestination,tvAirline,tvPrice;
    private Button btnSubmit;
    private EditText etEmail, etAdult, etChild, etInfant;
    private CalendarView calenViewDate;
    String tanggal_pesan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_process);
        intent = getIntent();
        route = (Route)intent.getSerializableExtra("routeObject");
        Toast.makeText(BookingProcess.this,route.getAirlines(),Toast.LENGTH_LONG).show();

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_booking));

        tvOriginIataIcao = (TextView) findViewById(R.id.tv_origin_iata_icao);
        tvDestinationIataIcao = (TextView) findViewById(R.id.tv_destination_iata_icao);
        tvSchedule = (TextView)findViewById(R.id.tv_schedule);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        tvOrigin = (TextView) findViewById(R.id.tv_origin);
        tvDestination = (TextView) findViewById(R.id.tv_destination);
        tvAirline = (TextView) findViewById(R.id.tv_airline);
        tvPrice = (TextView)findViewById(R.id.tv_price);

        etEmail = (EditText)findViewById(R.id.et_email);
        etAdult = (EditText)findViewById(R.id.et_adult);
        etChild = (EditText)findViewById(R.id.et_child);
        etInfant = (EditText)findViewById(R.id.et_infant);

        calenViewDate = (CalendarView)findViewById(R.id.calview_date);

        int y = Calendar.getInstance().get(Calendar.YEAR);
        int m = Calendar.getInstance().get(Calendar.MONTH);
        int d = Calendar.getInstance().get(Calendar.DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,y);
        calendar.set(Calendar.MONTH,m);
        calendar.set(Calendar.DATE,d);

        long milliTime = calendar.getTimeInMillis();

        calenViewDate.setMinDate(milliTime);

        calenViewDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                tanggal_pesan = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(dayOfMonth);
            }
        });

        btnSubmit = (Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!etInfant.getText().toString().equals("") &&
                !etChild.getText().toString().equals("") &&
                !etAdult.getText().toString().equals("") &&
                !etEmail.getText().toString().equals("")){
                    requestBooking();
                }else{
                    new AlertDialog.Builder(BookingProcess.this)
                            .setTitle("Wooops . . .")
                            .setMessage("Please fill the blank field")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }}).show().
                            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));
                }

            }
        });
        attachDataView();
    }
    
    void attachDataView(){
        tvOriginIataIcao.setText(route.getOrigin_iata() + "\n(" + route.getOrigin_icao() + ")");
        tvDestinationIataIcao.setText(route.getDestination_iata() + "\n(" + route.getDestination_icao() + ")");
        tvSchedule.setText("Flight Shedule : " + route.getFlight_schedule());
        tvDuration.setText("Duration : " + route.getFlight_duration_minutes() + " minutes");
        tvOrigin.setText("Departure : " + route.getOrigin_name());
        tvDestination.setText("Arrival : " + route.getDestination_name());
        tvAirline.setText(route.getAirlines());
        tvPrice.setText("IDR " + intent.getStringExtra("price") + "K");

        if(SmartAirport.user_email != null){
            etEmail.setText(SmartAirport.user_email);
        }
    }

    public void requestBooking(){
        Random random = new Random();
        int rnd = random.nextInt(999999 - 99) + 99;
        String tag_req_tourism = getResources().getString(R.string.tag_req_booking);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_booking))
                .concat(String.valueOf(rnd))
                .concat(getResources().getString(R.string.slash));
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getResources().getString(R.string.progress_loading));
        pDialog.show();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("route_id", route.getRoute_id());
        params.put("price", intent.getStringExtra("price"));
        params.put("email", etEmail.getText().toString());
        params.put("adult", etAdult.getText().toString());
        params.put("child", etChild.getText().toString());
        params.put("infant", etInfant.getText().toString());
        params.put("date", tanggal_pesan);
        JSONObject parameter = new JSONObject(params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,url, parameter,
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
                        pDialog.dismiss(); new AlertDialog.Builder(BookingProcess.this)
                                .setTitle("Whooops . . .")
                                .setMessage("Something went wrong. Please try again!")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        requestBooking();
                                    }}).show().
                                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_req_tourism);
    }


    public void parseData(JSONObject responseJsonObj){
        Log.d(getResources().getString(R.string.debug_tag),responseJsonObj.toString());
        try{
            String severity = responseJsonObj.getString(getResources().getString(R.string.json_key_severity));
            JSONObject content = responseJsonObj.getJSONObject(getResources().getString(R.string.json_key_content));
            if(severity.equals(getResources().getString(R.string.success))){
                new AlertDialog.Builder(BookingProcess.this)
                        .setTitle("Booking Success")
                        .setMessage(responseJsonObj.getString("message"))
                        .setIcon(android.R.drawable.ic_menu_info_details)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                finish();
                            }}).show().
                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));
            }else{
                new AlertDialog.Builder(BookingProcess.this)
                        .setTitle("Booking Failed")
                        .setMessage(responseJsonObj.getString("message"))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }}).show().
                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));
            }
        }catch (JSONException e){

        }
    }


}
