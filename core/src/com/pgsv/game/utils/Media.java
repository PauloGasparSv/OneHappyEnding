package com.pgsv.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

public class Media {

    public static HashMap<String, Texture> textures = new HashMap<String, Texture>();


    public static Texture loadTexture(String path) {
        if (textures.containsKey(path)) return textures.get(path);
        Texture asset = new Texture(Gdx.files.internal(C.PATH + path));
        textures.put(path, asset);
        return asset;
    }

    public static Animation<TextureRegion> createAnimation(Texture texture, float speed,
                                                           Vector2 numFrames, Vector2 start,
                                                           Vector2 dimension, Vector2 offset) {
        return new Animation<TextureRegion>(speed, getFrames(texture, numFrames, start,
                dimension, offset));
    }

    public static TextureRegion[] getFrames(Texture texture, Vector2 numFrames, Vector2 start,
                                            Vector2 dimension, Vector2 offset) {
        TextureRegion[] frames = new TextureRegion[(int) (numFrames.x * numFrames.y)];

        int counter = 0;
        for (int row = 0; row < numFrames.y; row++) {
            for (int col = 0; col < numFrames.x; col++) {
                frames[counter] = new TextureRegion(texture,
                        (int) start.x + ((int) dimension.x + (int) offset.x) * col,
                        (int) start.y + ((int) dimension.y + (int) offset.y) * row,
                        (int) dimension.x, (int) dimension.y);

                counter++;
            }
        }

        return frames;
    }

    //Todo: Add file handling methods

}
