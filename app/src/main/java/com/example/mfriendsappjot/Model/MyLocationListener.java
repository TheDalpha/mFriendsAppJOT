package com.example.mfriendsappjot.Model;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {

    IViewCallback m_view;
    int count = 0;

    public MyLocationListener(IViewCallback view) {
        m_view = view;
    }

    @Override
    public void onLocationChanged(Location location) {
        m_view.setSpeed(location.getSpeed());

        m_view.setCurrentLocation(location);

        m_view.setCounter(++count);
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
