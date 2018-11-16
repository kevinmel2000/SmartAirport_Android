package com.laurensius_dede_suhardiman.smartairport.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.TransportationGuide;
import com.laurensius_dede_suhardiman.smartairport.adapter.FacilityAdapter;
import com.laurensius_dede_suhardiman.smartairport.adapter.TransportationAdapter;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;
import com.laurensius_dede_suhardiman.smartairport.model.Facility;
import com.laurensius_dede_suhardiman.smartairport.model.Transportation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FragmentTransportationGuide extends Fragment {

    private LinearLayout llError, llSuccess;

    private RecyclerView rvTransportationGuide;
    private TransportationAdapter transportationAdapter = null;
    RecyclerView.LayoutManager mLayoutManager;
    List<Transportation> listTransportationGuide = new ArrayList<>();


    private MapView mvTransportationGuide = null;
    private ScaleBarOverlay mScaleBarOverlay;
    private IMapController mapController;

    private LocationManager locationManager;
    private LocationListener listener;
    private RoadManager roadManager;
    private Road road;

    int select_moda;
    public FragmentTransportationGuide() {}


    public static FragmentTransportationGuide newInstance(int sectionNumber) {
        FragmentTransportationGuide fragment = new FragmentTransportationGuide();
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
        View inflaterTransportationGuide = inflater.inflate(R.layout.fragment_transportation_guide, container, false);
        llError = (LinearLayout) inflaterTransportationGuide.findViewById(R.id.ll_error);
        llSuccess = (LinearLayout) inflaterTransportationGuide.findViewById(R.id.ll_success);
        rvTransportationGuide = (RecyclerView)inflaterTransportationGuide.findViewById(R.id.rv_transportation_guide);
        mvTransportationGuide = (MapView)inflaterTransportationGuide.findViewById(R.id.mv_transportation_guide);
        return inflaterTransportationGuide;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        select_moda = getArguments().getInt("section_number");

        llSuccess.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);

        rvTransportationGuide.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvTransportationGuide.setLayoutManager(mLayoutManager);
        transportationAdapter= new TransportationAdapter(listTransportationGuide);
        transportationAdapter.notifyDataSetChanged();
        rvTransportationGuide.setAdapter(transportationAdapter);

        //map
        mvTransportationGuide.setTileSource(TileSourceFactory.MAPNIK);
        mvTransportationGuide.setBuiltInZoomControls(true);
        mvTransportationGuide.setMultiTouchControls(true);
        mvTransportationGuide.setPadding(0,0,0,90);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        mScaleBarOverlay = new ScaleBarOverlay(mvTransportationGuide);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(width / 2, 20);
        mvTransportationGuide.getOverlays().add(mScaleBarOverlay);
        mvTransportationGuide.invalidate();
        setMapCenter(-6.65190, 108.15780);

        llError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSelectedTab();
            }
        });
        checkSelectedTab();
    }


    void checkSelectedTab(){
        if(select_moda>=1 && select_moda < 6){
            rvTransportationGuide.setVisibility(View.VISIBLE);
            mvTransportationGuide.setVisibility(View.GONE);
            if(select_moda == 1){
                requestTransportationGuide("Bus");
            }else
            if(select_moda == 2){
                requestTransportationGuide("Taxi");
            }else
            if(select_moda == 3){
                requestTransportationGuide("Travel");
            }else
            if(select_moda == 4){
                requestTransportationGuide("Train");
            }else
            if(select_moda == 5){
                requestTransportationGuide("Other");
            }
        }else
        if(select_moda==6){
            rvTransportationGuide.setVisibility(View.GONE);
            mvTransportationGuide.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void requestTransportationGuide(String moda){
        Random random = new Random();
        int rnd = random.nextInt(999999 - 99) + 99;
        String tag_req_transport_moda = getResources().getString(R.string.tag_req_trasport_by_moda);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_get_transportation_moda))
                .concat(moda)
                .concat(getResources().getString(R.string.slash))
                .concat(String.valueOf(rnd))
                .concat(getResources().getString(R.string.slash));
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
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
                        llSuccess.setVisibility(View.GONE);
                        llError.setVisibility(View.VISIBLE);
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_req_transport_moda);
    }


    public void parseData(JSONObject responseJsonObj){
        try{
            String severity = responseJsonObj.getString(getResources().getString(R.string.json_key_severity));
            JSONObject content = responseJsonObj.getJSONObject(getResources().getString(R.string.json_key_content));
            if(severity.equals(getResources().getString(R.string.success))){
                JSONArray jsonArrayTransportationGuide = content.getJSONArray(getResources().getString(R.string.json_key_transportation));
                if(jsonArrayTransportationGuide.length() > 0){
                    for(int x=0;x<jsonArrayTransportationGuide.length();x++){
                        Log.d(getResources().getString(R.string.debug_tag),String.valueOf(x));
                        JSONObject objTransportationGuide = jsonArrayTransportationGuide.getJSONObject(x);

                        listTransportationGuide.add(new Transportation(
                                objTransportationGuide.getString(getResources().getString(R.string.transportation_transportation_id)),
                                objTransportationGuide.getString(getResources().getString(R.string.transportation_name)),
                                objTransportationGuide.getString(getResources().getString(R.string.transportation_moda)),
                                objTransportationGuide.getString(getResources().getString(R.string.transportation_origin)),
                                objTransportationGuide.getString(getResources().getString(R.string.transportation_destination)),
                                objTransportationGuide.getString(getResources().getString(R.string.transportation_schedule)),
                                objTransportationGuide.getString(getResources().getString(R.string.transportation_ticket_price)),
                                objTransportationGuide.getString(getResources().getString(R.string.transportation_origin_lat)),
                                objTransportationGuide.getString(getResources().getString(R.string.transportation_origin_lon)),
                                objTransportationGuide.getString(getResources().getString(R.string.transportation_destination_lat)),
                                objTransportationGuide.getString(getResources().getString(R.string.transportation_destination_lon))
                        ));

                    }
                }
            }else{
                llSuccess.setVisibility(View.GONE);
                llError.setVisibility(View.VISIBLE);
            }
        }catch (JSONException e){
            llSuccess.setVisibility(View.GONE);
            llError.setVisibility(View.VISIBLE);
            Log.d(getResources().getString(R.string.debug_tag),e.getMessage());
        }
        transportationAdapter.notifyDataSetChanged();
    }


    void setMapCenter(double lat,double lon){
        mapController = mvTransportationGuide.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(lat,lon);
        mapController.setCenter(startPoint);
    }


}


