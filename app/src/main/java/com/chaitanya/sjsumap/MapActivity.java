package com.chaitanya.sjsumap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    ArrayList<Building> buildingList = new ArrayList<Building>();
    SearchView searchBuilding;
    ImageView pin;
    RelativeLayout.LayoutParams pinParams;
    LocationManager locationManager;
    //(110,685) -> (37.335813, -121.885899)
    //(1297,1960) -> (37.334602, -121.876608)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        relativeLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        searchBuilding = (SearchView) findViewById(R.id.searchBuilding);
        pin = new ImageView(MapActivity.this);
        pin.setImageResource(R.drawable.pin);

        //Adding Building as per list given by professor
        //Order is same as the list
        buildingList.add(new Building(1,R.drawable.kinglibrary,"King Library","150 East San Fernando Street, San Jose, CA 95112",new Coordinate(180,720),new Coordinate(304,945)));
        buildingList.add(new Building(2,R.drawable.engineeringbuilding,"Engineering Building","1 Washington Square, San Jose, CA 95112",new Coordinate(714,720),new Coordinate(918,985)));
        buildingList.add(new Building(3,R.drawable.yoshihirouchidahall,"Yoshihiro Uchida Hall","Yoshihiro Uchida Hall, San Jose, CA 95112",new Coordinate(160,1280),new Coordinate(300,1470)));
        buildingList.add(new Building(4,R.drawable.studentunion,"Student Union","Student Union Building, San Jose, CA 95112",new Coordinate(720,1010),new Coordinate(1065,1155)));
        buildingList.add(new Building(5,R.drawable.bbc, "BBC","Boccardo Business Complex, San Jose, CA 95112",new Coordinate(1110,1155),new Coordinate(1250,1280)));
        buildingList.add(new Building(6,R.drawable.southparkinggarage,"South Parking Garage","330 South 7th Street, San Jose, CA 95112",new Coordinate(450,1710),new Coordinate(684,1915)));

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
                        pinParams = new RelativeLayout.LayoutParams(((building.start.x + building.end.x)/2) - building.start.x,((building.start.y + building.end.y)/2) - building.start.y);
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
    }
}
