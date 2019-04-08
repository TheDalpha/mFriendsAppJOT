package com.example.mfriendsappjot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.mfriendsappjot.Model.BEFriend;
import com.example.mfriendsappjot.Model.DataAccessFactory;
import com.example.mfriendsappjot.Model.IDataAccess;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // Variable for the Google Map
    private GoogleMap mMap;
    // Class tag for Logging
    private static final String TAG = "MapsActivity";
    // Initialize Location permission request code
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    // Variable for the FusedLocationProviderClient
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // Setting default zoom on google maps
    private static final float DEFAULT_ZOOM = 15f;
    // Variable for the IDataAccess interface
    private IDataAccess fDataAccess;

    // Variable for Location
    Location currentLocation;
    //boolean to determent if home or show button was clicked
    Boolean isFriendSet;
    // Variable for the Friend object
    BEFriend f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //initialize the DataAccessFactory class
        fDataAccess = DataAccessFactory.getInstance(this);

        //Checks to see if there is permission, if not asks for it.
        permissionCheck();

        //Initialize BEFriend business class from the intent
        f = (BEFriend) getIntent().getSerializableExtra("friend");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initialize Boolean variable from the intent
        isFriendSet = (Boolean) getIntent().getSerializableExtra("home");
    }

    /**
     * Overrides the back button on the phone to be configurable
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5 && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Overrides the back button on the phone to be configurable
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MapsActivity.this, DetailActivity.class);
        intent.putExtra("friend", f);

        startActivity(intent);
    }

    /**
     * Gets the device location and sets markers and map camera to the users longtitude and latitude
     */
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location");

                        currentLocation = (Location) task.getResult();

                        // If isFriendSet is true, the method seeMarkers will set marker for the friend
                        // selected and for your current location.
                        // If it is false, you can select location for the current friend.
                        if (isFriendSet) {
                            seeMarkers();
                        } else {
                            double latitude = currentLocation.getLatitude();
                            double longtitude = currentLocation.getLongitude();
                            LatLng currentLatLng = new LatLng(latitude, longtitude);
                            moveCamera(currentLatLng, DEFAULT_ZOOM);

                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    MarkerOptions marker = new MarkerOptions().position(latLng).title("Friend is here");
                                    double longtitude = latLng.longitude;
                                    double latitude = latLng.latitude;

                                    BEFriend fr = new BEFriend(f.getID(), longtitude, latitude);

                                    fDataAccess.updateLocation(fr);
                                    mMap.addMarker(marker);
                                }
                            });
                        }

                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    /**
     * Moves the camera to the selected longtitude and latitude with a selected zoom on map.
     * @param latLng
     * @param zoom
     */
    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getDeviceLocation();
    }

    /**
     * Puts markers on the map for the current selected friend and your current location.
     */
    public void seeMarkers() {
        if (f.getLongtitude() > 0 && f.getLatitude() > 0) {
            LatLng friendLoc = new LatLng(f.getLatitude(), f.getLongtitude());
            mMap.addMarker(new MarkerOptions().position(friendLoc).title(f.getName() + " is here ! :)"));
            moveCamera(friendLoc, DEFAULT_ZOOM);
        }

        double latitude = currentLocation.getLatitude();
        double longtitude = currentLocation.getLongitude();
        LatLng currentLatLng = new LatLng(latitude, longtitude);
        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("You are here!"));

    }

    /**
     * Checks for permission, if permission has not been granted, it will request it
     */
    public void permissionCheck() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Checks to see if permission has been granted or not.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }
}
