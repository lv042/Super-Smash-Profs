package com.smashprofs.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smashprofs.game.Helper.GameThread;
import com.smashprofs.game.Helper.PropertiesReader;
import com.smashprofs.game.Helper.gamePropertiesManager;
import com.smashprofs.game.Screens.CharacterSelectScreen;
import com.smashprofs.game.Screens.IntroScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.smashprofs.game.Helper.Util.log4JconfLoad;

public class Game extends com.badlogic.gdx.Game {
	public static SpriteBatch batch;
	private static Logger log = LogManager.getLogger(Game.class);
	Texture img;
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	private GameThread thread;
	public static Boolean debugMode;
	
	@Override
	public void create () {
		log4JconfLoad(System.getProperty("user.dir") + "\\log4j2.xml");
		log.info("Game created, starting up...");
		thread = new GameThread();
		gamePropertiesManager.firstStart();
		thread.start();

		// --Toggles debug mode--
		debugMode = true;
		// ----------------------

		batch = new SpriteBatch();
		//setScreen(new MainMenuScreen(this));

		PropertiesReader propReader = new PropertiesReader();
		String  result = propReader.readProperties();

		log.info("Redirecting to IntroScreen...");
		setScreen(new IntroScreen(this));

		//setScreen(new PlayScreen(this)); // passes game to set screen on its own -> now started by intro screen
		if(debugMode) {
			log.warn("-------------------------------------------");
			log.warn("Game debug mode activated!");
			log.warn("You might encounter debug lines and more.");
			log.warn("-------------------------------------------");
		}
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
		thread.threadEnd();

	}
}
