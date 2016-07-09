package com.example.android.inventoryapp;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Daemian on 8/7/2016.
 */

public class ProductValidator {

    public boolean checkBlank(EditText words)
    {
        if (words.getText().toString().trim().length() > 0)
            return false;
        else
            return true;

    }
    public boolean checkIsFloat(EditText words)
    {
        try{
            Float.parseFloat(words.getText().toString());
            return true;
        }catch(Exception ex)
        {
            return false;
        }
    }

    public boolean checkIsInteger(EditText words)
    {
        try{
            Integer.parseInt(words.getText().toString());
            return true;
        }catch(Exception ex)
        {
            return false;
        }
    }
}
