package com.pvale.screens;

import com.pvale.screens.SimpleStage;
import com.pvale.utils.In;
import com.pvale.actors.Actor;
import com.pvale.actors.Player;
import com.pvale.maps.Map; 

public class SimpleStage extends Stage
{
    private Player player;
    private Map map;

    private boolean editMode = false;

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
        if(editMode)
            map.editMap(camera,delta);
        if(In.justMenu())
            editMode = !editMode;
        if(!editMode)
            player.update(camera, map, delta);

        cameraControl();
    }

    public void cameraControl()
    {
        if(player.x - camera.position.x > 30f)
            camera.position.x = player.x - 30f;
        else if(camera.position.x - player.x > 50f)
            camera.position.x = player.x + 50f;
        if(player.y - camera.position.y > 16f)
            camera.position.y = player.y - 16f;
        else if(camera.position.y - player.y > 20f)
            camera.position.y = player.y + 20f;
    }

    @Override
    public void draw()
    {
        map.draw(camera, batch);
        player.draw(batch);
    }

    @Override
    public void dispose()
    {
        map.dispose();
    }
}