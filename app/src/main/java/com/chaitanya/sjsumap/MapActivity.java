package com.chaitanya.sjsumap;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        AlertDialog.Builder builder =  new AlertDialog.Builder(MapActivity.this);
        builder.setMessage("Map App Started " )
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();







}
}
