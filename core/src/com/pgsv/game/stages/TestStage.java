package com.pgsv.game.stages;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pgsv.game.actors.Bullseye;
import com.pgsv.game.actors.CoinManager;
import com.pgsv.game.actors.Player;
import com.pgsv.game.consts.C;
import com.pgsv.game.utils.Text;

public class TestStage implements Screen
{
	private final int GAME = 0, TILEMAP = 1, ITENS = 3;
	
	private int currentState;
	
	private Map map;
	
	private CoinManager coins;
	private Player player;
	private Text text;
	
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private LinkedList<Bullseye> bulls;
	
	private Music theme;
	private Sound coinSound;
	
	private Texture [] water;
	private Texture titleCard;
	private Texture coin;
	private Texture background;
	private Texture tiles;
	private Texture bull;
	
	private Animation <TextureRegion> waterAnimation;
	private Animation <TextureRegion> coinAnimation;
	
	private TextureRegion cloud;
	
	private float intro;
	
	private float off;
	private float offy;
	private float waterDelta;
	
	private float [] cloudX;
	
	public TestStage(SpriteBatch batch) {
		super();
		this.batch = batch;
		
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false,256, 144);
		
		this.text = new Text();
		
		this.tiles = new Texture(Gdx.files.internal(C.path + "stages/1/tiles.png"));
		this.background = new Texture(Gdx.files.internal(C.path + "stages/1/back.png"));
		this.titleCard = new Texture(Gdx.files.internal(C.path + "stages/1/titleCard.png"));
		this.coin = new Texture(Gdx.files.internal(C.path + "stages/coin.png"));
		
		TextureRegion [] coinRegion = new TextureRegion[4];
		for(int i = 0; i < 4; i++)
		{
			coinRegion[i] = new TextureRegion(coin, i * 16, 0, 16, 16);
		}
		this.coinAnimation = new Animation<TextureRegion>(0.22f,coinRegion);
		
		
		TextureRegion [] tilesRegion = new TextureRegion[3 * 11];
		int current = 0;
		for(int line = 0; line < 11; line ++)
		{
			for(int col = 0; col < 3; col ++)
			{
				tilesRegion[current] = new TextureRegion(this.tiles, col * 16, line * 16, 16, 16);
				current ++;
			}
		}
		
		this.theme = Gdx.audio.newMusic(Gdx.files.internal(C.path + "Music/The Adventure Begins 8-bit remix.ogg"));
		this.theme.play();
		this.theme.setVolume(0.25f);
		this.theme.setLooping(true);
		
		this.coinSound = Gdx.audio.newSound(Gdx.files.internal(C.path + "SFX/Pickup_Coin.wav"));
		
		this.water = new Texture[2];
		this.water[0] = new Texture(Gdx.files.internal(C.path + "stages/1/back_river_1.png"));
		this.water[1] = new Texture(Gdx.files.internal(C.path + "stages/1/back_river_2.png"));
		
		this.cloud = new TextureRegion(tiles, 32, 0, 16, 16);
		this.cloudX = new float[3];
		for(int i = 0; i < 3; i ++) cloudX[i] = -200;
		
		TextureRegion [] waterRegion = new TextureRegion[2];
		waterRegion[0] = new TextureRegion(this.water[0]);
		waterRegion[1] = new TextureRegion(this.water[1]);
		
		this.waterAnimation = new Animation<TextureRegion>(1,waterRegion);
		
		this.off = 0;
		this.offy = 0;
		
		this.waterDelta = 0f;
		
		this.map = new Map("testMap", tilesRegion);
		int [] solids = {10, 11, 12, 13, 14, 15, 16, 18, 19, 20, 21};
		this.map.setSolids(solids);
		
		this.player = new Player(24f, 112f, map, camera);
		this.player.fall();
		
		this.bull = new Texture(Gdx.files.internal(C.path + "Actors/baddies/bullseye.png"));
		
		this.bulls = new LinkedList<Bullseye>();
		
		this.bulls.add(new Bullseye(230, 72, false, true, bull, map, player, camera));
		this.bulls.add(new Bullseye(426, 102, true, false, bull, map, player, camera));
		this.bulls.add(new Bullseye(640, 32, true, false, bull, map, player, camera));
		this.bulls.add(new Bullseye(1200, 54, false, true, bull, map, player, camera));
		
		
		this.coins = new CoinManager(player, coinAnimation, coinSound, camera);
		this.coins.loadCoins("testMapCoins.broc");
		
		
		
		this.intro = 0;
		C.debug = false;
	}
	
	public void update(float delta)
	{
		
		this.intro += delta * 55 + delta * this.intro / 3f;
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.F2))
		{
			C.debug = true;
			if(this.currentState == TILEMAP)
			{
				C.debug = false;
				this.currentState = GAME;
			}
			else this.currentState = TILEMAP;
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.F3))
		{
			C.debug = true;	
			if(this.currentState == ITENS)
			{
				C.debug = false;
				this.currentState = GAME;
			}
			else this.currentState = ITENS;
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.F4))
		{
			this.player.die();
		}
		
		
		if(C.debug)
		{
			if(Gdx.input.isKeyJustPressed(Input.Keys.F1))
			{
				this.map.saveMap();
				this.coins.saveCoins("testMapCoins.broc");
			}
			if(this.currentState == TILEMAP)
			{
				this.map.editMode(camera);
			}
			else if(this.currentState == ITENS)
			{
				this.coins.debugMode(delta);
			}
		}
		else
		{
			this.player.update(Gdx.graphics.getDeltaTime());
			
			for(Bullseye b : bulls)
			{
				b.update(delta);
			}
			
			this.coins.update(delta);
		}
				
		if(!C.debug && !this.player.isDead())
		{
			if(this.player.position.x > this.camera.position.x + 4f)
				this.camera.position.x = this.player.position.x - 4f;	
			else if(this.player.position.x < this.camera.position.x - 24f)
				this.camera.position.x = this.player.position.x + 24f;
			if(this.player.position.y > this.camera.position.y + 14)
				this.camera.position.y = this.player.position.y - 14;
			else if(this.player.position.y < this.camera.position.y - 36)
				this.camera.position.y = this.player.position.y + 36;
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
		
		if(this.player.position.y < camY - 80f)
		{
			if(!player.isDead())
			{
				player.position.y = camY - 24f;
				player.die();
			}
			else
			{
				player.respawn(1060f, 112f);
				this.player.fallParachute();
			}
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
		

		for(int i = 1; i < 12; i ++)
		{
			batch.draw(this.cloud, off + i * (i % 2 == 0? 200 : 70), 96 + offy + (i % 2 == 0? 7 : 2));
		}
		
		this.map.draw(camera, batch);
		
		if(C.debug)
		{
			
			if(this.currentState == TILEMAP)
			{
				this.map.drawEditMode(batch);
				this.text.draw(batch,  "tile editor", camera.position.x - 128, camera.position.y + 62);	
			}
			else if(this.currentState == ITENS)
			{
				this.coins.debugModeDraw(batch);
				this.text.draw(batch,  "Coins editor", camera.position.x - 128, camera.position.y + 62);
				this.text.draw(batch, this.coins.numCoins() + " coins", camera.position.x - 128, camera.position.y + 50);
			}
		}
		
		for(Bullseye b : bulls)
			b.draw(batch);
		this.coins.draw(batch);
		
		this.player.draw(this.batch);
		
		if(this.intro < 2000)
		{
			if(this.intro < 280)
			{
				batch.draw(this.titleCard, camera.position.x + 160 - this.intro, camera.position.y + 24);	
			}
			else if(this.intro < 600)
			{
				batch.draw(this.titleCard, camera.position.x + 160 - 280, camera.position.y + 24);
			}
			else
			{
				batch.draw(this.titleCard, camera.position.x + 160 + 320 - this.intro, camera.position.y + 24);
			}	
		}
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
		JOptionPane.showMessageDialog(null, "DISPOSE ME");
		this.text.dispose();
		this.player.dispose();
		this.background.dispose();
		this.tiles.dispose();
		this.water[0].dispose();
		this.water[1].dispose();
		this.bull.dispose();
		this.coin.dispose();
		this.titleCard.dispose();
		this.coinSound.dispose();
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
