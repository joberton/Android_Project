package com.example.android_project;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by johno on 3/13/2018.
 */

@Entity(foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "id",
        childColumns = "categoryId"))
public class Recipe implements Comparable<Recipe>{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int recipeId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "date_created")
    private String dateCreated;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "ingredients")
    private String recipeIngredients;

    @ColumnInfo(name = "instructions")
    private String instructions;

    @ColumnInfo(name = "image_data")
    private String imageData;

    @ColumnInfo(name = "categoryId")
    private int categoryId;

    public Recipe(String name, String dateCreated, String recipeIngredients, String description, String instructions, String imageData, int categoryId)
    {
        this.name = name;
        this.dateCreated = dateCreated;
        this.recipeIngredients = recipeIngredients;
        this.description = description;
        this.instructions = instructions;
        this.imageData = imageData;
        this.categoryId = categoryId;
    }

    public int getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(int recipeId)
    {
        this.recipeId = recipeId;
    }

    public String getInstructions()
    {
        return instructions;
    }

    public void setInstructions(String instructions)
    {
        this.instructions = instructions;
    }

    public String getRecipeIngredients()
    {
        return recipeIngredients;
    }

    public void setRecipeIngredients(String recipeIngredients)
    {
        this.recipeIngredients = recipeIngredients;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public String getImageData()
    {
        return imageData;
    }

    public void setImageData(String imageData)
    {
        this.imageData = imageData;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }

    //for sorting functionality later on
    public int compareTo(Recipe recipe)
    {
        return this.name.compareTo(recipe.getName());
    }
}
