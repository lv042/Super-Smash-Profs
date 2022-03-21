package com.mygdx.pahu;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.pahu.GameClass;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		String iconPath = "icon.png";
		int width = 1920;
		int height = 1080;
		boolean useFullScreenMode = false;
		boolean useOwnResolution = false;
		boolean useVSync = false;
		int fps = 60;

		config.setTitle("Our Game");
		config.setWindowIcon(iconPath);
		config.disableAudio(false);

		if(useVSync) config.useVsync(true); //uses max refresh rate of the monitor » prevents tearing and other errors
		else config.setForegroundFPS(fps);



		if(useFullScreenMode) config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());

		else if(useOwnResolution) config.setWindowedMode(width, height);
		else config.setMaximized(true);

		new Lwjgl3Application(new GameClass(), config);
	}
}
