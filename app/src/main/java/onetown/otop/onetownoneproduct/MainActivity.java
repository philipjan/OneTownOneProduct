package onetown.otop.onetownoneproduct;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

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
                Location location= tracker.getUsersLocationByCriteria(gMap);
                Log.d("onMapReady ",String.valueOf(location));

                LatLng latLng= new LatLng(location.getLatitude(),location.getLongitude());

                // Creating Circles in the marker (Your current location)
                gMap.addCircle(new CircleOptions()
                        .center(new LatLng(location.getLatitude(),location.getLongitude()))
                        .radius(1000)
                        .strokeColor(Color.DKGRAY)
                        .fillColor(Color.GREEN));

                // Zoom Caemra to the current location
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,30));



            }
        });

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
    }
}
