package com.pvale.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pvale.maps.Map;

public class Actor
{
    public static enum State
    {
        Iddle,
        Walk,
        Run,
        Hit,
        Jump,
        Attack,
        Death,
        Dead
    }

    public State myState = State.Iddle;

    public boolean facingRight = true;
    public boolean grounded = true;

    public float x = 0f;
    public float y = 0f;
    public float ac = 0f;

    public static float gravity = 10f;

    public void update(OrthographicCamera camera, Map map, float delta)
    {
        
    }
    
    public void draw(SpriteBatch batch)
    {

    }

    public void dispose()
    {

    }
}