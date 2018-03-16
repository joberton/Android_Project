package com.example.android_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RecipeDetailsActivity extends AppCompatActivity {

    private TextView name,description,ingredients,dateCreated,instructions;
    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        data = getIntent();

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
        ingredients.setText("Ingrediants: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean aliquet pellentesque quam, " +
                "eget malesuada nisi pharetra eu. Aenean at feugiat ipsum, sit amet ornare justo. Morbi vitae neque turpis. " +
                "Suspendisse volutpat dui ligula, sed accumsan nisi efficitur sit amet. Nam quis purus ut est vestibulum luctus " +
                "vel non quam. Nunc nibh elit, ultricies in dapibus sed, ornare posuere turpis. Nulla luctus ipsum erat, sit amet " +
                "malesuada lectus luctus ut. Morbi massa enim, egestas vel urna sed, congue pulvinar purus. Etiam posuere ex id risus molestie, " +
                "at dignissim ipsum blandit. Proin vestibulum dapibus lorem quis dignissim. Cras id viverra neque. Sed orci lacus, " +
                "auctor vitae ipsum sed, dictum efficitur turpis. Quisque consequat lacinia porttitor.");
        instructions.setText("Instructions: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean aliquet pellentesque quam, " +
                "eget malesuada nisi pharetra eu. Aenean at feugiat ipsum, sit amet ornare justo. Morbi vitae neque turpis. " +
                "Suspendisse volutpat dui ligula, sed accumsan nisi efficitur sit amet. Nam quis purus ut est vestibulum luctus " +
                "vel non quam. Nunc nibh elit, ultricies in dapibus sed, ornare posuere turpis. Nulla luctus ipsum erat, sit amet " +
                "malesuada lectus luctus ut. Morbi massa enim, egestas vel urna sed, congue pulvinar purus. Etiam posuere ex id risus molestie, " +
                "at dignissim ipsum blandit. Proin vestibulum dapibus lorem quis dignissim. Cras id viverra neque. Sed orci lacus, " +
                "auctor vitae ipsum sed, dictum efficitur turpis. Quisque consequat lacinia porttitor.");
    }
}
