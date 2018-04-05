package com.example.android_project;

/**
 * Created by johno on 4/5/2018.
 */

public class DrinkHistory {

    private String drinkName;
    private String drinkHistory;

    public DrinkHistory(String drinkName, String drinkHistory)
    {
        this.drinkName = drinkName;
        this.drinkHistory = drinkHistory;
    }

    public String getDrinkName()
    {
        return drinkName;
    }

    public void setDrinkName(String drinkName)
    {
        this.drinkName = drinkName;
    }

    public String getDrinkHistory()
    {
        return drinkHistory;
    }

    public void setDrinkHistory(String drinkHistory)
    {
        this.drinkHistory = drinkHistory;
    }
}
