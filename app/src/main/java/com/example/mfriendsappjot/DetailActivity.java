package com.example.mfriendsappjot;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mfriendsappjot.Model.BEFriend;
import com.example.mfriendsappjot.Model.DataAccessFactory;
import com.example.mfriendsappjot.Model.IDataAccess;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    // Class tag for logging
    private final static String LOGTAG = "Camtag";
    // Initialize the CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    // Initialize MY_PERMISSION
    private final static int MY_PERMISSION = 1;
    // Initialize the ERROR_DIALOG_REQUEST
    private static final int ERROR_DIALOG_REQUEST = 9001;
    // Variable for intent
    private Intent x;

    // Variable for File
    File pFile;

    // Variable for IDataAccess
    private IDataAccess fDataAccess;


    // Variable for xml objects
    EditText etName;
    EditText etPhone;
    CheckBox cbFavorite;
    EditText etMail;
    EditText etURL;
    ImageView ivProfile;
    Button btnShow;
    Boolean isFriendSet;
    EditText etDesc;
    EditText etBDay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the DataAccessFactory
        fDataAccess = DataAccessFactory.getInstance(this);

        // Checks for permissions, requests permission if not already given.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)  {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, MY_PERMISSION);
                ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.CAMERA
                }, MY_PERMISSION);
            }
        }

        setContentView(R.layout.activity_detail);
        Log.d(LOGTAG, "Detail Activity started");

        // Initialize the intent
        x = new Intent(this, MainActivity.class);

        // Initialize the xml objects
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        cbFavorite = findViewById(R.id.cbFavorite);
        etMail = findViewById(R.id.etMail);
        etURL = findViewById(R.id.etURL);
        ivProfile = findViewById(R.id.ivProfile);
        etDesc = findViewById(R.id.etDesc);
        etBDay = findViewById(R.id.etBDay);
        btnShow = findViewById(R.id.btnShow);
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

        Button btnTakeP = findViewById(R.id.btnTakeP);
        btnTakeP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        Button btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize a intent and adds data to it.
                Intent intent = new Intent(DetailActivity.this, MapsActivity.class);
                BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");
                isFriendSet = false;
                intent.putExtra("home", isFriendSet);
                addData(intent, f);
                startActivity(intent);
            }
        });
        // Sets up the GUI with data
        setGUI();

        if (isServicesOK()) {
            Button btnShow = findViewById(R.id.btnShow);
            btnShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Initialize a intent and adds data to it.
                    Intent intent = new Intent(DetailActivity.this, MapsActivity.class);
                    BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");
                    BEFriend fr = fDataAccess.getById(f.getID());
                    isFriendSet = true;
                    intent.putExtra("home", isFriendSet);
                    addData(intent, fr);
                    startActivity(intent);
                }
            });
        }


    }

    /**
     * Overrides the back button on the phone to be configurable
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5 && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Overrides the back button on the phone to be configurable
     */
    @Override
    public void onBackPressed() {
        startActivity(x);
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
     * Sets up the GUI with data if there is any
     */
    private void setGUI() {
        BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");

        if (f != null) {

            etName.setText(f.getName());
            etPhone.setText(f.getPhone());
            etMail.setText(f.getMail());
            etURL.setText(f.getURL());
            etDesc.setText(f.getDesc());
            etBDay.setText(f.getBDay());
            cbFavorite.setChecked(f.isFavorite());
            if(f.getImage() != "") {
                showPicture(pFile);
            }
        }
    }

    /**
     * Opens the native call app on the phone with the phone number
     */
    private void call() {
        BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + f.getPhone()));
        startActivity(intent);
    }

    /**
     * Opens the native mail app on the phone with the mail address
     */
    private void mail() {
        BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, f.getMail());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Test");
        intent.putExtra(Intent.EXTRA_TEXT, "Opening app");
        startActivity(intent);
    }

    /**
     * Opens the native text app on the phone with the phone number
     */
    private void text() {
        BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:" + f.getPhone()));
        intent.putExtra("sms_body", "Opening app");
        startActivity(intent);
    }

    /**
     * Opens the website in the native browser of the phone
     */
    private void website() {
        BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(f.getURL()));
        startActivity(intent);
    }

    /**
     * Creates or updates a friend
     */
    private void onClickCreate() {
        BEFriend fr = (BEFriend) getIntent().getSerializableExtra("friend");
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        boolean favorite = cbFavorite.isChecked();
        String mail = etMail.getText().toString();
        String url = etURL.getText().toString();
        String desc = etDesc.getText().toString();
        String bDay = etBDay.getText().toString();
        double longtitude = 0;
        double latitude = 0;

        if (fr != null) {
            String imageUri;
            if(pFile != null){
                imageUri = pFile.getAbsolutePath();
            } else {
                 imageUri = fr.getImage();
            }
            BEFriend f = new BEFriend(fr.getID(), name, phone, favorite, mail, url, imageUri, longtitude, latitude, desc, bDay);
            fDataAccess.update(f);
        } else {
            if (name.length() == 0) {
                Toast.makeText(this, "You must enter name", Toast.LENGTH_LONG).show();
                return;
            }
            String imageUri = "";
            if(pFile != null) {
                imageUri = pFile.getAbsolutePath();
            }
            int id = -1;
            BEFriend f = new BEFriend(id, name, phone, favorite, mail, url, imageUri, longtitude, latitude, desc, bDay);
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

    /**
     * Deletes a friend
     */
    private void onClickDelete() {
        BEFriend f = (BEFriend) getIntent().getSerializableExtra("friend");
        int id = f.getID();
        fDataAccess.deleteFriend(id);
        Intent x = new Intent(this, MainActivity.class);
        startActivity(x);
    }

    /**
     * Turns on the native camera app on the phone
     */
    private void takePicture() {
        pFile = getOutputMediaFile();
        if (pFile == null) {
            Toast.makeText(this, "Could not create file", Toast.LENGTH_LONG).show();
            return;
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pFile));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else {
            Log.d(LOGTAG, "camera app could NOT be started");
        }
    }

    /**
     * Creates directory and the image file
     * @return image file
     */
    private File getOutputMediaFile() {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_name));

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("mFriendsAppJOT", "failed to create directory");
                    return null;
                }
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String postfix = "jpg";
            String prefix = "IMG";

            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + prefix + "_" + timeStamp + "." + postfix);
            return mediaFile;

    }

    /**
     * Listens to activities to check if picture taken has been canceled or accepted
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showPicture(pFile);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_LONG).show();
                return;
            } else {
                Toast.makeText(this, "Picture NOT taken: unknown error", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Shows the picture
     * @param f
     */
    private void showPicture(File f) {
        BEFriend fr = (BEFriend) getIntent().getSerializableExtra("friend");
        if(fr == null || fr.getImage() == "") {
            ivProfile.setImageURI(Uri.fromFile(f));
        } else if (pFile != null) {
            ivProfile.setImageURI(Uri.fromFile(pFile));
        } else {
            ivProfile.setImageURI(Uri.parse(fr.getImage()));
        }
        ivProfile.setBackgroundColor(Color.RED);
    }

    /**
     * Checks to see if version of android is compatible with Google maps
     * @return
     */
    public boolean isServicesOK() {
        Log.d(LOGTAG, "Checking google maps version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(DetailActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            // No problems everything runs fine
            Log.d(LOGTAG, "Working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // and Error occured but we can fix it
            Log.d(LOGTAG, "Error");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(DetailActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
