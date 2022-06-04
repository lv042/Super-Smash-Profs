package com.smashprofs.game.Helper;


public class GameThread extends Thread{

    @Override
    public void run() {
    gamePropertiesManager.edit(Keys.TIMESPLAYED,Integer.toString(Integer.parseInt(gamePropertiesManager.getEntry(Keys.TIMESPLAYED))+1));

    while(this.isAlive()) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] time= gamePropertiesManager.getEntry(Keys.GAMETIME).split(" ");
        gamePropertiesManager.edit(Keys.GAMETIME,(Integer.parseInt(time[0])+1)+" sek");
      }

    }

}

