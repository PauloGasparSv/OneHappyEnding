package com.pvale.actors;

import javax.lang.model.util.ElementScanner6;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pvale.utils.In;
import com.pvale.utils.Media;

public class Player extends Actor
{
    private Texture spriteSheet;
    private TextureRegion currentFrame;

    private Animation<TextureRegion> iddleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> currentAnimation;
    
    private float currentDelta;
    private float speed = 40f;

    public Player()
    {
        spriteSheet = Media.loadTexture("Actors/hero/guy_sheet.png");

        iddleAnimation = new Animation<TextureRegion>(0.5f, 
            Media.getSheetFrames(spriteSheet, 0, 0, 1, 4, 16,16));
        walkAnimation = new Animation<TextureRegion>(0.2f, 
            Media.getSheetFrames(spriteSheet, 0, 16, 1, 3, 16,16));

        setState(State.Iddle);
        currentFrame = (TextureRegion) currentAnimation.getKeyFrame(0);
    }

    @Override
    public void update(float delta)
    {
        currentDelta += delta;
        
        controls(delta);
    }

    public void controls(float delta)
    {
        if(In.right())
        {
            if(myState == State.Iddle)
                setState(State.Walk);
            
            facingRight = true;
            x += delta * speed;
        }
        else if(In.left())
        {
            if(myState == State.Iddle)
                setState(State.Walk);
            
            facingRight = false;
            x -= delta * speed;
        }
        else
        {
            if(myState != State.Iddle)
                setState(State.Iddle);
        }
    }

    public void setState(State state)
    {
        myState = state;
        currentDelta = 0f;
        
        if(myState == State.Iddle)
            currentAnimation = iddleAnimation;
        else if(myState == State.Walk)
            currentAnimation = walkAnimation;
    }

    @Override 
    public void draw(SpriteBatch batch)
    {
        currentFrame = (TextureRegion) currentAnimation.getKeyFrame(currentDelta, true);

        if((facingRight && currentFrame.isFlipX()) || !facingRight && !currentFrame.isFlipX())
            currentFrame.flip(true, false);
        
        batch.draw(currentFrame, x, 0);
    }
    
}