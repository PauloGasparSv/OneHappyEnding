package com.pvale.screens;

import com.pvale.actors.Actor;
import com.pvale.actors.Princess;
import com.pvale.actors.Actor.State;
import com.pvale.maps.Map;
import com.pvale.ohe.MyGame;
import com.pvale.utils.Camera;
import com.pvale.utils.In;
import com.pvale.utils.Text; 

public class IntroStage extends Stage
{

    private enum IntroState
    {
        walkRightA,
        walkRightB,
        walkRightC,
        walkRightD,
        stopA
    }

    private Map map;

    private Princess princess;

    private IntroState currentState;

    private long textTimer;

    private String currentText;

    public IntroStage()
    {
        super();

        Actor.gravity = 380f;

        princess = new Princess();
        princess.x = -32f;
        princess.y = 32f;
        princess.setState(State.Walk);

        currentState = IntroState.walkRightA;

        textTimer = System.currentTimeMillis();
        
        currentText = "";

        map = new Map("intro.map");  
    }
    
    @Override
    public void update(float delta)
    {
        if(In.getPressedButton() > -1 || In.jump())
        {
            shake();
            setFade(FadeState.FADEOUT);
        }

        if(fadeState == FadeState.BLACK)
        {
            MyGame.game.setScreen(new SimpleStage());
            return;
        }

        if(currentState == IntroState.walkRightA)
        {
            princess.x += 16f * delta;

            text("IT WAS A VERY CALM NIGHT", 80);
            if(System.currentTimeMillis() - textTimer > 5000)
                changeState(IntroState.walkRightB);

            if(princess.x - Camera.getX() > 120)
                camera.position.x = princess.x;
    
            if(camera.position.x > 182f)
                camera.position.x = 182f;
    
        }   
        else if(currentState == IntroState.walkRightB)
        {
            princess.x += 16f * delta;

            text("NOBODY EXPECTED ANYTHING TO HAPPEN", 80);
            if(System.currentTimeMillis() - textTimer > 6000)
                changeState(IntroState.walkRightC);
            
            if(princess.x - Camera.getX() > 120)
                camera.position.x = princess.x;
    
            if(camera.position.x > 182f)
                camera.position.x = 182f;
        }   
        else if(currentState == IntroState.walkRightC)
        {
            princess.x += 16f * delta;

            text("BUT SOMETHING DID", 100);
            if(System.currentTimeMillis() - textTimer > 4000)
                changeState(IntroState.walkRightD);

            if(princess.x - Camera.getX() > 120)
                camera.position.x = princess.x;
    
            if(camera.position.x > 182f)
                camera.position.x = 182f;
        } 
        else if(currentState == IntroState.walkRightD)
        {
            if(camera.position.x < 182f)
            {
                princess.x += 16.5f * delta;
                
                if(princess.x - Camera.getX() > 120)
                    camera.position.x = princess.x;
            }
            else
            {
                princess.setState(State.Iddle);
                changeState(IntroState.stopA);
                camera.position.x = 182f;
            }
        }

        princess.update(camera, map, delta);
    }

    public void text(String text, int deltaTimer)
    {
        int currentChar = (int) (System.currentTimeMillis() - textTimer) / deltaTimer;
        if(currentChar > text.length()) currentChar = text.length();
        currentText = text.substring(0, currentChar);
    }

    public void changeState(IntroState state)
    {
        currentState = state;
        textTimer = System.currentTimeMillis();
        currentText = "";
    }


    @Override
    public void draw()
    {
        map.draw(camera, batch);
        princess.draw(batch);

        if(currentState == IntroState.walkRightA)
        {
            Text.draw(batch, currentText, Camera.getX() + 32f, 92f,1.4f);
        }
        else if(currentState == IntroState.walkRightB)
        {
            Text.draw(batch, currentText, Camera.getX() + 0f, 92f,1.4f);
        }
        else if(currentState == IntroState.walkRightC)
        {
            Text.draw(batch, currentText, Camera.getX() + 64f, 92f,1.4f);
        }
    }
    @Override
    public void dispose()
    {
        map.dispose();
    }
}