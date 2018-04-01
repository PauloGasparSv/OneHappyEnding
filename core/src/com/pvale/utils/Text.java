package com.pvale.utils;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Text
{
    public static Texture charSet;
    public static TextureRegion [] chars;

    public static HashMap charMap;

    public static void init()
    {
        charSet = Media.loadTexture("misc/boxyFontRedux.png");
        chars = Media.getSheetFrames(charSet, 5, 13, 9, 9);

        charMap = new HashMap<Integer,Character>();
        
        addChar('A', 0);
        addChar('B', 1);
        addChar('C', 2);
        addChar('D', 3);
        addChar('E', 4);
        addChar('F', 5);
        addChar('G', 6);
        addChar('H', 7);
        addChar('I', 8);
        addChar('J', 9);
        addChar('K', 10);
        addChar('L', 11);
        addChar('M', 12);
        addChar('N', 13);
        addChar('O', 14);
        addChar('P', 15);
        addChar('Q', 16);
        addChar('R', 17);
        addChar('S', 18);
        addChar('T', 19);
        addChar('U', 20);
        addChar('V', 21);
        addChar('W', 22);
        addChar('X', 23);
        addChar('Y', 24);
        addChar('Z', 25);

        addChar('0', 26);
        addChar('1', 27);
        addChar('2', 28);
        addChar('3', 29);
        addChar('4', 30);
        addChar('5', 31);
        addChar('6', 32);
        addChar('7', 33);
        addChar('8', 34);
        addChar('9', 35);

        addChar('!', 39);
        addChar('$', 40);
        addChar('?', 41);
        addChar('&', 42);

        addChar('[', 52);
        addChar('\\', 53);
        addChar(']', 54);
        addChar('^', 55);
        addChar('_', 56);
        addChar('\'', 57);
        addChar('(', 58);
        addChar('|', 59);
        addChar(')', 60);
        addChar('~', 61);
    }

    public static void draw(SpriteBatch batch, String text, float x, float y, float size)
    {
        int len = text.length();
        float curr = 0f;
        for(int i = 0; i < len; i++)
        {
            char c = text.charAt(i);
            if(c == ' ')
            { 
                curr += 3f * size;
                continue;
            }
            int position = (Integer) charMap.get(c);
            curr += 5f * size;
            if(position > chars.length - 1) continue;
            batch.draw(chars[position], x + curr, y, 6.5f * size, 6.5f * size);       
        }
    }

    public static void addChar(char c, int i)
    {
        charMap.put(new Character(c), new Integer(i));
    }
}