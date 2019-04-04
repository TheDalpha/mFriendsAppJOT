package com.example.mfriendsappjot.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

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
                + "(name, phone, favorite, mail, url) values (?,?,?,?,?) ";

        insertStmt = mDatabase.compileStatement(INSERT);
    }

    public long insert(BEFriend f) {
        int favorite = f.isFavorite() ? 1 : 0;
        insertStmt.bindString(1, f.getName());
        insertStmt.bindString(2, f.getPhone());
        insertStmt.bindLong(3, favorite);
        insertStmt.bindString(4, f.getMail());
        insertStmt.bindString(5, f.getURL());
        return insertStmt.executeInsert();
    }

    public void deleteFriend(int id) {
        mDatabase.delete(TABLE_NAME, "id=" + id, null);
    }

    public List<BEFriend> selectAll() {
        List<BEFriend> list = new ArrayList<BEFriend>();
        Cursor cursor = mDatabase.query(TABLE_NAME, new String[] { "id", "name", "phone", "favorite", "mail", "url"},
                null, null, null, null, "name");
        if (cursor.moveToFirst()) {
            do {
                boolean value = cursor.getInt( 3) == 1;
                list.add(new BEFriend(cursor.getInt(0), cursor.getString(1), cursor.getString(2), value, cursor.getString(4), cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
    public void update(BEFriend f)
    {
        int favorite = f.isFavorite() ? 1 : 0;
        ContentValues values = new ContentValues();
        values.put("name", f.getName());
        values.put("phone", f.getPhone());
        values.put("favorite", favorite);
        values.put("mail", f.getMail());
        values.put("url", f.getURL());
        mDatabase.update(TABLE_NAME, values, "id=" + f.getID(), null);
    }




    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME
                    + "(id INTEGER PRIMARY KEY, name TEXT, phone TEXT, favorite INT, mail TEXT, url TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
