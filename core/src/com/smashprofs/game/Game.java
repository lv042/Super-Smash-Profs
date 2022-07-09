package com.smashprofs.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smashprofs.game.Exceptions.NegativeSeconds;
import com.smashprofs.game.Helper.GameThread;
import com.smashprofs.game.Helper.gamePropertiesManager;
import com.smashprofs.game.Screens.IntroScreen;
import com.smashprofs.game.Screens.PlayScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.smashprofs.game.Helper.Util.log4JconfLoad;

public class Game extends com.badlogic.gdx.Game {
	public static SpriteBatch batch;
	private static Logger log = LogManager.getLogger(Game.class);
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	private GameThread thread;
	public static Boolean debugMode, showFPS, unitTestMode;
	
	@Override
	public void create () {
		log4JconfLoad(System.getProperty("user.dir") + "\\log4j2.xml");
		log.info("Game created, starting up...");
		log.warn("---------------------------------------");
		log.warn("This game uses assets made by others.");
		log.warn("For credits, see assets/credits.txt");
		log.warn("---------------------------------------");
		thread = new GameThread();
		gamePropertiesManager.firstStart();
		thread.start();

		// --Toggle unit test mode--
		unitTestMode = false;
		//--------------------------
		// --Toggles debug mode--
		debugMode = false;
		// ----------------------
		// --Toggles FPS visibility--
		showFPS = true;
		// --------------------------
		// Set other graphic settings in DesktopLauncher.java

		batch = new SpriteBatch();
		//setScreen(new MainMenuScreen(this));

		if(unitTestMode) {
			log.warn("Unit test mode enabled, skipping intro screen");
			log.warn("Redirecting to PlayScreen...");
			setScreen(new PlayScreen(this));
		}
		else {
			log.info("Redirecting to IntroScreen...");
			setScreen(new IntroScreen(this));
		}


		//setScreen(new PlayScreen(this)); // passes game to set screen on its own -> now started by intro screen
		if(debugMode) {
			log.warn("-------------------------------------------");
			log.warn("Game debug mode activated!");
			log.warn("You might encounter debug lines and more.");
			log.warn("-------------------------------------------");
		}
		if(showFPS) {
			log.warn("-------------------------------------------");
			log.warn("FPS counter visible!");
			log.warn("Keep in mind that the FPS indicator is only ");
			log.warn("visible when the PlayScreen is active.");
			log.warn("-------------------------------------------");
		}
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		try {
			thread.threadEnd();
		} catch (NegativeSeconds e) {
			e.printStackTrace();
		}

	}
}
