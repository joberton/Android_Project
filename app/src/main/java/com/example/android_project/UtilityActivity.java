package com.example.android_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class UtilityActivity extends AppCompatActivity {

    //global utility functions for the application go here...
    //such as parsing data

    public double parseDoubleData(int id)
    {
        String data = getViewString(id);
        return data.length() > 0 ? Double.parseDouble(data) : 0.0;
    }

    public int parseIntData(int id)
    {
        String data = getViewString(id);
        return data.length() > 0 ? Integer.parseInt(data) : 0;
    }

    public String getViewString(int id)
    {
        return ((EditText)findViewById(id)).getText().toString();
    }

    //default action bar behaviour
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
        return true;
    }

    //default action bar behaviour
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
