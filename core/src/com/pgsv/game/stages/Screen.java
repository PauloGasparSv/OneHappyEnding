package com.pgsv.game.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pgsv.game.MyGdxGame;
import com.pgsv.game.utils.C;
import com.pgsv.game.utils.Input;
import com.pgsv.game.utils.Vfx;

public class Screen implements com.badlogic.gdx.Screen {

    public Vfx vfx;
    public OrthographicCamera camera;
    public SpriteBatch batch;
    public MyGdxGame game;

    public Screen()
    {
        game = C.game;
        batch = C.batch;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, C.WIDTH, C.HEIGHT);

        vfx = new Vfx(camera);
    }

    public void update(float delta) {

    }

    public void draw(){

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        Input.listen();
        vfx.update(delta);

        update(delta);
        camera.update();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        draw();
        vfx.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
