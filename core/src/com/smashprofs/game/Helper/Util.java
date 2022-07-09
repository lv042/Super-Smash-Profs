package com.smashprofs.game.Helper;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Util {
    private static Logger log = LogManager.getLogger(Util.class);
    //Maybe make this class singleton?

  /*  public static boolean debugMode = true;

    private static boolean turnOnMusic = true;

    public static void setDebugMode(boolean debugMode) {
        Util.debugMode = debugMode;
    }

    public static void setTurnOnMusic(boolean turnOnMusic) {
        Util.turnOnMusic = turnOnMusic;
    }*/

    public static int wsAxis(){
        if(Gdx.input.isKeyPressed(Input.Keys.W)) return 1;
        if(Gdx.input.isKeyPressed(Input.Keys.S)) return -1;
        else return 0;
    }
    public static int adAxis(){
        if(Gdx.input.isKeyPressed(Input.Keys.A)) return -1;
        if(Gdx.input.isKeyPressed(Input.Keys.D)) return 1;
        else return 0;
    }
    public static int updownAxis(){
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) return 1;
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) return -1;
        else return 0;
    }
    public static int leftrightAxis(){
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) return -1;
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) return 1;
        else return 0;
    }

    public static int getLength() {
        return Gdx.graphics.getWidth();
    }

    public static int getHeight() {
        return Gdx.graphics.getHeight();
    }

    public static int getCentreX() {
        return Gdx.graphics.getWidth() / 2;
    }

    public static int getCentreY() {
        return Gdx.graphics.getHeight() / 2;
    }

    public static int getRealCentreX(int width){
        return Gdx.graphics.getWidth() / 2 - width / 2;
    }

    public static int getRealCentreY(int height){
        return Gdx.graphics.getHeight() / 2 - height / 2;
    }

    /**
     * Loads the log4j configuration file.
     * @param logConfigurationFile
     * The file to load.
     */
    public static void log4JconfLoad(String logConfigurationFile) {
        try {
            log.info("Trying to read log4j config file...");
            InputStream inputStream = new FileInputStream(logConfigurationFile);
            ConfigurationSource source = new ConfigurationSource(inputStream);
            Configurator.initialize(null, source);
            log.info("Read log4j config file successfully");
        } catch (IOException e) {
            log.error("Failed to read log4j config file!");
            log.error(e);
            e.printStackTrace();
        }
    }




}