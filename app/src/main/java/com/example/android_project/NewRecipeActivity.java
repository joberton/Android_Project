package com.example.android_project;

import android.content.ContentResolver;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;
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

public class NewRecipeActivity extends AppCompatActivity {

    private final int REQUEST_IMAGE = 1;

    private String imagePath = "";

    private EditText drinkName,drinkDescription,ingredientsData,drinkInstructions;
    private ArrayAdapter<String> categoriesAdapter;
    private Spinner categoriesSpinner;
    private Button galleryImage,create;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        db = AppDatabase.getDatabaseContext(this);

        drinkName = findViewById(R.id.newRecipeName);
        drinkDescription = findViewById(R.id.newRecipeDescription);
        ingredientsData = findViewById(R.id.ingredientsData);

        categoriesSpinner = findViewById(R.id.categoriesSpinner);

        drinkInstructions = findViewById(R.id.newRecipeInstructions);

        galleryImage = findViewById(R.id.galleryImage);
        create = findViewById(R.id.createRecipe);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == create)
                {
                    Recipe newRecipe = new Recipe(getViewString(drinkName.getId()),
                            Calendar.getInstance().getTime().toString(),
                            getViewString(ingredientsData.getId()),
                            getViewString(drinkDescription.getId()),
                            getViewString(drinkInstructions.getId()),
                            imagePath,categoriesSpinner.getSelectedItemPosition() + 1);

                    new NewRecipeAsyncTask(newRecipe).execute();
                }
                else if(view == galleryImage)
                {
                    Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    imageIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(imageIntent,"Choose a Picture"),REQUEST_IMAGE);
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
        if(requestCode == REQUEST_IMAGE && data != null) {
            imagePath = data.getDataString();
        }
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
            categoriesAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item);
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

    private String getViewString(int id)
    {
        return ((EditText)findViewById(id)).getText().toString();
    }
}
