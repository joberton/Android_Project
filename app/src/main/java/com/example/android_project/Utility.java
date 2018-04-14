package com.example.android_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.android.gms.security.ProviderInstaller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by johno on 4/12/2018.
 */

public class Utility
{
    //global functions for the application go here...

    public static final String[] RECIPES_ERROR_MESSAGES = {"Please provide a name for your recipe",
            "Please provide a description of your recipe",
            "Please provide ingredients for your recipe",
            "Please provide instructions for your recipe"};

    public static final String[] REVIEWS_ERROR_MESSAGES = {"Please provide a name for your review",
            "Please provide a description for your review"};

    public static final int NO_SORT = 0;
    public static final int SORT_BY_DATE = 1;
    public static final int SORT_BY_NAME = 2;
    public static final int SORT_BY_RATING = 3;

    public static void checkForTls(Context context)
    {
        ProviderInstaller.installIfNeededAsync(context, new ProviderInstaller.ProviderInstallListener() {
            @Override
            public void onProviderInstalled() {
                Log.i("ProviderInstall_success","Update installed successfully");
            }

            @Override
            public void onProviderInstallFailed(int i, Intent intent) {
                Log.d("ProviderInstall_failure", "An Error was encountered while requesting install for the google provider");
            }
        });
    }

    public static List reverseList(List listToReverse, int sortCode)
    {
        if(sortCode != NO_SORT)
        {
            Collections.reverse(listToReverse);
        }
        return listToReverse;
    }

    public static List<EditText> formValidation(final EditText[] FORM_EDIT_TEXTS, final boolean[] VALIDATION_CHECKS, final String[] ERROR_MESSAGES)
    {
        List<EditText> formErrors = new ArrayList<>();
        for(int i = 0; i < VALIDATION_CHECKS.length; i++)
        {
            if(!VALIDATION_CHECKS[i])
            {
                FORM_EDIT_TEXTS[i].setHintTextColor(Color.RED);
                FORM_EDIT_TEXTS[i].setHint(ERROR_MESSAGES[i]);
                formErrors.add(FORM_EDIT_TEXTS[i]);
            }
        }
        return formErrors;
    }

    public static ArrayAdapter<CharSequence> createArrayAdapterFromResource(Context context,int arrayResourceId)
    {
        return ArrayAdapter.createFromResource(context,arrayResourceId,R.layout.support_simple_spinner_dropdown_item);
    }

    public static boolean checkForWifiConnection(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeWork = cm.getActiveNetworkInfo();
        return activeWork != null && activeWork.isConnectedOrConnecting();
    }

    public static boolean isNotBlank(String data)
    {
        return data != null && !data.isEmpty();
    }

    public static String encodeToBase64(Bitmap imageData)
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        imageData.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
        return Base64.encodeToString(byteStream.toByteArray(), Base64.DEFAULT);
    }

    public static byte[] decodeBase64(String base64Data)
    {
        return Base64.decode(base64Data,Base64.DEFAULT);
    }

    public static Bitmap decodeBitmap(byte[] byteData)
    {
        return BitmapFactory.decodeByteArray(byteData,0,byteData.length);
    }

    public static Bitmap resizeBitMap(Bitmap imageBitmap,int width, int height)
    {
        return Bitmap.createScaledBitmap(imageBitmap, width, height,false);
    }
}
