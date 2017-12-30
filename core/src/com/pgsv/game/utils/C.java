package com.pgsv.game.utils;

import com.badlogic.gdx.controllers.Controller;

public final class C {

	public static final int WIDTH = 256;
	public static final int HEIGHT = 144;
	//Maybe for performance boost. there will be no need to divide
	//the width every single time if i use these.
	public static final int HALF_WIDTH = 128;
	public static final int HALF_HEIGHT = 72;

	public static String PATH = "./";
	
	public static Controller IN;
	
	public static boolean DEBUG = false;
	
	public static float TIME = 1f;

	public static float VOLUME = 1f;
	
}
