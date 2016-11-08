package onetown.otop.onetownoneproduct;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, PlacesListener {
    Location loc;
    LatLng latLng;
    LatLng placesLatLng;
    LocationTracker tracker;
    FloatingActionButton fab;
    GoogleMap gMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker= new LocationTracker(MainActivity.this);

        if (isGooglePlayAvailable()) {
            Toast.makeText(this,"Successfully connected to Google Play Services",Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_main);
            initMap();
        }

      // Get Users location when clicked
        fab= (FloatingActionButton)findViewById(R.id.fab_getLocation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng testLatlng= new LatLng(16.00222,123.23123123);

                boolean isMethodByNetworkAndGpsIsEmpty= true;

                if (isMethodByNetworkAndGpsIsEmpty) {
                    loc=tracker.getUsersLocationByCriteria(gMap);
                }else {
                    loc= tracker.getLocationByNetworkOrGps();
                }
                 latLng= new LatLng(loc.getLatitude(),loc.getLongitude());
                Log.d("MainActivity",String.valueOf(loc.getLatitude()+" "+String.valueOf(loc.getLongitude())));

                // Creating Circles in the marker (Your current location)
                Circle circle = gMap.addCircle(new CircleOptions()
                        .center(new LatLng(loc.getLatitude(),loc.getLongitude()))
                        .radius(1000)
                        .strokeColor(Color.DKGRAY)
                        .fillColor(Color.GREEN));


                // Zoom Camera to the current location
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,0));

                checkIfInsideCircleRadius(circle,testLatlng);
                tstNBRLib(latLng);
            }
        });

    }

    // Library for Getting Nearby Place using Google Place Web Service
    public void tstNBRLib(LatLng latLng) {
        new NRPlaces.Builder()
                .listener(this)
                .key("AIzaSyCzmZl2IFIAbv26FoDCdvfS1SOMsbkZS-4")
                .latlng(latLng.latitude,latLng.longitude)
                .radius(1000)
                .type(PlaceType.PHARMACY)
                .build()
                .execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // Check if Google Play Services is Available
    public boolean isGooglePlayAvailable() {
        GoogleApiAvailability api= GoogleApiAvailability.getInstance();
        int isAvailable= api.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }else if (api.isUserResolvableError(isAvailable)){
            Dialog dialog= api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        }else {
            Toast.makeText(this,"Can't connect to Play Services",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    //Initialize Map
    public void initMap() {
        MapFragment mapFragment= (MapFragment) getFragmentManager().findFragmentById(R.id.fragment_maps);
        mapFragment.getMapAsync(this);


    }

    // Interface methods
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap= googleMap;
        gMap.setMyLocationEnabled(true);



    }

    public void onSearchPlaces(View view) {
        List<Address> addressList=new ArrayList<Address>();

        EditText placesInput= (EditText)findViewById(R.id.textInput_places);
        String location= placesInput.getText().toString();

        if (location != null || location != "" || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 20);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < addressList.size(); i++) {
                LatLng latLng = new LatLng(addressList.get(i).getLatitude(), addressList.get(i).getLongitude());
                createMarker(latLng,addressList,i);
            }

        }
    }

    public Marker createMarker(LatLng latLng,List<Address> addresses,int i) {

        return gMap.addMarker(new MarkerOptions()
        .position(latLng)
        .title(toString(addresses,i)));
    }

    public String toString(List<Address> list,int i) {
        return String.valueOf(list.get(i).getLocality())+" "+
                String.valueOf(list.get(i).getAdminArea())+" "+
                        String.valueOf(list.get(i).getFeatureName());
    }

    // Check the latitude/longitude if its outside or inside the radius
    public Boolean checkIfInsideCircleRadius(Circle circleCenterPoint, LatLng locationToCheck) {

        float[] distance = new float[2];

        Location.distanceBetween(circleCenterPoint.getCenter().latitude,circleCenterPoint.getCenter().longitude,
                locationToCheck.latitude,locationToCheck.longitude,distance);
        if (distance[0] > circleCenterPoint.getRadius()) {

            Log.d("CircleRadius","Outside of the Radius");
            return false;
        }else {
            Log.d("CircleRadius","Inside of the Radius");
            return true;
        }


    }

    // PlaceListeners Generated method
    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {
        Log.d("onPlacesStart","starting");
    }

    // Data re printer here
    @Override
    public void onPlacesSuccess(final List<Place> places) {
        Log.d("onPlacesSuccess","starting");
        for(Place place: places) {
            Log.d("Success",place.toString());
        }

    }

    @Override
    public void onPlacesFinished() {
        Log.d("onPlacesFinished","starting");
    }
}
