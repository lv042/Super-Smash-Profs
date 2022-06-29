package com.smashprofs.game.Helper;

import com.smashprofs.game.Exceptions.NegativeSeconds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * Handles the game.properties file.
 */
public class gamePropertiesManager {
    private static Logger log = LogManager.getLogger(gamePropertiesManager.class);
    private static File file = new File("../core/src/resources/game_info.properties");
    private static Properties p = new Properties();

    /**
     * This needs to be called on the first start of the game.
     */
    public static void firstStart() {
        //checks if file exists
        if (!file.exists()) {
            log.warn("game properties file does not exist!");
            try {
                //creates new file
                log.info("Creating new game properties file...");
                file.createNewFile();
                edit(Keys.GAMETIME,"0 min 0 sek");
                edit(Keys.TIMESPLAYED,"0");
                edit(Keys.TEST,"teest");
                edit(Keys.EASTEREGG,"false");
                log.info("Created default game properties file.");
            } catch (IOException e) {
                log.error("Failed creating game properties file!");
                log.error(e);
                e.printStackTrace();
            }
        }
        else log.info("Found existing game properties file, not creating a new one.");
    }


    /**
     * Edits the value of a specific key.
     * @param key
     * The key whose value is to be changed.
     * @param value
     * The new value of the selected key.
     */
    public static void edit(Keys key,String value) {
        log.debug("Editing game properties file...");
        //creates file writer
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            log.debug("Created new FileWriter.");
        } catch (IOException e) {
            log.error("Failed creating new FileWriter!");
            log.error(e);
            e.printStackTrace();
        }
        p.setProperty (key.getValue(), value);
        log.debug("Set " + key.getValue() + " to the new value: " + value);
        try {
            //saving changes
            log.debug("Trying to save changes...");
            p.store (writer, "Update by FileWriter");
            writer.close();
            log.debug("Changes saved.");
        } catch (IOException e) {
            log.error("Failed saving changes!");
            log.error(e);
            e.printStackTrace();
        }
    }

    /**
     * Gets the value of a specific key.
     * @param key
     * The key to get the value of.
     * @return
     * The value of the specific key.
     */
    public static String getEntry(Keys key) {

        //creates file reader
        FileReader reader = null;
        try {
            log.debug("Trying to create new FileReader...");
            reader = new FileReader(file);
            log.debug("Created new FileReader.");
        } catch (FileNotFoundException e) {
            log.error("Failed creating new FileReader!");
            log.error(e);
            e.printStackTrace();
        }
        try {
            log.debug("Trying to load property with FileReader...");
            p.load(reader);
            reader.close();
            log.debug("Loaded property.");
        } catch (IOException e) {
            log.error("Failed loading property!");
            log.error(e);
            e.printStackTrace();
        }
        log.debug("Returning value of property...");
        return p.getProperty(key.getValue());
    }

    /**
     * Reformat the game time string to an int value.
     * @param time
     * The time string.
     * @return
     * The time in seconds.
     */
    public static int stringToSeconds(String time){
        String[] timesplit =time.split(" ");
        log.debug("Reformatted time string to int.");
        return Integer.parseInt(timesplit[0])*60+Integer.parseInt(timesplit[2]);
    }

    /**
     * Reformat the game time in seconds to a string.
     * @param seconds
     * The game time in seconds.
     * @return
     * The time as a string.
     */
    public static String secondsToString(int seconds) throws NegativeSeconds{
        if (seconds>=0) {
            int minutes = seconds / 60;
            int sec = seconds - minutes * 60;
            log.debug("Reformatted time int to string.");
            return minutes + " min " + sec + " sek";
        }
        else {
            throw new NegativeSeconds(seconds +" seconds is not possible!");
        }
    }
}
