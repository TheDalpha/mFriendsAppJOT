package com.example.mfriendsappjot.Model;

import java.io.Serializable;

public class BEFriend implements Serializable {
    private String m_name;
    private String m_phone;
    private Boolean m_isFavorite;
    private String m_mail;
    private String m_URL;
    private String m_image;
    private int m_id;

    public BEFriend(int id, String name, String phone, Boolean isFavorite, String mail, String url, String image) {
        m_id = id;
        m_name = name;
        m_phone = phone;
        m_isFavorite = isFavorite;
        m_mail = mail;
        m_URL = url;
        m_image = image;
    }

    public String getImage() { return m_image;}

    public String getPhone() {
        return m_phone;
    }

    public void setPhone(String phone) {phone = m_phone;}

    public String getName() {return m_name;}

    public boolean isFavorite() {return m_isFavorite;}

    public String getMail() {return m_mail;}

    public String getURL() {return m_URL;}

    public int getID()  {return m_id;}
}
