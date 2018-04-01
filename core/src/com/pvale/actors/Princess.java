package com.pvale.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pvale.maps.Map;
import com.pvale.utils.Media;

public class Princess extends Actor
{
    private Texture spriteSheet;
    private TextureRegion currentFrame;

    private Animation<TextureRegion> iddleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> currentAnimation;
    
    private float currentDelta;

    public Princess()
    {
        spriteSheet = Media.loadTexture("actors/princess/princess.png");

        iddleAnimation = new Animation<TextureRegion>(0.5f, 
            Media.getSheetFrames(spriteSheet, 0, 0, 1, 4, 16, 24));
        walkAnimation = new Animation<TextureRegion>(0.5f, 
            Media.getSheetFrames(spriteSheet, 0, 24, 1, 2, 16, 24));
        setState(State.Iddle);
        currentFrame = (TextureRegion) currentAnimation.getKeyFrame(0);

    }

    @Override
    public void update(OrthographicCamera camera, Map map, float delta)
    {
        currentDelta += delta;

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
        
        batch.draw(currentFrame, x, y);

    }
    
}