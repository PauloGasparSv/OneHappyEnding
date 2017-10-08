package com.pgsv.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pgsv.game.actors.Player;
import com.pgsv.game.consts.C;

public class TestStage implements Screen
{
	private Animation<TextureRegion> waterAnimation;
	private Map map;
	private OrthographicCamera camera;
	private Player player;
	private SpriteBatch batch;	
	
	private Texture [] water;
	private Texture background;
	private Texture tiles;
	
	private TextureRegion cloud;
	
	private boolean debug;
	
	private float off;
	private float offy;
	private float waterDelta;
	
	private float [] cloudX;
	private float [] cloudY;
		
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
		
		this.water = new Texture[2];
		this.water[0] = new Texture(Gdx.files.internal(C.path + "stages/1/back_river_1.png"));
		this.water[1] = new Texture(Gdx.files.internal(C.path + "stages/1/back_river_2.png"));
		
		this.cloud = new TextureRegion(tiles, 32, 0, 16, 16);
		this.cloudX = new float[3];
		this.cloudY = new float[3];
		for(int i = 0; i < 3; i ++) cloudX[i] = -200;
		
		TextureRegion [] waterRegion = new TextureRegion[2];
		waterRegion[0] = new TextureRegion(this.water[0]);
		waterRegion[1] = new TextureRegion(this.water[1]);
		
		this.waterAnimation = new Animation<TextureRegion>(1,waterRegion);
		
		this.off = 0;
		this.offy = 0;
		
		this.waterDelta = 0f;
		
		this.map = new Map("testMap", tilesRegion);
		int [] solids = {10, 11, 12, 13, 14, 15, 16, 18};
		this.map.setSolids(solids);
		
		this.player = new Player(24f, 148f, map);
		this.player.fall();
		
		
		this.debug = false;
	}
	
	public void update(float delta)
	{
		if(Gdx.input.isKeyJustPressed(Input.Keys.F2)) this.debug = !this.debug;
		if(Gdx.input.isKeyJustPressed(Input.Keys.F4)) this.player.die();
		
		if(debug) this.map.editMode(camera);
		this.player.update(Gdx.graphics.getDeltaTime());
				
		if(!this.debug && !this.player.isDead())
		{
			if(this.player.position.x > this.camera.position.x + 4f)
			{
				this.camera.position.x = this.player.position.x - 4f;	
			}
			else if(this.player.position.x < this.camera.position.x - 24f)
			{
				this.camera.position.x = this.player.position.x + 24f;
			}
			if(this.player.position.y > this.camera.position.y + 14)
			{
				this.camera.position.y = this.player.position.y - 14;
			}
			else if(this.player.position.y < this.camera.position.y - 36)
			{
				this.camera.position.y = this.player.position.y + 36;
			}
		}
		else
		{
			if(Gdx.input.isKeyPressed(Input.Keys.D))
				this.camera.position.x += delta * 120f;
			if(Gdx.input.isKeyPressed(Input.Keys.A))
				this.camera.position.x -= delta * 120f;
			if(Gdx.input.isKeyPressed(Input.Keys.W))
				this.camera.position.y += delta * 120f;
			if(Gdx.input.isKeyPressed(Input.Keys.S))
				this.camera.position.y -= delta * 120f;
		}
		
		if(this.camera.position.x < this.camera.viewportWidth / 2f)
			this.camera.position.x = camera.viewportWidth / 2f;
		if(camera.position.y < camera.viewportHeight / 2f)
			camera.position.y = camera.viewportHeight / 2f;
				
		float camX = this.camera.position.x - this.camera.viewportWidth /2f;
		float camY = this.camera.position.y - this.camera.viewportHeight /2f;
		
		if(this.player.position.x < -10)
			this.player.position.x = -10;
		
		if(this.player.position.y < camY - 120f)
		{
			player.respawn(12f, 128f);
			this.player.fallParachute();
		}
		
		this.off = camX / 1.2f;
		this.offy = camY / 1.2f + 16;
	
		if(offy > 94) offy = camY ;
		
		waterDelta += delta;
		
	}
	
	public void draw()
	{
		Gdx.gl.glClearColor(0.18f, 0.29f, 0.26f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.draw(this.background, off ,offy);
		batch.draw(this.waterAnimation.getKeyFrame(waterDelta, true), off, offy + 17);
		batch.draw(this.background,this.background.getWidth() + off, offy);
		batch.draw(this.waterAnimation.getKeyFrame(waterDelta, true), this.background.getWidth() + off, offy + 17);
		batch.draw(this.background,this.background.getWidth() * 2 + off, offy);
		batch.draw(this.waterAnimation.getKeyFrame(waterDelta, true), this.background.getWidth() * 2 + off, offy + 17);
		batch.draw(this.background,this.background.getWidth() * 3 + off, offy);
		batch.draw(this.waterAnimation.getKeyFrame(waterDelta, true), this.background.getWidth() * 3 + off, offy + 17);
		
		this.map.draw(camera, batch);
		
		for(int i = 1; i < 12; i ++)
		{
			batch.draw(this.cloud, off + i * (i % 2 == 0? 200 : 70), 96 + offy + (i % 2 == 0? 7 : 2));
		}
		
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
		this.water[0].dispose();
		this.water[1].dispose();
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
