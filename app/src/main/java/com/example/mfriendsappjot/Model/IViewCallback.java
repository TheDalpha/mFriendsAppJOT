package com.example.mfriendsappjot.Model;

import android.location.Location;

public interface IViewCallback {

    void setSpeed(double speed);

    void setCurrentLocation(Location location);

    void setCounter(int count);
}
