package com.example.mfriendsappjot.Model;

import android.content.Context;

public class DataAccessFactory {
    //Gets the instance of IDataAccess
    public static IDataAccess getInstance(Context c) {
        return new SQLiteImpl(c);
    }
}
