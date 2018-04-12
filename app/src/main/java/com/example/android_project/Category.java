package com.example.android_project;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by johno on 3/26/2018.
 */
@Entity
public class Category
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int categoryId;

    @ColumnInfo(name = "categoryName")
    private String categoryName;

    public Category(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }
}
