package com.example.android_project;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

/**
 * Created by johno on 3/26/2018.
 */

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipeId"))
public class Favorite
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int favoriteId;

    @ColumnInfo(name = "reviewName")
    private String reviewName;

    @ColumnInfo(name = "rating")
    private float rating;

    @ColumnInfo(name = "review")
    private String review;

    @ColumnInfo(name = "recipeId")
    private int recipeId;

    public Favorite(String reviewName,float rating, String review, int recipeId)
    {
        this.rating = rating;
        this.review = review;
        this.recipeId = recipeId;
        this.reviewName = reviewName;
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

    public float getRating()
    {
        return rating;
    }

    public void setRating(float rating)
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

    public int getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(int recipeId)
    {
        this.recipeId = recipeId;
    }
}
