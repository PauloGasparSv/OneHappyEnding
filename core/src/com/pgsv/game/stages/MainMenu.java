package com.pgsv.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pgsv.game.utils.C;
import com.pgsv.game.utils.Input;
import com.pgsv.game.utils.Media;
import com.pgsv.game.utils.Vfx;

public class MainMenu extends Screen
{

    private Animation<TextureRegion> titleAnimation;

    private Texture background;
    private Texture menuBox;
    private Texture cursor;

    private float backGroundDelta;
    private float menuOff;
    private float speed;
    private float titleDelta;

    private int menu;
    private int current;

    private boolean chosen;

    public MainMenu()
    {
        super();

        background = Media.loadTexture(C.ROOT + "ui/mainMenuBackground2.png");
        menuBox = Media.loadTexture("ui/board.png");
        cursor = Media.loadTexture("ui/cursor.png");
        titleAnimation = Media.createAnimation(Media.loadTexture("ui/title.png"),
                0.1f,new Vector2(1, 11), Vector2.Zero, new Vector2(101, 12), Vector2.Zero);


        backGroundDelta = 0f;
        menuOff = 220f;
        speed = 0f;
        current = 0;
        menu = 0;
        chosen = false;

    }

    public void update(float delta)
    {
        if (!this.chosen)
        {
            if(this.menu == 0 && this.backGroundDelta > -40f)
			{
				this.speed += delta * 30f;
				this.backGroundDelta -= delta * this.speed;
				if (this.backGroundDelta <= -40f)
				{
					this.backGroundDelta = -40f;
					this.menu = 1;
				}
			}

            if (this.menu == 1)
            {
                if (this.menuOff > 0)
                {
                    this.menuOff -= delta * 60f;
                    if (this.menuOff < 0) {
                        this.menuOff = 0;
                        this.menu = 2;
                    }
                }
            }

            if (this.menu == 2)
            {
                if (Input.isKeyJustPressed(Input.UP))
                {
                    this.current--;
                    if (this.current < 0) this.current = 0;
                }
                else if (Input.isKeyJustPressed(Input.DOWN))
                {
                    this.current++;
                    if (this.current > 2) this.current = 2;
                }

                if (Input.isKeyJustPressed(Input.ENTER) || Input.isKeyJustPressed(Input.Z) || Input.isKeyJustPressed(Input.SPACE))
                    chosen = true;
            }

            if(this.menu < 2 && Input.isKeyJustPressed(Input.SPACE))
            {
                this.backGroundDelta = -40f;
                this.menuOff = 0;
                this.menu = 2;
            }

        }
        else
        {
            this.titleDelta += delta;

            if (this.titleAnimation.isAnimationFinished(this.titleDelta) && vfx.getState() == Vfx.NONE)
                vfx.fadeOut();
            if (this.vfx.getState() == Vfx.BLACK)
                this.choose();

        }

    }

    public void choose()
    {
        dispose();

        if (current == 0)
            game.setScreen(new TestStage());
        else if(current == 1)
            game.setScreen(new SelectMap());
        if (current == 2)
            Gdx.app.exit();
    }

    public void draw()
    {
        batch.draw(background, 0, this.backGroundDelta);

        if (this.menu > 0)
        {
            batch.draw(menuBox, 66 + menuOff, 40);
            batch.draw(this.titleAnimation.getKeyFrame(titleDelta, false), 73 + menuOff, 108);
        }

        if (this.menu == 2)
        {
            switch (this.current)
            {
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

    }
}
