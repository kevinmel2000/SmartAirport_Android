package com.laurensius_dede_suhardiman.smartairport.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.adapter.FacilityAdapter;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;
import com.laurensius_dede_suhardiman.smartairport.model.Facility;

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

public class FragmentAirportFacilities extends Fragment {

    private RecyclerView rvFacilities;
    private FacilityAdapter facilityAdapter= null;
    RecyclerView.LayoutManager mLayoutManager;
    List<Facility> listFacilities = new ArrayList<>();

    private MapView mvFacilities = null;
    private ScaleBarOverlay mScaleBarOverlay;
    private IMapController mapController;

    private LocationManager locationManager;
    private LocationListener listener;
    private RoadManager roadManager;
    private Road road;

    int list_map;
    public FragmentAirportFacilities() {}


    public static FragmentAirportFacilities newInstance(int sectionNumber) {
        FragmentAirportFacilities fragment = new FragmentAirportFacilities();
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
        View inflaterFacilities = inflater.inflate(R.layout.fragment_airport_facilities, container, false);
        rvFacilities = (RecyclerView)inflaterFacilities.findViewById(R.id.rv_airport_facilities);
        mvFacilities = (MapView)inflaterFacilities.findViewById(R.id.mv_facilities);
        return inflaterFacilities;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        list_map = getArguments().getInt("section_number");

        rvFacilities.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvFacilities.setLayoutManager(mLayoutManager);
        facilityAdapter= new FacilityAdapter(listFacilities,list_map);
        facilityAdapter.notifyDataSetChanged();
        rvFacilities.setAdapter(facilityAdapter);

        //map
        mvFacilities.setTileSource(TileSourceFactory.MAPNIK);
        mvFacilities.setBuiltInZoomControls(true);
        mvFacilities.setMultiTouchControls(true);
        mvFacilities.setPadding(0,0,0,90);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        mScaleBarOverlay = new ScaleBarOverlay(mvFacilities);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(width / 2, 20);
        mvFacilities.getOverlays().add(mScaleBarOverlay);
        mvFacilities.invalidate();
        setMapCenter(-6.65190, 108.15780);

        requestFacilities();

        if(list_map==1){
            rvFacilities.setVisibility(View.VISIBLE);
            mvFacilities.setVisibility(View.GONE);
        }else
        if(list_map==2){
            rvFacilities.setVisibility(View.GONE);
            mvFacilities.setVisibility(View.VISIBLE);
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


    public void requestFacilities(){
        Random random = new Random();
        int rnd = random.nextInt(999999 - 99) + 99;
        String tag_req_airport_facilities= getResources().getString(R.string.tag_req_airport_facilities);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_get_facilities))
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
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_req_airport_facilities);
    }


    public void parseData(JSONObject responseJsonObj){
        try{
            String severity = responseJsonObj.getString(getResources().getString(R.string.json_key_severity));
            JSONObject content = responseJsonObj.getJSONObject(getResources().getString(R.string.json_key_content));
            if(severity.equals(getResources().getString(R.string.success))){
                JSONArray jsonArrayFacilities = content.getJSONArray(getResources().getString(R.string.json_key_facilities));
                if(jsonArrayFacilities.length() > 0){
                    for(int x=0;x<jsonArrayFacilities.length();x++){
                        Log.d(getResources().getString(R.string.debug_tag),String.valueOf(x));
                        JSONObject objFacilities = jsonArrayFacilities.getJSONObject(x);

                        setFacilityMarker(
                                Double.valueOf(objFacilities.getString(getResources().getString(R.string.facility_latitude))),
                                Double.valueOf(objFacilities.getString(getResources().getString(R.string.facility_longitude))),
                                objFacilities.getString(getResources().getString(R.string.facility_name)),
                                objFacilities.getString(getResources().getString(R.string.facility_description)),
                                objFacilities.getString(getResources().getString(R.string.facility_marker))
                        );

                        listFacilities.add(new Facility(
                                objFacilities.getString(getResources().getString(R.string.facility_id)),
                                objFacilities.getString(getResources().getString(R.string.facility_name)),
                                objFacilities.getString(getResources().getString(R.string.facility_latitude)),
                                objFacilities.getString(getResources().getString(R.string.facility_longitude)),
                                objFacilities.getString(getResources().getString(R.string.facility_location)),
                                objFacilities.getString(getResources().getString(R.string.facility_description)),
                                objFacilities.getString(getResources().getString(R.string.facility_image)),
                                objFacilities.getString(getResources().getString(R.string.facility_marker))
                        ));

                    }
                }
            }
        }catch (JSONException e){
            Log.d(getResources().getString(R.string.debug_tag),e.getMessage());
        }
        facilityAdapter.notifyDataSetChanged();
    }


    void setMapCenter(double lat,double lon){
        mapController = mvFacilities.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(lat,lon);
        mapController.setCenter(startPoint);
    }

    void setFacilityMarker(double lat, double lon, String name,String description,String marker){
        final Marker facilityMarker = new Marker(mvFacilities);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(getResources().getString(R.string.url_marker) + marker, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Drawable iconMarker = new BitmapDrawable(response.getBitmap());
                facilityMarker.setIcon(iconMarker);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley image error ","ERROR");
            }
        });

        facilityMarker.setPosition(new GeoPoint(lat, lon));
        facilityMarker.setTitle(name);
        facilityMarker.setSnippet(description);
        facilityMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                facilityMarker.showInfoWindow();
                return false;
            }
        });
        mvFacilities.getOverlays().add(facilityMarker);
        mvFacilities.invalidate();
    }

}


