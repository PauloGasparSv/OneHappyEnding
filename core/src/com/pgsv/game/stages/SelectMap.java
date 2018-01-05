package com.pgsv.game.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pgsv.game.MyGdxGame;
import com.pgsv.game.utils.C;
import com.pgsv.game.utils.Input;
import com.pgsv.game.utils.Media;

import java.util.LinkedList;

public class SelectMap extends Screen{

    private MyGdxGame game;
    private Map map;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture whiteBackground;
    private TextureRegion scrollTopBot;
    private Texture scrollMid;
    private Texture scrollTitle;
    private Texture cursor;

    private int numRows;

    public SelectMap(MyGdxGame game, SpriteBatch batch){
        this.game = game;
        this.batch = batch;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, C.WIDTH, C.HEIGHT);
        setSfx(camera,batch);
        sfx.fadeIn();
        whiteBackground = Media.loadTexture("ui/whiteBackground.png");
        scrollTopBot = new TextureRegion(Media.loadTexture("ui/ScrollTopBottom.png"));
        scrollMid = Media.loadTexture("ui/ScrollMiddle.png");
        scrollTitle = Media.loadTexture("ui/SelectMap.png");
        cursor = Media.loadTexture("ui/mouseCursor.png");

        LinkedList<String> dirs = Media.getDirectories("EditMaps");
        for(String i: dirs){
            System.out.println(i);
        }

        numRows = 1;
    }

    public void update(float delta){
        super.update(delta);

        if(Input.isKeyPressed(Input.S))
            camera.position.y -= delta * 50f;
        if(Input.isKeyPressed(Input.W))
            camera.position.y += delta * 50f;
    }

    public void draw(){
        batch.setColor(new Color(0.74f,0.85f,0.88f,1f));
        batch.draw(whiteBackground, 0,camera.position.y - C.HALF_HEIGHT);
        batch.setColor(Color.WHITE);

        batch.draw(scrollTitle, 82, 122);

        batch.draw(scrollTopBot, 16, 98);

        for(int i = 0; i < numRows * 3; i++)
            batch.draw(scrollMid, 16,82 - i * 16);

        batch.draw(scrollTopBot, 16, 80  - numRows * 48,112,9,224,18
                ,1,-1,0);

        this.batch.draw(cursor, Input.getMouse().x - 1, Input.getMouse().y - 12);
    }

}
