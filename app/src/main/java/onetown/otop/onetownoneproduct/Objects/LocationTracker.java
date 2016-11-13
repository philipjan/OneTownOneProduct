package onetown.otop.onetownoneproduct.Objects;


import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationTracker implements LocationListener {

    GoogleMap map;
    Context context;
    double latitude, longitude;
    LocationManager lm;
    Criteria criteria;
    double circleRadius;

    private static String TAG="LocationTracker";

    public LocationTracker(Context context, GoogleMap googleMap) {
        this.map=googleMap;
        this.context = context;

    }

    // Getting users location by criteria
  public Location getUsersLocationByCriteria(GoogleMap map1,double circRadius) {
            circRadius=circleRadius;

            Location loc= null;
            boolean isProviderEnabled;

            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();

            // Get best provider based on the status,(e.g like what mode is turned on,etc2)
            String provider = lm.getBestProvider(criteria, true);
            Log.d(TAG,"Choosed provider: "+provider);

            isProviderEnabled= lm.isProviderEnabled(provider);

      if (!isProviderEnabled) {

      }else {
          if (isProviderEnabled) {
              if (provider.equals("gps")) {
                  lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
                  if (lm != null) {
                      loc=lm.getLastKnownLocation(provider);
                      if (loc != null) {
                          latitude= loc.getLatitude();
                          longitude= loc.getLongitude();
                      }
                  }
              }else {
                 if (provider.equals("network")) {
                     lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
                     if (lm != null) {
                         loc=lm.getLastKnownLocation(provider);
                         if (loc != null) {
                             latitude=loc.getLatitude();
                             longitude=loc.getLongitude();
                         }
                     }
                 }
              }

          }
      }

        addCircle(map1,latitude,longitude,circRadius);
        addMarkerTo(map1,latitude,longitude);



      Log.d("Criteria",provider);
      Log.d("Location",String.valueOf(loc));

      return loc;
    }

    public void addCircle(GoogleMap map,double latitude,double longitude,double circleRad) {
        map.addCircle(new CircleOptions()
                .center(new LatLng(latitude,longitude))
                .radius(circleRad)
                .strokeColor(Color.GRAY)
                .fillColor(Color.LTGRAY));

        Log.d("addCircle",String.valueOf(circleRad));
    }
    public void addMarkerTo(GoogleMap map,double latitude, double longitude) {

        LatLng latLng= new LatLng(latitude,longitude);
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Your Location"));
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
          //  map.clear();
          //  addMarkerTo(map,location.getLatitude(),location.getLongitude());
            Log.d("onLocationChanged",String.valueOf(new LatLng(location.getLatitude(),location.getLongitude())));
        }

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
