package com.example.android_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewDetailsActivity extends UtilityActivity {

    private AppDatabase db;
    private Intent data;

    private TextView detailsName,detailsReview;
    private RatingBar detailsRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);

        db = AppDatabase.getDatabaseContext(this);
        data = getIntent();

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
            case R.id.updateReview:
                Intent updateData = new Intent(getApplicationContext(),ReviewUpdateActivity.class);
                updateData.putExtras(new Bundle(data.getExtras()));
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
            startActivity(new Intent(getApplicationContext(),ReviewsActivity.class));
            finish();
            return null;
        }
    }
}
