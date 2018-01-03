package com.pgsv.game.stages;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pgsv.game.MyGdxGame;

public class SelectMap extends Screen{

    private MyGdxGame game;
    private SpriteBatch batch;

    public SelectMap(MyGdxGame game, SpriteBatch batch){
        this.game = game;
        this.batch = batch;
    }

    public void update(float delta){
        super.update(delta);


    }

    public void draw(SpriteBatch batch){

    }

}
