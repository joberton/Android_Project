package com.example.android_project;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Recipe> recipes = new ArrayList();
    private AppDatabase db;
    private RecipeAdapter recipeAdapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"test-database").allowMainThreadQueries().build();

        list = findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe selectedRecipe = (Recipe) adapterView.getItemAtPosition(i);
                Intent data = new Intent(getApplicationContext(),RecipeDetailsActivity.class);

                data.putExtra("name",selectedRecipe.getName());
                data.putExtra("ingredients",selectedRecipe.getRecipeIngredients());
                data.putExtra("description",selectedRecipe.getDescription());
                data.putExtra("dateCreated",selectedRecipe.getDateCreated());
                data.putExtra("instructions",selectedRecipe.getInstructions());
                startActivity(data);
            }
        });

        recipes.addAll(db.recipeDao().getAll());

        recipeAdapter = new RecipeAdapter(getApplicationContext(),R.layout.recipes,recipes);
        list.setAdapter(recipeAdapter);

    }

    /*private List<HashMap<String,String>> createKeyMapForRecipeIngredients(String[][] data)
    {
        final String[] keys = {"ingredient","measurement"};
        List<HashMap<String,String>> ingredientsData = new ArrayList();
        for(int i = 0; i < data.length; i++) {
            HashMap<String,String> ingredient = new HashMap();
            for(int inner = 0; inner < data[i].length; inner++) {
                ingredient.put(keys[inner],data[i][inner]);
            }
            ingredientsData.add(ingredient);
        }
        return ingredientsData;
    }*/

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
            View view = convertView;
            if(view == null)
            {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(textViewResourceId,null);
            }
            Recipe i = recipes.get(position);

            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            dateCreated = view.findViewById(R.id.dateCreated);

            name.setText(i.getName());
            description.setText("Description: " + i.getDescription());
            dateCreated.setText("Date Created: " + i.getDateCreated());
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
