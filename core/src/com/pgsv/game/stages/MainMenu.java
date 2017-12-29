package com.pgsv.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pgsv.game.MyGdxGame;
import com.pgsv.game.consts.C;

import javax.swing.JOptionPane;

public class MainMenu implements Screen{

	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private Texture background;
	private Texture menuBox;
	private Texture mouseCursor;
	private Texture cursor;
	private Texture title;
	private Texture black;
	
	private Vector2 mouse;
	
	private float backGroundDelta;
	private float menuOff;
	private float speed;

	private int menu;
	
	private int current;
	
	private float titleDelta;
	
	private Animation<TextureRegion> titleAnimation;
	
	private Rectangle [] rects;
	
	private MyGdxGame game;
	
	private boolean choosen;
	
	private float fadeDelta;
	
	public MainMenu(MyGdxGame game, SpriteBatch batch)
	{
		super();
		
		this.game = game;
		
		this.batch = batch;

		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false,256, 144);
		
		this.background = new Texture(Gdx.files.internal(C.path + "ui/mainMenuBackground2.png"));
		this.menuBox = new Texture(Gdx.files.internal(C.path + "ui/board.png"));
		this.mouseCursor = new Texture(Gdx.files.internal(C.path + "ui/mouseCursor.png"));
		this.cursor =  new Texture(Gdx.files.internal(C.path + "ui/cursor.png"));
		this.title =  new Texture(Gdx.files.internal(C.path + "ui/title.png"));
		this.black = new Texture(Gdx.files.internal(C.path + "ui/black.png"));
		
		this.backGroundDelta = 0f;
		this.speed = 0f;
		this.menu = 0;
		this.menuOff = 220f;
		this.mouse = new Vector2();
		this.current = 0;
		this.choosen = false;
		this.fadeDelta = 0;
		
		TextureRegion [] regions = new TextureRegion[11];
		for(int i = 0; i < regions.length; i++)
		{
			regions[i] = new TextureRegion(this.title, 0, 12 * i, 101, 12);
		}
		this.titleAnimation = new Animation<TextureRegion>(0.1f, regions);
		
		
		this.rects = new Rectangle[3];
		this.rects[0] = new Rectangle(95,88,72,8);
		this.rects[1] = new Rectangle(95,69,72,8);
		this.rects[2] = new Rectangle(95,50,72,8);
	}
	
	public void update(float delta)
	{
		this.mouse.x =(float) Gdx.input.getX() / Gdx.graphics.getWidth() * 256f;
		this.mouse.y = 144 - (float) Gdx.input.getY() / Gdx.graphics.getHeight() * 144f;
		
	
		if(!this.choosen)
		{
			if(this.backGroundDelta > -40f)
			{
				this.speed += delta * 30f;
				this.backGroundDelta -= delta * this.speed;
				if(this.backGroundDelta <= -40f)
				{
					this.backGroundDelta = -40f;
					this.menu = 1;
				}
			}
			
			if(this.menu == 1)
			{
				if(this.menuOff > 0)
				{
					this.menuOff -= delta * 60f;
					if(this.menuOff < 0)
					{
						this.menuOff = 0;
						this.menu = 2;
					}
				}
			}
			
			if(this.menu == 2)
			{
				if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) 
				{
					this.current --;
					if(this.current < 0) this.current = 0;
				}
				else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) 
				{
					this.current ++;
					if(this.current> 2) this.current= 2;
				}
				
				if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.Z)  || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
				{
					if(this.current != 1)
						choosen = true;
				}
				for(int i = 0; i < 3; i++)
				{
					if(new Rectangle(mouse.x,mouse.y, 2, 2).overlaps(rects[i]))
					{
						this.current = i;
						if(Gdx.input.isTouched())
						{
							if(this.current != 1)
								choosen = true;
						}
					}
				}
				
			}
		}
		else
		{
			this.titleDelta += delta;
			if(this.titleAnimation.isAnimationFinished(this.titleDelta))
				this.fadeDelta += delta;
			
			if(this.fadeDelta > 1)
			{
				this.fadeDelta = 1;
				this.choose();
			}
		}
		
	}
	
	public void choose()
	{
		this.dispose();
		if(this.current == 0)
			this.game.setScreen(new TestStage(this.game, batch));
		if(this.current == 2)
		{
			Gdx.app.exit();
			System.exit(0);
		}
	}
	
	public void draw()
	{
		batch.draw(background, 0, this.backGroundDelta);
		
		if(this.menu > 0)
		{
			batch.draw(menuBox, 66 + menuOff, 40);
			batch.draw(this.titleAnimation.getKeyFrame(titleDelta, false), 73 + menuOff, 108);
		}
		
		if(this.menu == 2)
		{
			switch(this.current)
			{
				case 0:
					batch.draw(cursor, 74, 88);
					break;
				case 1:
					batch.draw(cursor, 74, 69);
					break;
				case 2:
					batch.draw(cursor, 74, 50);
					break;	
			}
			
		}
		
		
		
		
		this.batch.draw(mouseCursor, mouse.x - 1, mouse.y - 12);
		
		if(this.choosen)
		{
			batch.setColor(1,1,1,fadeDelta);
			batch.draw(black, camera.position.x - 128 , camera.position.y  - 72);
			batch.setColor(1,1,1,1);
		}
	}

	@Override
	public void dispose() 
	{
		this.background.dispose();
		this.menuBox.dispose();
		this.cursor.dispose();
		this.mouseCursor.dispose();
		this.title.dispose();
		this.black.dispose();
	}

	@Override
	public void render(float delta) 
	{
		update(delta);
		this.camera.update();
		
		this.batch.begin();
		batch.setProjectionMatrix(camera.combined);
		draw();
		this.batch.end();
	}

	
	@Override
	public void show() {}
	@Override
	public void resize(int width, int height) {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public void hide() {}

}
