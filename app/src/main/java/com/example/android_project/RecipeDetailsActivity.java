package com.example.android_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeDetailsActivity extends UtilityActivity {

    private AppDatabase db;
    private SharedPreferences sharedPreferences;
    private Intent data;

    private TextView name;
    private EditText description,ingredients,dateCreated,instructions;
    private ImageView detailsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences("myPerfs",MODE_PRIVATE);

        setTheme(sharedPreferences.getInt("theme",R.style.DarkAppTheme));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        db = AppDatabase.getDatabaseContext(this);

        data = getIntent();

        detailsImage = findViewById(R.id.detailsImage);
        name = findViewById(R.id.recipeName);
        dateCreated = findViewById(R.id.recipeDateCreated);
        description = findViewById(R.id.recipeDescription);
        ingredients = findViewById(R.id.recipeIngredients);
        instructions = findViewById(R.id.recipeInstructions);
    }

    @Override
    protected void onStart() {
        super.onStart();

        detailsImage.setImageBitmap(decodeBitmap(data.getByteArrayExtra("imageData")));

        name.setText(data.getStringExtra("name"));
        description.setText(data.getStringExtra("description"));
        dateCreated.setText(data.getStringExtra("dateCreated"));
        ingredients.setText(data.getStringExtra("ingredients"));
        instructions.setText(data.getStringExtra("instructions"));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent reviewData;
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
            case R.id.historyOfIngredients:
                startActivity(new Intent(getApplicationContext(),RecipeHistoryActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
            case R.id.review:
                reviewData = new Intent(getApplicationContext(), NewReviewActivity.class);
                reviewData.putExtra("id",data.getIntExtra("id",0));
                startActivity(reviewData);
                break;
            case R.id.allReviews:
                reviewData = new Intent(getApplicationContext(), ReviewsActivity.class);
                reviewData.putExtra("recipeId",data.getIntExtra("id",0));
                startActivity(reviewData);
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
