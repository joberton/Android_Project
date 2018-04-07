package com.example.android_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UtilityActivity extends AppCompatActivity {

    //global utility functions for the application go here...
    //such as parsing data
    public final int REQUEST_IMAGE = 1;

    public final int NO_SORT = 0;
    public final int SORT_BY_DATE = 1;
    public final int SORT_BY_NAME = 2;
    public final int SORT_BY_RATING = 3;

    public ArrayMap<EditText,String> formValidation(final ArrayMap<EditText,String> VALIDATION_MAP, final boolean[] VALIDATION_CHECKS)
    {
        ArrayMap<EditText,String> formErrors = new ArrayMap<>();
        for(int i = 0; i < VALIDATION_CHECKS.length; i++)
        {
            if(!VALIDATION_CHECKS[i])
            {
                EditText failedView = VALIDATION_MAP.keyAt(i);
                formErrors.put(failedView,VALIDATION_MAP.get(failedView));
            }
        }
        return formErrors;
    }

    public void buildErrorMessages(final ArrayMap<EditText,String> ERRORS)
    {
        for(int i = 0; i < ERRORS.size(); i++)
        {
            EditText failedView = ERRORS.keyAt(i);
            failedView.setHintTextColor(Color.RED);
            failedView.setHint(ERRORS.get(failedView));
        }
    }

    public List reverseList(List listToReverse,int sortCode)
    {
        if(sortCode != NO_SORT)
        {
            Collections.reverse(listToReverse);
        }
        return listToReverse;
    }

    public Context getThemedContext()
    {
        return getSupportActionBar().getThemedContext();
    }

    public boolean isNotBlank(String data)
    {
        return data != null && data.length() > 0;
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
            e.printStackTrace();
        }
        return imageData;
    }

    public String encodeToBase64(Bitmap imageData)
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        imageData.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
        return Base64.encodeToString(byteStream.toByteArray(), Base64.DEFAULT);
    }

    public byte[] decodeBase64(String base64Data)
    {
        return Base64.decode(base64Data,Base64.DEFAULT);
    }

    public Bitmap decodeBitmap(byte[] byteData)
    {
        return BitmapFactory.decodeByteArray(byteData,0,byteData.length);
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
            Bitmap imageMap = getContentUriBitMap(data.getData());
            base64ImageData = encodeToBase64(imageMap);
        }
        return base64ImageData;
    }

    //default action bar behaviour
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.historyOfIngredients:
                startActivity(new Intent(getApplicationContext(),RecipeHistoryActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
