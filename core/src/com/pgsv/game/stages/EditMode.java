package com.pgsv.game.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pgsv.game.MyGdxGame;
import com.pgsv.game.utils.C;
import com.pgsv.game.utils.Media;

public class EditMode extends  Screen{

    private MyGdxGame game;
    private Map map;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture player;

    public EditMode(MyGdxGame game){
        this.game = game;
        this.batch = new SpriteBatch();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, C.WIDTH, C.HEIGHT);
        this.camera.position.x = C.HALF_WIDTH;
        this.camera.position.y = C.HALF_HEIGHT;
        setSfx(camera, batch);

        player = Media.loadTexture("Actors/hero/guy_sheet.png");
    }

    public void update(float delta){
        super.update(delta);
    }

    public void draw(SpriteBatch batch){
        batch.draw(player, 0,0);
    }

}
