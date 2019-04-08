package com.example.mfriendsappjot.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLiteImpl implements IDataAccess {
    
    private static final String DATABASE_NAME = "sqlite.mDatabase";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "Friend";

    private SQLiteDatabase mDatabase;
    private SQLiteStatement insertStmt;

    public SQLiteImpl(Context context) {

        OpenHelper openHelper = new OpenHelper(context);
        mDatabase = openHelper.getWritableDatabase();

        String INSERT = "insert into " + TABLE_NAME
                + "(name, phone, favorite, mail, url, image, longtitude, latitude, description, birthday) values (?,?,?,?,?,?,?,?,?,?) ";

        insertStmt = mDatabase.compileStatement(INSERT);
    }

    /**
     * Creates and insert statement and execute it.
     * @param f
     * @return
     */
    public long insert(BEFriend f) {
        int favorite = f.isFavorite() ? 1 : 0;
        insertStmt.bindString(1, f.getName());
        insertStmt.bindString(2, f.getPhone());
        insertStmt.bindLong(3, favorite);
        insertStmt.bindString(4, f.getMail());
        insertStmt.bindString(5, f.getURL());
        insertStmt.bindString(6, f.getImage());
        insertStmt.bindDouble(7 ,f.getLongtitude());
        insertStmt.bindDouble(8, f.getLatitude());
        insertStmt.bindString(9, f.getDesc());
        insertStmt.bindString(10, f.getBDay());
        return insertStmt.executeInsert();
    }

    /**
     * Deletes the selected friend from database
     * @param id
     */
    public void deleteFriend(int id) {
        mDatabase.delete(TABLE_NAME, "id=" + id, null);
    }

    /**
     * Selects all friends from the database
     * @return a list of friends
     */
    public List<BEFriend> selectAll() {
        List<BEFriend> list = new ArrayList<BEFriend>();
        Cursor cursor = mDatabase.query(TABLE_NAME, new String[] { "id", "name", "phone", "favorite", "mail", "url", "image", "longtitude", "latitude", "description", "birthday"},
                null, null, null, null, "name");
        if (cursor.moveToFirst()) {
            do {
                boolean value = cursor.getInt( 3) == 1;
                list.add(new BEFriend(cursor.getInt(0), cursor.getString(1), cursor.getString(2), value, cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getDouble(7), cursor.getDouble(8), cursor.getString(9), cursor.getString(10)));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    /**
     * Gets the friend with the id from the database
     * @param id
     * @return a friend
     */
    public BEFriend getById(int id) {
        Cursor cursor = mDatabase.query(TABLE_NAME, new String[] {"id", "name", "phone", "favorite", "mail", "url", "image", "longtitude", "latitude", "description", "birthday"},
                "id=" + id, null, null, null, "name");
        if(cursor.moveToFirst()) {
            boolean value = cursor.getInt(3) == 1;
            BEFriend friend = new BEFriend(cursor.getInt(0), cursor.getString(1), cursor.getString(2), value, cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getDouble(7), cursor.getDouble(8), cursor.getString(9), cursor.getString(10));

            if (!cursor.isClosed()) {
                cursor.close();

                return friend;
            }
        }

        if(!cursor.isClosed()) {
            cursor.close();
        }
        return null;
    }

    /**
     * Updates the selected friend in the database
     * @param f
     */
    public void update(BEFriend f)
    {
        int favorite = f.isFavorite() ? 1 : 0;
        ContentValues values = new ContentValues();
        values.put("name", f.getName());
        values.put("phone", f.getPhone());
        values.put("favorite", favorite);
        values.put("mail", f.getMail());
        values.put("url", f.getURL());
        values.put("image", f.getImage());
        values.put("longtitude", f.getLongtitude());
        values.put("latitude", f.getLatitude());
        values.put("description", f.getDesc());
        values.put("birthday", f.getBDay());
        mDatabase.update(TABLE_NAME, values, "id=" + f.getID(), null);
    }

    /**
     * Updates the location of the selected friend
     * @param f
     */
    public void updateLocation(BEFriend f){
        Log.d("Updating", "currently updating");
        ContentValues values = new ContentValues();
        values.put("longtitude", f.getLongtitude());
        values.put("latitude", f.getLatitude());
        mDatabase.update(TABLE_NAME, values, "id=" + f.getID(), null);
        Log.d("Updated","Update complete");
    }




    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME
                    + "(id INTEGER PRIMARY KEY, name TEXT, phone TEXT, favorite INT, mail TEXT, url TEXT, image TEXT, longtitude DOUBLE, latitude DOUBLE, description STRING, birthday STRING)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
