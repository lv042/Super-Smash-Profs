package com.smashprofs.game.Helper;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameThread extends Thread{
    private static Logger log = LogManager.getLogger(GameThread.class);
    private int count;
    private boolean running=true;
    @Override
    public void run() {
        log.info("Thread started.");
    count=0;
    while(running) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("Thread action failed!");
            log.error(e);
            e.printStackTrace();
        }
        count++;
      }
    }

    //wegen performance gründen erst am ende erst properties verändern
    public void threadEnd(){
        running=false;
        log.info("Ending thread...");
        gamePropertiesManager.edit(Keys.TIMESPLAYED,Integer.toString(Integer.parseInt(gamePropertiesManager.getEntry(Keys.TIMESPLAYED))+1));
        gamePropertiesManager.edit(Keys.GAMETIME,gamePropertiesManager.secondsToString(gamePropertiesManager.stringToSeconds(gamePropertiesManager.getEntry(Keys.GAMETIME))+count));
        log.info("Saved game time and times played successfully.");
    }


}

