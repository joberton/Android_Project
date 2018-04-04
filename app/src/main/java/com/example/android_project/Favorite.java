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
    private String dateCreated;

    @ColumnInfo(name = "recipeId")
    private int recipeId;

    public Favorite(String reviewName,Float rating, String review, String dateCreated, int recipeId)
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

    public String getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated)
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

    //for sorting functionality later on
    public int compareTo(Favorite favorite)
    {
        return this.rating.compareTo(favorite.getRating());
    }

}
