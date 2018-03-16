package com.example.android_project;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLogTags;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Recipe> recipes = new ArrayList();
    private ArrayList<String> ingredients = new ArrayList();
    private ArrayList<String> ingredients1 = new ArrayList<>();
    private RecipeAdapter recipeAdapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe selectedRecipe = (Recipe) adapterView.getItemAtPosition(i);
                Intent data = new Intent(getApplicationContext(),RecipeDetailsActivity.class);
                data.putExtra("name",selectedRecipe.getName());
                data.putExtra("description",selectedRecipe.getDescription());
                data.putExtra("dateCreated",selectedRecipe.getDateCreated());
                startActivity(data);
            }
        });

        ingredients.addAll(Arrays.asList("Tabasco Sauce","Tequila"));
        ingredients1.addAll(Arrays.asList("Vodka","Spiced Rum","Whiskey","Gin"));

        Recipe prairieFire = new Recipe("Prairie Fire","13/3/2018",ingredients,"A nice kick to this drink.....");
        Recipe fourHorseMan = new Recipe("Four Horse Man","13/3/2018",ingredients1,"You ready to bring your own demise.....");

        recipes.addAll(Arrays.asList(prairieFire,fourHorseMan));

        recipeAdapter = new RecipeAdapter(getApplicationContext(),R.layout.recipes,recipes);
        list.setAdapter(recipeAdapter);

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
