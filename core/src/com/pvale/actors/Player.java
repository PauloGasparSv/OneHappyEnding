package com.pvale.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pvale.utils.In;
import com.pvale.utils.Media;
import com.pvale.maps.Map;

public class Player extends Actor
{
    private Texture spriteSheet;
    private TextureRegion currentFrame;

    private Animation<TextureRegion> iddleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> currentAnimation;
    
    private float currentDelta;
    private float speed = 60f;

    private int jumpCounter = 0;

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
    public void update(OrthographicCamera camera, Map map, float delta)
    {
        currentDelta += delta;
        controls(delta);

        if(facingRight) 
        {
            if(map.isSolid(x + 13, y + 8))
            {
                x -= delta * speed;
            }
        }
        else
        {
            if(map.isSolid(x + 3, y + 8))
            {
                x += delta * speed;
            }
        }

        if(map.isSolid(x + 4, y) || map.isSolid(x + 12, y))
        {
            if(!grounded && ac >= 0)
            {
                int row = (int) ( y / 16f );
                y = 16 * row + 14;
                grounded = true;
                jumpCounter = 0;
                ac = 0;
            }
        }
        else
        {
            grounded = false;
            if(jumpCounter == 0)
                jumpCounter = 1;
        }
        
        if(!grounded)
        {
            ac += delta * gravity;
            y -= ac * delta;
        }
        // if(In.up()) y += speed * delta;
        // if(In.down()) y -= speed * delta;

        System.out.println(map.getTile(x,y));

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
            if(myState == State.Walk)
                setState(State.Iddle);
        }

        if(In.justJumped() && (grounded || jumpCounter < 2))
        {
            grounded = false;
            y += 4;
            ac = -130f + jumpCounter * 30f;
            jumpCounter ++;
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
            
        batch.draw(currentFrame, x, y);
    }
    
}