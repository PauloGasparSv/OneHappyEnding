package com.pgsv.game.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sfx {

    public static final int NONE = 0, BLACK = 1, FADE_IN = 2, FADE_OUT = 3;

    private TextureRegion black;

    private OrthographicCamera camera;

    private float alpha;
    private float speed;

    private int state;

    private boolean shaking;

    public Sfx(OrthographicCamera camera) {
        this.black = new TextureRegion(Media.loadTexture("ui/black.png"));
        this.state = NONE;
        this.alpha = 0f;
        this.camera = camera;
        this.speed = 1;
        this.shaking = false;
    }

    public void shake(){
        shaking = true;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void fadeOut() {
        alpha = 0f;
        state = FADE_OUT;
    }

    public void fadeIn() {
        alpha = 1f;
        state = FADE_IN;
    }

    public void update(float delta) {
        if (state == FADE_IN) {
            alpha -= delta * speed;
            if (alpha <= 0)
                changeState(NONE);
        } else if (state == FADE_OUT) {
            alpha += delta * speed;
            if (alpha >= 1)
                changeState(BLACK);
        }
    }

    public void changeState(int state) {
        if (state == NONE) {
            alpha = 0f;
            state = NONE;
        } else if (state == BLACK) {
            alpha = 1f;
            state = BLACK;
        } else if (state == FADE_IN) {
            fadeIn();
        } else if (state == FADE_OUT) {
            fadeOut();
        }
    }

    public int getState(){
        return state;
    }

    public void draw(SpriteBatch batch) {
        if (state != NONE) {
            batch.setColor(1, 1, 1, alpha);
            batch.draw(black, camera.position.x - C.HALF_WIDTH,
                    camera.position.y - C.HALF_HEIGHT);
            batch.setColor(1, 1, 1, 1);
        }

    }


}
