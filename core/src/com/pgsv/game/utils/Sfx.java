package com.pgsv.game.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Fader {

    public static final int NONE = 0, BLACK = 1, FADE_IN = 2, FADE_OUT = 3;

    private TextureRegion black;

    private OrthographicCamera camera;

    private float alpha;
    private float speed;

    private int state;

    public Fader(OrthographicCamera camera) {
        this.black = new TextureRegion(Media.loadTexture("ui/black.png"));
        this.state = NONE;
        this.alpha = 0f;
        this.camera = camera;
        this.speed = 1;
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
        if (this.state == FADE_IN) {
            this.alpha -= delta * speed;
            if (this.alpha <= 0)
                changeState(NONE);
        } else if (this.state == FADE_OUT) {
            this.alpha += delta * speed;
            if (this.alpha >= 1)
                changeState(BLACK);
        }
    }

    public void changeState(int state) {
        if (state == NONE) {
            this.alpha = 0f;
            this.state = NONE;
        } else if (state == BLACK) {
            this.alpha = 1f;
            this.state = BLACK;
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
