package com.chaitanya.sjsumap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BuildingActivity extends AppCompatActivity {

    TextView address, duration;
    Button streetView;
    ImageView buildingImage;
    double x = 37.334065;
    double y = -121.880664;
    double z = 37.335822;
    double zz = -121.882692;
    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + x+","+y + "&destinations=" +z+","+zz + "&key=AIzaSyBjHXGKoV9SebN7V4XHrAoRsB7h3Ie7ZVg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        Intent iBuilding = getIntent();
        final Building building = (Building)iBuilding.getParcelableExtra("building");
        address = (TextView)findViewById(R.id.buildingAddress);
        buildingImage = (ImageView)findViewById(R.id.buildingImageView);
        buildingImage.setBackgroundResource(building.image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        duration = (TextView)findViewById(R.id.duration);

        getSupportActionBar().setTitle(getResources().getString(building.name));
        address.setText(getResources().getString(building.address));

        duration.setText(getResources().getString(building.address));

        System.out.println(url);

        

        streetView = (Button)findViewById(R.id.streetView);
        streetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(BuildingActivity.this, StreetView.class);
            startActivity(intent);

                /*  Intent i = new Intent(login.this, your_new_activity_name.class);
    startActivity(i);
*/
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}