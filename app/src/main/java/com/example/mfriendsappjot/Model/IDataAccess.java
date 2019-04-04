package com.example.mfriendsappjot.Model;

import java.util.List;

public interface IDataAccess {
    long insert(BEFriend f);

    void deleteFriend(int id);

    List<BEFriend> selectAll();

    void update(BEFriend f);
}
