package com.pvale.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pvale.utils.In;
import com.pvale.utils.Media;

public class Map
{
    public TextureRegion [] tiles;

    public int [][] map;
    private int [] solidTiles;

    private int ribbon;

    private String texturePath;
    private String mapName;

    public Map(String mapName)
    {
        this.mapName = mapName;

        FileHandle handle = Gdx.files.internal("Maps/" + mapName);
        if(handle.exists())
        {
            String file = handle.readString();
            String lines [] = file.split("\n");

            texturePath = lines[0];
            String [] rc = lines[1].split(" ");
            
            map = new int[Integer.parseInt(rc[0])][Integer.parseInt(rc[1])];

            for(int row = 0; row < map.length; row ++)
            {
                String [] line = lines[row + 2].split(" ");
                for(int col = 0; col < map[0].length; col ++)
                {
                    map[row][col] = Integer.parseInt(line[col]);
                }
            }

        }
        else
        {
            texturePath = "Screens/1/tiles.png";
            map = new int[16][32];
        }

        Texture tileSheet = Media.loadTexture(texturePath);

        if(texturePath.equals("Screens/1/tiles.png"))
        {
            tiles = Media.getSheetFrames(tileSheet, 11, 3, 16, 16);
            solidTiles = new int[12];
            for(int i = 0; i < solidTiles.length; i++)
                solidTiles[i] = 10 + i;
        }
        else
        {
            tiles = Media.getSheetFrames(tileSheet, 11, 3, 16, 16);
            solidTiles = new int[0];
        }

        ribbon = 0;
    }

    public int getTile(float x, float y) 
    {
        int tileX = (int) (x / 16f);
        int tileY = (int) (y / 16f);
        if(tileX < 0) tileX = 0;
        if(tileY < 0) tileY = 0;
        if(tileX > map[0].length - 1) tileX = map[0].length - 1;
        if(tileY > map.length - 1) tileY = map.length - 1;
        return map[tileY][tileX];
    }

    public boolean isSolid(float x, float y)
    {
        return isSolid(getTile(x, y));
    }

    public boolean isSolid(int tile)
    {
        for(int i = 0; i < solidTiles.length; i ++)
            if(tile == solidTiles[i])
                return true;
        return false;
    }

    public void editMap(OrthographicCamera camera, float delta)
    {
        float x = camera.position.x  + In.getX() - 120.0f;
        float y = camera.position.y + In.getY() - 67.5f;

        int tileX = (int) (x / 16f);
        int tileY = (int) (y / 16f);

        if(In.copy())
        {
            ribbon = map[tileY][tileX];
        }

        if(In.paste())
        {
            map[tileY][tileX] = ribbon;
        }

        if(In.clicked())
        {
            if(In.mousePress(0))
                map[tileY][tileX] ++;
            else if(In.mousePress(1))
                map[tileY][tileX] --;
            else
                map[tileY][tileX] = 0;

            if(map[tileY][tileX] > tiles.length - 2)
                map[tileY][tileX] = 0;
            else if(map[tileY][tileX] < 0)
                map[tileY][tileX] = tiles.length - 2;
        }

        if(In.save())
        {
            String file = texturePath + "\n" + map.length + " " + map[0].length;

            for(int row = 0; row < map.length; row ++ )
            {
                file += "\n";
                for(int col = 0; col < map[0].length; col ++)
                {
                    file += map[row][col] + " "; 
                }
            }
            
            FileHandle handle = Gdx.files.local("Maps/" + mapName);
            handle.writeString(file, false);

            System.out.println("File Saved!");
        }

        if(In.cameraRight())
           camera.position.x += delta * 100f;
        else if(In.cameraLeft())
            camera.position.x -= delta * 100f;
        if(In.cameraUp())
            camera.position.y += delta * 100f;
         else if(In.cameraDown())
             camera.position.y -= delta * 100f;

    }

    public void draw(OrthographicCamera camera, SpriteBatch batch)
    {
        float cameraX = camera.position.x - 120.0f;
        float cameraY = camera.position.y - 67.5f;
        
        int startX = (int) (cameraX / 16f);
        int startY = (int) (cameraY / 16f);
        int endX = startX + 16; 
        int endY = startY + 10;

        if(endX > map[0].length - 1) endX = map[0].length - 1;
        if(endY > map.length - 1) endY = map.length - 1;

        for(int row = startY; row < endY; row ++)
        {
            for(int col = startX; col < endX; col ++)
            {
                int tile = map[row][col] - 1;
                if(tile < 0) continue;
                batch.draw(tiles[tile], col * 16, row * 16);
            }
        }
    }


}