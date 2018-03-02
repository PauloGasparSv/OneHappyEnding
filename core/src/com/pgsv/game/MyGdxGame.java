package com.pgsv.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pgsv.game.stages.MainMenu;
import com.pgsv.game.utils.C;

public class MyGdxGame extends Game {

    @Override
    public void create()
    {
        Controller myController = null;

        for (Controller controller : Controllers.getControllers())
        {
            myController = controller;
            break;
        }

        C.IN = myController;
        C.batch = new SpriteBatch();
        C.game = this;

        setScreen(new MainMenu());
    }

    @Override
    public void render()
    {
        super.render();
        //if (Input.isKeyJustPressed(Input.ESCAPE)) Gdx.app.exit();
    }

    @Override
    public void dispose()
    {
        C.batch.dispose();
    }
}
