package com.chaitanya.sjsumap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BuildingActivity extends AppCompatActivity {

    TextView name, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        Intent iBuilding = getIntent();
        Building building = (Building)iBuilding.getParcelableExtra("building");
        name = (TextView)findViewById(R.id.buildingName);
        address = (TextView)findViewById(R.id.buildingAddress);
        name.setText(building.name);
        address.setText(building.address);
    }
}
