package com.pvale.utils;

import com.pvale.screens.Stage;

public class Camera
{
    public static float getX()
    {
        return Stage.camera.position.x - 120f;
    }
    public static float getY()
    {
        return Stage.camera.position.y - 67.5f;
    }
}