package com.pgsv.game.utils;

import com.badlogic.gdx.Input.TextInputListener;

public class TextInput implements TextInputListener {

	@Override
	public void input(String text) 
	{
		System.out.println(text);
	}

	@Override
	public void canceled() 
	{
		
	}

}
