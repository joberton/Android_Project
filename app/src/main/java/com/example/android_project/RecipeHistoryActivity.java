package com.example.android_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeHistoryActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private ArrayList<DrinkHistory> drinkHistoryList = new ArrayList();
    private DrinkHistoryAdapter drinkHistoryAdapter;
    private ListView historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences("myPerfs",MODE_PRIVATE);

        setTheme(sharedPreferences.getInt("theme",R.style.DarkAppTheme));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_history);

        historyList = findViewById(R.id.historyList);

        drinkHistoryList.addAll(Arrays.asList(new DrinkHistory("Vodka","Talk about the history and how it refers to drink"),
                                          new DrinkHistory("Gin","Talk about the history and how it refers to drink"),
                                          new DrinkHistory("Rum","Talk about the history and how it refers to drink")));
        drinkHistoryAdapter = new DrinkHistoryAdapter(getApplicationContext(),R.layout.drink_history,drinkHistoryList);

        historyList.setAdapter(drinkHistoryAdapter);
    }

    private class DrinkHistoryAdapter extends ArrayAdapter<DrinkHistory>
    {
        private ArrayList<DrinkHistory> drinkHistoryList;
        private int textViewResourceId;

        public DrinkHistoryAdapter(Context context, int textViewResourceId, ArrayList<DrinkHistory> drinkHistoryList)
        {
            super(context,textViewResourceId,drinkHistoryList);
            this.drinkHistoryList = drinkHistoryList;
            this.textViewResourceId = textViewResourceId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            TextView drinkName,drinkHistory;
            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(textViewResourceId,null);;

            }
            DrinkHistory i = drinkHistoryList.get(position);

            drinkName = view.findViewById(R.id.drinkName);
            drinkHistory = view.findViewById(R.id.drinkHistory);

            drinkName.setText(i.getDrinkName());
            drinkHistory.setText(getString(R.string.history).concat(i.getDrinkHistory()));
            return view;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.just_settings_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
