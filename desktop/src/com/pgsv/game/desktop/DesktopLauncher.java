package com.pgsv.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pgsv.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "One Happy Ending";
		config.resizable = false;
		config.width = 1280;
		config.height = 720;
		config.fullscreen = false;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
//CREDIT
//ART: GrafxKid http://grafxkid.tumblr.com/
//FONT: GravityGames https://opengameart.org/users/gravitygames
//SFX: 8-bit Platformer SFX commissioned by Mark McCorkle for OpenGameArt.org ( http://o--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------bpengameart.org )
//Avgvst - Dispersion Found: t4ngr4m https://opengameart.org/users/t4ngr4m