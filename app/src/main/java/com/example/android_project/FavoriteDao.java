package com.example.android_project;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johno on 3/26/2018.
 */

@Dao
public interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    List<Favorite> getAllFavorites();

    @Query("SELECT * FROM favorite WHERE recipeId = :recipeId")
    List<Favorite> findAllReviewsByRecipe(int recipeId);

    @Query("SELECT * FROM favorite WHERE id = :favoriteId")
    Favorite findFavorite(int favoriteId);

    @Insert
    void createFavorite(Favorite favorite);
}
