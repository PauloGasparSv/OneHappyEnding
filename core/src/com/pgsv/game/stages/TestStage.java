package com.pgsv.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pgsv.game.actors.Player;
import com.pgsv.game.consts.C;

public class TestStage implements Screen
{
	private SpriteBatch batch;
	private Player player;
	private OrthographicCamera camera;
	private Map map;
	private Texture tiles;
	private Texture background;
	private float off;
	
	public TestStage(SpriteBatch batch) {
		super();
		this.batch = batch;
		
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false,256, 144);
		
		this.tiles = new Texture(Gdx.files.internal(C.path + "stages/1/tiles.png"));
		this.background = new Texture(Gdx.files.internal(C.path + "stages/1/back.png"));
		
		TextureRegion [] tilesRegion = new TextureRegion[3 * 6];
		int current = 0;
		for(int line = 0; line < 6; line ++)
		{
			for(int col = 0; col < 3; col ++)
			{
				tilesRegion[current] = new TextureRegion(this.tiles, col * 16, line * 16, 16, 16);
				current ++;
			}
		}
		
		off = 0;
		
		this.map = new Map(24, 12, tilesRegion);
		
		
		this.player = new Player(map);
		this.player.fallParachute();
	}
	
	public void update(float delta)
	{
		this.player.update(Gdx.graphics.getDeltaTime());
		
		
		if(this.player.position.x > this.camera.position.x )
		{
			this.camera.position.x = this.camera.viewportWidth /2f + this.player.position.x - 128f;	
		}
		
		if(this.player.position.x < this.camera.position.x && this.camera.position.x > this.camera.viewportWidth /2f)
		{
			this.camera.position.x = this.camera.viewportWidth /2f + this.player.position.x - 128f;

		}
		
		
		if(Gdx.input.isKeyPressed(Input.Keys.D))
			this.camera.position.x += delta * 120f;
		if(Gdx.input.isKeyPressed(Input.Keys.A))
			this.camera.position.x -= delta * 120f;
		if(Gdx.input.isKeyPressed(Input.Keys.W))
			this.camera.position.y += delta * 120f;
		if(Gdx.input.isKeyPressed(Input.Keys.S))
			this.camera.position.y -= delta * 120f;
		if(this.camera.position.x < this.camera.viewportWidth / 2f)
			this.camera.position.x = camera.viewportWidth / 2f;
		//if(camera.position.y < camera.viewportHeight / 2f)
		//	camera.position.y = camera.viewportHeight / 2f;
		
		
		if(this.player.position.y < -20)
		{
			this.player.position.y = 132f;
			this.player.position.x = 12f;
			this.player.fallParachute();
		}
		this.off = (this.camera.position.x - this.camera.viewportWidth /2f ) / 2f;
		
	}
	
	public void draw()
	{
		batch.draw(this.background, off ,0);
		batch.draw(this.background,this.background.getWidth() + off,0);
		batch.draw(this.background,this.background.getWidth() * 2 + off,0);
		this.map.draw(camera, batch);
		this.player.draw(this.batch);
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
	public void dispose() 
	{
		this.player.dispose();
		this.background.dispose();
		this.tiles.dispose();
		this.batch.dispose();
	}

	@Override
	public void resize(int width, int height) {}
	@Override
	public void show() {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public void hide() {}

}
