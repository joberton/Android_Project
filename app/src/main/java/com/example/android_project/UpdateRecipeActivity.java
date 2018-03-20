package com.example.android_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateRecipeActivity extends AppCompatActivity {

    private String action;

    private AppDatabase db;
    private Intent data;

    private TextView typeOfAction;
    private EditText drinkName,drinkDescription,dateCreated,ingredientsData,drinkInstructions;
    private Button update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        action = getString(R.string.updateButton);

        db = AppDatabase.getDatabaseContext(this);
        data = getIntent();

        typeOfAction = findViewById(R.id.typeOfAction);
        typeOfAction.setText(action);

        update = findViewById(R.id.createRecipe);
        update.setText(action);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateRecipeTask updateRecipeTask = new UpdateRecipeTask();
                updateRecipeTask.execute();
            }
        });

        drinkName = findViewById(R.id.newRecipeName);
        drinkDescription = findViewById(R.id.newRecipeDescription);
        ingredientsData = findViewById(R.id.ingredientsData);
        dateCreated = findViewById(R.id.newDateCreated);
        drinkInstructions = findViewById(R.id.newRecipeInstructions);
    }

    @Override
    protected void onStart() {
        super.onStart();
        drinkName.setText(data.getStringExtra("name"));
        drinkDescription.setText(data.getStringExtra("description"));
        ingredientsData.setText(data.getStringExtra("ingredients"));
        dateCreated.setText(data.getStringExtra("dateCreated"));
        drinkInstructions.setText(data.getStringExtra("instructions"));
    }

    private String getViewString(int id)
    {
        return ((EditText)findViewById(id)).getText().toString();
    }

    private class UpdateRecipeTask extends AsyncTask<Void,Void,Void>
    {
        private Recipe updateRecipe;

        @Override
        protected Void doInBackground(Void... voids) {
            updateRecipe = db.recipeDao().findRecipe(data.getIntExtra("id",-1));
            updateRecipe.setName(getViewString(drinkName.getId()));
            updateRecipe.setDescription(getViewString(drinkDescription.getId()));
            updateRecipe.setRecipeIngredients(getViewString(ingredientsData.getId()));;
            updateRecipe.setDateCreated(getViewString(dateCreated.getId()));
            updateRecipe.setInstructions(getViewString(drinkInstructions.getId()));
            db.recipeDao().update(updateRecipe);
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
            return null;
        }
    }
}
