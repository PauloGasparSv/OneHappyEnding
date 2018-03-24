package com.pvale.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Actor
{
    public static enum State
    {
        Iddle,
        Walk,
        Run,
        Hit,
        Attack,
        Death,
        Dead
    }

    public State myState = State.Iddle;

    public boolean facingRight = true;

    public float x = 0f;
    public float y = 0f;

    public void update(float delta)
    {
        
    }
    
    public void draw(SpriteBatch batch)
    {

    }
}