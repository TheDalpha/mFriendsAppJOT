package com.example.mfriendsappjot.Model;

import android.support.design.widget.CoordinatorLayout;

import java.util.List;

public interface IDataAccess {
    /**
     * Creates a friend in database
     * @param f
     * @return
     */
    long insert(BEFriend f);

    /**
     * Deletes a friend from database
     * @param id
     */
    void deleteFriend(int id);

    /**
     * Gets all friends from database
     * @return
     */
    List<BEFriend> selectAll();

    /**
     * Updates a friend in database
     * @param f
     */
    void update(BEFriend f);

    /**
     * Updates the location of a friend in database
     * @param f
     */
    void updateLocation(BEFriend f);

    /**
     * Gets a friend by the id
     * @param id
     * @return
     */
    BEFriend getById(int id);
}
