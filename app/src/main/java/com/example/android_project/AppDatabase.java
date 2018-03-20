package com.example.android_project;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.android_project.Recipe;
import com.example.android_project.RecipeDao;

/**
 * Created by johno on 3/20/2018.
 */
@Database(entities = {Recipe.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract RecipeDao recipeDao();
}
