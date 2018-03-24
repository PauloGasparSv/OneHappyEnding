package com.pvale.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class In
{
    public static boolean right()
    {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    public static boolean justRight()
    {
        return Gdx.input.isKeyJustPressed(Input.Keys.RIGHT);
    }
    
    public static boolean left()
    {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }

    public static boolean justLeft()
    {
        return Gdx.input.isKeyJustPressed(Input.Keys.LEFT);
    }

    

}