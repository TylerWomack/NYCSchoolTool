package com.example.twomack.nycschooldataviewer;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.List;


/**
 * Created by twomack on 4/5/18.
 */




public class LocationUtil extends Activity {

}

     /*

    LocationUtil(){}

    public String[] usersLocation;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 11254;
    Context context;

    MutableLiveData<String[]> location = null;

    LocationUtil(Context context){
        this.context = context;
        if (location == null){
            location = new MutableLiveData<String[]>() {};
        }
    }

    public LiveData<String[]> getLocation(){
        if (location == null){
            location = new MutableLiveData<>();
        }
        return location;
    }

    public void setUsersLocation() {
        Location location = null;
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        //in other words, if permission hasn't already been granted...
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[1];
            permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {

            location = findLastLocation();

            if (location != null) {
                updateLocUtil(location);
                //now that we have the location, we're updating the ui
                //viewSchoolsByDistance(null);
            } else {
                Log.e("warning", "location is still not set");
                notifyUserGPS();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Location location = null;

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            location = findLastLocation();

            if (location != null) {
                updateLocUtil(location);
                //now that we have the locations, we're updating the ui
                //viewSchoolsByDistance(null);
            } else {
                notifyUserGPS();
            }
        }
    }

    private void updateLocUtil(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String strLongitude = String.valueOf(longitude);
        String strLatitude = String.valueOf(latitude);

        String[] locationString = new String[2];
        locationString[0] = strLatitude;
        locationString[1] = strLongitude;
        usersLocation = locationString;
        location.set();
    }

    public Location findLastLocation() {
        LocationManager mLocationManager;
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public void notifyUserGPS(){
        // notify user
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
        dialog.setPositiveButton(context.getResources().getString(R.string.open_loc_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);
                //get gps
            }
        });
        dialog.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub

            }
        });
        dialog.show();

    }


}
 */