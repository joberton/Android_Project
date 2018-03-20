package com.example.android_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;

public class RecipeDetailsActivity extends AppCompatActivity {

    private TextView name,description,ingredients,dateCreated,instructions;
    private Button update,delete;
    private Intent data;
    ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        data = getIntent();

        update = findViewById(R.id.editRecipe);
        delete = findViewById(R.id.deleteRecipe);

        name = findViewById(R.id.recipeName);
        dateCreated = findViewById(R.id.recipeDateCreated);
        description = findViewById(R.id.recipeDescription);
        ingredients = findViewById(R.id.recipeIngredients);
        instructions = findViewById(R.id.recipeInstructions);
    }

    @Override
    protected void onStart() {
        super.onStart();

        name.setText(data.getStringExtra("name"));
        description.setText("Description: " + data.getStringExtra("description"));
        dateCreated.setText("Date Created: " + data.getStringExtra("dateCreated"));
        ingredients.setText("Ingrediants: \n" + data.getStringExtra("ingredients"));
        instructions.setText("Instructions: \n" + data.getStringExtra("instructions"));
    }
}
