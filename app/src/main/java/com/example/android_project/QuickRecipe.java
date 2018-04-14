package com.example.android_project;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by johno on 4/5/2018.
 */

public class QuickRecipe implements Comparable<QuickRecipe> {

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

    @Override
    public int compareTo(@NonNull QuickRecipe quickRecipe) {
        return Comparators.DATE_CREATED.compare(this,quickRecipe);
    }

    public static class Comparators
    {
        public static Comparator<QuickRecipe> NAME =  new Comparator<QuickRecipe>() {
            @Override
            public int compare(QuickRecipe quickRecipe, QuickRecipe t1) {
                return quickRecipe.getDrinkName().compareTo(t1.getDrinkName());
            }
        };

        public static Comparator<QuickRecipe> DATE_CREATED = new Comparator<QuickRecipe>() {
            @Override
            public int compare(QuickRecipe quickRecipe, QuickRecipe t1) {
                return quickRecipe.getDateCreated().compareTo(t1.getDateCreated());
            }
        };
    }
}
