package com.chaitanya.sjsumap;

import android.Manifest;
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

import java.util.ArrayList;


public class MapActivity extends AppCompatActivity {

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

    double ImageSizeW = 1200;
    double ImageSizeH = 1275;

    double campusWidth;
    double campusHeight;

    Bitmap bmp;
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
        dot.setDrawingCacheEnabled(true);
        dotParams = new RelativeLayout.LayoutParams(50, 50);

        upperLeft.setLatitude(37.335813);
        upperLeft.setLongitude(-121.885899);

        upperRight.setLatitude(37.338893);
        upperRight.setLongitude(-121.879698);

        lowerLeft.setLatitude(37.334557);
        lowerLeft.setLongitude(-121.876453);

        lowerRight.setLatitude(37.331550);
        lowerRight.setLongitude(-121.882851);

        campusWidth = upperLeft.distanceTo(upperRight);
        campusHeight = upperLeft.distanceTo(lowerLeft);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        bmp = ((BitmapDrawable) imageView.getBackground()).getBitmap();

        //Adding Building as per list given by professor
        //Order is same as the list
        buildingList.add(new Building(1, R.drawable.kinglibrary, R.string.kingname,R.string.kingabb, R.string.kingaddress, new Coordinate(180, 720), new Coordinate(304, 945), R.string.kingstreetview, 37.335507, -121.884999));
        buildingList.add(new Building(2, R.drawable.engineeringbuilding, R.string.engname,R.string.engabb, R.string.engaddress, new Coordinate(714, 720), new Coordinate(918, 985), R.string.engstreetview, 37.337317, -121.882306));
        buildingList.add(new Building(3, R.drawable.yoshihirouchidahall, R.string.yuhname,R.string.yuhabb, R.string.yuhaddress, new Coordinate(160, 1280), new Coordinate(300, 1470), R.string.yuhstreetview, 37.3338, -121.8834));
        buildingList.add(new Building(4, R.drawable.studentunion, R.string.suname, R.string.suabb, R.string.suaddress, new Coordinate(720, 1010), new Coordinate(1065, 1155), R.string.sustreetview, 37.336216, -121.881373));
        buildingList.add(new Building(5, R.drawable.bbc, R.string.bbcname, R.string.bbcabb, R.string.bbcaddress, new Coordinate(1110, 1155), new Coordinate(1250, 1280), R.string.sustreetview, 37.337086, -121.878755));
        buildingList.add(new Building(6, R.drawable.southparkinggarage, R.string.spgname, R.string.spgabb, R.string.spgaddress, new Coordinate(450, 1710), new Coordinate(684, 1915), R.string.spgstreetview, 37.333195, -121.880096));

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
                    if ((getResources().getString(building.name).toUpperCase().contains(query.toUpperCase())) || (getResources().getString(building.abbreviation).toUpperCase().contains(query.toUpperCase())) && !query.equals("")) {
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
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLocation == null){
                    currentLocation = new Location("");
                    currentLocation.setLatitude(37.335813);
                    currentLocation.setLongitude(-121.885899);
                }
                if (relativeLayout.indexOfChild(dot) != 0) {
                    relativeLayout.removeView(dot);
                }
                double a = Math.abs(upperLeft.distanceTo(currentLocation));
                System.out.println("a = " + a);

                double b = Math.abs(lowerLeft.distanceTo(currentLocation));
                System.out.println("b = " + b);

                double c = Math.abs(campusHeight);
                System.out.println("c = " + c);

                double area = Math.sqrt((a+b+c)/2);
                double altitude = (2 * area) / c;

                double locationHeigth = Math.sqrt(Math.pow(a,2) - Math.pow(altitude,2));
                double locationWidth = altitude;

                double xpx = locationWidth * ImageSizeW / campusWidth;
                double ypx = locationHeigth * ImageSizeH / campusHeight;

                System.out.println("xpx = " + xpx);
                System.out.println("ypx = " + ypx);

                System.out.println("Current(" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + ")");
                System.out.println("Pixel(" + ((int) xpx + 105) + "," + ((int) ypx + 685) + ")");
                dotParams = new RelativeLayout.LayoutParams(100, 100);
                dotParams.leftMargin = (int) xpx + 105;
                dotParams.topMargin = (int) ypx + 685;
                relativeLayout.addView(dot, dotParams);
                dot.bringToFront();
                System.out.println("done");
            }
        });

        listener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        userLocation = location;
                        if (relativeLayout.indexOfChild(dot) != 0) {
                            relativeLayout.removeView(dot);
                        }
                        double a = Math.abs(upperLeft.distanceTo(location));
                        System.out.println("k = " + a);

                        double b = Math.abs(lowerLeft.distanceTo(location));
                        System.out.println("m = " + b);

                        double c = Math.abs(campusHeight);
                        System.out.println("n = " + c);

                        double area = Math.sqrt((a+b+c)/2);
                        double altitude = (2 * area) / c;

                        double locationHeigth = Math.sqrt(Math.pow(a,2) - Math.pow(altitude,2));
                        double locationWidth = altitude;

                        double xpx = locationWidth * ImageSizeW / campusWidth;
                        double ypx = locationHeigth * ImageSizeH / campusHeight;

                        System.out.println("xpx = " + xpx);
                        System.out.println("ypx = " + ypx);

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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        System.out.println("Ia m here");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
}