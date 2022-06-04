package com.smashprofs.game.Helper;

import java.io.*;
import java.util.Properties;

public class gamePropertiesManager {

    private static File file = new File("../core/src/resources/game_info.properties");
    private static Properties p = new Properties();
    public static void firstStart() {
        //checks if file exists
        if (!file.exists()) {
            try {
                //creates new file
                System.out.println("created");
                file.createNewFile();
                edit(Keys.GAMETIME,"0 sek");
                edit(Keys.TIMESPLAYED,"0");
            } catch (IOException e) {
                System.out.println("net geklappt");
                e.printStackTrace();
            }
        }
        else System.out.println("existiert schon");
    }


    public static void edit(Keys key,String value) {

        //creates file writer
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.setProperty (key.getValue(), value);
        try {
            //saving changes
            p.store (writer, "Update by FileWriter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getEntry(Keys key) {

        //creates file reader
        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            p.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p.getProperty(key.getValue());
    }
}
