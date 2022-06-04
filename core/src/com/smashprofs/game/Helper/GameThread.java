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

    public void threadEnd(){
        gamePropertiesManager.edit(Keys.TIMESPLAYED,Integer.toString(Integer.parseInt(gamePropertiesManager.getEntry(Keys.TIMESPLAYED))+1));
        gamePropertiesManager.edit(Keys.GAMETIME,secondsToString(StringToSeconds(gamePropertiesManager.getEntry(Keys.GAMETIME))+count));
    }

    public int StringToSeconds(String time){
        int newtime=0;
        String[] timesplit =time.split(" ");
        return Integer.parseInt(timesplit[0])*60+Integer.parseInt(timesplit[2]);
    }

    public String secondsToString(int seconds){
        int minutes = seconds/60;
        int sec=seconds-minutes*60;
        return minutes+" min "+sec+" sek";
    }
}

