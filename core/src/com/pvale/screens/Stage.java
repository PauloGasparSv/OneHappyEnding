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