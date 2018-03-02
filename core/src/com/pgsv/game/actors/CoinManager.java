package com.pgsv.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pgsv.game.utils.C;
import com.pgsv.game.utils.Media;

import java.util.LinkedList;

import javax.swing.JOptionPane;

public class CoinManager {

	private Animation<TextureRegion> coinAnimation;
	private Animation<TextureRegion> sparkleAnimation;

	public LinkedList<Coin> coins;
	public LinkedList<Coin> removeLater;

	private OrthographicCamera camera;

	private Player player;

	private Sound coinSound;

	private Vector2 preview;

	private boolean hasDead;
	private boolean mousePressed;
	
	public CoinManager(Player player, Texture coin, Texture sparkle,
					   Sound coinSound, OrthographicCamera camera) {

		this.coins = new LinkedList<Coin>();
		this.removeLater = new LinkedList<Coin>();
		this.hasDead = this.mousePressed = false;
		this.player = player;

		this.coinAnimation = Media.createAnimation(coin,0.22f,
				new Vector2(4,1), new Vector2(0,0),
				new Vector2(16,16), new Vector2(0,0));


		this.sparkleAnimation = Media.createAnimation(sparkle, 0.09f,
				new Vector2(4,1), new Vector2(0,0),
				new Vector2(16,12), new Vector2(0,0));

		this.coinSound = coinSound;
		this.camera = camera;
		this.preview = new Vector2();
	}
	
	public void update(float delta) {
		for(Coin c : this.coins) {
			c.update(delta);
			if(c.isDead()) {
				this.hasDead = true;
				this.removeLater.add(c);
			}
		}
		if(this.hasDead) {
			this.hasDead = false;
			for(Coin c : this.removeLater) {
				this.coins.remove(c);
			}
		}
	}
	
	public void debugMode(float delta) {
		int x = (int) ( Gdx.input.getX() * 0.2f  + camera.position.x - camera.viewportWidth / 2f);
		int y = (int) (144 - Gdx.input.getY()  * 0.2f + camera.position.y - camera.viewportHeight / 2f);
		
		this.preview.x = x / 16;
		this.preview.y = y / 16;
		
		if(Gdx.input.isButtonPressed(0)) {
			if(!this.mousePressed) {
				this.mousePressed = true;
				this.addCoin(preview.x * 16 + 6, preview.y * 16 + 2);
			}
		}
		else if(Gdx.input.isButtonPressed(1)) {
			if(!this.mousePressed) {
				this.mousePressed = true;
				float tx = preview.x * 16;
				float ty = preview.y * 16;
				LinkedList<Coin> tempDel = new LinkedList<Coin>();
				for(Coin c : coins) {
					Rectangle r = c.getRect();
					if(r.x > tx - 8 && r.x < tx + 8 && r.y < ty + 8 && r.y > ty - 8) {
						tempDel.add(c);
					}
				}
				for(Coin c : tempDel) {
					coins.remove(c);
				}
			}
		}
		else {
			this.mousePressed = false;
		}		
	}
	
	public void debugModeDraw(SpriteBatch batch) {
		batch.setColor(new Color(0.2f,0.2f,1f,0.7f));
		batch.draw(this.coinAnimation.getKeyFrame(0), preview.x * 16 - 4 + 6, preview.y * 16 + 2,10,10);
		batch.setColor(new Color(1,1,1,1));
	}
	
	public void draw(SpriteBatch batch) {
		for(Coin c : coins) {
			c.draw(batch);
		}
	}
	
	public void addCoin(float x, float y) {
		this.coins.add(new Coin(this.coinAnimation,this.sparkleAnimation, this.player, this.coinSound, this.camera, x, y));	
	}
	
	public int numCoins() {
		return this.coins.size();
	}
	
	public void saveCoins(String path) {
		String coinsFile = "";
		
		for(Coin c : coins) {
			Rectangle r = c.getRect();
			coinsFile += (int)(r.x) + " " + (int)(r.y) + "\n";
		}
		
		FileHandle file = Gdx.files.absolute("C:/temp/" + path);	
		file.writeString(coinsFile, false);		
		JOptionPane.showMessageDialog(null, "COINS SAVED");		
	}
	
	public void loadCoins(String path) {
		FileHandle coinFile = Gdx.files.internal(C.ROOT + "maps/" + path);
		String coinText = coinFile.readString();
		
		String [] lines = coinText.split("\n");
		
		for(String line : lines) {
			String [] temp = line.split("\\s");
			this.addCoin(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
		}
	}
	
}
