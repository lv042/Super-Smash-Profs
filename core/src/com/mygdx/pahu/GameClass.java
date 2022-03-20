package com.mygdx.pahu;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameClass extends Game {


	public static GameClass INSTANCE;

	public GameClass() {
		if(INSTANCE == null) INSTANCE = this;
	}

	@Override
	public void create () {
		INSTANCE.setScreen(new GameScreen());


	}

	@Override
	public void dispose () {

	}


}
