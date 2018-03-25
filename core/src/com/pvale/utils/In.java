package com.pvale.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class In
{

    public static int right = Input.Keys.RIGHT;
    public static int left = Input.Keys.LEFT;
    public static int up = Input.Keys.UP;
    public static int down = Input.Keys.DOWN;

    public static int cameraRight = Input.Keys.D;
    public static int cameraLeft = Input.Keys.A;
    public static int cameraUp = Input.Keys.W;
    public static int cameraDown = Input.Keys.S;

    public static int jump = Input.Keys.Z;

    public static boolean right()
    {
        return Gdx.input.isKeyPressed(right);
    }

    public static boolean justRight()
    {
        return Gdx.input.isKeyJustPressed(right);
    }
    
    public static boolean left()
    {
        return Gdx.input.isKeyPressed(left);
    }

    public static boolean justLeft()
    {
        return Gdx.input.isKeyJustPressed(left);
    }

    public static boolean up()
    {
        return Gdx.input.isKeyPressed(up);
    }

    public static boolean justUp()
    {
        return Gdx.input.isKeyJustPressed(up);
    }

    public static boolean down()
    {
        return Gdx.input.isKeyPressed(down);
    }

    public static boolean jump()
    {
        return Gdx.input.isKeyPressed(jump);
    }

    public static boolean justJumped()
    {
        return Gdx.input.isKeyJustPressed(jump);
    }

    public static boolean justDown()
    {
        return Gdx.input.isKeyJustPressed(down);
    }

    public static boolean cameraRight()
    {
        return Gdx.input.isKeyPressed(cameraRight);
    }
    
    public static boolean cameraLeft()
    {
        return Gdx.input.isKeyPressed(cameraLeft);
    }

    public static boolean cameraUp()
    {
        return Gdx.input.isKeyPressed(cameraUp);
    }

    public static boolean cameraDown()
    {
        return Gdx.input.isKeyPressed(cameraDown);
    }


    public static float getX()
    {
        return Gdx.input.getX() * 0.25f;
    }
    
    public static float getY()
    {
        float y = 540 - Gdx.input.getY(); 
        return y * 0.25f;
    }

    public static boolean clicked()
    {
        return Gdx.input.justTouched();
    }

    public static boolean mousePress(int button)
    {
        switch(button)
        {
            case 0:
                return Gdx.input.isButtonPressed(Input.Buttons.LEFT);
            case 1:
                return Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
            default:
                return Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        }
    }

    public static boolean copy()
    {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.C);
    }

    public static boolean paste()
    {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.V);
    }

    public static boolean save()
    {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.S);
    }

}