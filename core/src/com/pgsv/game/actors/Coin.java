package com.pgsv.game.actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Coin 
{
	private Player player;
	
	private Sound sound;
	
	private Animation<TextureRegion> animation;
	
	private Rectangle rect;
	
	private boolean ignoreMe;
	private boolean active;
	
	private float animationDelta;
	
	public Coin(TextureRegion [] coin, Player player, Sound sound, float x, float y)
	{
		this.player = player;
		this.sound = sound;
		
		this.ignoreMe = true;
		this.active = true; 
		
		this.rect = new Rectangle(x,y, 2,8);
		this.animation = new Animation<TextureRegion>(0.22f,coin);
		this.animationDelta = (float) (Math.random() * 10f);
	}
	
	public void update(float delta)
	{
		float offX = this.player.position.x - this.rect.x;
		this.ignoreMe = offX > 300 || offX < -300;
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
		if(this.ignoreMe || isDead()) return;
		batch.draw(this.animation.getKeyFrame(animationDelta,true), this.rect.x - 4, this.rect.y,10,10);
	}
	
	public boolean isDead()
	{
		return !this.active;
	}
	
}
