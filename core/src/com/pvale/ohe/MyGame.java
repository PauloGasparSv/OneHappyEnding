package com.pvale.ohe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.pvale.screens.SimpleStage;
import com.pvale.screens.Stage;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;

public class MyGame extends Game 
{
    @Override
    public void create()
    {
        Stage.batch = new SpriteBatch();
        Stage.camera = new OrthographicCamera(240, 135);
        
        SimpleStage stage = new SimpleStage();
        
        setScreen(stage);
    }

    @Override
    public void render()
    {
        super.render();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
    }

    @Override
    public void dispose()
    {
        System.out.println("My game disposed");
    }

}
