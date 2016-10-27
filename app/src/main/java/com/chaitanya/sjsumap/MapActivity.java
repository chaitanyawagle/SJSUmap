package com.chaitanya.sjsumap;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
        buildingList.add(new Building(1,R.drawable.kinglibrary,"King Library","150 East San Fernando Street, San Jose, CA 95112",new Coordinate(180,720),new Coordinate(304,945),"https://www.google.com/maps/uv?hl=en&pb=!1s0x808fccbc0b22b081:0xe370813fc863d57f!2m13!2m2!1i80!2i80!3m1!2i20!16m7!1b1!2m2!1m1!1e1!2m2!1m1!1e3!3m1!7e115!4s/maps/place/kings%2Blibrary/@37.3360532,-121.8855803,3a,75y,139.76h,90t/data%3D*213m4*211e1*213m2*211scUN-8ElgDRMt2c_kqqLKDQ*212e0*214m2*213m1*211s0x0:0xe370813fc863d57f!5skings+library+-+Google+Search&imagekey=!1e2!2scUN-8ElgDRMt2c_kqqLKDQ&sa=X&ved=0ahUKEwiS-bWOxvrPAhXHg1QKHdByCowQpx8IezAK"));
        buildingList.add(new Building(2,R.drawable.engineeringbuilding,"Engineering Building","1 Washington Square, San Jose, CA 95112",new Coordinate(714,720),new Coordinate(918,985),"https://www.google.com/maps/@37.3377787,-121.8820119,3a,75y,146.84h,75.66t/data=!3m6!1e1!3m4!1soO4EqGCOWwLy5eXy-EKx-g!2e0!7i13312!8i6656!6m1!1e1"));
        buildingList.add(new Building(3,R.drawable.yoshihirouchidahall,"Yoshihiro Uchida Hall","Yoshihiro Uchida Hall, San Jose, CA 95112",new Coordinate(160,1280),new Coordinate(300,1470),"https://www.google.com/maps/@37.3335287,-121.8843338,3a,75y,63.46h,84.1t/data=!3m6!1e1!3m4!1stBY5-l3dxDZ9q003KFjVkw!2e0!7i13312!8i6656"));
        buildingList.add(new Building(4,R.drawable.studentunion,"Student Union","Student Union Building, San Jose, CA 95112",new Coordinate(720,1010),new Coordinate(1065,1155),"unknown "));
        buildingList.add(new Building(5,R.drawable.bbc, "BBC","Boccardo Business Complex, San Jose, CA 95112",new Coordinate(1110,1155),new Coordinate(1250,1280),"https://www.google.com/maps/@37.3372125,-121.8784565,3a,75y,278.88h,84.51t/data=!3m6!1e1!3m4!1sBaI2lHplpsQMZCNik6C8zQ!2e0!7i13312!8i6656"));
        buildingList.add(new Building(6,R.drawable.southparkinggarage,"South Parking Garage","330 South 7th Street, San Jose, CA 95112",new Coordinate(450,1710),new Coordinate(684,1915),"https://www.google.com/maps/@37.3386745,-121.8809632,3a,75y,355.98h,92.61t/data=!3m6!1e1!3m4!1s3O0i45z_Vl7Q9-as0iaIEg!2e0!7i13312!8i6656!6m1!1e1"));

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
