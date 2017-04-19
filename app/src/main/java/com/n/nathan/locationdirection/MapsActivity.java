package com.n.nathan.locationdirection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Location mLastLocation;

    public static Double lat_from_main;
    public static Double lng_from_main;

    private MarkerOptions marker;
    private Marker real_marker;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        //

        //
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        //
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {

                //

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                //set title
                alertDialogBuilder.setTitle("NO or OLD Google Play Services/Google api found on device");
                //set dialog message
                alertDialogBuilder
                        .setMessage("To use this app you need latest Google Play Services app. Please update or contact your manufacturer.")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //
                                try {
                                    finish();
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms&hl=en")));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                //
            }
        }
        //
    }
    //
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        marker = new MarkerOptions().position(sydney).title("Marker").icon(BitmapDescriptorFactory.fromResource(R.drawable.image0));

        if (real_marker != null) {
            real_marker.remove();
        }

        real_marker = mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        //
    }
    //
    @Override
    public void onDestroy() {

        super.onDestroy();
        //
        if (mGoogleApiClient != null) {
            mGoogleApiClient.unregisterConnectionCallbacks(this);
            mGoogleApiClient.unregisterConnectionFailedListener(this);
            //
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
            //
        }
        //
    }

    //location change listeners
    @Override
    public void onLocationChanged(Location location) {

        //
        if (location != null) {

            //
            lat_from_main = location.getLatitude();
            lng_from_main = location.getLongitude();
            //

            //
            Log.v("myapp", "lat and lng on Main from onChanged have been updated - assisted location from main - are:" + Double.toString(location.getLatitude()) +
                    " " + Double.toString(location.getLongitude()));
            //
            LatLng sydney = new LatLng(lat_from_main, lng_from_main);
            //
            marker = new MarkerOptions().position(sydney).title("Marker").icon(BitmapDescriptorFactory.fromResource(R.drawable.image0));

            //
            if (real_marker != null) {
                real_marker.remove();
            }
            //
            real_marker = mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            //

            //
            Handler mVolHandler = new Handler();
            Runnable mVolRunnable = new Runnable() {
                public void run() {
                    //

                    //
                    LatLng sydney = new LatLng(lat_from_main, lng_from_main);
                    marker = new MarkerOptions().position(sydney).title("Marker").icon(BitmapDescriptorFactory.fromResource(R.drawable.image1));
                    //
                    if (real_marker != null) {
                        real_marker.remove();
                    }
                    //
                    real_marker = mMap.addMarker(marker);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                    //
                }
            };

            //
            mVolHandler.removeCallbacks(mVolRunnable);
            mVolHandler.postDelayed(mVolRunnable, 500);
            //

            //
            Handler mVolHandler1 = new Handler();
            Runnable mVolRunnable1 = new Runnable() {
                public void run() {
                    
                    //
                    LatLng sydney = new LatLng(lat_from_main, lng_from_main);
                    //
                    marker = new MarkerOptions().position(sydney).title("Marker").icon(BitmapDescriptorFactory.fromResource(R.drawable.image2));
                    //
                    if (real_marker != null) {
                        real_marker.remove();
                    }
                    //
                    real_marker = mMap.addMarker(marker);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                    //
                }
            };

            //
            mVolHandler1.removeCallbacks(mVolRunnable1);
            mVolHandler1.postDelayed(mVolRunnable1, 1000);
            //
            Handler mVolHandler2 = new Handler();
            Runnable mVolRunnable2 = new Runnable() {
                public void run() {

                    //
                    LatLng sydney = new LatLng(lat_from_main, lng_from_main);

                    int k = R.drawable.image2;
                    marker = new MarkerOptions().position(sydney).title("Marker").icon(BitmapDescriptorFactory.fromResource(R.drawable.image3));
                    //
                    if (real_marker != null) {
                        real_marker.remove();
                    }
                    //
                    real_marker = mMap.addMarker(marker);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                    //
                }
            };

            //
            mVolHandler2.removeCallbacks(mVolRunnable2);
            mVolHandler2.postDelayed(mVolRunnable2, 1500);
            //
        }
        //
    }
    //
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0); // Update location every second
        //
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        //
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        //
        if (mLastLocation != null) {
            //
            lat_from_main = mLastLocation.getLatitude();
            lng_from_main = mLastLocation.getLongitude();
            //
        }
        //
    }
    //
    @Override
    public void onConnectionSuspended(int i) {
    }
    //
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        //
        buildGoogleApiClient();
        Log.v("myapp", "search new - in onConnected FAILED, creating googleapi client again");
        //
    }
    //
    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    //
    @Override
    public void onResume()
    {
        super.onResume();
    }
}
