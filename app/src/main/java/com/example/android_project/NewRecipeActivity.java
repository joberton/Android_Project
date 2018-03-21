package com.example.android_project;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NewRecipeActivity extends AppCompatActivity {

    private EditText drinkName,drinkDescription,dateCreated,ingredientsData,drinkInstructions;
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
        dateCreated = findViewById(R.id.newDateCreated);
        drinkInstructions = findViewById(R.id.newRecipeInstructions);

        galleryImage = findViewById(R.id.galleryImage);
        create = findViewById(R.id.createRecipe);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == create)
                {
                    Recipe newRecipe = new Recipe(getViewString(drinkName.getId()),
                            getViewString(dateCreated.getId()),
                            getViewString(ingredientsData.getId()),
                            getViewString(drinkDescription.getId()),
                            getViewString(drinkInstructions.getId()));

                    newRecipeAsyncTask newRecipeTask = new newRecipeAsyncTask(newRecipe);
                    newRecipeTask.execute();
                }
                else if(view == galleryImage)
                {
                    //select image from the gallery to be implemented
                }
            }
        };

        galleryImage.setOnClickListener(onClickListener);
        create.setOnClickListener(onClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private class newRecipeAsyncTask extends AsyncTask<Void,Void,Void>
    {
        Recipe newRecipe;

        public newRecipeAsyncTask(Recipe newRecipe)
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
