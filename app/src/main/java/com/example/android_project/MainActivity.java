package com.example.android_project;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Date;
import java.util.List;


public class MainActivity extends UtilityActivity {

    private ArrayList<Recipe> recipes = new ArrayList();
    private AppDatabase db;
    private RecipeAdapter recipeAdapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                data.putExtra("ingredients",selectedRecipe.getRecipeIngredients());
                data.putExtra("description",selectedRecipe.getDescription());
                data.putExtra("dateCreated",selectedRecipe.getDateCreated());
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

            //image.setImageURI(Uri.parse(i.getImagePath()));
            //image.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(i.getImagePath()), 120, 120, false));

            name.setText(i.getName());
            description.setText(getString(R.string.descriptionTextView).concat(i.getDescription()));
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
