package com.pgsv.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pgsv.game.MyGdxGame;
import com.pgsv.game.utils.C;
import com.pgsv.game.utils.Fader;
import com.pgsv.game.utils.Input;
import com.pgsv.game.utils.Media;

public class MainMenu extends Screen {

    private MyGdxGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Fader fader;

    private Animation<TextureRegion> titleAnimation;

    private Texture background;
    private Texture menuBox;
    private Texture mouseCursor;
    private Texture cursor;

    private Rectangle[] options;

    private Vector2 mouse;

    private float backGroundDelta;
    private float menuOff;
    private float speed;
    private float titleDelta;

    private int menu;
    private int current;

    private boolean chosen;

    public MainMenu(MyGdxGame game, SpriteBatch batch) {
        super();

        this.game = game;
        this.batch = batch;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 256, 144);
        this.fader = new Fader(camera);

        this.background = new Texture(Gdx.files.internal(C.PATH + "ui/mainMenuBackground2.png"));
        this.menuBox = Media.loadTexture("ui/board.png");
        this.mouseCursor = Media.loadTexture("ui/mouseCursor.png");
        this.cursor = Media.loadTexture("ui/cursor.png");

        this.mouse = new Vector2();

        this.backGroundDelta = 0f;
        this.menuOff = 220f;
        this.speed = 0f;

        this.current = 0;
        this.menu = 0;

        this.chosen = false;

        this.titleAnimation = Media.createAnimation(
                Media.loadTexture("ui/title.png"), 0.1f,
                new Vector2(1, 11), Vector2.Zero,
                new Vector2(101, 12), Vector2.Zero);

        this.options = new Rectangle[3];
        this.options[0] = new Rectangle(95, 88, 72, 8);
        this.options[1] = new Rectangle(95, 69, 72, 8);
        this.options[2] = new Rectangle(95, 50, 72, 8);
    }

    public void update(float delta) {
        super.update(delta);

        this.mouse = Input.getMouse();

        if (!this.chosen) {
            if (this.backGroundDelta > -40f) {
                this.speed += delta * 30f;
                this.backGroundDelta -= delta * this.speed;
                if (this.backGroundDelta <= -40f) {
                    this.backGroundDelta = -40f;
                    this.menu = 1;
                }
            }

            if (this.menu == 1) {
                if (this.menuOff > 0) {
                    this.menuOff -= delta * 60f;
                    if (this.menuOff < 0) {
                        this.menuOff = 0;
                        this.menu = 2;
                    }
                }
            }

            if (this.menu == 2) {
                if (Input.isKeyJustPressed(Input.UP)) {
                    this.current--;
                    if (this.current < 0) this.current = 0;
                } else if (Input.isKeyJustPressed(Input.DOWN)) {
                    this.current++;
                    if (this.current > 2) this.current = 2;
                }

                if (Input.isKeyJustPressed(Input.ENTER) ||
                        Input.isKeyJustPressed(Input.Z) ||
                        Input.isKeyJustPressed(Input.SPACE)) {
                    if (this.current != 1)
                        chosen = true;
                }
                for (int i = 0; i < 3; i++) {
                    if (new Rectangle(mouse.x, mouse.y, 2, 2).overlaps(options[i])) {
                        this.current = i;
                        if (Input.isTouched()) {
                            if (this.current != 1)
                                chosen = true;
                        }
                    }
                }

            }
        } else {
            this.titleDelta += delta;

            this.fader.update(delta);

            if (this.titleAnimation.isAnimationFinished(this.titleDelta) &&
                    fader.getState() == Fader.NONE)
                fader.fadeOut();

            if (this.fader.getState() == Fader.BLACK) {
                this.choose();
            }
        }

    }

    public void choose() {
        this.dispose();
        if (this.current == 0)
            this.game.setScreen(new TestStage(this.game, batch));
        if (this.current == 2)
            Gdx.app.exit();

    }

    public void draw() {
        batch.draw(background, 0, this.backGroundDelta);

        if (this.menu > 0) {
            batch.draw(menuBox, 66 + menuOff, 40);
            batch.draw(this.titleAnimation.getKeyFrame(titleDelta, false), 73 + menuOff, 108);
        }

        if (this.menu == 2) {
            switch (this.current) {
                case 0:
                    batch.draw(cursor, 74, 88);
                    break;
                case 1:
                    batch.draw(cursor, 74, 69);
                    break;
                case 2:
                    batch.draw(cursor, 74, 50);
                    break;
            }
        }

        this.batch.draw(mouseCursor, mouse.x - 1, mouse.y - 12);

        if (this.chosen)
            this.fader.draw(batch);
    }

    @Override
    public void dispose() {
        this.background.dispose();
    }

    @Override
    public void render(float delta) {
        update(delta);
        this.camera.update();

        this.batch.begin();
        batch.setProjectionMatrix(camera.combined);
        draw();
        this.batch.end();
    }


    @Override
    public void show() {
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

}
