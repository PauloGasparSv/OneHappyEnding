package com.pvale.ohe.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pvale.ohe.MyGame;

public class DesktopLauncher
{
	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 960;
		config.height = 540;
		config.resizable = false;
		config.fullscreen = false;

		new LwjglApplication(new MyGame(), config);
	}
}

//CREDIT
//ART: GrafxKid http://grafxkid.tumblr.com/
//FONT: GravityGames https://opengameart.org/users/gravitygames
//Sfx: 8-bit Platformer Sfx commissioned by Mark McCorkle for OpenGameArt.org ( http://opengameart.org )
//Avgvst - Dispersion Found: t4ngr4m https://opengameart.org/users/t4ngr4m