package com.example.android_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class UpdateRecipeActivity extends UtilityActivity {

    private String action;

    private AppDatabase db;
    private Intent data;

    private TextView typeOfAction;
    private EditText drinkName,drinkDescription,ingredientsData,drinkInstructions;
    private ArrayAdapter<String> categoriesAdapter;
    private Spinner categoriesSpinner;
    private Button update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        action = getString(R.string.updateView);

        db = AppDatabase.getDatabaseContext(this);
        data = getIntent();

        typeOfAction = findViewById(R.id.typeOfAction);
        typeOfAction.setText(action);

        update = findViewById(R.id.createRecipe);
        update.setText(action);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateRecipeTask().execute();
            }
        });

        drinkName = findViewById(R.id.newRecipeName);
        drinkDescription = findViewById(R.id.newRecipeDescription);
        ingredientsData = findViewById(R.id.ingredientsData);
        drinkInstructions = findViewById(R.id.newRecipeInstructions);
        categoriesSpinner = findViewById(R.id.categoriesSpinner);

        new LoadCategoriesTask().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        drinkName.setText(data.getStringExtra("name"));
        drinkDescription.setText(data.getStringExtra("description"));
        ingredientsData.setText(data.getStringExtra("ingredients"));
        drinkInstructions.setText(data.getStringExtra("instructions"));
    }

    private class LoadCategoriesTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPostExecute(Void aVoid) {
            categoriesSpinner.setAdapter(categoriesAdapter);
            categoriesSpinner.setSelection(data.getIntExtra("categoryId",0) - 1);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            categoriesAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item);
            categoriesAdapter.addAll(db.categoryDao().fetchCategoryNames());
            return null;
        }
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
            updateRecipe.setDateCreated(Calendar.getInstance().getTime().toString());
            updateRecipe.setInstructions(getViewString(drinkInstructions.getId()));
            updateRecipe.setCategoryId(categoriesSpinner.getSelectedItemPosition() + 1);

            db.recipeDao().update(updateRecipe);

            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
            return null;
        }
    }
}
