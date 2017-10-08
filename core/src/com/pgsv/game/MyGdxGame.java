package com.pgsv.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pgsv.game.actors.Player;
import com.pgsv.game.stages.TestStage;

public class MyGdxGame extends Game {

	private SpriteBatch batch;
	
	@Override
	public void create () 
	{
		this.batch = new SpriteBatch();
		setScreen(new TestStage(batch));
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
