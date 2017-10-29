package com.pgsv.game.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pgsv.game.consts.C;
import com.pgsv.game.stages.Map;

public class FirstBoss extends Actor
{
	private final int INTRO = - 1,IDLE = 0, HIT = 1, DEAD = 2;
	
	private Player player;

	private Animation<TextureRegion> hitAnimation;
	private Animation<TextureRegion> normalAnimation;
	private Animation<TextureRegion> deathAnimation;
	private Animation<TextureRegion> puff;
	
	private TextureRegion currentFrame;
	
	private Rectangle rect;
	
	private Vector2 speed;
	
	private float startX;
	private float startY;
	private float puffDelta;
	
	private boolean up;
	
	private int state;
	private int hits;
	
	private long timer;
	private long hitTimer;
	
	public FirstBoss(float x, float y,Map map, OrthographicCamera camera, Player player,Texture myTexture,Texture puffTexture) 
	{
		super(x, y, map, camera);
		this.startX = x;
		this.startY = y;
		
		TextureRegion [] regions = new TextureRegion[2];
		for(int i = 0; i < 2; i ++ )
		{
			regions[i] = new TextureRegion(myTexture, 19 * i, 0 , 19, 63);
		}
		this.normalAnimation = new Animation<TextureRegion>(0.15f, regions);
		
		regions = new TextureRegion[2];
		for(int i = 0; i < 2; i ++ )
		{
			regions[i] = new TextureRegion(myTexture, 19 * i, 63, 19, 63);
		}
		this.hitAnimation = new Animation<TextureRegion>(0.15f, regions);
		
		regions = new TextureRegion[2];
		for(int i = 0; i < 2; i ++ )
		{
			regions[i] = new TextureRegion(myTexture, 19 * i, 126, 19, 63);
		}
		this.deathAnimation = new Animation<TextureRegion>(0.15f, regions);
		
		regions = new TextureRegion[5];
		for(int i = 0; i < 5; i ++ )
		{
			regions[i] = new TextureRegion(puffTexture, 16 * i, 0, 16, 16);
		}
		this.puff = new Animation<TextureRegion>(0.12f, regions);
		
		this.player = player;
		this.rect = new Rectangle(0,0,6,60);
		
		this.init();
	}

	public void init()
	{
		this.state = 0;
		this.grounded = true;
		this.animationDelta = 0f;
		this.gravity = 0f;
		this.right = false;
		this.up = false;
		this.hits = 0;
		
			
		this.position = new Vector2(startX, startY - 59);
		this.speed = new Vector2(50,40);
		
		changeState(INTRO);
		
		fall();
	}
	
	public void update(float delta)
	{
		if(C.debug) return;

		animationDelta += delta;
		
		if(this.currentState == HIT && System.currentTimeMillis() - hitTimer > 1400)
		{
			changeState(IDLE);
		}
		
		if(this.currentState == DEAD)
		{
			this.puffDelta += delta;
			
			this.position.y -= delta * 10f;
			
			return;
		}
		
		if(this.state == 1)
		{
			
			if(System.currentTimeMillis() - timer > 1000)
			{
				this.position.x += this.speed.x * delta * (right ? 1: -1);
				if(this.up)
				{
					this.position.y += delta * this.speed.y;
					if(this.position.y > startY)
					{
						this.position.y = startY;
						this.up = false;
					}
				}
				else
				{	
					this.position.y -= delta * this.speed.y;
					if(this.position.y < startY - 48)
					{
						this.position.y = startY - 48;
						this.up = true;
					}
				}
				
				if(this.position.x < this.startX - 200)
				{
					this.right = true;
				}
				if(this.position.x > this.startX)
				{
					this.right = false;
				}
				
			}
			
			
		}
		
		if(this.state == 0)
		{
			if(this.position.y < this.startY)
			{
				this.position.y += delta * 20f;
				
				float perc = (this.startY - this.position.y) /60f;
				
				if(this.position.x > this.startX )
				{
					this.position.x -= delta * 70f * perc;
				}
				else 
				{
					this.position.x += delta * 70f * perc;
				}
				if(this.position.y >= this.startY)
				{
					this.position.y = this.startY;
					this.position.x = this.startX;
					this.timer = System.currentTimeMillis();
					this.state = 1;
				}
			}
			
		}
		
		
		if(player.isDead()) return;
		
		if(player.getRect().overlaps(this.getRect()) && !isDead())
		{
			if(player.position.y > this.position.y + 46)
			{
				player.jump(0.8f);
				if(this.currentState != HIT)
				{
					hits ++;
					changeState(HIT);
					this.speed.x += 28f;
					this.speed.y += 17f;
					hitTimer = System.currentTimeMillis();
					if(this.hits == 6)
					{
						die();
					}
				}
			}
			else
			{
				player.die();
			}
		}
		
	}
	
	public Rectangle getRect()
	{
		this.rect.x = this.position.x +5;
		this.rect.y = this.position.y;

		return this.rect;
	}
	
	
	public void draw(SpriteBatch batch)
	{
		
		if(this.currentState  == HIT)
			this.currentFrame = this.hitAnimation.getKeyFrame(this.animationDelta, true);
		else if(this.currentState  == DEAD)
			this.currentFrame = this.deathAnimation.getKeyFrame(this.animationDelta, true);
		else 
			this.currentFrame = this.normalAnimation.getKeyFrame(this.animationDelta, true);
		
		if(this.currentFrame.isFlipX() != this.right)
		{
			this.currentFrame.flip(true, false);
		}
		
		
		batch.setColor(1, (6 - hits) / 6f, (6 - hits) / 6f, 1);
		batch.draw(this.currentFrame,this.position.x,this.position.y);
		batch.setColor(1, 1,1, 1);
		
		if(this.currentState == DEAD)
		{
			batch.draw(this.puff.getKeyFrame(puffDelta,true),this.position.x - 12,this.position.y + 24);
			batch.draw(this.puff.getKeyFrame(puffDelta + 1,true),this.position.x,this.position.y + 48);
			batch.draw(this.puff.getKeyFrame(puffDelta + 2,true),this.position.x + 12 ,this.position.y + 32);
			batch.draw(this.puff.getKeyFrame(puffDelta + 3,true),this.position.x ,this.position.y + 32);
			batch.draw(this.puff.getKeyFrame(puffDelta + 1,true),this.position.x - 12,this.position.y + 8);
			batch.draw(this.puff.getKeyFrame(puffDelta,true),this.position.x + 12,this.position.y );
		}

	}
	
	public void dispose()
	{
		
	}
	
	
	public void die()
	{
		if(this.currentState != DEAD)
		{
			changeState(DEAD);
		}
	}
	
	public boolean isDead()
	{
		return currentState == DEAD && this.position.y < -4.5f;
	}
	
	
	public int getId()
	{
		return 6;
	}
	
	
}
