package com.example.android_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class RecipeDetailsActivity extends AppCompatActivity {

    private AppDatabase db;
    private TextView name,description,ingredients,dateCreated,instructions;
    private Intent data;
    private String nameValue,descriptionValue,dateCreatedValue,ingrediantsValue,instructionsValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        db = AppDatabase.getDatabaseContext(this);

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

        nameValue = data.getStringExtra("name");
        descriptionValue =  "\n" + data.getStringExtra("description");
        dateCreatedValue = data.getStringExtra("dateCreated");
        ingrediantsValue = "\n" + data.getStringExtra("ingredients");
        instructionsValue = "\n" + data.getStringExtra("instructions");

        name.setText(nameValue);
        description.setText(getString(R.string.descriptionTextView).concat(descriptionValue));
        dateCreated.setText(getString(R.string.dateCreatedTextView).concat(dateCreatedValue));
        ingredients.setText(getString(R.string.ingredientsTextView).concat(ingrediantsValue));
        instructions.setText(getString(R.string.instructionsTextView).concat(instructionsValue));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.updateRecipe:
                Intent currentRecipeData = new Intent(getApplicationContext(),UpdateRecipeActivity.class);
                currentRecipeData.putExtras(new Bundle(data.getExtras()));
                startActivity(currentRecipeData);
                finish();
                break;
            case R.id.deleteRecipe:
                new DeleteRecipeTask().execute();
                break;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
            case R.id.review:
                startActivity(new Intent(getApplicationContext(), NewReviewActivity.class));
                break;
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_details_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    private class DeleteRecipeTask extends AsyncTask<Void,Void,Void>
    {
        private Recipe deleteRecipe;

        @Override
        protected Void doInBackground(Void... voids) {
            deleteRecipe = db.recipeDao().findRecipe(data.getIntExtra("id",-1));

            db.recipeDao().delete(deleteRecipe);

            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
            return null;
        }
    }
}
