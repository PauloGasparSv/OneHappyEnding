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
	private Animation<TextureRegion> sparkleAnimation;
	
	private Rectangle rect;
	
	private Sound sound;
	
	private OrthographicCamera camera;
	
	private boolean ignoreMe;
	private boolean active;
	private boolean sparkle;
	
	private float animationDelta;
	
	public Coin(Animation<TextureRegion> animation,Animation<TextureRegion> sparkle, Player player, Sound sound, OrthographicCamera camera, float x, float y)
	{
		this.player = player;
		this.sound = sound;
		this.camera = camera;
		this.sparkleAnimation = sparkle;
		
		this.ignoreMe = true;
		this.active = true; 
		this.sparkle = false;
		
		this.rect = new Rectangle(x,y, 2,8);
		this.animation = animation;
		this.animationDelta = (float) (Math.random() * 10f);
	}
	
	public void update(float delta)
	{
		float offX = this.camera.position.x - this.rect.x;
		this.ignoreMe = offX > 200 || offX < -200;
		if(this.ignoreMe || isDead()) return; 
		
		if(this.rect.overlaps(player.getRect()) && !this.sparkle)
		{
			this.sound.play(0.08f);
			player.addCoin(1);
			this.sparkle = true;
			this.animationDelta = 0f;
		}
		
		if(this.sparkle && this.sparkleAnimation.isAnimationFinished(this.animationDelta))
		{
			this.active = false;
		}
		this.animationDelta += delta;
	}
	
	public void draw(SpriteBatch batch)
	{
		if(!C.debug && (this.ignoreMe || isDead())) return;
		if(!this.sparkle)
			batch.draw(this.animation.getKeyFrame(animationDelta,true), this.rect.x - 4, this.rect.y,10,10);
		else
			batch.draw(this.sparkleAnimation.getKeyFrame(animationDelta,false), this.rect.x - 7, this.rect.y - 2,16,12);
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
