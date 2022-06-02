package com.smashprofs.game.Helper;

import com.sun.org.apache.xml.internal.utils.res.XResources_zh_CN;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private static Logger log = LogManager.getLogger(PropertiesReader.class);


    public String readProperties() {

        String result = "";

        try (InputStream input = new FileInputStream("properties/game.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            //System.out.println(prop.getProperty("name"));
            log.info(prop.getProperty("name"));
            //System.out.println(prop.getProperty("lang"));
            log.info(prop.getProperty("lang"));

            //result += prop.getProperty("name");


        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
