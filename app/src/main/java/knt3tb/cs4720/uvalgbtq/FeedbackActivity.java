package knt3tb.cs4720.uvalgbtq;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedbackActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Uri fileUri;
    String pictureFileName;
    private static final int CAMERA_REQUEST = 123;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_feedback);

        Button bugButton = (Button) findViewById(R.id.bugReport);
        bugButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                bugReport();
            }
        });

        Button brButton = (Button) findViewById(R.id.newBR);
        brButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                newBR();
            }
        });

        Button brPhotoButton = (Button) findViewById(R.id.newBRPhoto);
        brPhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                newBRPhoto();
            }
        });

        Button supportButton = (Button) findViewById(R.id.support);
        supportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                support();
            }
        });
    }

    protected void bugReport() {
        //Log.i("Send email", "");
        String[] TO = {"QCAppTeam@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Bug Report]");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please include a report of the bug below: " +
                "\n\n");

        try {
            startActivity(Intent.createChooser(emailIntent, "Email Client:"));
            finish();
            //Log.i("Finished sending email...","");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FeedbackActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void newBR() {
        //Log.i("Send email", "");
        String[] TO = {"QCAppTeam@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[New BR!]");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "\nPlease include the following information about the new bathroom:" +
                "\n\n1. Gender (M, F, or Unisex): " +
                "\n\n2. Handicap accessible (Yes or No): " +
                "\n\n3. Description of location: ");

        try {
            startActivity(Intent.createChooser(emailIntent, "Email Client:"));
            finish();
            //Log.i("Finished sending email...","");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FeedbackActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void newBRPhoto() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(); // create a file to save the image
        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(i, CAMERA_REQUEST);

    }

    //Handle permissions for accessing fine location for user's location
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    newBRPhoto();
                } else {
                    // Permission Denied
                    Toast.makeText(FeedbackActivity.this, "ACCESS_CAMERA or WRITE_EXTERNAL Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            String[] TO = {"QCAppTeam@gmail.com"};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("image/jpg");

            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[New BR with Photo!]");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "\nPlease include the following information about the new bathroom:" +
                    "\n\n1. Gender (M, F, or Unisex): " +
                    "\n\n2. Handicap accessible (Yes or No): " +
                    "\n\n3. Description of location: ");

            try {
                startActivity(Intent.createChooser(emailIntent, "Email Client:"));
                finish();
                //Log.i("Finished sending email...","");
            }
            catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(FeedbackActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }




    protected void support() {
        //Log.i("Send email", "");
        String[] TO = {"QCAppTeam@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[General Support]");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please provide us details on how we can help you: " +
                "\n\n");

        try {
            startActivity(Intent.createChooser(emailIntent, "Email Client:"));
            finish();
            //Log.i("Finished sending email...","");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FeedbackActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_bathroommap) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(this, EventActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_resources) {
            Intent intent = new Intent(this, ResourceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_business) {
            Intent intent = new Intent(this, BusinessActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_policies) {
            Intent intent = new Intent(this, PolicyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
