package com.pvale.screens;

import com.pvale.screens.SimpleStage;
import com.pvale.actors.Actor;
import com.pvale.actors.Player;
import com.pvale.maps.Map; 

public class SimpleStage extends Stage
{
    private Player player;
    private Map map;

    public SimpleStage()
    {
        super();

        Actor.gravity = 380f;
  
        map = new Map("simpleMap.map");
        
        player = new Player();
        player.x = 12f;
        player.y = 64f;
        player.grounded = false;
  
    }
    
    @Override
    public void update(float delta)
    {
        map.editMap(camera,delta);

        player.update(camera, map, delta);

        if(player.x - camera.position.x > 30f)
            camera.position.x = player.x - 30f;
        else if(camera.position.x - player.x > 50f)
            camera.position.x = player.x + 50f;
        
    }

    @Override
    public void draw()
    {
        map.draw(camera, batch);
        player.draw(batch);
    }
}