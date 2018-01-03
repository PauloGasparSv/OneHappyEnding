package com.pgsv.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pgsv.game.MyGdxGame;
import com.pgsv.game.actors.BaddieBuilder;
import com.pgsv.game.actors.CoinManager;
import com.pgsv.game.actors.FirstBoss;
import com.pgsv.game.actors.Player;
import com.pgsv.game.utils.C;
import com.pgsv.game.utils.Media;
import com.pgsv.game.utils.Text;

import javax.swing.JOptionPane;

public class TestStage extends Screen {
    private final int GAME = 0, TILEMAP = 1, ITENS = 3, BADDIES = 4;

    private MyGdxGame game;

    private Map map;

    private CoinManager coins;
    private BaddieBuilder baddies;
    private Player player;
    private Text text;
    private FirstBoss boss;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Music theme;
    private Sound coinSound;

    private Texture[] water;
    private Texture titleCard, coin, sparkle, background, tiles, black, allBlack,
            wall, bossTexture, puff, princess;

    private Animation<TextureRegion> waterAnimation;
    private Animation<TextureRegion> princessAnimation;

    private TextureRegion cloud;

    private String bossText;

    private float intro, firstFade, off, offy, waterDelta, fadeDelta, wallDelta, princessDelta;

    private float[] cloudX;

    private int currentState, state, fadeState;

    private long bossTextLong;

    private boolean show;
    private boolean shown;
    private boolean endStage;

    private float endStageAlpha;


    public TestStage(MyGdxGame game, SpriteBatch batch) {
        super();
        this.game = game;

        this.batch = batch;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 256, 144);

        this.text = new Text();

        this.tiles = Media.loadTexture("stages/1/tiles.png");
        this.background = Media.loadTexture("stages/1/back.png");
        this.titleCard = Media.loadTexture("stages/1/titleCard.png");
        this.coin = Media.loadTexture("stages/coin.png");
        this.sparkle = Media.loadTexture("stages/sparkle.png");
        this.black = Media.loadTexture("ui/fade.png");
        this.allBlack = Media.loadTexture("ui/black.png");
        this.wall = Media.loadTexture("stages/1/wall.png");
        this.bossTexture = Media.loadTexture("Actors/baddies/boss1.png");
        this.puff = Media.loadTexture("stages/puff_alpha.png");
        this.princess = Media.loadTexture("Actors/Princess/princess.png");

        TextureRegion[] tilesRegion = new TextureRegion[3 * 11];
        int current = 0;
        for (int line = 0; line < 11; line++) {
            for (int col = 0; col < 3; col++) {
                tilesRegion[current] = new TextureRegion(this.tiles, col * 16, line * 16, 16, 16);
                current++;
            }
        }

        TextureRegion[] framesPrincesa = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            framesPrincesa[i] = new TextureRegion(princess, i * 16, 0, 16, 24);
        }
        princessAnimation = new Animation<TextureRegion>(0.24f, framesPrincesa);
        princessDelta = 0f;

        this.theme = Gdx.audio.newMusic(Gdx.files.internal(C.PATH + "Music/The Adventure Begins 8-bit remix.ogg"));
        //this.theme.play();
        this.theme.setVolume(0.25f);
        this.theme.setLooping(true);

        this.coinSound = Gdx.audio.newSound(Gdx.files.internal(C.PATH + "Sfx/Pickup_Coin.wav"));

        this.water = new Texture[2];
        this.water[0] = new Texture(Gdx.files.internal(C.PATH + "stages/1/back_river_1.png"));
        this.water[1] = new Texture(Gdx.files.internal(C.PATH + "stages/1/back_river_2.png"));

        this.cloud = new TextureRegion(tiles, 32, 0, 16, 16);
        this.cloudX = new float[3];
        for (int i = 0; i < 3; i++) cloudX[i] = -200;

        TextureRegion[] waterRegion = new TextureRegion[2];
        waterRegion[0] = new TextureRegion(this.water[0]);
        waterRegion[1] = new TextureRegion(this.water[1]);

        this.waterAnimation = new Animation<TextureRegion>(1, waterRegion);

        this.off = 0;
        this.offy = 0;

        this.waterDelta = 0f;

        this.map = new Map("testMap", tilesRegion);
        int[] solids = {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
        this.map.setSolids(solids);

        this.player = new Player(-10f, 47f, map, camera);

        this.coins = new CoinManager(player, coin, sparkle, coinSound, camera);
        this.coins.loadCoins("testMapCoins.broc");

        this.baddies = new BaddieBuilder(player, camera, map, text);
        this.baddies.loadBaddies("testMapBaddies.broc");

        this.intro = 0;
        C.DEBUG = false;

        this.fadeState = 2;
        this.fadeDelta = 60;
        this.wallDelta = 0;

        this.boss = new FirstBoss(2190, 63, map, camera, player, this.bossTexture, this.puff);

        this.bossText = "";
        this.bossTextLong = System.currentTimeMillis();
        this.show = true;
        this.shown = false;

        this.endStage = false;
        this.endStageAlpha = 0f;
        this.firstFade = 1;

        this.state = -1;
        this.camera.zoom = 0.6f;
        this.camera.position.x = 78;
        this.player.ground(47f);
        this.player.cannotControl();
        this.player.changeState(this.player.WALK);


    }

    public void update(float delta) {
        super.update(delta);
        delta *= C.TIME;

        if (this.firstFade > 0)
            this.firstFade -= delta;

        if (state == -1) {
            if (this.player.getState() == this.player.WALK)
                this.player.position.x += delta * 40f;

            if (this.player.position.x > 58f && this.player.getState() != this.player.IDLE)
                this.player.changeState(this.player.IDLE);

            if (this.camera.zoom < 1 && this.player.getState() == this.player.IDLE) {
                this.camera.zoom += delta * 0.2f;
                this.camera.position.x += delta * 25f;
                if (this.camera.zoom > 1) {
                    this.camera.zoom = 1;
                    this.state = 0;
                    this.player.canControl();
                }
            }
        } else if (state == 0) {
            if (this.player.position.x > 2110)
                this.state = 1;
        }

        //0 = BLACK
        //1 = FROM BLACK TO OK
        //2 = OK
        //3 = FROM OK TO BLACK

        if (this.fadeState == 1) {
            this.fadeDelta += delta * 40f + this.fadeDelta * delta;
            if (this.fadeDelta > 60f) {
                this.fadeDelta = 60f;
                this.fadeState = 2;
            }
        } else if (this.fadeState == 3) {
            this.fadeDelta -= delta * 50f + this.fadeDelta * delta;

            if (this.fadeDelta < 1) {
                this.fadeDelta = 1;
                this.fadeState = 1;

                if (this.currentState == 0) {
                    player.respawn(40f, 112f);
                } else if (this.currentState == 1) {
                    player.respawn(1100f, 142f);
                } else if (this.currentState == 2) {
                    player.respawn(2024f, 142f);
                }
                this.boss.init();
                this.wallDelta = 0;
                this.state = 0;
                this.bossText = "";
                this.bossTextLong = System.currentTimeMillis();
                this.show = true;
                this.shown = false;
                this.player.fallParachute();
            }
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            C.DEBUG = true;
            if (this.currentState == TILEMAP) {
                C.DEBUG = false;
                this.currentState = GAME;
            } else this.currentState = TILEMAP;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            C.DEBUG = true;
            if (this.currentState == ITENS) {
                C.DEBUG = false;
                this.currentState = GAME;
            } else this.currentState = ITENS;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
            C.DEBUG = true;
            if (this.currentState == BADDIES) {
                C.DEBUG = false;
                this.currentState = GAME;
            } else this.currentState = BADDIES;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            this.player.die();
        }

        if (C.DEBUG) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
                this.map.saveMap();
                this.coins.saveCoins("testMapCoins.broc");
                this.baddies.saveBaddies("testMapBaddies.broc");
            }
            if (this.currentState == TILEMAP)
                this.map.editMode(camera);
            else if (this.currentState == ITENS)
                this.coins.debugMode(delta);
            else if (this.currentState == BADDIES)
                this.baddies.debugMode(delta);
        } else {
            this.player.update(delta);
        }

        if (C.TIME < 1) {
            C.TIME += delta / C.TIME;
            if (C.TIME > 1) C.TIME = 1;
        }
        if (C.TIME > 1) {
            C.TIME -= delta / C.TIME;
            if (C.TIME < 1) C.TIME = 1;
        }

        this.intro += delta * 55 + delta * this.intro / 3f;

        this.baddies.update(delta);

        this.coins.update(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            fadeDelta += delta;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            fadeDelta -= delta;

        if (!C.DEBUG && !this.player.isDead() && this.state > -1) {
            if (this.player.position.x > this.camera.position.x + 4f)
                this.camera.position.x = this.player.position.x - 4f;
            else if (this.player.position.x < this.camera.position.x - 24f)
                this.camera.position.x = this.player.position.x + 24f;
            if (this.player.position.y > this.camera.position.y + 14)
                this.camera.position.y = this.player.position.y - 14;
            else if (this.player.position.y < this.camera.position.y - 36)
                this.camera.position.y = this.player.position.y + 36;
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.D))
                this.camera.position.x += delta * 120f;
            if (Gdx.input.isKeyPressed(Input.Keys.A))
                this.camera.position.x -= delta * 120f;
            if (Gdx.input.isKeyPressed(Input.Keys.W))
                this.camera.position.y += delta * 120f;
            if (Gdx.input.isKeyPressed(Input.Keys.S))
                this.camera.position.y -= delta * 120f;
        }

        if (this.camera.position.x < 128 && this.currentState < 2 && this.state > -1) {
            this.camera.position.x = 128;
        }

        if (camera.position.y < 72 && this.state > -1)
            camera.position.y = 72;
        if (camera.position.x > 2100) {
            if (this.currentState < 2) {
                this.currentState = 2;
            }
            camera.position.x = 2100;
        }

        if (this.state > 0) {
            if (this.wallDelta < 94) {
                this.wallDelta += delta * 34f;
            } else {
                this.wallDelta = 94;
            }

            if (this.camera.position.x < 2100)
                this.camera.position.x = 2100;
            if (player.position.x > 2200)
                player.position.x = 2200;
            if (player.position.x < 1984)
                player.position.x = 1984;

            boss.update(delta);

            if (this.boss.isDead() && !this.endStage) {
                this.endStage = true;
                this.endStageAlpha = 0;
            }

            if (!this.shown) {
                if (this.show) {
                    if (System.currentTimeMillis() - bossTextLong > 50) {
                        if (this.bossText.length() != 23) {
                            this.bossText = "Prepare to fight shame!".substring(0, this.bossText.length() + 1);
                        } else {
                            this.show = false;
                        }
                        this.bossTextLong = System.currentTimeMillis();
                    }
                } else {
                    if (this.bossText.length() == 23) {
                        if (System.currentTimeMillis() - this.bossTextLong > 1200) {
                            this.bossText = "Prepare to fight shame";
                            this.bossTextLong = System.currentTimeMillis();
                        }
                    } else {
                        if (System.currentTimeMillis() - bossTextLong > 50) {
                            this.bossText = "Prepare to fight shame!".substring(0, this.bossText.length() - 1);
                            this.bossTextLong = System.currentTimeMillis();
                            if (this.bossText.length() == 0) {
                                this.shown = true;
                            }
                        }
                    }

                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            this.boss.die();

        if (this.endStage) {
            this.princessDelta += delta;
            this.endStageAlpha += delta * 0.1f;
            if (this.endStageAlpha > 1)
                this.endStageAlpha = 1;
        }

        float camX = this.camera.position.x - 128;
        float camY = this.camera.position.y - 72;

        if (this.player.position.x < -10)
            this.player.position.x = -10;

        if (this.player.position.y < camY - 80f) {
            if (!player.isDead()) {
                player.position.y = camY - 24f;
                player.die();
            } else if (fadeState != 3) {
                this.fadeState = 3;
                this.fadeDelta = 60f;
            }
        }

        if (player.position.x > 980 && this.currentState == 0) {
            this.currentState = 1;
        }

        //if(player.position <)

        this.off = camX / 1.2f;
        this.offy = camY / 1.2f + 16;

        if (offy > 94) offy = camY;

        waterDelta += delta;
    }

    public void draw() {
        Gdx.gl.glClearColor(0.18f, 0.29f, 0.26f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        batch.draw(this.background, off, offy);
        batch.draw(this.waterAnimation.getKeyFrame(waterDelta, true), off, offy + 17);
        batch.draw(this.background, this.background.getWidth() + off, offy);
        batch.draw(this.waterAnimation.getKeyFrame(waterDelta, true), this.background.getWidth() + off, offy + 17);
        batch.draw(this.background, this.background.getWidth() * 2 + off, offy);
        batch.draw(this.waterAnimation.getKeyFrame(waterDelta, true), this.background.getWidth() * 2 + off, offy + 17);
        batch.draw(this.background, this.background.getWidth() * 3 + off, offy);
        batch.draw(this.waterAnimation.getKeyFrame(waterDelta, true), this.background.getWidth() * 3 + off, offy + 17);

        for (int i = 1; i < 12; i++) {
            batch.draw(this.cloud, off + i * (i % 2 == 0 ? 200 : 70), 96 + offy + (i % 2 == 0 ? 7 : 2));
        }

        if (this.state > 0) {
            batch.draw(this.wall, 1972, -64 + wallDelta);
            batch.draw(this.wall, 2212, -64 + wallDelta);
            this.boss.draw(batch);
        }

        this.map.draw(camera, batch);


        this.baddies.draw(batch);
        this.coins.draw(batch);

        this.player.draw(this.batch);

        if (this.state > 0) this.text.draw(batch, this.bossText, 2008, 120);

        if (C.DEBUG) {
            this.text.draw(batch, (int) camera.position.x + " ; " + (int) camera.position.y, camera.position.x + 42, camera.position.y + 62);

            if (this.currentState == TILEMAP) {
                this.map.drawEditMode(batch);
                this.text.draw(batch, "tile editor", camera.position.x - 128, camera.position.y + 62);
            } else if (this.currentState == ITENS) {
                this.coins.debugModeDraw(batch);
                this.text.draw(batch, "Coins editor", camera.position.x - 128, camera.position.y + 62);
                this.text.draw(batch, this.coins.numCoins() + " coins", camera.position.x - 128, camera.position.y + 50);
            } else if (this.currentState == BADDIES) {
                this.baddies.debugModeDraw(batch);
                this.text.draw(batch, "Baddies editor", camera.position.x - 128, camera.position.y + 62);
            }
        }

        if (this.intro < 2000) {
            if (this.intro < 280) {
                batch.draw(this.titleCard, camera.position.x + 160 - this.intro, camera.position.y + 24);
            } else if (this.intro < 600) {
                batch.draw(this.titleCard, camera.position.x + 160 - 280, camera.position.y + 24);
            } else {
                batch.draw(this.titleCard, camera.position.x + 160 + 320 - this.intro, camera.position.y + 24);
            }
        }

        if (this.fadeState == 0) {
            batch.draw(allBlack, camera.position.x - 128, camera.position.y - 72);
        } else if (this.fadeState != 2) {
            batch.draw(black, camera.position.x - 128 - 128 * (fadeDelta - 1), camera.position.y - 72 - 72 * (fadeDelta - 1), 256 * fadeDelta, 144 * fadeDelta);
        }

        if (this.endStage) {
            float y = 170 - princessDelta * 24f;
            if (y < 63) y = 63;
            batch.draw(princessAnimation.getKeyFrame(princessDelta, true), 2092, y);

            batch.setColor(1, 1, 1, endStageAlpha);
            batch.draw(allBlack, camera.position.x - 128, camera.position.y - 72);
            batch.setColor(1, 1, 1, 1);

            if (endStageAlpha > 0.9f) {
                this.text.draw(batch, "Thanks for playing!", 2018, 110);
            }
        }
        if (this.firstFade > 0) {
            batch.setColor(1, 1, 1, firstFade);
            batch.draw(allBlack, camera.position.x - 128, camera.position.y - 72);
            batch.setColor(1, 1, 1, 1);
        }

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
    public void dispose() {
        JOptionPane.showMessageDialog(null, "DISPOSE ME");
        this.text.dispose();
        this.player.dispose();
        this.background.dispose();
        this.black.dispose();
        this.allBlack.dispose();
        this.bossTexture.dispose();
        this.wall.dispose();
        this.tiles.dispose();
        this.water[0].dispose();
        this.water[1].dispose();
        this.baddies.dispose();
        this.coin.dispose();
        this.titleCard.dispose();
        this.coinSound.dispose();
        this.batch.dispose();
        this.sparkle.dispose();
        this.puff.dispose();
        this.princess.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
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
