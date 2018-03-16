package com.example.android_project;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by johno on 3/13/2018.
 */

public class Recipe {

    private String name;
    private String dateCreated;
    private ArrayList<String> ingredients;
    private String description;

    public Recipe(String name, String dateCreated, ArrayList<String> ingredients,String description)
    {
        this.name = name;
        this.dateCreated = dateCreated;
        this.ingredients = ingredients;
        this.description = description;
    }

    public ArrayList<String> getIngredients()
    {
        return ingredients;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public String getDateCreated()
    {
        return dateCreated;
    }
}
