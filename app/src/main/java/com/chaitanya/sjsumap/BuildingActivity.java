package com.chaitanya.sjsumap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class BuildingActivity extends AppCompatActivity {

    TextView address, duration;
    Button streetView;
    ImageView buildingImage;
    double x = 37.334065;
    double y = -121.880664;
    double z = 37.335822;
    double zz = -121.882692;
    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + x + "," + y + "&destinations=" + z + "," + zz + "&key=AIzaSyBjHXGKoV9SebN7V4XHrAoRsB7h3Ie7ZVg";
    public String mapurl ;
    public static String jsonObject;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        Intent iBuilding = getIntent();
        final Building building = (Building) iBuilding.getParcelableExtra("building");
        x = iBuilding.getDoubleExtra("currLatitude", 37.334065);
        y = iBuilding.getDoubleExtra("currLongitude", -121.880664);

        z = building.blat;
        zz = building.blon;

        address = (TextView) findViewById(R.id.buildingAddress);
        buildingImage = (ImageView) findViewById(R.id.buildingImageView);
        buildingImage.setBackgroundResource(building.image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        duration = (TextView) findViewById(R.id.duration);

        getSupportActionBar().setTitle(getResources().getString(building.name));
        address.setText(getResources().getString(building.address));


        new GetDuration().execute();

        // duration.setText(getResources().getString(jsonObject.rows.elements.duration.text));


        System.out.println(url);
        System.out.println(x);
        System.out.println(y);


        streetView = (Button) findViewById(R.id.streetView);


            streetView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Uri uri = Uri.parse(String.valueOf(building.url));
                    Intent intent = new Intent(St, uri);
                    startActivity(intent);
*/
                    String one = "https://www.google.com/maps/@37.335967,-121.8856837,3a,75y,134.01h,89t/data=!3m6!1e1!3m4!1sQWDTbzdSCqqCFL5sSO_cCg!2e0!7i13312!8i6656!6m1!1e1";
                    String two = "https://www.google.com/maps/@37.3377787,-121.8820119,3a,75y,146.84h,75.66t/data=!3m6!1e1!3m4!1soO4EqGCOWwLy5eXy-EKx-g!2e0!7i13312!8i6656!6m1!1e1";
                    String three = "https://www.google.com/maps/@37.3335287,-121.8843338,3a,75y,63.46h,84.1t/data=!3m6!1e1!3m4!1stBY5-l3dxDZ9q003KFjVkw!2e0!7i13312!8i6656";
                    String four = "https://www.google.com/maps/@37.3372125,-121.8784565,3a,75y,278.88h,84.51t/data=!3m6!1e1!3m4!1sBaI2lHplpsQMZCNik6C8zQ!2e0!7i13312!8i6656";
                    String five = "https://www.google.com/maps/@37.3372125,-121.8784565,3a,75y,278.88h,84.51t/data=!3m6!1e1!3m4!1sBaI2lHplpsQMZCNik6C8zQ!2e0!7i13312!8i6656";
                    String six = "https://www.google.com/maps/@37.3386745,-121.8809632,3a,75y,355.98h,92.61t/data=!3m6!1e1!3m4!1s3O0i45z_Vl7Q9-as0iaIEg!2e0!7i13312!8i6656!6m1!1e1";


                    if(building.id == 1){
                        Intent viewIntent = new Intent("android.intent.action.VIEW",Uri.parse(one));
                        startActivity(viewIntent);
                    }
                    else if(building.id == 2) {
                        Intent viewIntent = new Intent("android.intent.action.VIEW",Uri.parse(two));
                        startActivity(viewIntent);

                    }
                    else if(building.id == 3) {
                        Intent viewIntent = new Intent("android.intent.action.VIEW",Uri.parse(three));
                        startActivity(viewIntent);
                    }
                    else if(building.id == 4) {
                        Intent viewIntent = new Intent("android.intent.action.VIEW",Uri.parse(four));
                        startActivity(viewIntent);

                    }
                    else if(building.id == 5) {
                        Intent viewIntent = new Intent("android.intent.action.VIEW",Uri.parse(five));
                        startActivity(viewIntent);

                    }
                    else if(building.id == 6) {
                        Intent viewIntent = new Intent("android.intent.action.VIEW",Uri.parse(six));
                        startActivity(viewIntent);

                    }
                    else {

                    }


/*
                    System.out.println(building.id);


                    //Uri uri = Uri.parse(String.valueOf(building.url));
                    //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    Intent intent = new Intent(BuildingActivity.this, StreetView.class);
                    startActivity(intent);
*/
                }
            });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Building Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.chaitanya.sjsumap/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Building Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.chaitanya.sjsumap/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class GetDuration extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {

            // run url
            try {

                url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + x + "," + y + "&destinations=" + z + "," + zz + "&key=AIzaSyBjHXGKoV9SebN7V4XHrAoRsB7h3Ie7ZVg";

                System.err.println("Distance URL: " + "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + x + "," + y + "&destinations=" + z + "," + zz + "&key=AIzaSyBjHXGKoV9SebN7V4XHrAoRsB7h3Ie7ZVg");

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    System.err.println("Unexpected code " + response);
                }
//                Headers responseHeaders = response.headers();
//                for (int i = 0; i < responseHeaders.size(); i++) {
//                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                }

                StringBuilder builder = new StringBuilder(response.body().string());
                String jsonString = builder.toString();

                JSONObject jsonObject = new JSONObject(jsonString);

                System.err.println("Distance API: " + jsonObject);
                return jsonString;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            // execution of result of Long time consuming operation
            //rows - elements - duration - text

            try {
                JSONObject jsonObject = new JSONObject(jsonString)
                    .getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray("elements")
                    .getJSONObject(0)
                    .getJSONObject("duration");


                String dis = jsonObject.get("text").toString();
                System.out.println(dis);

                duration.setText(dis );
                //address.setText(dis);

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }




}