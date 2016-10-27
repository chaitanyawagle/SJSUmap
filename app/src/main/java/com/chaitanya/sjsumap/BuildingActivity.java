package com.chaitanya.sjsumap;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BuildingActivity extends AppCompatActivity {

    TextView name, address, duration;
    Button streetView;
    ImageView buildingImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        Intent iBuilding = getIntent();
        final Building building = (Building)iBuilding.getParcelableExtra("building");
        name = (TextView)findViewById(R.id.buildingName);
        address = (TextView)findViewById(R.id.buildingAddress);
        buildingImage = (ImageView)findViewById(R.id.buildingImageView);
        buildingImage.setBackgroundResource(building.image);
        name.setText(building.name);
        address.setText(building.address);
        streetView = (Button)findViewById(R.id.streetView);
                streetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(building.url));
                startActivity(intent);
            }
        });
    }
}