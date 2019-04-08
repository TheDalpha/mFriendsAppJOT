package com.example.mfriendsappjot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mfriendsappjot.Model.BEFriend;
import com.example.mfriendsappjot.Model.DataAccessFactory;
import com.example.mfriendsappjot.Model.IDataAccess;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    // Class tag for logging
    public static String TAG = "Friend2";
    // Variable for IDataAccess class
    private IDataAccess fDataAccess;
    // Variable for the ListView
    private ListView lvFriends;
    // Variable for the fAdpapter class
    private fAdapter fadapt;
    // Variable for a list of the BEFriend class
    List<BEFriend> friendsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("mFriendsJOT");

        // Initialize the ListView
        lvFriends = findViewById(R.id.lvfriendslist);
        // Initialize the DataAccessFactory class
        fDataAccess = DataAccessFactory.getInstance(this);
        // Variable for the Intent
        final Intent x = new Intent(this, DetailActivity.class);

        showFriends();

        // Listens to clicks on the ListView and gets position of the click
        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BEFriend friend = friendsList.get(position);
                addData(x, friend);
                startActivity(x);
            }
        });
        // Initialize the button add
        Button btnShow = findViewById(R.id.btnAdd);
        btnShow.setText("Add Contact");


        //Starts activity based on the intent
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(x);
            }
        });

    }

    /**
     * Adds data to the intent.
     * @param x
     * @param f
     */
    private void addData(Intent x, BEFriend f)
    {
        x.putExtra("friend", f);
    }


    /**
     * Gets all friends from database and sets up the adapter
     */
    private void showFriends() {
        friendsList = fDataAccess.selectAll();

        fadapt = new fAdapter(this, R.layout.single_row, friendsList);
        lvFriends.setAdapter(fadapt);
    }

    // The adapter class for the ListView
    private class fAdapter extends ArrayAdapter<BEFriend> {

        //Initialize a list of BEFriend
        List<BEFriend> listFriends = new ArrayList<>();

        public fAdapter(Context ctx, int single_row, List<BEFriend> friends){
            super(ctx, single_row, friends);
            this.listFriends.addAll(friends);
        }

        /**
         * Overrides the getView method and adds the image, name and description of a friend.
         * @param position
         * @param v
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View v, ViewGroup parent) {

            BEFriend friend = listFriends.get(position);

            if ( v == null) {
                LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.single_row, null);
                Log.d("list", "Position: " + position + " View created");
            } else {
                Log.d("list", "Position: " + position + "View reused");
            }

            TextView name = v.findViewById(R.id.tvName);
            TextView desc = v.findViewById(R.id.tvDesc);
            ImageView image = v.findViewById(R.id.ivImage);

            name.setText(friend.getName());
            desc.setText(friend.getDesc());


            if(friend.getImage() == "") {

            } else {
                image.setImageURI(Uri.fromFile(new File(friend.getImage())));
            }

            return v;
        }
    }



}
