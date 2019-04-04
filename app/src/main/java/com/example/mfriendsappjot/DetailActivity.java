package com.example.mfriendsappjot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mfriendsappjot.Model.BEFriend;
import com.example.mfriendsappjot.Model.DataAccessFactory;
import com.example.mfriendsappjot.Model.IDataAccess;

import java.util.logging.Logger;

public class DetailActivity extends AppCompatActivity {
    String TAG = MainActivity.TAG;

    private IDataAccess fDataAccess;

    EditText etName;
    EditText etPhone;
    CheckBox cbFavorite;
    EditText etMail;
    EditText etURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "Detail Activity started");
        final Intent x = new Intent(this, MainActivity.class);

        fDataAccess = DataAccessFactory.getInstance(this);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        cbFavorite = findViewById(R.id.cbFavorite);
        etMail = findViewById(R.id.etMail);
        etURL = findViewById(R.id.etURL);
        Button btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        Button btnMail = findViewById(R.id.btnMail);
        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail();
            }
        });

        Button btnText = findViewById(R.id.btnText);
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text();
            }
        });

        Button btnWebsite = findViewById(R.id.btnWebsite);
        btnWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                website();
            }
        });

        Button btnSave = findViewById(R.id.btnOk);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreate();
            }
        });

        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDelete();
            }
        });



        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(x);
            }
        });


        setGUI();


    }

    private void setGUI() {
        BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");

        if (f != null) {
            etName.setText(f.getName());
            etPhone.setText(f.getPhone());
            etMail.setText(f.getMail());
            etURL.setText(f.getURL());
            cbFavorite.setChecked(f.isFavorite());
        }
    }

    private void call() {
        BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + f.getPhone()));
        startActivity(intent);
    }

    private void mail() {
        BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, f.getMail());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Test");
        intent.putExtra(Intent.EXTRA_TEXT, "Testing this shit");
        startActivity(intent);
    }

    private void text() {
        BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:" + f.getPhone()));
        intent.putExtra("sms_body", "Testing this shit");
        startActivity(intent);
    }

    private void website() {
        BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(f.getURL()));
        startActivity(intent);
    }

    private void onClickCreate() {
        BEFriend fr = (BEFriend) getIntent().getSerializableExtra("friend");
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        boolean favorite = cbFavorite.isChecked();
        String mail = etMail.getText().toString();
        String url = etURL.getText().toString();
        if (fr.getID() > 0) {
            BEFriend f = new BEFriend(fr.getID(), name, phone, favorite, mail, url);
            fDataAccess.update(f);
        } else {
            if (name.length() == 0) {
                Toast.makeText(this, "You must enter name", Toast.LENGTH_LONG).show();
                return;
            }
            int id = -1;
            BEFriend f = new BEFriend(id, name, phone, favorite, mail, url);
            fDataAccess.insert(f);
            etName.setText("");
            etPhone.setText("");
            cbFavorite.setChecked(false);
            etMail.setText("");
            etURL.setText("");
        }
        Intent x = new Intent(this, MainActivity.class);
        startActivity(x);
    }

    private void onClickDelete() {
        BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");
        int id = f.getID();
        fDataAccess.deleteFriend(id);
        Intent x = new Intent(this, MainActivity.class);
        startActivity(x);
    }
}
