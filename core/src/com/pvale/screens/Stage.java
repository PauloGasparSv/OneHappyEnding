package com.pvale.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pvale.utils.Camera;
import com.pvale.utils.In;
import com.pvale.utils.Media;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;

public class Stage implements Screen
{

    public static enum FadeState
    {
        NONE,
        BLACK,
        FADEIN,
        FADEOUT
    }

    public static OrthographicCamera camera;   
    public static SpriteBatch batch;
    
    private static Texture fade;
    private static Texture black;

    private static boolean shaking = false;

    private static int shakeState = 0;
    
    public static FadeState fadeState = FadeState.FADEIN;

    private static float cameraAngle = 0f;
    private static float shakeAngle = 0f;
    private static float fadeDelta = 1f;

    public static void init()
    {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(240, 135);
        camera.position.x = 120f;
        camera.position.y = 67.5f;

        fade = Media.loadTexture("misc/fade.png");
        black = Media.loadTexture("misc/blackBackground.png");
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

        if(fadeState == FadeState.BLACK)
        {
            batch.draw(black, Camera.getX() , Camera.getY(), 240, 135);
        }
        else if(fadeState != FadeState.NONE)
        {
            if(fadeState == FadeState.FADEIN)
                fadeDelta += delta * 22f;
            else 
                fadeDelta -= delta * 52f;

            if(fadeDelta > 50f) fadeState = FadeState.NONE;
            if(fadeDelta < 1f) 
            {
                fadeDelta = 1f;
                fadeState = FadeState.BLACK;
            }   
            
            batch.draw(fade, Camera.getX() , Camera.getY() ,
                128f, 61f, 240f, 135f, fadeDelta, fadeDelta, 0f, 0, 0, 240, 135, false, false);
        }

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

    public static void setFade(FadeState state)
    {
        if(state == fadeState) return;
        fadeState = state;
        if(fadeState == FadeState.FADEIN)
        {
            fadeDelta = 1f;
        }
        else if(fadeState == FadeState.FADEOUT)
        {
            fadeDelta = 50f;
        }
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