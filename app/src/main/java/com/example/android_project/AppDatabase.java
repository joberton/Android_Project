package com.example.android_project;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by johno on 3/20/2018.
 */
@Database(entities = {Recipe.class,Category.class,Favorite.class}, version = 10)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase databaseInstance;
    public abstract RecipeDao recipeDao();
    public abstract CategoryDao categoryDao();
    public abstract FavoriteDao favoriteDao();

    public static AppDatabase getDatabaseContext(Context context)
    {
        if(databaseInstance == null)
        {
            databaseInstance = Room.databaseBuilder(context,AppDatabase.class,"test-database").fallbackToDestructiveMigration().build();
        }
        return databaseInstance;
    }

    public void destroyInstance()
    {
        databaseInstance = null;
    }

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

    static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //dropped my entire schema and recreated it to avoid headaches
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Favorite " + " ADD COLUMN reviewName TEXT");
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5,6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Favorite " + " ADD COLUMN dateCreated TEXT");
        }
    };

    static final Migration MIGRATION_6_7 = new Migration(6,7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //dropped my entire schema and recreated it to avoid headaches
        }
    };
    static final Migration MIGRATION_7_8 = new Migration(7,8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //dropped my entire schema and recreated it to avoid headaches
        }
    };
}
