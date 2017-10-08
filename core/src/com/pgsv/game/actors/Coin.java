package com.pgsv.game.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Coin {

	public static int IDLE = 0, DEAD = 1;
	
	private Vector2 position;
	private TextureRegion coin;
	private Player player;
	private boolean ignoreMe;
	private int currentState;
	
	public Coin(TextureRegion coin, Player player, float x, float y)
	{
		this.coin = coin;
		this.position = new Vector2(x,y);
		this.player = player;
		this.ignoreMe = true;
		this.currentState = IDLE;
	}
	
	public void update(float delta)
	{
		float offX = this.player.position.x - this.position.x;
		this.ignoreMe = offX > 300 || offX < -300;
		if(this.ignoreMe || isDead()) return; 
		
		
		
	}
	
	public void draw(SpriteBatch batch)
	{
		if(this.ignoreMe || isDead()) return;
		batch.draw(this.coin, this.position.x, this.position.y);
	}
	
	public boolean isDead()
	{
		return this.currentState == DEAD;
	}
	
}
