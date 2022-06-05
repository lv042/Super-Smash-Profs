package com.smashprofs.game.Helper;


public class GameThread extends Thread{
    private int count;
    @Override
    public void run() {
    count=0;
    while(this.isAlive()) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;
      }
    }

    //wegen performance gründen erst am ende erst properties verändern
    public void threadEnd(){
        gamePropertiesManager.edit(Keys.TIMESPLAYED,Integer.toString(Integer.parseInt(gamePropertiesManager.getEntry(Keys.TIMESPLAYED))+1));
        gamePropertiesManager.edit(Keys.GAMETIME,gamePropertiesManager.secondsToString(gamePropertiesManager.stringToSeconds(gamePropertiesManager.getEntry(Keys.GAMETIME))+count));
    }


}

