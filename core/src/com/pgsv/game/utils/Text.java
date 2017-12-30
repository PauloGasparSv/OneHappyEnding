package com.pgsv.game.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Text {
    private TextureRegion[] abc;

    public Text() {
        this.abc = new TextureRegion[40];

        this.abc = Media.getFrames(Media.loadTexture("ui/font.png"), new Vector2(8, 5),
                Vector2.Zero, new Vector2(8, 8), Vector2.Zero);

    }

    public void draw(SpriteBatch batch, String text, float x, float y) {
        int len = text.length();
        text = text.toLowerCase();
        for (int i = 0; i < len; i++) {
            int j = getRegion(text.charAt(i));
            if (j < 0 || j > this.abc.length - 1) continue;
            batch.draw(this.abc[j], x + i * 8, y);
        }
    }

    public int getRegion(char c) {
        if (c == ' ') return -1;

        int n = (int) c;

        if (n > 96) return n - 97;
        else if (n > 47) return n - 48 + 26;

        switch (n) {
            case 45:
                return 36;
            case 42:
                return 37;
            case 33:
                return 38;
        }

        return -1;
    }

    public void dispose() {

    }

}
