package com.example.android_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewUpdateActivity extends UtilityActivity {

    private AppDatabase db;
    private Intent data;

    private String action;

    private TextView typeOfAction;
    private RatingBar updateRatingBar;
    private EditText updateReviewName,updateDescription;
    private Button updateReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_review);

        db = AppDatabase.getDatabaseContext(this);
        data = getIntent();

        action = getString(R.string.updateReview);

        typeOfAction = findViewById(R.id.reviewTypeOfAction);

        updateRatingBar = findViewById(R.id.newRecipeRating);
        updateReviewName = findViewById(R.id.newReviewName);
        updateDescription = findViewById(R.id.newRecipeReview);

        updateReview = findViewById(R.id.newReview);

        updateReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateReviewTask().execute();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateReview.setText(action);
        typeOfAction.setText(action);
        updateRatingBar.setRating(data.getFloatExtra("rating",0));
        updateReviewName.setText(data.getStringExtra("reviewName"));
        updateDescription.setText(data.getStringExtra("review"));
    }

    private class UpdateReviewTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            Favorite updateFavorite = db.favoriteDao().findFavorite(data.getIntExtra("id",0));

            updateFavorite.setRating(updateRatingBar.getRating());
            updateFavorite.setReview(getViewString(updateDescription.getId()));
            updateFavorite.setReviewName(getViewString(updateReviewName.getId()));

            db.favoriteDao().updateFavorite(updateFavorite);

            Intent updateIntent = new Intent(getApplicationContext(),ReviewsActivity.class);
            updateIntent.putExtras(data.getExtras());
            startActivity(updateIntent);
            finish();
            return null;
        }
    }
}
