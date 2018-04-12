package com.example.android_project;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by johno on 3/26/2018.
 */

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipeId",
        onDelete = ForeignKey.CASCADE))
public class Favorite implements Comparable<Favorite>
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int favoriteId;

    @ColumnInfo(name = "reviewName")
    private String reviewName;

    @ColumnInfo(name = "rating")
    private Float rating;

    @ColumnInfo(name = "review")
    private String review;

    @ColumnInfo(name = "dateCreated")
    private Date dateCreated;

    @ColumnInfo(name = "recipeId")
    private int recipeId;

    public Favorite(String reviewName,Float rating, String review, Date dateCreated, int recipeId)
    {
        this.rating = rating;
        this.review = review;
        this.recipeId = recipeId;
        this.reviewName = reviewName;
        this.dateCreated = dateCreated;
    }

    public String getReviewName()
    {
        return reviewName;
    }

    public void setReviewName(String reviewName)
    {
        this.reviewName = reviewName;
    }

    public int getFavoriteId()
    {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId)
    {
        this.favoriteId = favoriteId;
    }

    public Float getRating()
    {
        return rating;
    }

    public void setRating(Float rating)
    {
        this.rating = rating;
    }

    public String getReview()
    {
        return review;
    }

    public void setReview(String review)
    {
        this.review = review;
    }

    public Date getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public int getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(int recipeId)
    {
        this.recipeId = recipeId;
    }

    public int compareTo(Favorite favorite)
    {
        return Comparators.DATE_CREATED.compare(this,favorite);
    }

    public static class Comparators
    {
        public static Comparator<Favorite> DATE_CREATED = new Comparator<Favorite>() {
            @Override
            public int compare(Favorite favorite, Favorite t1) {
                return favorite.getDateCreated().compareTo(t1.getDateCreated());
            }
        };

        public static Comparator<Favorite> REVIEW_NAME = new Comparator<Favorite>() {
            @Override
            public int compare(Favorite favorite, Favorite t1) {
                return favorite.getReviewName().compareTo(t1.getReviewName());
            }
        };

        public static Comparator<Favorite> RATING = new Comparator<Favorite>() {
            @Override
            public int compare(Favorite favorite, Favorite t1) {
                return favorite.getRating().compareTo(t1.getRating());
            }
        };
    }
}
