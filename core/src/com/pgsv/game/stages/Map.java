package com.pgsv.game.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class Map {

	public int [][] map;
	private TextureRegion [] tiles;
	
	public Map(int w, int h, TextureRegion [] tiles)
	{
		this.map = new int[h][w];
		
		int i[][] = 
		{
			{10,11,11,11,11,11,11,11,11,11,11,0,0,11,11,12},
			{0,0,0,0,0,11,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		};
		this.map = i;
		
		this.tiles = tiles;
	}
	
	public void changeTile()
	{
		
	}
	
	public int getTile(float x, float y)
	{
		int tileX = (int)(x / 16);
		int tileY = (int)(y / 16);
		
		if(tileX > map[0].length - 1 || tileY > map.length - 1 || tileX < 0 || tileY < 0) return 0;
		
		return map[tileY][tileX];
	}
	
	public Vector3 getTileVector(float x, float y)
	{
		Vector3 info = new Vector3();
		int tileX = (int)(x / 16);
		int tileY = (int)(y / 16);
		
		info.x = tileX;
		info.y = tileY;
		
		if(tileX > map[0].length - 1 || tileY > map.length - 1 || tileX < 0 || tileY < 0) info.z = 0;
		else info.z = map[tileY][tileX];
		return info;
	}
	
	public void draw(OrthographicCamera camera, SpriteBatch batch)
	{
		float w = camera.viewportWidth;
		float h = camera.viewportHeight;
		float cameraX = camera.position.x - w / 2f;
		float cameraY = camera.position.y - h / 2f;
		
		int left = (int) (cameraX / 16f);
		int right = (int) ((cameraX + w) / 16f) + 1;
		int bottom = (int) (cameraY / 16f);
		int top = (int) ((cameraY + h) / 16f) + 1;
		
		if(left < 0) left = 0;
		if(right > map[0].length - 1) right = map[0].length - 1;
		if(bottom < 0) bottom= 0;
		if(top > map.length - 1) top = map.length - 1;
		
		for(int line = bottom; line <= top; line ++)
		{
			for(int col = left; col <= right ; col ++)
			{
				int tile = this.map[line][col];
				if(tile == 0) continue;
				batch.draw(this.tiles[tile - 1], col * 16, line * 16);
			}
		}
	}
	
}
