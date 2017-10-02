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
		new LwjglApplication(new MyGdxGame(), config);
	}
}
