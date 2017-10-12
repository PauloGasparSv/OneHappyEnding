package com.pgsv.game.actors;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pgsv.game.consts.C;

public class CoinManager {

	public LinkedList<Coin> coins;
	
	private Animation<TextureRegion> coinAnimation;
	
	private Sound coinSound;
	
	private Player player;
	
	private OrthographicCamera camera;
	
	private Vector2 preview;
	
	private boolean mousePressed;
	
	public CoinManager(Player player, Animation<TextureRegion> coinAnimation, Sound coinSound,OrthographicCamera camera)
	{
		this.coins = new LinkedList<Coin>();
		this.player = player;
		this.coinAnimation = coinAnimation;
		this.coinSound = coinSound;
		this.camera = camera;
		this.preview = new Vector2();
		this.mousePressed = false;
	}
	
	public void update(float delta)
	{
		for(Coin c : coins)
		{
			c.update(delta);
		}
	}
	
	public void debugMode(float delta)
	{
		int x = (int) ( Gdx.input.getX() * 0.2f  + camera.position.x - camera.viewportWidth / 2f);
		int y = (int) (144 - Gdx.input.getY()  * 0.2f + camera.position.y - camera.viewportHeight / 2f);
		
		this.preview.x = x / 16;
		this.preview.y = y / 16;
		
		if(Gdx.input.isButtonPressed(0))
		{
			if(!this.mousePressed)
			{
				this.mousePressed = true;
				
				this.addCoin(preview.x * 16 + 6, preview.y * 16 + 2);
				
			}
		}
		else if(Gdx.input.isButtonPressed(1))
		{
			if(!this.mousePressed)
			{
				float tx = preview.x * 16;
				float ty = preview.y * 16;
				LinkedList<Coin> tempDel = new LinkedList<Coin>();
				for(Coin c : coins)
				{
					Rectangle r = c.getRect();
					if(r.x > tx - 8 && r.x < tx + 8 && r.y < ty + 8 && r.y > ty - 8)
					{
						tempDel.add(c);
					}
				}
				for(Coin c : tempDel)
				{
					coins.remove(c);
				}
			}
		}
		else
		{
			this.mousePressed = false;
		}		
	}
	
	public void debugModeDraw(SpriteBatch batch)
	{
		batch.setColor(new Color(0.2f,0.2f,1f,0.7f));
		batch.draw(this.coinAnimation.getKeyFrame(0), preview.x * 16 - 4 + 6, preview.y * 16 + 2,10,10);
		batch.setColor(new Color(1,1,1,1));
	}
	
	public void draw(SpriteBatch batch)
	{
		for(Coin c : coins)
		{
			c.draw(batch);
		}
	}
	
	public void addCoin(float x, float y)
	{
		this.coins.add(new Coin(this.coinAnimation, this.player, this.coinSound, this.camera, x, y));	
	}
	
	public int numCoins()
	{
		return this.coins.size();
	}
	
	public void saveCoins(String path)
	{	
		String coinsFile = "";
		
		for(Coin c : coins)
		{
			Rectangle r = c.getRect();
			coinsFile += r.x + " " + r.y + "\n";
		}
		
		FileHandle file = Gdx.files.absolute("C:/temp/" + path);	
		file.writeString(coinsFile, false);		
		JOptionPane.showMessageDialog(null, "COINS SAVED");		
	}
	
	public void loadCoins(String path)
	{
		FileHandle coinFile = Gdx.files.internal(C.path + "maps/" + path);
		String coinText = coinFile.readString();
		
		String [] lines = coinText.split("\n");
		
		for(String line : lines)
		{
			String [] temp = line.split(" ");
			this.addCoin(Float.parseFloat(temp[0]), Float.parseFloat(temp[1]));
		}
	}
	
}
