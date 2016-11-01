package com.chaitanya.sjsumap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

//import com.google.android.gms.common.api.GoogleApiClient;

//import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;


public class MapActivity extends AppCompatActivity {

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

//    protected GoogleApiClient mGoogleApiClient;
//
//    protected LocationRequest ?mLocationRequest;

    RelativeLayout relativeLayout;
    ArrayList<Building> buildingList = new ArrayList<Building>();
    SearchView searchBuilding;
    ImageView pin;
    RelativeLayout.LayoutParams pinParams;
    ImageView dot;
    RelativeLayout.LayoutParams dotParams;

    LocationListener listener;

    Location upperLeft = new Location("");
    Location upperRight = new Location("");
    Location lowerLeft = new Location("");
    Location lowerRight = new Location("");
    Location userLocation;

    double ImageSizeW;
    double ImageSizeH;

    LocationManager locationManager;

    Location currentLocation;

    Bitmap bmp;
    //(110,680) -> (37.335813, -121.885899)
    //(1297,1960) -> (37.334602, -121.876608)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        relativeLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        searchBuilding = (SearchView) findViewById(R.id.searchBuilding);
        pin = new ImageView(MapActivity.this);
        pin.setImageResource(R.drawable.pin);
        pin.setDrawingCacheEnabled(true);

        dot = new ImageView(MapActivity.this);
        dot.setImageResource(R.drawable.red_dot);
        dotParams = new RelativeLayout.LayoutParams(50, 50);

        upperLeft.setLatitude(37.335813);
        upperLeft.setLongitude(-121.885899);

        upperRight.setLatitude(37.338893);
        upperRight.setLongitude(-121.879698);

        lowerLeft.setLatitude(37.334557);
        lowerLeft.setLongitude(-121.876453);

        lowerRight.setLatitude(37.331550);
        lowerRight.setLongitude(-121.882851);

        final double campusWidth = upperLeft.distanceTo(upperRight);
        final double campusHeight = upperLeft.distanceTo(lowerLeft);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        bmp = ((BitmapDrawable) imageView.getBackground()).getBitmap();
        ImageSizeW = relativeLayout.getWidth();
        ImageSizeH = relativeLayout.getHeight();
        //Adding Building as per list given by professor
        //Order is same as the list

        buildingList.add(new Building(1, R.drawable.kinglibrary, R.string.kingname, R.string.kingaddress, new Coordinate(180, 720), new Coordinate(304, 945), R.string.kingstreetview, 37.335507, -121.884999));
        buildingList.add(new Building(2, R.drawable.engineeringbuilding, R.string.engname, R.string.kingaddress, new Coordinate(714, 720), new Coordinate(918, 985), R.string.engstreetview, 37.337317, -121.882306));
        buildingList.add(new Building(3, R.drawable.yoshihirouchidahall, R.string.yuhname, R.string.yuhaddress, new Coordinate(160, 1280), new Coordinate(300, 1470), R.string.yuhstreetview, 37.3338, -121.8834));
        buildingList.add(new Building(4, R.drawable.studentunion, R.string.suname, R.string.suaddress, new Coordinate(720, 1010), new Coordinate(1065, 1155), R.string.sustreetview, 37.336216, -121.881373));
        buildingList.add(new Building(5, R.drawable.bbc, R.string.bbcname, R.string.bbcaddress, new Coordinate(1110, 1155), new Coordinate(1250, 1280), R.string.sustreetview, 37.337086, -121.878755));
        buildingList.add(new Building(6, R.drawable.southparkinggarage, R.string.spgname, R.string.spgaddress, new Coordinate(450, 1710), new Coordinate(684, 1915), R.string.spgstreetview, 37.333195, -121.880096));


        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("(" + ((int) event.getX() - v.getLeft()) + "," + ((int) event.getY() - v.getTop()) + ")");
                for (Building building : buildingList) {
                    if (building.checkTouch((int) event.getX() - v.getLeft(), (int) event.getY() - v.getTop())) {
                        try {
                            Intent buildingIntent = new Intent(MapActivity.this, Class.forName("com.chaitanya.sjsumap.BuildingActivity"));
                            buildingIntent.putExtra("building", building);
                            if (currentLocation != null) {
                                buildingIntent.putExtra("currLatitude", currentLocation.getLatitude());
                                buildingIntent.putExtra("currLongitude", currentLocation.getLongitude());
                            }
                            startActivity(buildingIntent);
                        } catch (ClassNotFoundException e) {
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
                for (Building building : buildingList) {
                    if ((getResources().getString(building.name).contains(query)) && query != null) {
                        if (relativeLayout.indexOfChild(pin) != 0)
                            relativeLayout.removeView(pin);
                        pinParams = new RelativeLayout.LayoutParams(100, 100);
                        pinParams.leftMargin = building.start.x;
                        pinParams.topMargin = building.start.y;
                        relativeLayout.addView(pin, pinParams);
                        break;
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    if (relativeLayout.indexOfChild(pin) != 0)
                        relativeLayout.removeView(pin);
                }
                return false;
            }
        });
        ImageButton imageButton = (ImageButton) findViewById(R.id.getLocation);
        imageButton.bringToFront();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                System.out.println("permission given");
                return;
            }else{
                System.out.println("no perm");
            }
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(currentLocation != null)
                System.out.println("Got " + currentLocation.getLatitude());
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        userLocation = location;
                        if(relativeLayout.indexOfChild(dot) != 0){
                            relativeLayout.removeView(dot);
                        }
                        double translate = (Math.pow(Math.abs(upperLeft.distanceTo(location)),2) + Math.pow(Math.abs(lowerLeft.distanceTo(location)),2) + Math.pow(Math.abs(campusHeight), 2))/(2*campusHeight);
                        double xpx = getXPixel(location, translate);
                        double ypx = getYPixel(location, translate);
                        System.out.println("Current(" + location.getLatitude() + "," + location.getLongitude() + ")");
                        System.out.println("Pixel(" + ((int)xpx+105) + "," + ((int)ypx+685) + ")");
                        dotParams = new RelativeLayout.LayoutParams(100,100);
                        dotParams.leftMargin = (int)xpx + 105;
                        dotParams.topMargin = (int)ypx + 685;
                        relativeLayout.addView(dot, dotParams);
                        dot.bringToFront();
                        System.out.println("done");
                    }

                    public double getXPixel(Location location, double translate){
                        return (Math.sqrt(Math.abs(Math.pow(Math.abs(upperLeft.distanceTo(location)),2)) - Math.pow(translate,2))) * ImageSizeW / campusWidth;
                    }
                    public double getYPixel(Location current, double translate){
                        return translate*ImageSizeH/campusHeight;
                    }


                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                    userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(userLocation != null)            System.out.println(userLocation.toString());
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }
        });

//
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
//                    1);
//        } else {
//            buildGoogleApiClient();
//            mGoogleApiClient.connect();
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 1: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    buildGoogleApiClient();
//                    mGoogleApiClient.connect();
//
//                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//                        startLocationUpdates();
//                    }
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//
//                } else {
//
//                    Toast.makeText(MapActivity.this, "You need to provide location permission in order to use this app", Toast.LENGTH_SHORT).show();
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            startLocationUpdates();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
//            mGoogleApiClient.disconnect();
//    }
//
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//        createLocationRequest();
//    }
//
//    protected void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        // Sets the desired interval for active location updates. This interval
//        // is
//        // inexact. You may not receive updates at all if no location sources
//        // are available, or
//        // you may receive them slower than requested. You may also receive
//        // updates faster than
//        // requested if other applications are requesting location at a faster
//        // interval.
//        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
//        // Sets the fastest rate for active location updates. This interval is
//        // exact, and your
//        // application will never receive updates faster than this value.
//        mLocationRequest
//                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//    protected void startLocationUpdates() {
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, this);
//    }
//
//    Location lastKnownLocation;
//
//
//    @Override
//    public void onConnected(Bundle connectionHint) {
//
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        lastKnownLocation = LocationServices.FusedLocationApi
//                .getLastLocation(mGoogleApiClient);
//
//            startLocationUpdates();
//
//        if (lastKnownLocation != null)
//        {
//            System.err.println("Last known location " + lastKnownLocation.getLatitude() + ", " + lastKnownLocation.getLongitude());
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int arg0) {
//        Log.i("Google Api", "Connection suspended");
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult arg0) {
//        // TODO Auto-generated method stub
//        Log.i("Google Api", "Connection failed");
//    }


//    @Override
//    public void onLocationChanged(Location location) {

//        lastKnownLocation = location;
//
//        if(relativeLayout.indexOfChild(dot) != 0){
//            relativeLayout.removeView(dot);
//        }
//        double xpx = getXPixel(location);
//        double ypx = getYPixel(location);
//        System.out.println("Current(" + location.getLatitude() + "," + location.getLongitude() + ")");
//        System.out.println("Pixel(" + ((int)xpx+105) + "," + ((int)ypx+685) + ")");
//        dotParams = new RelativeLayout.LayoutParams(100,100);
//        dotParams.leftMargin = (int)xpx + 105;
//        dotParams.topMargin = (int)ypx + 685;
//        relativeLayout.addView(dot, dotParams);
//        dot.bringToFront();
//        System.out.println("done");
//    }

//    public final static double OneEightyDeg = 180.0d;
//    public double ImageSizeW, ImageSizeH;

//    public double getXPixel(Location current){
//        double hypotenuse = upperLeft.distanceTo(current);
//        System.out.println("Hypo" + hypotenuse);
//        double bearing = upperLeft.bearingTo(current);
//        System.out.println("bearing" + bearing);
//        double currentDistanceX = Math.sin(bearing * Math.PI / OneEightyDeg) * hypotenuse;
//        //                           "percentage to mark the position"
//        System.out.println("current distanc x" + currentDistanceX);
//        double totalHypotenuse = upperLeft.distanceTo(lowerRight);
//        System.out.println("total hypo" + totalHypotenuse);
//        double totalDistanceX = totalHypotenuse * Math.sin(upperLeft.bearingTo(lowerRight) * Math.PI / OneEightyDeg);
//        System.out.println("total dist" + totalDistanceX);
//        System.out.println("Image" + ImageSizeW);
//        double currentPixelX = currentDistanceX / totalDistanceX * ImageSizeW;
//        double currentPixelX = current.getLatitude() * Math.PI;
//        return currentPixelX ;
//    }
//
//    public double getYPixel(Location current){
//        double hypotenuse = upperLeft.distanceTo(current);
//        double bearing = upperLeft.bearingTo(current);
//        double currentDistanceY = Math.cos(bearing * Math.PI / OneEightyDeg) * hypotenuse;
//        //                           "percentage to mark the position"
//        double totalHypotenuse = upperLeft.distanceTo(lowerRight);
//        double totalDistanceY = totalHypotenuse * Math.cos(upperLeft.bearingTo(lowerRight) * Math.PI / OneEightyDeg);
//        double currentPixelY = currentDistanceY / totalDistanceY * ImageSizeH;
//        System.out.println("height image" + ImageSizeH);
//        return currentPixelY;
//    }

//    @Override
//    public void onMyLocationChange(Location location) {
//        System.out.println(" My Location: lat " + location.getLatitude() + " longitude " + location.getLongitude());
//    }
}