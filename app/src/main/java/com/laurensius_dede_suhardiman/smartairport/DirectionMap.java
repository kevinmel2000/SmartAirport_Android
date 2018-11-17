package com.laurensius_dede_suhardiman.smartairport;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.laurensius_dede_suhardiman.smartairport.model.Facility;
import com.laurensius_dede_suhardiman.smartairport.model.ParkingArea;
import com.laurensius_dede_suhardiman.smartairport.model.Tourism;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.util.ArrayList;
import java.util.List;

public class DirectionMap extends AppCompatActivity {

    private Boolean create_route = false;

    private MapView mvDirection;
    private ScaleBarOverlay mScaleBarOverlay;
    private IMapController mapController;

    private LocationManager locationManager;
    private LocationListener listener;
    private RoadManager roadManager;
    private Road road;

    ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
    GeoPoint startPoint, endPoint;
    Polyline roadOverlay;


    private Intent intent;

    private Tourism tourism;
    private ParkingArea parking;
    private Facility facility;

    public static double lat;
    public static double lon;

    String type = "";

    Marker destinationMarker, bijbMarker, userMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_map);
        mvDirection = (MapView)findViewById(R.id.mv_direction);
        //map


        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_direction));

        mvDirection.setTileSource(TileSourceFactory.MAPNIK);
        mvDirection.setBuiltInZoomControls(true);
        mvDirection.setMultiTouchControls(true);
        mvDirection.setPadding(0,0,0,90);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        mScaleBarOverlay = new ScaleBarOverlay(mvDirection);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(width / 2, 20);
        mvDirection.getOverlays().add(mScaleBarOverlay);
        intentCheck();
        mvDirection.invalidate();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                if((lat != 0 && lon != 0) && create_route == false){
                    new AsyncDirection().execute();
                }
                removeUserMarker();
                setUserMarker(lat,lon);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configureGPS();
        configureStorage();


    }

    void intentCheck(){
        intent = getIntent();
        type = intent.getStringExtra("object_type");
        if(type.equals("tourism") ==  true){
            tourism = (Tourism)intent.getSerializableExtra("destinationObject");
            setMapCenter(Double.valueOf(tourism.getLatitude()), Double.valueOf(tourism.getLongitude()));
            setDestionationMarker(Double.valueOf(tourism.getLatitude()), Double.valueOf(tourism.getLongitude()));
        }else
        if(type.equals("facility") ==  true){
            facility = (Facility) intent.getSerializableExtra("destinationObject");
        }else
        if(type.equals("parking") ==  true){
            parking = (ParkingArea) intent.getSerializableExtra("parkingObject");
            setMapCenter(Double.valueOf(parking.getLatitude()), Double.valueOf(parking.getLongitude()));
            setDestionationMarker(Double.valueOf(parking.getLatitude()), Double.valueOf(parking.getLongitude()));
        }
    }

    void setMapCenter(double lat,double lon){
        mapController = mvDirection.getController();
        mapController.setZoom(10);
        GeoPoint center = new GeoPoint(lat,lon);
        mapController.setCenter(center);
    }


    void setUserMarker(double lat,double lon){
        userMarker = new Marker(mvDirection);
        Drawable iconMarker = ResourcesCompat.getDrawable(getResources(), R.drawable.user_marker, null);
        userMarker.setIcon(iconMarker);
        userMarker.setTitle("Hello!");
        userMarker.setSnippet ("You are here! It's your current location.");
        userMarker.setSubDescription ("Coordinate :" + String.valueOf(lat) + " , " + String.valueOf(lon));
        userMarker.setPosition(new GeoPoint(lat, lon));
        userMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                userMarker.showInfoWindow();
                return false;
            }
        });
        mvDirection.getOverlays().add(userMarker);
        mvDirection.invalidate();
    }

    void removeUserMarker(){
        mvDirection.getOverlays().remove(userMarker);
        mvDirection.invalidate();
    }

    void setDestionationMarker(double lat,double lon){
        destinationMarker = new Marker(mvDirection);
        Drawable iconMarker = ResourcesCompat.getDrawable(getResources(), R.drawable.destination_marker, null);
        destinationMarker.setIcon(iconMarker);
        if(type.equals("tourism")){
            destinationMarker.setTitle(tourism.getName());
            destinationMarker.setSnippet(tourism.getDescription());
            destinationMarker.setSubDescription(tourism.getAddress());
        }else
        if(type.equals("facility")){
            destinationMarker.setTitle(facility.getName());
            destinationMarker.setSnippet(facility.getDescription());
        }else
        if(type.equals("parking")){
            destinationMarker.setTitle(parking.getName());
            destinationMarker.setSnippet("Car : " + parking.getStatus_car() + " # Motorcycle  : " + parking.getStatus_motorcycle() + "\n" );
            destinationMarker.setSubDescription(parking.getDescription());
        }
        destinationMarker.setPosition(new GeoPoint(lat, lon));
        destinationMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                destinationMarker.showInfoWindow();
                return false;
            }
        });
        mvDirection.getOverlays().add(destinationMarker);
        mvDirection.invalidate();
    }

    void configureGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        locationManager.requestLocationUpdates(getResources().getString(R.string.provider_gps), 5000, 0, listener);
    }

    void configureStorage(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private class AsyncDirection extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            startPoint = new GeoPoint(lat,lon);
            if(type.equals("tourism")){
                endPoint = new GeoPoint(Double.valueOf(tourism.getLatitude()), Double.valueOf(tourism.getLongitude()));
            }else
            if(type.equals("facility")){
                endPoint = new GeoPoint(Double.valueOf(facility.getLatitude()), Double.valueOf(facility.getLongitude()));
            }else
            if(type.equals("parking")){
                endPoint = new GeoPoint(Double.valueOf(parking.getLatitude()), Double.valueOf(parking.getLongitude()));
            }

            waypoints.add(startPoint);
            waypoints.add(endPoint);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            roadManager = new OSRMRoadManager(getApplicationContext());
            road = roadManager.getRoad(waypoints);
            roadOverlay = RoadManager.buildRoadOverlay(road);
            while(roadOverlay.getNumberOfPoints() < 1){
                roadOverlay = RoadManager.buildRoadOverlay(road);
                Log.d("DEBUG DIRECTION" , String.valueOf(roadOverlay.getNumberOfPoints()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mvDirection.getOverlays().add(roadOverlay);
            mvDirection.invalidate();
            create_route = true;
        }
    }
}
