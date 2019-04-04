package com.example.mfriendsappjot;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mfriendsappjot.Model.BEFriend;
import com.example.mfriendsappjot.Model.DataAccessFactory;
import com.example.mfriendsappjot.Model.Friends;
import com.example.mfriendsappjot.Model.IDataAccess;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    public static String TAG = "Friend2";
    private IDataAccess fDataAccess;

    Friends m_friends;
    ArrayList<String> friendsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Friends v2");

        fDataAccess = DataAccessFactory.getInstance(this);

        final Intent x = new Intent(this, DetailActivity.class);
        m_friends = new Friends();


        Button btnShow = findViewById(R.id.btnAdd);
        btnShow.setText("Add Contact");



       // friends = m_friends.getNames();
        if (fDataAccess.selectAll().size() != 0) {
            showFriends(fDataAccess.selectAll());
            ListAdapter adapter =
                    new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1,
                            friendsList);
            setListAdapter(adapter);
        }


        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(x);
            }
        });

    }


    @Override
    public void onListItemClick(ListView parent, View v, int position,
                                long id) {

        Intent x = new Intent(this, DetailActivity.class);
        Log.d(TAG, "Detail activity will be started");
        BEFriend friend = fDataAccess.selectAll().get(position);
        addData(x, friend);
        startActivity(x);
        Log.d(TAG, "Detail activity is started");

    }

    private void addData(Intent x, BEFriend f)
    {
        x.putExtra("friend", f);
    }


    private void showFriends(List<BEFriend> friends) {
        for (BEFriend f : friends) {
            Log.d("List of friends", f.getName());
            friendsList.add(f.getName());
        }
    }



}
