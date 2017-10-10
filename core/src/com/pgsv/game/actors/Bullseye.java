package com.pgsv.game.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pgsv.game.stages.Map;

public class Bullseye {

	private final int WALK = 0,DEAD = 3;

	private Player player;
	private Map map;
	public Vector2 position;
	private Vector2 speed;
	private TextureRegion currentFrame;
	private Animation<TextureRegion> walkAnimation;
	private Animation<TextureRegion> [] animations;
	private Rectangle rect;
	
	private boolean right;
	private boolean grounded;
	private boolean smart;
	private boolean ignoreMe;

	private int currentState;
	
	private float animationDelta;
	private float gravity;
	
	@SuppressWarnings("unchecked")
	public Bullseye(float x, float y, boolean right, boolean smart, Texture spriteSheet, Map map, Player player)
	{
		this.map = map;
		this.player = player;
		this.smart = smart;
		this.right = right;
		
		
		//WALK ANIMATION
		TextureRegion [] currentSheet = new TextureRegion[6];
		for(int i = 0; i < currentSheet.length; i ++)
		{
			currentSheet[i] = new TextureRegion(spriteSheet,16 * i,0,16,13);
		}
		this.walkAnimation = new Animation<TextureRegion>(0.15f ,currentSheet);
		
		
		//SETTING ANIMATIONS
		this.animations = new Animation[1];
		this.animations[WALK] = walkAnimation;
	
		this.currentFrame = walkAnimation.getKeyFrame(0);
		
		this.rect = new Rectangle();
		this.rect.width = 4;
		this.rect.height = 10;
		
		this.init(x, y);
	}
	
	public void init(float x, float y)
	{
		this.grounded = true;
		this.ignoreMe = true;
		
		this.animationDelta = 0f;
		this.gravity = 0f;
		
		this.position = new Vector2(x, y);
		this.speed = new Vector2(20f, 60f);
		
		changeState(WALK);
		fall();
	}
	
	public void update(float delta)
	{	
		float distX = this.position.x - this.player.position.x; 
		ignoreMe = distX > 280 || distX < -280;
		if(ignoreMe || isDead()) return;
			
		this.animationDelta += delta;
		
		if(this.right)
		{
			this.position.x += this.speed.x * delta;
		}
		else
		{
			this.position.x -= this.speed.x * delta;
		}
		
		Vector3 tileBottomLeft = this.map.getTileVector(this.position.x + 5, this.position.y - 1);
		Vector3  tileBottomRight = this.map.getTileVector(this.position.x + 10, this.position.y - 1);		
		
		Vector3 tileLeft = this.map.getTileVector(this.position.x + 3, this.position.y + 6);
		Vector3 tileRight = this.map.getTileVector(this.position.x + 13, this.position.y + 6);
		
		if(this.map.isSolid(tileLeft))
		{
			this.position.x = tileLeft.x + 13;
			this.right = !this.right;
		}
		else if(this.map.isSolid(tileRight))
		{
			this.position.x = tileRight.x - 13;
			this.right = !this.right;
		}
		
		if(!this.grounded)
		{	
			this.gravity -= delta * 500f;
			if(this.gravity < - 200f) this.gravity = -200f;
			this.position.y += this.gravity * delta;
			if(this.gravity < 0)				
			{
				if(this.map.isSolid(tileBottomLeft) && this.position.y > tileBottomLeft.y - 8 )
				{
					ground(tileBottomLeft.y- 1);
				}
				else if(this.map.isSolid(tileBottomRight) && this.position.y > tileBottomRight.y - 8)
				{
					ground(tileBottomRight.y - 1);	
				}
			}
		}
		else
		{
			if(tileBottomLeft.z < 10 && tileBottomRight.z < 10)
			{
				if(!this.smart) fall();
				else this.right = !this.right;
			}
		}
		
		if(player.isDead()) return;
		if(player.getRect().overlaps(this.myRect()))
		{
			if(player.position.y > this.position.y + 2)
			{
				die();
				player.jump(true);
			}
			else
			{
				player.die();
			}
		}
		
	}
	
	public Rectangle myRect()
	{
		this.rect.x = this.position.x + 6;
		this.rect.y = this.position.y;

		return this.rect;
	}
	
	public void draw(SpriteBatch batch)
	{
		if(ignoreMe || isDead()) return;
		float offX = 0;
		
		this.currentFrame = this.animations[this.currentState].getKeyFrame(this.animationDelta, true);
		
		
		if(this.currentFrame.isFlipX() != this.right)
		{
			this.currentFrame.flip(true, false);
		}
		
		batch.draw(this.currentFrame,this.position.x + offX,this.position.y);
	}
	
	private void changeState(int state)
	{
		this.animationDelta = 0f;
		this.currentState = state;
	}
	
	public void ground(float y)
	{
		this.grounded = true;
		this.position.y = y;
		this.gravity = 0f;
		changeState(WALK);
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
		return currentState == DEAD;
	}
	
	public void fall()
	{
		this.grounded = false;
		changeState(WALK);
	}
	
	
	
}
