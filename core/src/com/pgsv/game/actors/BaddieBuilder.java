package com.pgsv.game.actors;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pgsv.game.consts.C;
import com.pgsv.game.stages.Map;
import com.pgsv.game.utils.Text;

public class BaddieBuilder 
{
	private final int SPIKY = 0, SPIKY_SMART = 1, BALLY = 2;
	
	private int currentBaddie;
	
	private LinkedList<Actor> baddies;

	private Texture spikyTexture;
	private Texture spikyDeadTexture;
	private Texture ballyTexture;	
	private Texture exclamationTexture;
	
	private Animation<TextureRegion> spikyWalkAnimation;
	private Animation<TextureRegion> spikyDeathAnimation;
	private Animation<TextureRegion> ballyAnimation;
	private Animation<TextureRegion> exclamationAnimation;
	
	float temp;
	
	private TextureRegion [] baddiePreview;
	
	private Player player;
	
	private Text text;
	
	private Map map;
	
	private OrthographicCamera camera;
	
	private Vector2 preview;
	
	private boolean mousePressed;
	
	private boolean right;
	
	private LinkedList<Actor> remove;
	
	public BaddieBuilder(Player player, OrthographicCamera camera, Map map, Text text)
	{
		this.baddies = new LinkedList<Actor>();
		this.player = player;
		this.camera = camera;
		this.mousePressed = false;
		this.map = map;
		this.text = text;
		this.currentBaddie = 0;
		this.preview = new Vector2();
		this.remove = new LinkedList<Actor>();
		
		this.setup();
		
		this.baddiePreview = new TextureRegion[3];
		
		this.baddiePreview[SPIKY] = new TextureRegion(spikyTexture,0,0,16,13);
		this.baddiePreview[SPIKY_SMART] = new TextureRegion(spikyTexture,0,0,16,13);
		this.baddiePreview[BALLY] = new TextureRegion(ballyTexture,0,0,14,14);
		
		this.right = false;
	}
	
	private void setup()
	{
		this.spikyTexture = new Texture(Gdx.files.internal(C.path + "Actors/baddies/bullseye.png"));
		this.ballyTexture = new Texture(Gdx.files.internal(C.path + "Actors/baddies/bally14.png"));
		this.spikyDeadTexture = new Texture(Gdx.files.internal(C.path + "Actors/baddies/spikyDead.png"));
		this.exclamationTexture = new Texture(Gdx.files.internal(C.path + "stages/exclamation.png"));
		
		TextureRegion [] currentSheet = new TextureRegion[6];
		for(int i = 0; i < currentSheet.length; i ++)
		{
			currentSheet[i] = new TextureRegion(this.spikyTexture,16 * i,0,16,13);
		}
		this.spikyWalkAnimation = new Animation<TextureRegion>(0.15f ,currentSheet);
		
		currentSheet = new TextureRegion[2];
		currentSheet[0] = new TextureRegion(this.spikyDeadTexture,0,0,16, 8);
		currentSheet[1] = new TextureRegion(this.spikyDeadTexture,16,0, 16, 8);
		this.spikyDeathAnimation = new Animation<TextureRegion>(0.04f,currentSheet);
		

		currentSheet = new TextureRegion[6];
		for(int i = 0; i < 6; i ++ )
		{
			currentSheet[i] = new TextureRegion(ballyTexture, 14 * i, 0 , 14, 14);
		}
		this.ballyAnimation = new Animation<TextureRegion>(0.15f, currentSheet);
		
		currentSheet = new TextureRegion[6];
		for(int i = 0; i < 6; i ++ )
		{
			currentSheet[i] = new TextureRegion(exclamationTexture, 16 * i, 0 , 16, 14);
		}
		this.exclamationAnimation = new Animation<TextureRegion>(0.05f, currentSheet);
		
	}
	
	
	public void update(float delta)
	{
		for(Actor a : baddies)
		{
			a.update(delta);
			if(a.isDead())this.remove.add(a);
		}
		
		if(!this.remove.isEmpty())
		{
			for(Actor a: this.remove) this.baddies.remove(a);
			this.remove.clear();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) temp = 0f;
		temp += delta;
	}
	
	public void draw(SpriteBatch batch)
	{
		batch.draw(exclamationAnimation.getKeyFrame(temp,false), player.position.x,player.position.y + 16);
		
		
		for(Actor a : baddies)
		{
			a.draw(batch);
		}
	}
	
	public void debugMode(float delta)
	{
		int x = (int) ( Gdx.input.getX() * 0.2f  + camera.position.x - camera.viewportWidth / 2f);
		int y = (int) (144 - Gdx.input.getY()  * 0.2f + camera.position.y - camera.viewportHeight / 2f);
		
		this.preview.x = (x / 16) * 16;
		this.preview.y = (y / 16) * 16;
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
		{
			if(!this.right)
			{
				this.right = true;
			}
			else
			{
				this.right = false;
				this.currentBaddie ++;
				if(this.currentBaddie > 2) this.currentBaddie = 0;
			}
		}
		
		if(Gdx.input.isButtonPressed(0))
		{
			if(!this.mousePressed)
			{
				this.mousePressed = true;
				
				boolean ok = true;
				for(Actor b : baddies)
				{
					if(preview.x > b.position.x - 6 && preview.x < b.position.x + 6 &&
							preview.y > b.position.y - 6 && preview.y < b.position.y + 6)
					{
						ok = false;
						return;
					}
				}
				if(ok) this.addBaddie(preview.x , preview.y );
			}
		}
		else if(Gdx.input.isButtonPressed(1))
		{
			if(!this.mousePressed)
			{
				LinkedList<Actor> del = new LinkedList<Actor>();
				for(Actor b : baddies)
				{
					if(preview.x > b.position.x - 6 && preview.x < b.position.x + 6 &&
							preview.y > b.position.y - 6 && preview.y < b.position.y + 6)
					{
						del.add(b);
					}
				}
				for(Actor b : del)
				{
					baddies.remove(b);
				}
			}
		}
		else
		{
			this.mousePressed = false;
		}		
	}
	
	public void addBaddie(float x, float y)
	{
		switch(this.currentBaddie)
		{
			case SPIKY:
				this.baddies.add(new Spiky(x, y, this.right, false, this.spikyWalkAnimation,this.spikyDeathAnimation, map, player, camera));
			break;
			case SPIKY_SMART:
				this.baddies.add(new Spiky(x, y, this.right, true, this.spikyWalkAnimation,this.spikyDeathAnimation, map, player, camera));
			break;
			case BALLY:
				this.baddies.add(new Bally(x, y, this.right , map, camera, player, this.ballyAnimation));
			break;
		}
	}
	
	public void addBaddie(int bad, float x, float y, boolean right, boolean special)
	{
		switch(bad)
		{
			case 2:
				this.baddies.add(new Spiky(x, y, right, special, this.spikyWalkAnimation, this.spikyDeathAnimation, map, player, camera));
			break;
			case 4:
				this.baddies.add(new Bally(x, y, right , map, camera, player, this.ballyAnimation));
			break;
		}
	}
	public void debugModeDraw(SpriteBatch batch)
	{
		batch.setColor(new Color(0.2f,0.2f,1f,0.7f));
		text.draw(batch,this.getDesc(), preview.x, preview.y + 12);
		if(this.baddiePreview[this.currentBaddie].isFlipX() != this.right)
		{
			this.baddiePreview[this.currentBaddie].flip(true, false);
		}
		batch.draw(this.baddiePreview[this.currentBaddie], preview.x, preview.y );
		batch.setColor(new Color(1,1,1,1));
	}
	
	public String getDesc()
	{
		switch(this.currentBaddie)
		{
			case SPIKY: return "Spiky";
			case SPIKY_SMART: return "Smart Spiky";
			case BALLY: return "Bally";
		}
		return "None";
	}
	
	
	
	
	public void loadBaddies(String path)
	{
		FileHandle badsFile = Gdx.files.internal(C.path + "maps/" + path);
		String badsText = badsFile.readString();
		
		String [] lines = badsText.split("\n");
		
		for(String line : lines)
		{
			String [] temp = line.split(" ");
			this.addBaddie(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]), Integer.parseInt(temp[2]),temp[3].equals("1"), temp[4].equals("1"));
		}
	}
	
	public void saveBaddies(String path)
	{	
		String badsFile = "";
		
		for(Actor a : baddies)
		{
			badsFile += a.getId() + " " + (int)(a.position.x) + " " + (int)(a.position.y)+ " " + (a.right ? "1" : "0") + " " + (a.special? "1" : "0") + "\n";
		}
		
		FileHandle file = Gdx.files.absolute("C:/temp/" + path);	
		file.writeString(badsFile, false);		
		JOptionPane.showMessageDialog(null, "BADDIES SAVED");		
	}
	
	public void dispose()
	{
		this.spikyTexture.dispose();
		this.spikyDeadTexture.dispose();
		this.ballyTexture.dispose();
		this.exclamationTexture.dispose();
	}
	
}
