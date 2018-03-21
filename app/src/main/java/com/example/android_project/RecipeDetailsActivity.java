package com.example.android_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;

public class RecipeDetailsActivity extends AppCompatActivity {

    private AppDatabase db;
    private TextView name,description,ingredients,dateCreated,instructions;
    private Button update,delete;
    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        db = AppDatabase.getDatabaseContext(this);

        data = getIntent();

        update = findViewById(R.id.editRecipe);
        delete = findViewById(R.id.deleteRecipe);

        name = findViewById(R.id.recipeName);
        dateCreated = findViewById(R.id.recipeDateCreated);
        description = findViewById(R.id.recipeDescription);
        ingredients = findViewById(R.id.recipeIngredients);
        instructions = findViewById(R.id.recipeInstructions);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == delete)
                {
                    DeleteRecipeTask deleteTask = new DeleteRecipeTask();
                    deleteTask.execute();
                }
                else if(view == update)
                {
                    Intent currentRecipeData = new Intent(getApplicationContext(),UpdateRecipeActivity.class);
                    currentRecipeData.putExtras(new Bundle(data.getExtras()));
                    startActivity(currentRecipeData);
                    finish();
                }
            }
        };

        update.setOnClickListener(onClickListener);
        delete.setOnClickListener(onClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        name.setText(data.getStringExtra("name"));
        description.setText(getString(R.string.descriptionTextView).concat(data.getStringExtra("description")));
        dateCreated.setText(getString(R.string.dateCreatedTextView).concat(data.getStringExtra("dateCreated")));
        ingredients.setText(getString(R.string.ingredientsTextView).concat(data.getStringExtra("ingredients")));
        instructions.setText(getString(R.string.instructionsTextView).concat(data.getStringExtra("instructions")));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
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
