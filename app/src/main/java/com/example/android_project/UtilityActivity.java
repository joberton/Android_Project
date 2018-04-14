package com.example.android_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class UtilityActivity extends AppCompatActivity {

    //global activity functions for the application go here...
    public final int REQUEST_IMAGE = 1;

    public Context getThemedContext()
    {
        return getSupportActionBar().getThemedContext();
    }

    public double parseDoubleData(int id)
    {
        String data = getViewString(id);
        return data.length() > 0 ? Double.parseDouble(data) : 0.0;
    }

    public int parseIntData(int id)
    {
        String data = getViewString(id);
        return data.length() > 0 ? Integer.parseInt(data) : 0;
    }

    public String getViewString(int id)
    {
        return ((EditText)findViewById(id)).getText().toString();
    }

    public Bitmap getContentUriBitMap(Uri uri)
    {
        Bitmap imageData = null;
        try
        {
            imageData = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        }
        catch (Exception e)
        {
            Log.e("Uri_Bitmap_Error",e.getMessage());
        }
        return imageData;
    }

    public void requestImageFromGallery()
    {
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(imageIntent,"Choose a Picture"),REQUEST_IMAGE);
    }

    public String onImageGalleryResult(int requestCode, Intent data)
    {
        String base64ImageData = "";
        if(requestCode == REQUEST_IMAGE && data != null) {
            Bitmap imageMap = Utility.resizeBitMap(getContentUriBitMap(data.getData()),250,250);
            base64ImageData = Utility.encodeToBase64(imageMap);
        }
        return base64ImageData;
    }

    //default action bar behaviour
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.recipesToGo:
                startActivity(new Intent(getApplicationContext(),QuickRecipesActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return true;
    }

    //default action bar behaviour
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
