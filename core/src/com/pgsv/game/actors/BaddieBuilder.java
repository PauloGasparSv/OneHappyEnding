package com.pgsv.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pgsv.game.stages.Map;
import com.pgsv.game.utils.C;
import com.pgsv.game.utils.Media;
import com.pgsv.game.utils.Text;

import java.util.LinkedList;

import javax.swing.JOptionPane;

public class BaddieBuilder {
    private final int SPIKY = 0, SPIKY_SMART = 1, BALLY = 2;

    private int currentBaddie;

    private LinkedList<Actor> baddies;

    private Animation<TextureRegion> spikyWalkAnimation;
    private Animation<TextureRegion> spikyIdleAnimation;
    private Animation<TextureRegion> spikyDeathAnimation;
    private Animation<TextureRegion>[] spikyAnimations;
    private Animation<TextureRegion> ballyAnimation;
    private Animation<TextureRegion> exclamationAnimation;

    private TextureRegion[] baddiePreview;

    private Player player;

    private Text text;

    private Map map;

    private OrthographicCamera camera;

    private Vector2 preview;

    private boolean mousePressed;

    private boolean right;

    private LinkedList<Actor> remove;

    public BaddieBuilder(Player player, OrthographicCamera camera, Map map, Text text) {
        this.baddies = new LinkedList<Actor>();
        this.player = player;
        this.camera = camera;
        this.mousePressed = false;
        this.map = map;
        this.text = text;
        this.currentBaddie = 0;
        this.preview = new Vector2();
        this.remove = new LinkedList<Actor>();

        this.setup();

        this.right = false;
    }

    @SuppressWarnings("unchecked")
    private void setup() {
        this.baddiePreview = new TextureRegion[3];

        this.spikyWalkAnimation = Media.createAnimation(Media.loadTexture("Actors/baddies/bullseye.png"), 0.15f,
                new Vector2(6, 1), new Vector2(0, 0),
                new Vector2(16, 13), new Vector2(0, 0));

        this.spikyIdleAnimation = Media.createAnimation(Media.loadTexture("Actors/baddies/bullseye.png"), 0.18f,
                new Vector2(4, 1), new Vector2(0, 13),
                new Vector2(16, 13), new Vector2(0, 0));

        this.spikyDeathAnimation = Media.createAnimation(Media.loadTexture("Actors/baddies/spikyDead.png"), 0.04f,
                new Vector2(2, 1), new Vector2(0, 0),
                new Vector2(16, 8), new Vector2(0, 0));

        this.ballyAnimation = Media.createAnimation(Media.loadTexture("Actors/baddies/bally14.png"), 0.12f,
                new Vector2(6, 1), new Vector2(0, 0),
                new Vector2(14, 14), new Vector2(0, 0));

        this.exclamationAnimation = Media.createAnimation(Media.loadTexture("stages/exclamation.png"), 0.072f,
                new Vector2(6, 1), new Vector2(0, 0),
                new Vector2(16, 14), new Vector2(0, 0));

        this.baddiePreview[BALLY] = ballyAnimation.getKeyFrame(0);
        this.baddiePreview[SPIKY] = spikyWalkAnimation.getKeyFrame(0);
        this.baddiePreview[SPIKY_SMART] = spikyWalkAnimation.getKeyFrame(0);

        this.spikyAnimations = new Animation[4];
        this.spikyAnimations[0] = this.spikyIdleAnimation;
        this.spikyAnimations[1] = this.spikyWalkAnimation;
        this.spikyAnimations[2] = this.spikyDeathAnimation;
        this.spikyAnimations[3] = this.exclamationAnimation;
    }


    public void update(float delta) {
        for (Actor a : baddies) {
            a.update(delta);
            if (a.isDead()) this.remove.add(a);
        }

        if (!this.remove.isEmpty()) {
            for (Actor a : this.remove) this.baddies.remove(a);
            this.remove.clear();
        }

    }

    public void draw(SpriteBatch batch) {
        for (Actor a : baddies) {
            a.draw(batch);
        }
    }

    public void debugMode(float delta) {
        int x = (int) (Gdx.input.getX() * 0.2f + camera.position.x - camera.viewportWidth / 2f);
        int y = (int) (144 - Gdx.input.getY() * 0.2f + camera.position.y - camera.viewportHeight / 2f);

        this.preview.x = (x / 16) * 16;
        this.preview.y = (y / 16) * 16;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (!this.right) {
                this.right = true;
            } else {
                this.right = false;
                this.currentBaddie++;
                if (this.currentBaddie > 2) this.currentBaddie = 0;
            }
        }

        if (Gdx.input.isButtonPressed(0)) {
            if (!this.mousePressed) {
                this.mousePressed = true;

                boolean ok = true;
                for (Actor b : baddies) {
                    if (preview.x > b.position.x - 6 && preview.x < b.position.x + 6 &&
                            preview.y > b.position.y - 6 && preview.y < b.position.y + 6) {
                        ok = false;
                        return;
                    }
                }
                if (ok)
                    this.addBaddie(preview.x, preview.y);
            }
        } else if (Gdx.input.isButtonPressed(1)) {
            if (!this.mousePressed) {
                LinkedList<Actor> del = new LinkedList<Actor>();
                for (Actor b : baddies) {
                    if (preview.x > b.position.x - 6 && preview.x < b.position.x + 6 &&
                            preview.y > b.position.y - 6 && preview.y < b.position.y + 6) {
                        del.add(b);
                    }
                }
                for (Actor b : del) {
                    baddies.remove(b);
                }
            }
        } else {
            this.mousePressed = false;
        }
    }

    public void addBaddie(float x, float y) {
        switch (this.currentBaddie) {
            case SPIKY:
                this.baddies.add(new Spiky(x, y, this.right, false, this.spikyAnimations, map, player, camera));
                break;
            case SPIKY_SMART:
                this.baddies.add(new Spiky(x, y, this.right, true, this.spikyAnimations, map, player, camera));
                break;
            case BALLY:
                this.baddies.add(new Bally(x, y, this.right, map, camera, player, this.ballyAnimation));
                break;
        }
    }

    public void addBaddie(int bad, float x, float y, boolean right, boolean special) {
        switch (bad) {
            case 2:
                this.baddies.add(new Spiky(x, y, right, special, this.spikyAnimations, map, player, camera));
                break;
            case 4:
                this.baddies.add(new Bally(x, y, right, map, camera, player, this.ballyAnimation));
                break;
        }
    }

    public void debugModeDraw(SpriteBatch batch) {
        batch.setColor(new Color(0.2f, 0.2f, 1f, 0.7f));
        text.draw(batch, this.getDesc(), preview.x, preview.y + 12);
        if (this.baddiePreview[this.currentBaddie].isFlipX() != this.right) {
            this.baddiePreview[this.currentBaddie].flip(true, false);
        }
        batch.draw(this.baddiePreview[this.currentBaddie], preview.x, preview.y);
        batch.setColor(new Color(1, 1, 1, 1));
    }

    public String getDesc() {
        switch (this.currentBaddie) {
            case SPIKY:
                return "Spiky";
            case SPIKY_SMART:
                return "Smart Spiky";
            case BALLY:
                return "Bally";
        }
        return "None";
    }


    public void loadBaddies(String path) {
        FileHandle badsFile = Gdx.files.internal(C.ROOT + "maps/" + path);
        String badsText = badsFile.readString();

        String[] lines = badsText.split("\n");

        for (String line : lines) {
            String[] temp = line.split("\\s");
            this.addBaddie(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]),
                    Integer.parseInt(temp[2]), temp[3].equals("1"), temp[4].equals("1"));
        }
    }

    public void saveBaddies(String path) {
        String badsFile = "";

        for (Actor a : baddies) {
            badsFile += a.getId() + " " + (int) (a.position.x) + " " + (int) (a.position.y) + " " + (a.right ? "1" : "0") + " " + (a.special ? "1" : "0") + "\n";
        }

        FileHandle file = Gdx.files.absolute("C:/temp/" + path);
        file.writeString(badsFile, false);
        JOptionPane.showMessageDialog(null, "BADDIES SAVED");
    }

    public void dispose() {
    }

}
