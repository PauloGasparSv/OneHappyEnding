package com.pgsv.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pgsv.game.consts.C;

public class Text 
{
	private Texture sheet;
	private TextureRegion [] abc;
	
	public Text()
	{
		this.sheet = new Texture(Gdx.files.internal(C.path + "ui/font.png"));
		
		this.abc = new TextureRegion[40];
		
		int i = 0;
		for(int line = 0; line < 5; line ++)
		{
			for(int col = 0; col < 8; col ++)
			{
				this.abc[i] = new TextureRegion(this.sheet, 8 * col, 8 * line, 8, 8);
				i ++;
			}
		}
		
	}
	
	public void draw(SpriteBatch batch, String text, float x, float y)
	{
		int len = text.length();
		text = text.toLowerCase();
		for(int i = 0; i < len; i++)
		{
			int j = getRegion(text.charAt(i));
			if(j < 0 || j > this.abc.length - 1) continue;
			batch.draw(this.abc[j], x + i * 8, y);
		}
	}
	
	public int getRegion(char c)
	{
		if(c == ' ') return -1;
		
		int n = (int)c;
		
		if(n > 96)return n - 97;
		else if(n > 47)	return n - 48 + 26;	
		
		switch(n)
		{
			case 45: return 36;
			case 42: return 37;
			case 33: return 38;			
		}

		return -1;
	}
	
	public void dispose()
	{
		this.abc = null;
		this.sheet.dispose();
	}
	
}
