package com.mygdx.pahu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameClass extends Game {

	private int screenWidth;
	private int screenHeight;
	private OrthographicCamera camera;

	public static GameClass INSTANCE;

	public GameClass() {
		if(INSTANCE == null) INSTANCE = this;
	}

	@Override
	public void create () {
		this.screenWidth = Gdx.graphics.getWidth();
		this.screenHeight = Gdx.graphics.getHeight();
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, 30, 20);
		INSTANCE.setScreen(new GameScreen(camera));


	}

	@Override
	public void dispose () {

	}


}
