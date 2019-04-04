package com.example.mfriendsappjot.Model;

import java.util.ArrayList;

public class Friends {
    ArrayList<BEFriend> m_friends;

    public Friends()
    {
    }

    public ArrayList<BEFriend> getAll()
    { return m_friends; }

    public String[] getNames()
    {
        String[] res = new String[m_friends.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = m_friends.get(i).getName();
        }
        return res;
    }
}
