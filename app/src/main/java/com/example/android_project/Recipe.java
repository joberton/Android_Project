package com.example.android_project;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.util.ArrayMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by johno on 3/13/2018.
 */

@Entity
public class Recipe {

    @PrimaryKey(autoGenerate = true)
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

    //private Bitmap image;

    public Recipe(String name, String dateCreated, String recipeIngredients, String description, String instructions)
    {
        this.name = name;
        this.dateCreated = dateCreated;
        this.recipeIngredients = recipeIngredients;
        this.description = description;
        this.instructions = instructions;
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
}
