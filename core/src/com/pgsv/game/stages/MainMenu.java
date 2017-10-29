package com.pgsv.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pgsv.game.consts.C;

public class MainMenu implements Screen{

	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private Texture background;
	private Texture menuBox;
	
	private Vector2 mouse;
	private boolean mousePress;
	
	private float backGroundDelta;
	private float menuOff;
	private float speed;

	private int menu;
	
	public MainMenu(SpriteBatch batch)
	{
		super();
		
		this.batch = batch;

		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false,256, 144);
		
		this.background = new Texture(Gdx.files.internal(C.path + "ui/mainMenuBackground2.png"));
		this.menuBox = new Texture(Gdx.files.internal(C.path + "ui/board.png"));
		
		this.backGroundDelta = 0f;
		this.speed = 0f;
		this.menu = 0;
		this.menuOff = 220f;
		this.mouse = new Vector2();
		this.mousePress = false;
	}
	
	public void update(float delta)
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
			
		}
		
		
	}
	
	public void draw()
	{
		batch.draw(background, 0, this.backGroundDelta);
		
		if(this.menu > 0)
			batch.draw(menuBox, 66 + menuOff, 40);
	}

	@Override
	public void dispose() 
	{
		this.background.dispose();
		this.menuBox.dispose();
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
