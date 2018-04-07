package com.example.android_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class NewReviewActivity extends UtilityActivity {

    private AppDatabase db;
    private SharedPreferences sharedPreferences;
    private Intent data;

    private String reviewNameValue;
    private String reviewValue;

    private RatingBar newRatingBar;
    private EditText newReviewName,newReview;
    private Button createReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences("myPerfs",MODE_PRIVATE);

        setTheme(sharedPreferences.getInt("theme",R.style.DarkAppTheme));

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

                reviewNameValue = getViewString(newReviewName.getId()).trim();
                reviewValue = getViewString(newReview.getId()).trim();

                final boolean[] VALIDATION_CHECKS = {isNotBlank(reviewNameValue),
                                                     isNotBlank(reviewValue)};

                ArrayMap<EditText,String> validationMap = new ArrayMap<>();
                validationMap.put(newReviewName,"Please provide a name for your review");
                validationMap.put(newReview,"Please provide a review for this recipe");

                final ArrayMap<EditText,String> FORM_ERRORS = formValidation(validationMap,VALIDATION_CHECKS);

                if(FORM_ERRORS.size() <= 0) {
                    new NewReviewTask().execute();
                }
                else
                {
                    buildErrorMessages(FORM_ERRORS);
                }
                //Toast.makeText(getApplicationContext(),buildErrorMessages(FORM_ERRORS),Toast.LENGTH_LONG).show();
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
                                            Calendar.getInstance().getTime(),
                                            data.getIntExtra("id",0)));
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return null;
        }
    }

}
