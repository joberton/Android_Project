package com.example.android_project;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by johno on 3/20/2018.
 */
@Database(entities = {Recipe.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase databaseInstance;
    public abstract RecipeDao recipeDao();

    public static AppDatabase getDatabaseContext(Context context)
    {
        if(databaseInstance == null)
        {
            databaseInstance = Room.databaseBuilder(context,AppDatabase.class,"test-database").build();
        }
        return databaseInstance;
    }

    public void destroyInstance()
    {
        databaseInstance = null;
    }
}
