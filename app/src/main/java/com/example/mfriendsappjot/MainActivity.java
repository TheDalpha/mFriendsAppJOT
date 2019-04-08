package com.example.mfriendsappjot;

import android.app.Activity;
import android.app.ListActivity;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mfriendsappjot.Model.BEFriend;
import com.example.mfriendsappjot.Model.DataAccessFactory;
import com.example.mfriendsappjot.Model.Friends;
import com.example.mfriendsappjot.Model.IDataAccess;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    public static String TAG = "Friend2";
    private IDataAccess fDataAccess;
    private ListView lvFriends;
    private fAdapter fadapt;

    List<BEFriend> friendsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Friends v2");


        lvFriends = findViewById(R.id.lvfriendslist);
        fDataAccess = DataAccessFactory.getInstance(this);

        final Intent x = new Intent(this, DetailActivity.class);

        showFriends();

        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BEFriend friend = friendsList.get(position);
                addData(x, friend);
                startActivity(x);
            }
        });
        Button btnShow = findViewById(R.id.btnAdd);
        btnShow.setText("Add Contact");


        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(x);
            }
        });

    }

    private void addData(Intent x, BEFriend f)
    {
        x.putExtra("friend", f);
    }


    private void showFriends() {
        friendsList = fDataAccess.selectAll();

        fadapt = new fAdapter(this, R.layout.single_row, friendsList);
        lvFriends.setAdapter(fadapt);
    }

    private class fAdapter extends ArrayAdapter<BEFriend> {

        List<BEFriend> listFriends = new ArrayList<>();

        public fAdapter(Context ctx, int single_row, List<BEFriend> friends){
            super(ctx, single_row, friends);
            this.listFriends.addAll(friends);
        }

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
            desc.setText("Hej min pik er lille");

            if(friend.getImage() == "") {

            } else {
                image.setImageURI(Uri.fromFile(new File(friend.getImage())));
            }

            return v;
        }
    }



}
