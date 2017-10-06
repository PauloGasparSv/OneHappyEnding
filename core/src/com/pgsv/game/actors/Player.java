package com.pgsv.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pgsv.game.consts.C;
import com.pgsv.game.stages.Map;

public class Player {
	
	private final int IDLE = 0, WALK = 1, JUMP = 3;

	private Map map;
	public Vector2 position;
	private Vector2 speed;
	private Texture spriteSheet;
	private TextureRegion[] jump;
	private TextureRegion currentFrame;
	private TextureRegion fall;
	private TextureRegion parachute;
	private Animation<TextureRegion> idleAnimation;
	private Animation<TextureRegion> walkAnimation;
	private Animation<TextureRegion> [] animations;
	
	private boolean right;
	private boolean grounded;
	private boolean isParachute;
	
	private int jumpCount;
	private int currentState;
	
	private float animationDelta;
	private float angle;
	private float gravity;
	
	
	@SuppressWarnings("unchecked")
	public Player(Map map)
	{
		this.map = map;
		
		this.spriteSheet = new Texture(Gdx.files.internal(C.path + "Actors/guy_sheet.png"));
		
		if(this.spriteSheet == null)
			System.out.println("WHAAT");
		
		//IDLE ANIMATION
		TextureRegion [] currentSheet = new TextureRegion[4];
		for(int i = 0; i < currentSheet.length; i ++)
		{
			currentSheet[i] = new TextureRegion(this.spriteSheet,16 * i,0,16,16);
		}
		this.idleAnimation = new Animation<TextureRegion>(0.26f ,currentSheet);
		
		//WALK ANIMATION
		currentSheet = new TextureRegion[3];
		for(int i = 0; i < currentSheet.length; i ++)
		{
			currentSheet[i] = new TextureRegion(this.spriteSheet,16 * i,16,16,16);
		}
		this.walkAnimation = new Animation<TextureRegion>(0.15f ,currentSheet);
		
		//SETTING ANIMATIONS
		this.animations = new Animation[2];
		this.animations[IDLE] = idleAnimation;
		this.animations[WALK] = walkAnimation;
		
		this.jump = new TextureRegion[2];
		this.jump[0] = new TextureRegion(this.spriteSheet, 48, 16, 16, 16);
		this.jump[1] = new TextureRegion(this.spriteSheet, 64, 48, 16, 16);
		
		this.fall = new TextureRegion(this.spriteSheet, 64, 16, 16, 16);
		
		this.parachute = new TextureRegion(this.spriteSheet, 48, 48, 16, 16);
		
		this.currentFrame = new TextureRegion(this.spriteSheet,0,0,16,16);
		
		this.init();
	}
	
	public void init()
	{
		this.right = true;
		this.grounded = true;
		this.isParachute = true;
		
		this.jumpCount = 0;
		this.currentState = IDLE;
		
		this.animationDelta = 0f;
		this.angle = 0f;
		this.gravity = 0f;
		
		this.position = new Vector2(0f, 54f);
		this.speed = new Vector2(52f, 60f);
	}
	
	public void update(float delta)
	{	
		controls(delta);
		
		this.animationDelta += delta;
		
		//System.out.println("Tile: " + map.getTile(position.x, position.y));
		Vector3 tileBottomLeft = map.getTileVector(position.x + 3, position.y - 1);
		Vector3  tileBottomRight = map.getTileVector(position.x + 12, position.y - 1);
		
		if(!this.grounded)
		{	
			float gravityDelta = 1;
			if(isParachute) gravityDelta = 0.18f;
			this.gravity -= delta * 500f * gravityDelta;
			this.position.y += gravity * delta * gravityDelta;
			if(tileBottomLeft.z > 9 || tileBottomRight.z > 9)
			{
				ground((tileBottomLeft.y + 1) * 16 - 1);
			}
		}
		else
		{
			if(tileBottomLeft.z < 10 && tileBottomRight.z < 10)
			{
				fall();
			}
		}
		
	}
	
	private void controls(float delta)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			right = true;
			if(currentState == IDLE)changeState(WALK);
			this.position.x += delta * this.speed.x;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			right = false;
			if(currentState == IDLE)changeState(WALK);
			this.position.x -= delta * this.speed.x;	
		}
		else
		{
			if(currentState == WALK)changeState(IDLE);
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.Z))
		{
			if((this.currentState != JUMP && this.grounded) || (this.currentState == JUMP && this.jumpCount == 1))
			{
				this.currentState = JUMP;
				this.grounded = false;
				this.position.y += 4f;
				this.gravity = this.jumpCount == 1 ? 140f : 164f;
				this.jumpCount ++;
			}
		}
		
	}
	
	private void changeState(int state)
	{
		animationDelta = 0f;
		currentState = state;
	}
	
	public void ground(float y)
	{
		this.grounded = true;
		this.isParachute = false;
		this.position.y = y;
		this.gravity = 0f;
		this.jumpCount = 0;
		changeState(IDLE);
	}
	
	public void fall()
	{
		this.grounded = false;
		this.isParachute = false;
		this.jumpCount = 1;
		this.gravity = 0f;
		changeState(JUMP);
	}
	
	public void fallParachute()
	{
		this.isParachute = true;
		this.grounded = false;
		this.jumpCount = 0;
		this.gravity = 0f;
		changeState(JUMP);
	}
	
	public void draw(SpriteBatch batch)
	{
		if(this.isParachute)
		{
			batch.draw(this.parachute,this.position.x,this.position.y + 7);
		}
		
		if(this.currentState != JUMP)
		{
			this.currentFrame = this.animations[this.currentState].getKeyFrame(this.animationDelta, true);
		}
		else
		{
			if(this.gravity > - 40f * jumpCount)
				this.currentFrame = this.jump[this.jumpCount < 2 ? 0 : 1];
			else
				this.currentFrame = this.fall;
		}
		
		if(this.currentFrame.isFlipX() == this.right)
		{
			this.currentFrame.flip(true, false);
		}
		
		batch.draw(this.currentFrame,this.position.x,this.position.y);
	}
	
	public void dispose()
	{
		spriteSheet.dispose();
	}

}
