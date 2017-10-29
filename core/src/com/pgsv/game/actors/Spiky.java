package com.pgsv.game.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pgsv.game.consts.C;
import com.pgsv.game.stages.Map;

public class Spiky extends Actor{

	private final int IDLE = 0,WALK = 1,DEAD = 3, SMOOCHED = 2;

	private Player player;
	
	private Rectangle rect;
	private Vector2 speed;
	
	private TextureRegion currentFrame;
	
	private Animation<TextureRegion> exclamationAnimation;
	private Animation<TextureRegion> idleAnimation;
	private Animation<TextureRegion> walkAnimation;
	private Animation<TextureRegion> deathAnimation;
	private Animation<TextureRegion> [] animations;
	
	private boolean ignoreMe;

	private long lastDead;
		
	private float exclamationDelta;
	private int seenPlayer;
	
	@SuppressWarnings("unchecked")
	public Spiky(float x, float y, boolean right, boolean special, Animation<TextureRegion>[] spikyAnimations, Map map, Player player, OrthographicCamera camera)
	{
		super(x,y,map, camera);
		
		this.player = player;
		this.special = special;
		this.right = right;
		
		this.idleAnimation = spikyAnimations[0];
		this.walkAnimation = spikyAnimations[1];
		this.deathAnimation = spikyAnimations[2];
		this.exclamationAnimation = spikyAnimations[3];
		
		//SETTING ANIMATIONS
		this.animations = new Animation[3];
		this.animations[IDLE] = this.idleAnimation;
		this.animations[WALK] = this.walkAnimation;
		this.animations[SMOOCHED] = this.deathAnimation;
	
		this.currentFrame = walkAnimation.getKeyFrame(0);
		
		this.rect = new Rectangle();
		this.rect.width = 4;
		this.rect.height = 10;
		
		this.init(x, y);
	}
	
	public void init(float x, float y)
	{
		this.grounded = true;
		this.ignoreMe = false;
		
		this.seenPlayer = -1;
		this.lastDead = 0;
		
		this.animationDelta = 0f;
		this.exclamationDelta = 0f;
		this.gravity = 0f;
		
		this.position = new Vector2(x, y);
		this.speed = new Vector2(20f, 60f);
		
		changeState(WALK);
		fall();
	}
	
	public void update(float delta)
	{	
		if(seenPlayer == 0)
		{
			this.currentState = IDLE;
			this.exclamationDelta += delta;
			if(this.exclamationAnimation.isAnimationFinished(this.exclamationDelta))
			{
				this.seenPlayer = 1;
				this.changeState(WALK);
			}
		}
		
		float distX = this.position.x - this.camera.position.x;
		ignoreMe = distX > 180 || distX < -180;
		if(ignoreMe || isDead() || C.debug) return;

		
		if(seenPlayer == -1)
		{
			this.currentState = IDLE;
			float off = player.position.x - this.position.x;
			if(this.right && off > 10 && off < 80)
			{
				seenPlayer = 0;
			}
			if(!this.right && off > -76 && off < -10)
			{
				seenPlayer = 0;
			}
		}
		
		this.animationDelta += delta;
		
		if(this.currentState == SMOOCHED)
		{
			if(System.currentTimeMillis() - lastDead > 2000)
				die();
			return;
		}
		
		if(seenPlayer == 1)
		{
			if(this.right)
			{
				this.position.x += this.speed.x * delta;
			}
			else
			{
				this.position.x -= this.speed.x * delta;
			}
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
		
		if(player.isDead() || this.currentState == SMOOCHED) return;
		if(player.getRect().overlaps(this.getRect()))
		{
			if(player.position.y > this.position.y + 2)
			{
				changeState(SMOOCHED);
				this.seenPlayer = 1;
				this.lastDead = System.currentTimeMillis();
				player.jump(true);
			}
			else
			{
				player.die();
			}
		}
		
	}
	
	public Rectangle getRect()
	{
		this.rect.x = this.position.x + 6;
		this.rect.y = this.position.y;

		return this.rect;
	}
	
	public void draw(SpriteBatch batch)
	{
		if(ignoreMe || isDead() ) return;
		float offX = 0;
		
		if(currentState != SMOOCHED)
			this.currentFrame = this.animations[this.currentState].getKeyFrame(this.animationDelta, true);
		else
			this.currentFrame = this.animations[SMOOCHED].getKeyFrame(this.animationDelta, false);
		
		if(this.currentFrame.isFlipX() != this.right)
		{
			this.currentFrame.flip(true, false);
		}
		
		if(this.special) batch.setColor(1f, 0.3f, 1f, 1f);
		
		batch.draw(this.currentFrame,this.position.x + offX,this.position.y);
		
		batch.setColor(1f, 1f, 1f, 1f);
		
		if(this.seenPlayer == 0)
		{
			batch.draw(this.exclamationAnimation.getKeyFrame(this.exclamationDelta,false), this.position.x, this.position.y + 14);
		}
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
	
	public int getId()
	{
		return 2;
	}
	
	public void fall()
	{
		this.grounded = false;
		changeState(WALK);
	}
	
	
	
}
