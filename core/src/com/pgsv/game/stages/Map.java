package com.pgsv.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pgsv.game.utils.C;
import com.pgsv.game.utils.Input;
import com.pgsv.game.utils.Media;

import javax.swing.JOptionPane;

public class Map {

    //Todo: Have this guy handle all the actors and itens

    private TextureRegion[] tiles;
    private TextureRegion mouseCursor;
    private Vector2 preview;
    private Vector2 mouse;
    private String path;

    public int[][] map;
    private int[] solids;
    private int ribbon;
    private long lastTouched;

    public Map(String path, TextureRegion[] tiles) {
        this.tiles = tiles;
        this.ribbon = 0;
        this.solids = new int[0];
        this.lastTouched = System.currentTimeMillis();
        this.path = path + ".broc";
        this.preview = new Vector2();
        this.mouse = new Vector2();
        this.mouseCursor = new TextureRegion(Media.loadTexture("ui/mouseCursor.png"));
        this.loadMap();
    }

    public void setSolids(int[] solids) {
        this.solids = solids;
    }

    public boolean isSolid(int tile) {
        if (tile == 0) return false;
        for (int i : solids) if (tile == i) return true;
        return false;
    }

    public boolean isSolid(Vector3 tile) {
        return this.isSolid((int)tile.z);
    }

    public void changeTile(int x, int y) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > map[0].length - 1) x = map[0].length - 1;
        if (y > map.length - 1) y = map.length - 1;

        this.map[y][x]++;
        if (this.map[y][x] > tiles.length) this.map[y][x] = 0;

    }

    public void changeTile(int x, int y, int tile) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > map[0].length - 1) x = map[0].length - 1;
        if (y > map.length - 1) y = map.length - 1;

        this.map[y][x] = tile;
    }

    public void editMode(OrthographicCamera camera) {

        float xDelta  = Input.getX() / (float)Gdx.graphics.getWidth();
        float yDelta = (Gdx.graphics.getHeight() - Input.getY()) / (float) Gdx.graphics.getHeight();

        int x = (int) (xDelta * 256 + camera.position.x - 128);
        int y = (int) (yDelta * 144 + camera.position.y - 72);

        this.mouse.x = x + 1;
        this.mouse.y = y - 14;

        this.preview.x = x / 16;
        this.preview.y = y / 16;
        
        if (Input.isKeyPressed(Input.CONTROL_LEFT) && Input.isKeyPressed(Input.C)) {
            ribbon = getTile(x, y);
        }

        if (Input.isJustTouched(1)) {
            lastTouched = System.currentTimeMillis();
            changeTile(x / 16, y / 16);
        }
        if (Input.isTouched(0)) {
            changeTile(x / 16, y / 16, ribbon);
        }

    }

    public void drawEditMode(SpriteBatch batch) {
        batch.draw(mouseCursor, mouse.x, mouse.y);
        if (this.ribbon > 0) {
            batch.setColor(new Color(0.2f, 0.2f, 1f, 0.7f));
            batch.draw(this.tiles[this.ribbon - 1], preview.x * 16, preview.y * 16);
            batch.setColor(new Color(1, 1, 1, 1));
        }
    }

    public int getTile(float x, float y) {
        int tileX = (int) (x / 16);
        int tileY = (int) (y / 16);

        if (tileX > map[0].length - 1 || tileY > map.length - 1 || tileX < 0 || tileY < 0) return 0;

        return map[tileY][tileX];
    }

    public Vector3 getTileVector(float x, float y) {
        Vector3 info = new Vector3();
        int tileX = (int) (x / 16);
        int tileY = (int) (y / 16);

        info.x = tileX * 16;
        info.y = tileY * 16 + 16;

        if (tileX > map[0].length - 1 || tileY > map.length - 1 || tileX < 0 || tileY < 0)
            info.z = 0;
        else info.z = map[tileY][tileX];
        return info;
    }

    public void draw(OrthographicCamera camera, SpriteBatch batch) {
        float w = 256;
        float h = 144;
        float cameraX = camera.position.x - w / 2f;
        float cameraY = camera.position.y - h / 2f;

        int left = (int) (cameraX / 16f);
        int right = (int) ((cameraX + w) / 16f) + 1;
        int bottom = (int) (cameraY / 16f);
        int top = (int) ((cameraY + h) / 16f) + 1;

        if (left < 0) left = 0;
        if (right > map[0].length - 1) right = map[0].length - 1;
        if (bottom < 0) bottom = 0;
        if (top > map.length - 1) top = map.length - 1;
        for (int line = bottom; line <= top; line++) {
            for (int col = left; col <= right; col++) {
                int tile = this.map[line][col];
                if (tile == 0) continue;
                batch.draw(this.tiles[tile - 1], col * 16, line * 16);
            }
        }
    }

    public void saveMap() {
        String mapFile = map[0].length + " " + map.length;

        for (int line = 0; line < map.length; line++) {
            mapFile += "\n";
            for (int col = 0; col < map[0].length; col++) {
                mapFile += map[line][col] + " ";
            }
        }

        FileHandle file = Gdx.files.local("maps/" + path);
        file.writeString(mapFile, false);
        JOptionPane.showMessageDialog(null, "MAP SAVED");
    }

    public void loadMap() {
        FileHandle mapFile = Gdx.files.internal(C.PATH + "maps/" + this.path);
        String mapText = mapFile.readString();

        String[] lines = mapText.split("\n");
        String[] temp = lines[0].split("\\s");

        int w = Integer.parseInt(temp[0]);
        int h = Integer.parseInt(temp[1]);

        this.map = new int[h][w];

        for (int line = 1; line < lines.length; line++) {
            temp = lines[line].split(" ");
            for (int col = 0; col < w; col++) {
                this.map[line - 1][col] = Integer.parseInt(temp[col]);
            }
        }
    }

}
