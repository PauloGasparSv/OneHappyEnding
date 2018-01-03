package com.pgsv.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pgsv.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "One Happy Ending";
		config.resizable = false;
		config.width = 3 * (1280 / 4);
		config.height = 3 * (720 / 4);
		config.fullscreen = true;
		new LwjglApplication(new MyGdxGame(), config);
	}
}

//CREDIT
//ART: GrafxKid http://grafxkid.tumblr.com/
//FONT: GravityGames https://opengameart.org/users/gravitygames
//Sfx: 8-bit Platformer Sfx commissioned by Mark McCorkle for OpenGameArt.org ( http://opengameart.org )
//Avgvst - Dispersion Found: t4ngr4m https://opengameart.org/users/t4ngr4m