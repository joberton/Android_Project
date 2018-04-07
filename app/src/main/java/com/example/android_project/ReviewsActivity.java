package com.example.android_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class ReviewsActivity extends UtilityActivity {

    private AppDatabase db;
    private SharedPreferences sharedPreferences;
    private Intent data;

    private int sortCode;

    private ArrayList<Favorite> favorites = new ArrayList();

    private ReviewAdapter reviewAdapter;
    private ListView reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences("myPerfs",MODE_PRIVATE);

        setTheme(sharedPreferences.getInt("theme",R.style.DarkAppTheme));

        sortCode = sharedPreferences.getInt("sortingCode",0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        db = AppDatabase.getDatabaseContext(this);
        data = getIntent();

        reviewAdapter = new ReviewAdapter(getApplicationContext(),R.layout.reviews,favorites);
        reviewList = findViewById(R.id.reviewList);

        reviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Favorite selectedFavorite = (Favorite) adapterView.getItemAtPosition(i);
                Intent favoriteIntent = new Intent(getApplicationContext(),ReviewDetailsActivity.class);
                favoriteIntent.putExtra("id",selectedFavorite.getFavoriteId());
                favoriteIntent.putExtra("createdDate",selectedFavorite.getDateCreated().toString());
                favoriteIntent.putExtra("reviewName",selectedFavorite.getReviewName());
                favoriteIntent.putExtra("rating",selectedFavorite.getRating());
                favoriteIntent.putExtra("review",selectedFavorite.getReview());
                favoriteIntent.putExtra("recipeId",selectedFavorite.getRecipeId());
                startActivity(favoriteIntent);
            }
        });

        new FetchAllReviews().execute();
    }

    private class FetchAllReviews extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPostExecute(Void aVoid) {
            reviewList.setAdapter(reviewAdapter);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            favorites.addAll(db.favoriteDao().findAllReviewsByRecipe(data.getIntExtra("recipeId",0)));
            switch(sortCode)
            {
                case SORT_BY_DATE:
                    Collections.sort(favorites);
                    reverseList(favorites,sortCode);
                    break;
                case SORT_BY_NAME:
                    Collections.sort(favorites,Favorite.Comparators.REVIEW_NAME);
                    break;
                case SORT_BY_RATING:
                    Collections.sort(favorites,Favorite.Comparators.RATING);
                    reverseList(favorites,sortCode);
                    break;
            }
            return null;
        }
    }

    private class ReviewAdapter extends ArrayAdapter<Favorite>
    {
        private ArrayList<Favorite> favorites;
        private int textViewResourceId;

        public ReviewAdapter(Context context, int textViewResourceId, ArrayList<Favorite> favorites)
        {
            super(context,textViewResourceId,favorites);
            this.textViewResourceId = textViewResourceId;
            this.favorites = favorites;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            RatingBar reviewRating;
            TextView reviewName,reviewDateCreated;
            if(view == null)
            {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(textViewResourceId,null);
            }
            Favorite i = favorites.get(position);

            reviewRating = view.findViewById(R.id.reviewRating);
            reviewName = view.findViewById(R.id.reviewName);
            reviewDateCreated = view.findViewById(R.id.reviewDateCreated);

            reviewRating.setRating(i.getRating());
            reviewName.setText(i.getReviewName());
            reviewDateCreated.setText(getString(R.string.dateCreatedTextView) + i.getDateCreated());
            return view;
        }
    }
}
