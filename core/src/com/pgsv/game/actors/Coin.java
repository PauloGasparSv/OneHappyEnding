package com.pgsv.game.actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.pgsv.game.consts.C;

public class Coin 
{
	private Player player;

	private Animation<TextureRegion> animation;
	
	private Rectangle rect;
	
	private Sound sound;
	
	private OrthographicCamera camera;
	
	private boolean ignoreMe;
	private boolean active;
	
	private float animationDelta;
	
	public Coin(Animation<TextureRegion> animation, Player player, Sound sound, OrthographicCamera camera, float x, float y)
	{
		this.player = player;
		this.sound = sound;
		this.camera = camera;
		
		this.ignoreMe = true;
		this.active = true; 
		
		this.rect = new Rectangle(x,y, 2,8);
		this.animation = animation;
		this.animationDelta = (float) (Math.random() * 10f);
	}
	
	public void update(float delta)
	{
		float offX = this.camera.position.x - this.rect.x;
		this.ignoreMe = offX > 200 || offX < -200;
		if(this.ignoreMe || isDead()) return; 
		
		if(this.rect.overlaps(player.getRect()))
		{
			this.sound.play(0.08f);
			player.addCoin(1);
			this.active = false;
		}
		
		this.animationDelta += delta;
	}
	
	public void draw(SpriteBatch batch)
	{
		if(!C.debug && (this.ignoreMe || isDead())) return;
		batch.draw(this.animation.getKeyFrame(animationDelta,true), this.rect.x - 4, this.rect.y,10,10);
	}
	
	public Rectangle getRect()
	{
		return this.rect;
	}
	
	public boolean isDead()
	{
		return !this.active;
	}
	
}
