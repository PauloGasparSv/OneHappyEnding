package com.pvale.ohe;

import com.badlogic.gdx.Game;
import com.pvale.screens.IntroStage;
import com.pvale.screens.SimpleStage;
import com.pvale.screens.Stage;
import com.pvale.utils.In;
import com.pvale.utils.Text;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Gdx;

public class MyGame extends Game 
{
    public static Preferences prefs;

    public static MyGame game;

    @Override
    public void create()
    {
        prefs = Gdx.app.getPreferences("onehappyending");

        game = this;

        In.loadController();
        In.loadPreferences();

        Text.init();
        Stage.init();

        setScreen(new SimpleStage());
    }

    @Override
    public void render()
    {
        super.render();
    }

    @Override
    public void dispose()
    {
        System.out.println("My game disposed");
    }

}
