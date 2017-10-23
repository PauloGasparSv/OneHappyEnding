package com.pgsv.game.stages;

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
import com.pgsv.game.actors.BaddieBuilder;
import com.pgsv.game.actors.CoinManager;
import com.pgsv.game.actors.Player;
import com.pgsv.game.consts.C;
import com.pgsv.game.utils.Text;

public class TestStage implements Screen
{
	private final int GAME = 0, TILEMAP = 1, ITENS = 3, BADDIES = 4;
	
	private int currentState;
	
	private Map map;
	
	private CoinManager coins;
	private BaddieBuilder baddies;
	private Player player;
	private Text text;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private Music theme;
	private Sound coinSound;
	
	private Texture [] water;
	private Texture hearts;
	private Texture titleCard;
	private Texture coin;
	private Texture background;
	private Texture tiles;
	
	private TextureRegion [] heartsRegion;
	
	private Animation <TextureRegion> waterAnimation;
	
	private TextureRegion cloud;
	
	private float intro;
	
	private float off;
	private float offy;
	private float waterDelta;
	
	private float [] cloudX;
	
	private int state;
	
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
		this.hearts = new Texture(Gdx.files.internal(C.path + "ui/heart.png"));
		
		this.heartsRegion = new TextureRegion[3];
		for(int i = 0; i < 3; i++) this.heartsRegion[i] = new TextureRegion(hearts, i * 19, 0, 19, 16);
		
		TextureRegion [] coinRegion = new TextureRegion[4];
		for(int i = 0; i < 4; i++)
		{
			coinRegion[i] = new TextureRegion(coin, i * 16, 0, 16, 16);
		}
		
		this.state = 0;
		
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
		int [] solids = {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
		this.map.setSolids(solids);
		
		//this.player = new Player(24f, 122f, map, camera);
		this.player = new Player(2024f, 122f, map, camera);
		this.player.fall();
		
		this.coins = new CoinManager(player, coinRegion, coinSound, camera);
		this.coins.loadCoins("testMapCoins.broc");
		
		this.baddies = new BaddieBuilder(player, camera, map, text);
		this.baddies.loadBaddies("testMapBaddies.broc");
		
		this.intro = 0;
		C.debug = false;
	}
	
	public void update(float delta)
	{
		delta *= C.time;
		
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
			C.debug = true;	
			if(this.currentState == BADDIES)
			{
				C.debug = false;
				this.currentState = GAME;
			}
			else this.currentState = BADDIES;
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.F5))
		{
			this.player.die();
		}
		
		if(C.debug)
		{
			if(Gdx.input.isKeyJustPressed(Input.Keys.F1))
			{
				this.map.saveMap();
				this.coins.saveCoins("testMapCoins.broc");
				this.baddies.saveBaddies("testMapBaddies.broc");
			}
			if(this.currentState == TILEMAP)
			{
				this.map.editMode(camera);
			}
			else if(this.currentState == ITENS)
			{
				this.coins.debugMode(delta);
			}
			else if(this.currentState == BADDIES)
			{
				this.baddies.debugMode(delta);
			}
		}
		else
		{
			this.player.update(delta);
			
		}
		
		if(C.time < 1)
		{
			C.time += delta / C.time ;
			if(C.time > 1)C.time = 1;
		}
		if(C.time > 1)
		{
			C.time -= delta / C.time;
			if(C.time < 1)C.time = 1;
		}
	
		this.baddies.update(delta);
		
		this.coins.update(delta);
		
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
		
		if(this.camera.position.x < this.camera.viewportWidth / 2f && this.currentState < 2)
		{
			this.camera.position.x = camera.viewportWidth / 2f;
		}
		
		if(camera.position.y < camera.viewportHeight / 2f)
			camera.position.y = camera.viewportHeight / 2f;
		if(camera.position.x > 2100)
		{
			if(this.currentState < 2)
			{
				this.currentState = 2;
			}
			camera.position.x = 2100;
		}
				
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
				if(this.currentState == 0)
					player.respawn(40f, 112f);
				else if(this.currentState == 1)
					player.respawn(1070f, 112f);
				this.player.fallParachute();
			}
		}
		
		if(player.position.x > 980 && this.currentState == 0)
		{
			this.currentState = 1;
		}
		
		//if(player.position <)
		
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
		

		this.baddies.draw(batch);
		this.coins.draw(batch);
		
		this.player.draw(this.batch);
		
		if(C.debug)
		{
			this.text.draw(batch,(int)camera.position.x +" ; " + (int)camera.position.y, camera.position.x + 42, camera.position.y + 62);
			
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
			else if(this.currentState == BADDIES)
			{
				this.baddies.debugModeDraw(batch);
				this.text.draw(batch,  "Baddies editor", camera.position.x - 128, camera.position.y + 62);
			}
		}
		else
		{
			int hp = player.getHp();
			
			if(hp % 2 == 1)
				batch.draw(heartsRegion[1], camera.position.x - 126 + (hp - 1) / 2 * 12, camera.position.y + 60.5f, 10, 8.5f);
			for(int i = 0; i < hp / 2; i ++)
				batch.draw(heartsRegion[0], camera.position.x - 126 + i * 12, camera.position.y + 60.5f, 10, 8.5f);	
			for(int i = 0; i < 3; i ++)
				batch.draw(heartsRegion[2], camera.position.x - 126 + i * 12, camera.position.y + 60.5f, 10, 8.5f);
		}
				
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
		this.baddies.dispose();
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
