package com.example.android_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewDetailsActivity extends UtilityActivity {

    private AppDatabase db;
    private SharedPreferences sharedPreferences;
    private Intent data;

    private EditText detailsReview,detailsDateCreated;
    private TextView detailsName;
    private RatingBar detailsRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences("myPerfs",MODE_PRIVATE);

        setTheme(sharedPreferences.getInt("theme",R.style.DarkAppTheme));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);

        db = AppDatabase.getDatabaseContext(this);
        data = getIntent();

        detailsDateCreated = findViewById(R.id.recipeDateCreated);
        detailsName = findViewById(R.id.detailsName);

        detailsReview = findViewById(R.id.detailsDescription);
        detailsRating = findViewById(R.id.detailsRating);
    }

    @Override
    protected void onStart() {
        super.onStart();

        detailsName.setText(data.getStringExtra("reviewName"));
        detailsReview.setText(data.getStringExtra("review"));
        detailsRating.setRating(data.getFloatExtra("rating",0));
        detailsDateCreated.setText(data.getStringExtra("createdDate"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.recipesToGo:
                startActivity(new Intent(getApplicationContext(),QuickRecipesActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
            case R.id.updateReview:
                Intent updateData = new Intent(getApplicationContext(),ReviewUpdateActivity.class);
                updateData.putExtras(data.getExtras());
                startActivity(updateData);
                break;
            case R.id.deleteReview:
                new DeleteReviewTask().execute();
                break;
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.review_details_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class DeleteReviewTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            Favorite deleteFavorite = db.favoriteDao().findFavorite(data.getIntExtra("id",0));
            db.favoriteDao().deleteFavorite(deleteFavorite);
            Intent updateIntent = new Intent(getApplicationContext(),ReviewsActivity.class);
            updateIntent.putExtras(data.getExtras());
            startActivity(updateIntent);
            finish();
            return null;
        }
    }
}
