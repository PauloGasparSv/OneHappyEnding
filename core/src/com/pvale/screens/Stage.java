package com.pvale.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pvale.utils.In;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;

public class Stage implements Screen
{
    public static SpriteBatch batch;
    public static OrthographicCamera camera;

    private static boolean shaking = false;

    private static int shakeState = 0;

    private static float cameraAngle = 0f;
    private static float shakeAngle = 0f;

    public Stage()
    {
        camera.position.x = 120f;
        camera.position.y = 67.5f;
    }

    @Override
    public void render(float delta)
    {
        update(delta);
        In.updateController();
        
        if(shaking)
        {
            if(shakeState % 2 == 0)
            {
                shakeAngle += delta * 60f;
                rotate(shakeAngle);
                if(shakeAngle > 2.5f) shakeState++;
            }
            else
            {
                shakeAngle -= delta * 60f;
                rotate(shakeAngle);
                if(shakeAngle < -2.5f) shakeState++;
            }
            if(shakeState >= 5)
            {
                shaking = false;
                camera.zoom = 1;
                shakeState = 0;
                rotate(0);
            }
        }

        if(camera.position.x < 120f) camera.position.x = 120f;
        if(camera.position.y < 67.5f) camera.position.y = 67.5f;
        camera.update();

        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        draw();

        batch.end();
    }

    @Override
    public void dispose()
    {
        System.out.print("Stage dispose");
    }

    public void update(float delta)
    {
        
    }

    public void draw()
    {

    }

    public static void rotate(float angle)
    {
        camera.rotate(angle - cameraAngle);
        cameraAngle += angle - cameraAngle;
        if(cameraAngle > 360) cameraAngle -= 360;
    }

    public static void shake()
    {
        if(shaking) return;
        shaking = true;
        shakeState = 0;
        shakeAngle = 0f;
        camera.zoom = 0.97f;
    }

    public static float getAngle()
    {
        return cameraAngle;
    } 

    @Override
    public void pause(){}
    @Override
    public void resume(){}
    @Override
    public void show(){}
    @Override
    public void hide(){}
    @Override
    public void resize(int w, int h){}
}