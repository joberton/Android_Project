package com.example.android_project;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by johno on 3/20/2018.
 */
@Database(entities = {Recipe.class,Category.class,Favorite.class}, version = 20)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase databaseInstance;
    private final static List<Category> CATEGORIES = Arrays.asList(new Category("Original Drink"),
                                                      new Category("Cocktail"),
                                                      new Category("Shooter"),
                                                      new Category("Liqueur"),
                                                      new Category("Other"));
    public abstract RecipeDao recipeDao();
    public abstract CategoryDao categoryDao();
    public abstract FavoriteDao favoriteDao();

    public static AppDatabase getDatabaseContext(Context context)
    {
        if(databaseInstance == null)
        {
            databaseInstance = createDatabase(context);
        }
        return databaseInstance;
    }

    public static AppDatabase createDatabase(Context context)
    {
        return Room.databaseBuilder(context,AppDatabase.class,"test-database").fallbackToDestructiveMigration().addCallback(new Callback() {
            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                //using anonymous classes to avoid recursive database calls
                // and then pre-populate the categories table
                AsyncTask initialSetup = new AsyncTask<Object,Object,Object>()
                {
                    @Override
                    protected Object doInBackground(Object... objects) {
                        //check to see if the result set from the categories table is less then categories list
                        if(databaseInstance.categoryDao().fetchAllCategories().size() < CATEGORIES.size()) {
                            databaseInstance.categoryDao().insertAllCategories(CATEGORIES);
                        }
                        return null;
                    }
                };
                initialSetup.execute();
            }
        }).build();
    }

    public void destroyInstance()
    {
        databaseInstance = null;
    }

    //Database migrations that I went through for educational purposes

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Recipe " + " ADD COLUMN image_path TEXT");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE 'Recipe' " + " ADD COLUMN 'categoryId' INTEGER DEFAULT 0 NOT NULL" +
                   " REFERENCES 'Category'(categoryId) ON UPDATE NO ACTION ON DELETE CASCADE");

            database.execSQL("CREATE TABLE Category ('id' INTEGER NOT NULL, 'categoryName' TEXT, PRIMARY KEY('id'))");

            database.execSQL("CREATE TABLE Favorite ('id' INTEGER NOT NULL, 'rating' REAL NOT NULL, 'review' TEXT, " +
                    " 'recipeId' INTEGER DEFAULT 0 NOT NULL, PRIMARY KEY('id') " +
                    "FOREIGN KEY(recipeId) REFERENCES 'Recipe'(recipeId) ON UPDATE NO ACTION ON DELETE NO ACTION)");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Favorite " + " ADD COLUMN reviewName TEXT");
        }
    };
}
