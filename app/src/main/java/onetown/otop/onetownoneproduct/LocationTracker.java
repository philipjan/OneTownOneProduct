package onetown.otop.onetownoneproduct;


import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationTracker implements LocationListener {

    GoogleMap map;
    Context context;
    double latitude, longitude;
    LocationManager lm;
    Location loc;
    Criteria criteria;

    private static String TAG="LocationTracker";

    public LocationTracker(Context context) {
        this.context = context;

    }

    // Getting users location by criteria
  public Location getUsersLocationByCriteria(GoogleMap googleMap) {

            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();

            // Get best provider based on the status,(e.g like what mode is turned on,etc2)
            String provider = lm.getBestProvider(criteria, true);
            Log.d(TAG,"Choosed provider: "+provider);

            // Get Current Location
            loc = lm.getLastKnownLocation(provider);
      try {
            latitude=loc.getLatitude();
            longitude= loc.getLongitude();

          // Setting markers
          googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("You are here"));

          /** Adding Circle to the users location */
          googleMap.addCircle(new CircleOptions()
                  .center(new LatLng(latitude,longitude))
                  .radius(1000)
                  .strokeColor(Color.GRAY)
                  .fillColor(Color.LTGRAY));

            Log.d("Via Criteria","Latitude: "+String.valueOf(latitude)+" Longitude: "+String.valueOf(longitude));
            Toast.makeText(context,"Latitude: "+String.valueOf(latitude)+" Longitude: "+String.valueOf(longitude),Toast.LENGTH_LONG).show();
        }catch (NullPointerException e) {
            throw new NullPointerException("Null values are detected!");
        }

      return loc;
    }

    public Location getLocationByNetworkOrGps() {

        boolean isGpsEnabled,isNetworkEnabled;


        lm= (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        isGpsEnabled= lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled= lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGpsEnabled && !isNetworkEnabled) {
            Toast.makeText(context,"GPS & Network is not enabled !",Toast.LENGTH_LONG).show();
        }else {

            // Using GPS Provider
            if (isGpsEnabled) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);

                if (lm != null) {
                    loc= lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (loc != null) {
                         latitude= loc.getLatitude();
                         longitude= loc.getLongitude();
                    }
                }
            }
            // using network provider
            if (isNetworkEnabled) {
                lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (lm != null) {
                    loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (loc != null) {
                        latitude= loc.getLatitude();
                        longitude= loc.getLongitude();
                    }
                }
            }
        }

        Log.d("Via Network/GPS","Latitude: "+String.valueOf(latitude)+" Longitude: "+String.valueOf(longitude));
        return loc;
    }


    @Override
    public void onLocationChanged(Location location) {

        drawMarkerandCirclewhenLocationChanged(location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void drawMarkerandCirclewhenLocationChanged(Location location) {
        try {
            map.clear();
            LatLng latLng= new LatLng(location.getLatitude(),location.getLongitude());


            Circle c= map.addCircle(new CircleOptions()
                    .radius(1000)
                    .strokeColor(Color.GRAY)
                    .fillColor(Color.LTGRAY));
            c.setCenter(new LatLng(location.getLatitude(),location.getLongitude()));
            map.addMarker(new MarkerOptions()
            .position(latLng)
            .title("My Location"));

            CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(latLng,10);
            map.animateCamera(cameraUpdate);
            lm.removeUpdates(this);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
