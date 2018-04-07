package com.example.android_project;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NewRecipeActivity extends UtilityActivity {

    private String imageData;

    private EditText drinkName,drinkDescription,ingredientsData,drinkInstructions;
    private ImageView drinkImage;
    private ArrayAdapter<String> categoriesAdapter;
    private Spinner categoriesSpinner;
    private Button galleryImage,create;
    private AppDatabase db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences("myPerfs",MODE_PRIVATE);

        setTheme(sharedPreferences.getInt("theme",R.style.DarkAppTheme));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        db = AppDatabase.getDatabaseContext(this);

        drinkName = findViewById(R.id.newRecipeName);
        drinkDescription = findViewById(R.id.newRecipeDescription);
        ingredientsData = findViewById(R.id.ingredientsData);

        categoriesSpinner = findViewById(R.id.categoriesSpinner);

        drinkInstructions = findViewById(R.id.newRecipeInstructions);

        drinkImage = findViewById(R.id.uploadedImage);

        galleryImage = findViewById(R.id.galleryImage);
        create = findViewById(R.id.createRecipe);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == create && isNotBlank(imageData))
                {
                    Recipe newRecipe = new Recipe(getViewString(drinkName.getId()),
                            Calendar.getInstance().getTime(),
                            getViewString(ingredientsData.getId()),
                            getViewString(drinkDescription.getId()),
                            getViewString(drinkInstructions.getId()),
                            imageData,categoriesSpinner.getSelectedItemPosition() + 1);

                    new NewRecipeAsyncTask(newRecipe).execute();
                }
                else if(view == galleryImage)
                {
                    requestImageFromGallery();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Upload an Image",Toast.LENGTH_SHORT).show();
                }
            }
        };

        new LoadCategoriesTask().execute();

        galleryImage.setOnClickListener(onClickListener);
        create.setOnClickListener(onClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageData = onImageGalleryResult(requestCode,data);
        drinkImage.setImageBitmap(decodeBitmap(decodeBase64(imageData)));
        drinkImage.setVisibility(View.VISIBLE);
    }

    private class LoadCategoriesTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPostExecute(Void aVoid) {
            categoriesSpinner.setAdapter(categoriesAdapter);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            categoriesAdapter = new ArrayAdapter(getThemedContext(),android.R.layout.simple_spinner_dropdown_item);
            categoriesAdapter.addAll(db.categoryDao().fetchCategoryNames());
            return null;
        }
    }

    private class NewRecipeAsyncTask extends AsyncTask<Void,Void,Void>
    {
        Recipe newRecipe;

        public NewRecipeAsyncTask(Recipe newRecipe)
        {
            this.newRecipe = newRecipe;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            db.recipeDao().insertAll(newRecipe);
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
            return null;
        }
    }
}
