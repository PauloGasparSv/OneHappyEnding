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
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> pushingAnimation;
    private Animation<TextureRegion> fingerAnimation;
    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> attackAnimation;
    
    private boolean pushing = false;
    private boolean lookingUp = false;
    public boolean hasControls = true;

    private int jumpCounter = 0;
    
    private float currentDelta;
    private float speed = 60f;

    public Player()
    {
        spriteSheet = Media.loadTexture("actors/hero/guy_sheet.png");

        iddleAnimation = new Animation<TextureRegion>(0.5f, 
            Media.getSheetFrames(spriteSheet, 0, 0, 1, 4, 16, 16));
        walkAnimation = new Animation<TextureRegion>(0.2f, 
            Media.getSheetFrames(spriteSheet, 0, 16, 1, 3, 16, 16));
        jumpAnimation = new Animation<TextureRegion>(0.1f, 
            Media.getSheetFrames(spriteSheet, 48, 16, 1, 2, 16, 16));
        pushingAnimation = new Animation<TextureRegion>(0.2f, 
            Media.getSheetFrames(spriteSheet, 0, 64, 1, 3, 16, 16));
        fingerAnimation = new Animation<TextureRegion>(0.2f, 
            Media.getSheetFrames(spriteSheet, 32, 80, 1, 1, 16, 16));
        attackAnimation = new Animation<TextureRegion>(0.068f, 
            Media.getSheetFrames(spriteSheet, 0, 32, 1, 5, 16, 16));

        setState(State.Iddle);
        currentFrame = (TextureRegion) currentAnimation.getKeyFrame(0);

    }

    @Override
    public void update(OrthographicCamera camera, Map map, float delta)
    {
        //In.getControllerInfo();
        pushing = false;
        currentDelta += delta;

        if(hasControls) controls(delta);

        if(myState == State.Attack && attackAnimation.isAnimationFinished(currentDelta))
            setState(State.Iddle);

        if(facingRight) 
        {
            if(map.isSolid(x + 13, y + 8))
            {
                x -= delta * speed;
                pushing = true;
            }
        }
        else
        {
            if(map.isSolid(x + 3, y + 8))
            {
                x += delta * speed;
                pushing = true;
            }
        }

        if(map.isSolid(x + 4, y) || map.isSolid(x + 12, y))
        {
            groundMe();
        }
        else if(grounded)
        {
            setState(State.Jump);
            grounded = false;
            if(jumpCounter == 0)
                jumpCounter = 1;
        }
        
        if(!grounded)
        {
            ac += delta * gravity;
            if(ac > 200) ac = 200;
            y -= ac * delta;

            if(ac < 0 && (map.isSolid(x + 3, y + 13) || map.isSolid(x + 13, y + 13)))
            {   
                ac = 0;
                y = ((int) y / 16) * 16 + 2;
            }

        }


    }

    public void groundMe()
    {
        if(!grounded && ac >= 0)
        {
            int row = (int) ( y / 16f );
            y = 16 * row + 14;
            grounded = true;
            jumpCounter = 0;
            ac = 0;
            if(myState == State.Jump)
                setState(State.Iddle);
        }
    }

    public void controls(float delta)
    {
        if(myState != State.Attack)
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
        }

        lookingUp = In.up();

        if(In.justJumped() && (grounded || jumpCounter < 2))
        {
            setState(State.Jump);

            grounded = false;
            y += 4;
            ac = -130f + jumpCounter * 30f;
            jumpCounter ++;
        }

        if(In.justAttacked() && grounded && myState != State.Jump && myState != State.Attack)
        {
            setState(State.Attack);
            
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
        else if(myState == State.Attack)
            currentAnimation = attackAnimation;
    }

    @Override 
    public void draw(SpriteBatch batch)
    {
        if(pushing && myState == State.Walk)
            currentFrame = (TextureRegion) pushingAnimation.getKeyFrame(currentDelta, true);
        else if(lookingUp && myState == State.Iddle)
            currentFrame = (TextureRegion) fingerAnimation.getKeyFrame(currentDelta, false);
        else if(myState != State.Jump)
            currentFrame = (TextureRegion) currentAnimation.getKeyFrame(currentDelta, true);
        else
            currentFrame = (TextureRegion) jumpAnimation.getKeyFrame(ac > 20 ? 1 : 0, false);

        if((facingRight && currentFrame.isFlipX()) || !facingRight && !currentFrame.isFlipX())
            currentFrame.flip(true, false);
        if(!pushing)
            batch.draw(currentFrame, x, y);
        else
            batch.draw(currentFrame, x + (facingRight ? -2 : 2), y);

    }
    
}