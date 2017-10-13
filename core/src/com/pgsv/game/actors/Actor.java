package com.pgsv.game.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pgsv.game.stages.Map;

public class Actor 
{
	protected Map map;
	protected OrthographicCamera camera;
	
	public Vector2 position;
	
	protected int currentState;
	
	protected float gravity;
	protected float animationDelta;
	
	protected boolean right;
	protected boolean grounded;
	protected boolean special;
	
	public Actor(float x, float y,Map map, OrthographicCamera camera)
	{
		this.map = map;
		this.camera = camera;
		this.position = new Vector2();
		this.position.x = x;
		this.position.y = y;
	}
	
	public void update(float delta)
	{
		
	}
	
	public void draw(SpriteBatch batch)
	{
		
	}
	
	public Rectangle getRect()
	{
		return new Rectangle(0,0,0,0);
	}
	
	public boolean isDead()
	{
		return false;
	}
	
	public void die(){}
	
	public void changeState(int state)
	{
		this.animationDelta = 0f;
		this.currentState = state;
	}
	
	public void ground(){}
	public void fall(){}
	
	public int getId()
	{
		return 0;
	}
	
}
