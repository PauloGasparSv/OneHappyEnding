package com.pvale.ohe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.pvale.screens.SimpleStage;
import com.pvale.screens.Stage;
import com.pvale.utils.In;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Gdx;

public class MyGame extends Game 
{
    public static Preferences prefs;

    @Override
    public void create()
    {
        prefs = Gdx.app.getPreferences("onehappyending");

        //Static setups
        In.loadController();
        Stage.batch = new SpriteBatch();
        Stage.camera = new OrthographicCamera(240, 135);
        
        setScreen(new SimpleStage());
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
