package com.pgsv.game.stages;
import com.pgsv.game.utils.Input;
import com.pgsv.game.utils.Sfx;

public class Screen implements com.badlogic.gdx.Screen {

    public Sfx sfx;

    public void update(float delta) {
        Input.listen();
        sfx.update(delta);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
