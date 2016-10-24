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
    ArrayList<Building> buildingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        buildingList.add(new Building("MLK",new Coordinate(130,450),new Coordinate(235,637)));
        buildingList.add(new Building("MLK",new Coordinate(540,450),new Coordinate(690,680)));
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
