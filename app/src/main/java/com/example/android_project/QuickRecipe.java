package com.example.android_project;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by johno on 4/5/2018.
 */

public class QuickRecipe {

    private String drinkName;
    private String drinkHistory;
    private Date dateCreated;
    private Bitmap imageData;

    public QuickRecipe(String drinkName, String drinkHistory, Date dateCreated, Bitmap imageData)
    {
        this.drinkName = drinkName;
        this.drinkHistory = drinkHistory;
        this.imageData = imageData;
        this.dateCreated = dateCreated;
    }

    public String getDrinkName()
    {
        return drinkName;
    }

    public void setDrinkName(String drinkName)
    {
        this.drinkName = drinkName;
    }

    public String getDrinkInstructions()
    {
        return drinkHistory;
    }

    public void setDrinkInstructions(String drinkHistory)
    {
        this.drinkHistory = drinkHistory;
    }

    public Date getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public Bitmap getImageData()
    {
        return imageData;
    }

    public void setImageData(Bitmap imageData)
    {
        this.imageData = imageData;
    }
}
