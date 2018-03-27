package com.example.android_project;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by johno on 3/26/2018.
 */

@Dao
public interface CategoryDao
{
    @Query("SELECT * FROM category")
    List<Category> fetchAllCategories();

    @Query("SELECT categoryName FROM category")
    List<String> fetchCategoryNames();

    @Query("SELECT * FROM category WHERE id = :categoryId")
    Category findCategory(int categoryId);

    @Insert
    void insertCategory(Category category);

    @Delete
    void deleteCategory(Category category);
}
