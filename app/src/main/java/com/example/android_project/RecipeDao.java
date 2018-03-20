package com.example.android_project;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by johno on 3/20/2018.
 */

@Dao
public interface RecipeDao {
    @Query("Select * FROM recipe")
    List<Recipe> getAll();

    @Update
    void update(Recipe recipe);

    @Insert
    void insertAll(Recipe... recipes);

    @Delete
    void delete(Recipe recipe);
}
