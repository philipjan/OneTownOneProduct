package onetown.otop.onetownoneproduct;


import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationTracker implements LocationListener {

    Context context;
    double latitude, longitude;
    LocationManager lm;
    Location loc;
    Criteria criteria;

    private static String TAG="LocationTracker";

    public LocationTracker(Context context) {
        this.context = context;

    }

   /** public Location getLocationManually() {

        boolean isGpsEnabled;
        boolean isNetworkEnabled;
        try {
            lm= (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

            isGpsEnabled= lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled= lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGpsEnabled && !isNetworkEnabled) {
                // Do nothing
            }else {
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

                if (isNetworkEnabled) {
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
                    if (lm != null) {
                        loc= lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null) {
                            latitude= loc.getLatitude();
                            longitude= loc.getLongitude();
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        // Lat Long object for current location
        LatLng latLng= new LatLng(latitude,longitude);

        // Show current location in google map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(20));
        // Add Marker
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("You are here!"));

        Log.d(TAG,String.valueOf(loc));
        return loc;

    }  */

    // Getting users location
  public void getUsersLocationByCriteria() {


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
            Log.d(TAG,"Latitude: "+String.valueOf(latitude)+" Longitude: "+String.valueOf(longitude));
            Toast.makeText(context,"Latitude: "+String.valueOf(latitude)+" Longitude: "+String.valueOf(longitude),Toast.LENGTH_LONG).show();
        }catch (NullPointerException e) {
            throw new NullPointerException("Null values are detected!");
        }

    }



    @Override
    public void onLocationChanged(Location location) {

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
}
