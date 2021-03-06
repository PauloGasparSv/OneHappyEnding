package com.pgsv.game.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pgsv.game.utils.C;
import com.pgsv.game.stages.Map;

public class Bally extends Actor
{
	private final int WALK = 1, DEAD = 3;
	
	private Player player;

	private Animation<TextureRegion> rollingAnimation;
	
	private TextureRegion currentFrame;
	
	private Rectangle rect;
	
	private Vector2 speed;
	
	private boolean ignoreMe;
	
	public Bally(float x, float y, boolean right,Map map, OrthographicCamera camera, Player player,Animation<TextureRegion> rollingAnimation) 
	{
		super(x, y, map, camera);

		this.rollingAnimation = rollingAnimation;
		
		this.player = player;
		this.rect = new Rectangle(0,0, 6,10);
		this.right = right;
		this.init(x, y);
	}

	public void init(float x ,float y)
	{
		this.grounded = true;
		this.ignoreMe = false;
		
		this.animationDelta = 0f;
		this.gravity = 0f;
		
		
		this.position = new Vector2(x,y);
		this.speed = new Vector2(20,60);
		
		changeState(WALK);
		
		fall();
	}
	
	public void update(float delta)
	{
		float distX = this.position.x - this.camera.position.x;
		ignoreMe = distX > 180 || distX < -180;
		if(ignoreMe || isDead() || C.DEBUG) return;
		
		animationDelta += delta;
		

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
			if(!this.map.isSolid(tileBottomLeft)  && !this.map.isSolid(tileBottomRight))
			{
				if(!this.special) fall();
				else this.right = !this.right;
			}
		}
		
		if(player.isDead()) return;
		if(player.getRect().overlaps(this.getRect()))
		{
			if(player.position.y > this.position.y + 2)
			{
				//die();
				player.jump(1.4f);
			}
			else
			{
				player.die();
			}
		}
		
	}
	
	public Rectangle getRect()
	{
		this.rect.x = this.position.x + 3;
		this.rect.y = this.position.y;

		return this.rect;
	}
	
	
	public void ground(float y)
	{
		this.grounded = true;
		this.position.y = y;
		this.gravity = 0f;
		changeState(WALK);
	}
	
	public void draw(SpriteBatch batch)
	{
		if(ignoreMe || isDead()) return;
		float offX = -2;
		
		this.currentFrame = this.rollingAnimation.getKeyFrame(this.animationDelta, true);
		
		
		if(this.currentFrame.isFlipX() != this.right)
		{
			this.currentFrame.flip(true, false);
		}
		
		
		batch.draw(this.currentFrame,this.position.x + offX,this.position.y);

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
		return currentState == DEAD;
	}
	
	public void fall()
	{
		this.grounded = false;
		changeState(WALK);
	}
	
	
	public int getId()
	{
		return 4;
	}
	
	
}
