package onetown.otop.onetownoneproduct.Activity;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import onetown.otop.onetownoneproduct.Objects.LocationTracker;
import onetown.otop.onetownoneproduct.R;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    Location loc;
    LatLng latLng;
    LocationTracker tracker;
    FloatingActionButton fab;
    GoogleMap gMap;
    double radiusValue;
    Marker myCurrentLocationMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker= new LocationTracker(this,gMap);
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
                loc= tracker.getUsersLocationByCriteria(gMap,radiusValue);
                latLng= new LatLng(loc.getLatitude(),loc.getLongitude());
                Log.d("MainActivity",String.valueOf(loc.getLatitude()+" "+String.valueOf(loc.getLongitude())));
                // Zoom Camera to the current location
                myCurrentLocationMarker= gMap.addMarker(new MarkerOptions()
                                                        .position(latLng)
                                                        .title("My Current Location")
                                                        .snippet("This is a sample snippet"));
                myCurrentLocationMarker.showInfoWindow();
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
                // On marker click listener
                gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Log.i("onMarkerClick","Successfull, Title: "+marker.getTitle());
                        return false;
                    }
                });
            }
        });


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
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.8417947,123.057734),5));
        gMap.getUiSettings().setZoomControlsEnabled(true);
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
                createMarkerInSearch(latLng,addressList,i);
            }
        }
    }
    // Creation of markers
    public Marker createMarkerInSearch(LatLng latLng,List<Address> addresses,int i) {
        return gMap.addMarker(new MarkerOptions()
        .position(latLng)
        .title(toString(addresses,i)));

    }

    public String toString(List<Address> list,int i) {
        return String.valueOf(list.get(i).getLocality())+" "+
                String.valueOf(list.get(i).getAdminArea())+" "+
                        String.valueOf(list.get(i).getFeatureName());
    }


}
