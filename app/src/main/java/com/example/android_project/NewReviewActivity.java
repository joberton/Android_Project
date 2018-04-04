package com.example.android_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import java.util.Calendar;

public class NewReviewActivity extends UtilityActivity {

    private AppDatabase db;
    private Intent data;

    private RatingBar newRatingBar;
    private EditText newReviewName,newReview;
    private Button createReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_review);

        db = AppDatabase.getDatabaseContext(this);
        data = getIntent();

        newReviewName = findViewById(R.id.newReviewName);
        newRatingBar = findViewById(R.id.newRecipeRating);
        newReview = findViewById(R.id.newRecipeReview);
        createReview = findViewById(R.id.newReview);

        createReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewReviewTask().execute();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

    private class NewReviewTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {

            db.favoriteDao().createFavorite(new Favorite(getViewString(newReviewName.getId()),
                                            newRatingBar.getRating(),
                                            getViewString(newReview.getId()),
                                            Calendar.getInstance().getTime().toString(),
                                            data.getIntExtra("id",0)));
            return null;
        }
    }

}
