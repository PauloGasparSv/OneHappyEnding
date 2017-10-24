package com.pgsv.game.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pgsv.game.consts.C;
import com.pgsv.game.stages.Map;

public class FirstBoss extends Actor
{
	private final int INTRO = -1,IDLE = 0, WALK = 1, JUMP = 2, DEAD = 3;
	
	private Player player;

	private Animation<TextureRegion> rollingAnimation;
	
	private Animation<TextureRegion> hitAnimation;
	private Animation<TextureRegion> normalAnimation;
	
	private TextureRegion currentFrame;
	
	private Rectangle rect;
	
	private Vector2 speed;
	
	private boolean ignoreMe;
	
	@SuppressWarnings("unchecked")
	public FirstBoss(float x, float y, boolean right,Map map, OrthographicCamera camera, Player player,Texture myTexture) 
	{
		super(x, y, map, camera);
		
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
		this.normalAnimation = new Animation<TextureRegion>(0.15f, regions);
		
		this.player = player;
		this.rect = new Rectangle(0,0, 9,10);
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
		if(ignoreMe || isDead() || C.debug) return;
		
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
		float offX = 0;
		
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
