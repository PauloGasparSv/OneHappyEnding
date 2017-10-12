package com.pgsv.game.actors;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pgsv.game.consts.C;
import com.pgsv.game.consts.IpegaPc;
import com.pgsv.game.stages.Map;

public class Player extends Actor{
	
	private final int IDLE = 0, WALK = 1, JUMP = 5, DEAD = 6;
	
	private Controller in;
	
	private Vector2 speed;
	private Rectangle hitBox;
	
	private Sound jumpSound;
	
	private Texture spriteSheet;
	private TextureRegion[] jump;
	private TextureRegion currentFrame;
	private TextureRegion fall;
	private TextureRegion parachute;
	private TextureRegion dead;
	
	private Animation<TextureRegion> idleAnimation;
	private Animation<TextureRegion> walkAnimation;
	private Animation<TextureRegion> walkBlockedAnimation;
	private Animation<TextureRegion> [] animations;
	
	private boolean isParachute;
	private boolean respawn;
	private boolean blocked;
	private boolean pressingJump; //CONTROLLER ONLY
	
	private int jumpCount;
	private int coins;
	
	private float angle;
	
	private float blockedTime;
	
	
	@SuppressWarnings("unchecked")
	public Player(float x, float y, Map map, OrthographicCamera camera)
	{
		super(x,y,map, camera);
		
		this.spriteSheet = new Texture(Gdx.files.internal(C.path + "Actors/hero/guy_sheet.png"));
		
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
		
		//WALK BLOCKED ANIMATION
		currentSheet = new TextureRegion[3];
		for(int i = 0; i < currentSheet.length; i ++)
		{
			currentSheet[i] = new TextureRegion(this.spriteSheet,16 * i,64,16,16);
		}
		this.walkBlockedAnimation = new Animation<TextureRegion>(0.15f ,currentSheet);
		
		//SETTING ANIMATIONS
		this.animations = new Animation[2];
		this.animations[IDLE] = idleAnimation;
		this.animations[WALK] = walkAnimation;
		
		this.jump = new TextureRegion[2];
		this.jump[0] = new TextureRegion(this.spriteSheet, 48, 16, 16, 16);
		this.jump[1] = new TextureRegion(this.spriteSheet, 64, 48, 16, 16);
		
		this.dead = new TextureRegion(this.spriteSheet, 64, 0, 16, 16);
		
		this.fall = new TextureRegion(this.spriteSheet, 64, 16, 16, 16);
		
		this.parachute = new TextureRegion(this.spriteSheet, 48, 48, 16, 16);
		
		this.currentFrame = new TextureRegion(this.spriteSheet,0,0,16,16);
		
		this.jumpSound = Gdx.audio.newSound(Gdx.files.internal(C.path+"SFX/Jump.wav"));
		
		this.in = C.in;
		
		this.hitBox = new Rectangle();
		
		this.init(x, y);
	}
	
	public void init(float x, float y)
	{
		this.right = true;
		this.grounded = true;
		this.isParachute = true;
		this.respawn = false;
		this.blocked = false;
		this.pressingJump = false;
		
		this.jumpCount = 0;
		this.blockedTime = 0;
		this.coins = 0;
		
		this.animationDelta = 0f;
		this.angle = 0f;
		this.gravity = 0f;
		
		this.position = new Vector2(x, y);
		this.speed = new Vector2(60f, 60f);
		
		changeState(IDLE);
	}
	
	public void update(float delta)
	{	
		if(currentState == DEAD) 
		{
			deathUpdate(delta);
			return;
		}
		controls(delta);
		
		this.animationDelta += delta;
	
		Vector3 tileLeft = this.map.getTileVector(this.position.x + 3, this.position.y + 6);
		Vector3 tileRight = this.map.getTileVector(this.position.x + 13, this.position.y + 6);
		
		if(this.map.isSolid(tileLeft))
		{
			this.position.x = tileLeft.x + 13;
			this.blockedTime += delta;
			if(!this.right && this.grounded && this.blockedTime > 0.9f)
			{
				this.blocked = true;
			}
		}
		else if(this.map.isSolid(tileRight))
		{
			this.position.x = tileRight.x - 13;
			this.blockedTime += delta;
			if(this.right && this.grounded && this.blockedTime > 0.9f)
			{
				this.blocked = true;
			}
		}
		else
		{
			this.blocked = false;
			this.blockedTime = 0;
		}
		

		Vector3 tileTopLeft = this.map.getTileVector(this.position.x + 5, this.position.y + 15);
		Vector3 tileTopRight = this.map.getTileVector(this.position.x + 10, this.position.y + 15);
		
		
		if(this.gravity > 60)
		{
			if(this.map.isSolid(tileTopLeft))
			{
				this.gravity = 0;
				this.position.y = tileTopLeft.y - 30;
				fall();
			}
			else if(this.map.isSolid(tileTopRight))
			{
				this.gravity = 0;
				this.position.y = tileTopRight.y - 30;
				fall();
			}
		}

		
		//System.out.println("Tile: " + map.getTile(position.x, position.y));
		Vector3 tileBottomLeft = this.map.getTileVector(this.position.x + 4, this.position.y - 1);
		Vector3  tileBottomRight = this.map.getTileVector(this.position.x + 11, this.position.y - 1);		
		
		if(!this.grounded)
		{	
			this.blocked = false;
			this.blockedTime = 0;
			float gravityDelta = 1;
			if(isParachute) gravityDelta = 0.22f;
			this.gravity -= delta * 500f * gravityDelta;
			if(this.gravity < - 200f) this.gravity = -200f;
			this.position.y += this.gravity * delta * gravityDelta;
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
				fall();
			}
		}
		
	}
	
	private void deathUpdate(float delta)
	{
		this.gravity -= delta * 500f;
		if(this.gravity < - 200f) this.gravity = -200f;
		this.position.y += this.gravity * delta;
	}
	
	private void controls(float delta)
	{
		if(this.in == null) keyboard(delta);
		else controller(delta);
		
		
	}
	
	private void keyboard(float delta)
	{
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			this.right = true;
			if(this.currentState == IDLE) this.changeState(WALK);
			this.position.x += delta * this.speed.x;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			this.right = false;
			if(this.currentState == this.IDLE) this.changeState(WALK);
			this.position.x -= delta * this.speed.x;	
		}
		else
		{
			if(this.currentState == WALK) this.changeState(IDLE);
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.Z))
		{
			if((this.currentState != JUMP && this.grounded) || (this.currentState == JUMP && this.jumpCount == 1))
			{
				jump(false);
			}
			else if(isParachute)
			{
				this.isParachute = false;
				this.gravity = 100f;
			}
		}
	}
	
	private void controller(float delta)
	{
		System.out.println(in.getAxis(IpegaPc.AXIS_RIGHT_X) );
		if(in.getAxis(IpegaPc.AXIS_RIGHT_X) > 0.2f)
		{
			this.right = true;
			if(this.currentState == IDLE) this.changeState(WALK);
			this.position.x += delta * this.speed.x;
		}
		else if(in.getAxis(IpegaPc.AXIS_RIGHT_X) < -0.2f)
		{
			this.right = false;
			if(this.currentState == this.IDLE) this.changeState(WALK);
			this.position.x -= delta * this.speed.x;	
		}
		else
		{
			if(this.currentState == WALK) this.changeState(IDLE);
		}
		
		if(in.getButton(IpegaPc.BUTTON_A))
		{
			if(!this.pressingJump)
			{
				this.pressingJump = true;
				if((this.currentState != JUMP && this.grounded) || (this.currentState == JUMP && this.jumpCount == 1))
				{
					jump(false);
				}
				else if(isParachute)
				{
					this.isParachute = false;
					this.gravity = 100f;
				}
			}
		}
		else
		{
			this.pressingJump = false;
		}
	}
	
	public void draw(SpriteBatch batch)
	{
		float offX = 0;
		
		if(this.isParachute)
		{
			batch.draw(this.parachute,this.position.x,this.position.y + 7);
		}

		if(this.currentState == JUMP)
		{
			if(this.gravity > - 40f * jumpCount)
				this.currentFrame = this.jump[this.jumpCount < 2 ? 0 : 1];
			else
				this.currentFrame = this.fall;
		}
		else if(this.currentState == DEAD)
		{
			this.currentFrame = dead;
		}
		else if(this.currentState == WALK && this.blocked)
		{
			this.currentFrame = walkBlockedAnimation.getKeyFrame(this.animationDelta, true);
			if(!right) offX = 2;
			else offX = -2;
		}
		else
		{
			this.currentFrame = this.animations[this.currentState].getKeyFrame(this.animationDelta, true);
		}
		
		if(this.currentFrame.isFlipX() == this.right)
		{
			this.currentFrame.flip(true, false);
		}
		
		batch.draw(this.currentFrame,this.position.x + offX,this.position.y,0,0,16,16,1,1,angle);
	}
	
	public Rectangle getRect()
	{
		this.hitBox.x = this.position.x;
		this.hitBox.y = this.position.y;
		this.hitBox.width = 16;
		this.hitBox.height = 13;
		return this.hitBox;
	}
	
	public void jump(boolean enemy)
	{
		changeState(JUMP);
		long id = this.jumpSound.play(0.1f);
		if(enemy)
		{
			this.jumpSound.setPitch(id, 1.4f);
			this.jumpSound.setVolume(id, 0.07f);
		}
		else if(jumpCount > 0)
		{
			this.jumpSound.setPitch(id, 1.7f);
			this.jumpSound.setVolume(id, 0.07f);
		}
		
		this.grounded = false;
		this.position.y += 4f;
		this.jumpCount ++;
		if(!enemy)
			this.gravity = this.jumpCount == 1 ? 140f : 164f;
		else 
		{
			this.gravity = 180f;
			this.jumpCount = 1;
		}
	}
	
	public boolean isDead()
	{
		return this.currentState == DEAD;
	}
	
	public void die()
	{
		if(this.currentState != DEAD)
		{
			changeState(DEAD);
			this.gravity = 200f;
		}
	}
	
	public int getCoins()
	{
		return this.coins;
	}
	
	public void addCoin(int coins)
	{
		this.coins += coins;
	}
	
	public boolean isRespawn()
	{
		return this.respawn;
	}
	
	public void respawn(float x, float y)
	{
		this.init(x, y);
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
		changeState(JUMP);
	}
	
	public void fallParachute()
	{
		this.isParachute = true;
		this.grounded = false;
		this.jumpCount = 0;
		changeState(JUMP);
	}
	
	
	
	public void dispose()
	{
		this.spriteSheet.dispose();
		this.jumpSound.dispose();
	}

}
