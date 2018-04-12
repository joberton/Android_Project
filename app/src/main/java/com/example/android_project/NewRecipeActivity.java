package com.example.android_project;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class NewRecipeActivity extends UtilityActivity {

    private AppDatabase db;
    private SharedPreferences sharedPreferences;

    private ArrayAdapter<String> categoriesAdapter;

    private String drinkNameValue;
    private String drinkDescriptionValue;
    private String ingredientsDataValue;
    private String drinkInstructionsValue;
    private String imageData;

    private EditText drinkName,drinkDescription,ingredientsData,drinkInstructions;
    private ImageView drinkImage;
    private Spinner categoriesSpinner;
    private Button galleryImage,create;


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

                drinkNameValue = getViewString(drinkName.getId());
                drinkDescriptionValue = getViewString(drinkDescription.getId());
                ingredientsDataValue = getViewString(ingredientsData.getId());
                drinkInstructionsValue = getViewString(drinkInstructions.getId());

                final EditText[] FORM_EDIT_TEXTS = {drinkName,drinkDescription,ingredientsData,drinkInstructions};

                final boolean[] VALIDATION_CHECKS = {Utility.isNotBlank(drinkNameValue.trim()),
                        Utility.isNotBlank(drinkDescriptionValue.trim()),
                        Utility.isNotBlank(ingredientsDataValue.trim()),
                        Utility.isNotBlank(drinkInstructionsValue.trim())};

                final boolean IMAGE_DATA_UPLOADED = Utility.isNotBlank(imageData);

                final List<EditText> FORM_ERRORS = Utility.formValidation(FORM_EDIT_TEXTS,VALIDATION_CHECKS,Utility.RECIPES_ERROR_MESSAGES);

                if(view == create && FORM_ERRORS.size() <= 0 && IMAGE_DATA_UPLOADED)
                {
                    Recipe newRecipe = new Recipe(drinkNameValue,
                            Calendar.getInstance().getTime(),
                            ingredientsDataValue,
                            drinkDescriptionValue,
                            drinkInstructionsValue,
                            imageData,categoriesSpinner.getSelectedItemPosition() + 1);

                    new NewRecipeAsyncTask(newRecipe).execute();
                }
                else if(view == galleryImage)
                {
                    requestImageFromGallery();
                }
                else if(!IMAGE_DATA_UPLOADED)
                {
                    Toast.makeText(getApplicationContext(),"Please upload an image",Toast.LENGTH_SHORT).show();
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
        drinkImage.setImageBitmap(Utility.decodeBitmap(Utility.decodeBase64(imageData)));
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
