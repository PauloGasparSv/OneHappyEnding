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
