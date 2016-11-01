package com.chaitanya.sjsumap;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
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
    ImageButton imageButton;


    LocationListener listener;
    LocationManager locationManager;

    Location upperLeft = new Location("");
    Location upperRight = new Location("");
    Location lowerLeft = new Location("");
    Location lowerRight = new Location("");

    Location userLocation;
    Location currentLocation;

    double ImageSizeW;
    double ImageSizeH;
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
        buildingList.add(new Building(2, R.drawable.engineeringbuilding, R.string.engname, R.string.kingaddress, new Coordinate(714, 720), new Coordinate(918, 985), R.string.engstreetview, 37.335142, -121.881276));
        buildingList.add(new Building(3, R.drawable.yoshihirouchidahall, R.string.yuhname, R.string.yuhaddress, new Coordinate(160, 1280), new Coordinate(300, 1470), R.string.yuhstreetview, -121.879916, -121.879916));
        buildingList.add(new Building(4, R.drawable.studentunion, R.string.suname, R.string.suaddress, new Coordinate(720, 1010), new Coordinate(1065, 1155), R.string.sustreetview, 37.424197, -122.170939));
        buildingList.add(new Building(5, R.drawable.bbc, R.string.bbcname, R.string.bbcaddress, new Coordinate(1110, 1155), new Coordinate(1250, 1280), R.string.sustreetview, 37.336561, -121.878723));
        buildingList.add(new Building(6, R.drawable.southparkinggarage, R.string.spgname, R.string.spgaddress, new Coordinate(450, 1710), new Coordinate(684, 1915), R.string.spgstreetview, 37.333474, -121.879916));


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

        imageButton = (ImageButton) findViewById(R.id.getLocation);
        imageButton.bringToFront();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = location;
                if (relativeLayout.indexOfChild(dot) != 0) {
                    relativeLayout.removeView(dot);
                }
                double translate = (Math.pow(Math.abs(upperLeft.distanceTo(location)), 2) + Math.pow(Math.abs(lowerLeft.distanceTo(location)), 2) + Math.pow(Math.abs(campusHeight), 2)) / (2 * campusHeight);
                double xpx = getXPixel(location, translate);
                double ypx = getYPixel(location, translate);
                System.out.println("Current(" + location.getLatitude() + "," + location.getLongitude() + ")");
                System.out.println("Pixel(" + ((int) xpx + 105) + "," + ((int) ypx + 685) + ")");
                dotParams = new RelativeLayout.LayoutParams(100, 100);
                dotParams.leftMargin = (int) xpx + 105;
                dotParams.topMargin = (int) ypx + 685;
                relativeLayout.addView(dot, dotParams);
                dot.bringToFront();
                System.out.println("done");
            }

            public double getXPixel(Location location, double translate) {
                return (Math.sqrt(Math.abs(Math.pow(Math.abs(upperLeft.distanceTo(location)), 2)) - Math.pow(translate, 2))) * ImageSizeW / campusWidth;
            }

            public double getYPixel(Location current, double translate) {
                return translate * ImageSizeH / campusHeight;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET}, 1);
            }
            return;
        } else {
            setLocationButton();
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                return;
        }
    }

    private void setLocationButton() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
}