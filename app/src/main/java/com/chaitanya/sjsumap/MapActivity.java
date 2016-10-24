package com.chaitanya.sjsumap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    ArrayList<Building> buildingList = new ArrayList<Building>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //Adding Building as per list given by professor
        //Order is same as the list
        buildingList.add(new Building(1,"MLK","MLK Address",new Coordinate(170,600),new Coordinate(310,850)));
        buildingList.add(new Building(2,"ENG","ENG Address",new Coordinate(720,600),new Coordinate(920,900)));
        buildingList.add(new Building(3,"YUH","YUH Address",new Coordinate(150,1210),new Coordinate(300,1420)));
        buildingList.add(new Building(4,"SU","SU Address",new Coordinate(720,930),new Coordinate(1065,1070)));
        buildingList.add(new Building(5,"BBC","BBC Address",new Coordinate(1110,1075),new Coordinate(1250,1215)));
        buildingList.add(new Building(6,"SPG","SPG Address",new Coordinate(450,1675),new Coordinate(695,1910)));
        relativeLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("(" + ((int)event.getX() - v.getLeft()) + "," + ((int)event.getY()- v.getTop()) + ")");
                for(Building building: buildingList){
                    if(building.checkTouch((int)event.getX() - v.getLeft(),(int)event.getY()- v.getTop())){
                        Toast.makeText(MapActivity.this, building.name.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }
}
