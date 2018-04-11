package com.example.android_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private int sortCode;
    private boolean themeSelection;

    private Switch setTheme;
    private Spinner sortingOptions;
    private SharedPreferences.Editor editor;
    private ArrayAdapter<CharSequence> sortingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences("myPerfs",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setTheme(sharedPreferences.getInt("theme",R.style.DarkAppTheme));

        sortCode = sharedPreferences.getInt("sortingCode",0);
        themeSelection = sharedPreferences.getBoolean("themeSelection",false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setTheme = findViewById(R.id.themeSetting);
        sortingOptions = findViewById(R.id.sortingSettings);

        sortingAdapter = ArrayAdapter.createFromResource(this,R.array.sortingOptions,R.layout.support_simple_spinner_dropdown_item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setTheme.setChecked(themeSelection);
        sortingOptions.setAdapter(sortingAdapter);
        sortingOptions.setSelection(sortCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.just_drink_history_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.recipesToGo:
                startActivity(new Intent(getApplicationContext(),QuickRecipesActivity.class));
                break;
            case android.R.id.home:
                persistSettings();
                break;
        }
        return true;
    }

    public void persistSettings()
    {
        editor.putInt("theme",!setTheme.isChecked() ? R.style.DarkAppTheme : R.style.LightAppTheme);
        editor.putBoolean("themeSelection",setTheme.isChecked());
        editor.putInt("sortingCode",sortingOptions.getSelectedItemPosition());
        editor.commit();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        persistSettings();
    }
}
