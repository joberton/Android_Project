package com.example.android_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class QuickRecipesDetailsActivity extends UtilityActivity {

    private SharedPreferences sharedPreferences;
    private Intent data;

    private TextView apiDrinkName;
    private EditText apiDrinkCreated,apiDrinkInstructions;
    private ImageView apiDrinkImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences("myPerfs",MODE_PRIVATE);

        setTheme(sharedPreferences.getInt("theme",R.style.DarkAppTheme));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_recipes_details);

        data = getIntent();

        apiDrinkImage = findViewById(R.id.apiDrinkImageDetails);
        apiDrinkName = findViewById(R.id.apiDrinkNameDetails);
        apiDrinkCreated = findViewById(R.id.apiDrinkDateCreatedDetails);
        apiDrinkInstructions = findViewById(R.id.apiDrinkInsructions);
    }

    @Override
    protected void onStart() {
        super.onStart();

        apiDrinkImage.setImageBitmap(decodeBitmap(data.getByteArrayExtra("imageData")));
        apiDrinkName.setText(data.getStringExtra("drinkName"));
        apiDrinkCreated.setText(data.getStringExtra("drinkDateCreated"));
        apiDrinkInstructions.setText(data.getStringExtra("drinkInstructions"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
            case android.R.id.home:
                onBackPressed();
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
