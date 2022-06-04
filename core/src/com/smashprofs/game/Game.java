package com.smashprofs.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smashprofs.game.Helper.GameThread;
import com.smashprofs.game.Helper.PropertiesReader;
import com.smashprofs.game.Helper.gamePropertiesManager;
import com.smashprofs.game.Screens.IntroScreen;

public class Game extends com.badlogic.gdx.Game {
	public static SpriteBatch batch;


	Texture img;
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	private GameThread thread;
	
	@Override
	public void create () {

		thread = new GameThread();
		gamePropertiesManager.firstStart();
		thread.start();

		batch = new SpriteBatch();
		//setScreen(new MainMenuScreen(this));

		PropertiesReader propReader = new PropertiesReader();
		String  result = propReader.readProperties();

		setScreen(new IntroScreen(this));

		//setScreen(new PlayScreen(this)); // passes game to set screen on its own -> now started by intro screen
	}

	@Override
	public void render () {
/*		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();*/

		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		thread.stop();

	}
}
