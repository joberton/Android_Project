package com.example.android_project;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NewRecipeActivity extends AppCompatActivity {

    private EditText drinkName,drinkDescription,dateCreated,ingredientsData,drinkInstructions;
    private Button create;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"test-database").allowMainThreadQueries().build();

        drinkName = findViewById(R.id.newRecipeName);
        drinkDescription = findViewById(R.id.newRecipeDescription);
        ingredientsData = findViewById(R.id.ingredientsData);
        dateCreated = findViewById(R.id.newDateCreated);
        drinkInstructions = findViewById(R.id.newRecipeInstructions);

        create = findViewById(R.id.createRecipe);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe newRecipe = new Recipe(getViewString(drinkName.getId()),
                                              getViewString(dateCreated.getId()),
                                              getViewString(ingredientsData.getId()),
                                              getViewString(drinkDescription.getId()),
                                              getViewString(drinkInstructions.getId()));
                db.recipeDao().insertAll(newRecipe);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }

    private String getViewString(int id)
    {
        return ((EditText)findViewById(id)).getText().toString();
    }
}
