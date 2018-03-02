package com.pgsv.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.LinkedList;

public class Media {

    public static HashMap<String, Texture> textures = new HashMap<String, Texture>();


    public static Texture loadTexture(String path) {
        if (textures.containsKey(path)) return textures.get(path);
        Texture asset = new Texture(Gdx.files.internal(C.ROOT + path));
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
                frames[counter] = getFrame(texture,
                    start.x + (dimension.x +  offset.x) * col,
                    start.y + ( dimension.y + offset.y) * row,
                     dimension.x, dimension.y);
                counter++;
            }
        }

        return frames;
    }

    public static TextureRegion getFrame(String path,float x, float y, float w, float h){
        return getFrame(Media.loadTexture(path), x, y, w, h);
    }

    public static TextureRegion getFrame(Texture texture, float x, float y, float w, float h){
        return new TextureRegion(texture,
                (int) x, (int) y, (int) w, (int) h);
    }

    public static void saveFile(String path, String content){
        FileHandle file = Gdx.files.local(C.ROOT + path);
        file.writeString(content, false);
    }

    public static String loadFile(String path){
        return Gdx.files.internal(C.ROOT + path).readString();
    }

    public static LinkedList<String> getDirectories(String path){
        LinkedList<String> dirs = new LinkedList<String>();
        FileHandle dirHandle = Gdx.files.internal(C.ROOT + path);
        for (FileHandle entry: dirHandle.list()) {
            if(entry.isDirectory())
                dirs.add(entry.name());
        }
        return dirs;
    }

    public static LinkedList<String> getFiles(String path){
        LinkedList<String> dirs = new LinkedList<String>();
        FileHandle dirHandle = Gdx.files.internal(C.ROOT + path);
        for (FileHandle entry: dirHandle.list()) {
            if(!entry.isDirectory())
                dirs.add(entry.name());
        }
        return dirs;
    }
}
