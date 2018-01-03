package com.pgsv.game.stages;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pgsv.game.utils.Input;
import com.pgsv.game.utils.Sfx;

public class Screen implements com.badlogic.gdx.Screen {

    public Sfx sfx;
    public OrthographicCamera camera;
    public SpriteBatch batch;

    public void setSfx(OrthographicCamera camera, SpriteBatch batch){
        sfx = new Sfx(camera);
        this.camera = camera;
        this.batch = batch;
    }

    public void update(float delta) {
        Input.listen();
        sfx.update(delta);

    }

    public void draw(){

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        this.camera.update();

        this.batch.begin();
        batch.setProjectionMatrix(camera.combined);
        draw();
        this.sfx.draw(batch);
        this.batch.end();
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
