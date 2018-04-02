package com.example.android_project;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Update
    void updateFavorite(Favorite favorite);

    @Delete
    void deleteFavorite(Favorite favorite);
}
