package com.pvale.screens;

import com.pvale.screens.SimpleStage;
import com.pvale.actors.Player; 

public class SimpleStage extends Stage
{
    private Player player;

    public SimpleStage()
    {
        super();
        player = new Player();
    }
    
    @Override
    public void update(float delta)
    {
        player.update(delta);
    }

    @Override
    public void draw()
    {
        player.draw(batch);
    }
}