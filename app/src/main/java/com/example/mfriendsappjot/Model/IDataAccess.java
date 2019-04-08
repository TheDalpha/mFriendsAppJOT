package com.example.mfriendsappjot.Model;

import android.support.design.widget.CoordinatorLayout;

import java.util.List;

public interface IDataAccess {
    long insert(BEFriend f);

    void deleteFriend(int id);

    List<BEFriend> selectAll();

    void update(BEFriend f);

    void updateLocation(BEFriend f);

    BEFriend getById(int id);
}
