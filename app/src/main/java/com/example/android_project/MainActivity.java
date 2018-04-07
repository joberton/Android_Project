package com.example.android_project;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MainActivity extends UtilityActivity {

    private AppDatabase db;
    private SharedPreferences sharedPreferences;

    private final int MAXIMUM_DESCRIPTION_LENGTH = 35;
    private int sortCode;

    private ArrayList<Recipe> recipes = new ArrayList();
    private RecipeAdapter recipeAdapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences("myPerfs",MODE_PRIVATE);

        sortCode = sharedPreferences.getInt("sortingCode",0);

        setTheme(sharedPreferences.getInt("theme",R.style.DarkAppTheme));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getDatabaseContext(this);
        list = findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe selectedRecipe = (Recipe) adapterView.getItemAtPosition(i);
                Intent data = new Intent(getApplicationContext(),RecipeDetailsActivity.class);

                data.putExtra("id",selectedRecipe.getRecipeId());
                data.putExtra("name",selectedRecipe.getName());
                data.putExtra("imageData",decodeBase64(selectedRecipe.getImageData()));
                data.putExtra("ingredients",selectedRecipe.getRecipeIngredients());
                data.putExtra("description",selectedRecipe.getDescription());
                data.putExtra("dateCreated",selectedRecipe.getDateCreated().toString());
                data.putExtra("instructions",selectedRecipe.getInstructions());
                data.putExtra("categoryId", selectedRecipe.getCategoryId());

                startActivity(data);
            }
        });

        recipeAdapter = new RecipeAdapter(getApplicationContext(),R.layout.recipes,recipes);

        new PopulateListViewThread().execute();
    }

    private class PopulateListViewThread extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPostExecute(Void aVoid) {
            list.setAdapter(recipeAdapter);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            recipes.addAll(db.recipeDao().getAll());
            switch(sortCode)
            {
                case SORT_BY_DATE:
                    Collections.sort(recipes);
                    reverseList(recipes,sortCode);
                    break;
                case SORT_BY_NAME:
                    Collections.sort(recipes,Recipe.Comparators.NAME);
                    break;
            }
            return null;
        }
    }

    private class RecipeAdapter extends ArrayAdapter<Recipe>
    {
        private ArrayList<Recipe> recipes;
        private int textViewResourceId;

        public RecipeAdapter(Context context,int textViewResourceId, ArrayList<Recipe> recipes)
        {
            super(context,textViewResourceId,recipes);
            this.recipes = recipes;
            this.textViewResourceId = textViewResourceId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView name,description,dateCreated;
            String truncatedDescription;
            ImageView image;
            View view = convertView;

            if(view == null)
            {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(textViewResourceId,null);
            }
            Recipe i = recipes.get(position);
            image = view.findViewById(R.id.drinkImage);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            dateCreated = view.findViewById(R.id.dateCreated);


            Bitmap imageMap = Bitmap.createScaledBitmap(decodeBitmap(decodeBase64(i.getImageData())), 250, 250, false);
            image.setImageBitmap(imageMap);

            truncatedDescription = i.getDescription();
            truncatedDescription =  MAXIMUM_DESCRIPTION_LENGTH >= truncatedDescription.length() ? truncatedDescription : truncatedDescription.substring(0,MAXIMUM_DESCRIPTION_LENGTH).concat("........");

            name.setText(i.getName());
            description.setText(getString(R.string.descriptionTextView).concat(truncatedDescription));
            dateCreated.setText(getString(R.string.dateCreatedTextView).concat(i.getDateCreated().toString()));
            return view;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.newRecipe:
                startActivity(new Intent(getApplicationContext(),NewRecipeActivity.class));
                break;
            case R.id.historyOfIngredients:
                startActivity(new Intent(getApplicationContext(),RecipeHistoryActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
