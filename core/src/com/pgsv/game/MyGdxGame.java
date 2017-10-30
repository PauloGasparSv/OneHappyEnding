package com.pgsv.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pgsv.game.consts.C;
import com.pgsv.game.stages.MainMenu;

public class MyGdxGame extends Game {

	private SpriteBatch batch;
	
	@Override
	public void create () 
	{
		this.batch = new SpriteBatch();
		
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal(C.path + "ui/alpha.png")), 0, 0));
		
		Controller myController = null;
		for (Controller controller : Controllers.getControllers()) 
		{
			myController = controller;
			break;
		}
		C.in = myController;
		
		setScreen(new MainMenu(this,batch));
	}

	@Override
	public void render () 
	{
		super.render();
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))Gdx.app.exit();
	}
	
	@Override
	public void dispose () 
	{
		this.batch.dispose();
	}
}
