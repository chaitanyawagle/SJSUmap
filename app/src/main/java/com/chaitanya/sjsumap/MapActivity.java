package com.chaitanya.sjsumap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;



public class MapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMyLocationChangeListener{

    /**
     * The desired interval for location updates. Inexact. Updates may be more
     * or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never
     * be more frequent than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    protected GoogleApiClient mGoogleApiClient;

    protected LocationRequest mLocationRequest;

    RelativeLayout relativeLayout;
    ArrayList<Building> buildingList = new ArrayList<Building>();
    SearchView searchBuilding;
    ImageView pin;
    RelativeLayout.LayoutParams pinParams;

    //(110,685) -> (37.335813, -121.885899)
    //(1297,1960) -> (37.334602, -121.876608)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        relativeLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        searchBuilding = (SearchView)findViewById(R.id.searchBuilding);
        pin = new ImageView(MapActivity.this);
        pin.setImageResource(R.drawable.pin);
        //Adding Building as per list given by professor
        //Order is same as the list
        buildingList.add(new Building(1,R.drawable.kinglibrary,R.string.kingname,R.string.kingaddress,new Coordinate(180,720),new Coordinate(304,945),R.string.kingstreetview));
        buildingList.add(new Building(2,R.drawable.engineeringbuilding,R.string.engname,R.string.kingaddress,new Coordinate(714,720),new Coordinate(918,985),R.string.engstreetview));
        buildingList.add(new Building(3,R.drawable.yoshihirouchidahall,R.string.yuhname ,R.string.yuhaddress,new Coordinate(160,1280),new Coordinate(300,1470),R.string.yuhstreetview));
        buildingList.add(new Building(4,R.drawable.studentunion,R.string.suname,R.string.suaddress,new Coordinate(720,1010),new Coordinate(1065,1155),R.string.suaddress));
        buildingList.add(new Building(5,R.drawable.bbc,R.string.bbcname,R.string.bbcaddress,new Coordinate(1110,1155),new Coordinate(1250,1280),R.string.bbcstreetview));
        buildingList.add(new Building(6,R.drawable.southparkinggarage,R.string.spgname,R.string.spgaddress,new Coordinate(450,1710),new Coordinate(684,1915),R.string.spgstreetview));

        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("(" + ((int)event.getX() - v.getLeft()) + "," + ((int)event.getY()- v.getTop()) + ")");
                for(Building building: buildingList){
                    if(building.checkTouch((int)event.getX() - v.getLeft(),(int)event.getY()- v.getTop())){
                        try {
                            Intent buildingIntent = new Intent(MapActivity.this, Class.forName("com.chaitanya.sjsumap.BuildingActivity"));
                            buildingIntent.putExtra("building",building);
                            startActivity(buildingIntent);
                        }catch (ClassNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });

        searchBuilding.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for(Building building: buildingList){
                    if(query.equals(building.name)){
                        if(relativeLayout.indexOfChild(pin) != 0)
                            relativeLayout.removeView(pin);
                        pinParams = new RelativeLayout.LayoutParams(100,100);
                        //((building.start.x + building.end.x)/2) - building.start.x
//                        ((building.start.y + building.end.y)/2) - building.start.y
                        pinParams.leftMargin = building.start.x;
                        pinParams.topMargin = building.start.y;
                        relativeLayout.addView(pin, pinParams);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                System.out.println(newText);
                return false;
            }
        });

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
        }
        else {

            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    buildGoogleApiClient();
                    mGoogleApiClient.connect();

                    if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
                    {
                        startLocationUpdates();
                    }


                } else {

                    Toast.makeText(MapActivity.this, "You need to provide location permission in order to use this app", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
        {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        // Sets the desired interval for active location updates. This interval
        // is
        // inexact. You may not receive updates at all if no location sources
        // are available, or
        // you may receive them slower than requested. You may also receive
        // updates faster than
        // requested if other applications are requesting location at a faster
        // interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates. This interval is
        // exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a
        // LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Location lastKnownLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

            startLocationUpdates();

        if (lastKnownLocation != null)
        {
            System.err.println("Last known location " + lastKnownLocation.getLatitude() + ", " + lastKnownLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Log.i("Google Api", "Connection suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
        Log.i("Google Api", "Connection failed");
    }

    ;


    @Override
    public void onLocationChanged(Location location) {
        ImageView dot = new ImageView(MapActivity.this);
        dot.setImageResource(R.drawable.red_dot);
        RelativeLayout.LayoutParams dotParams = new RelativeLayout.LayoutParams(20,20);
        dotParams.leftMargin = 644;
        dotParams.topMargin = 765;
        relativeLayout.addView(dot, dotParams);
    }

    @Override
    public void onMyLocationChange(Location location) {
        System.out.println(" My Location: lat " + location.getLatitude() + " longitude " + location.getLongitude());
    }
}
