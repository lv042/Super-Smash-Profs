package com.smashprofs.game.Helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoundManager {

    private boolean turnOnMusic= false;

    private Music currenMusic;

    //implements singelton sound manager

    private static final SoundManager soundManager_INSTANCE = new SoundManager();

    private static Logger log = LogManager.getLogger(SoundManager.class);


    //private constructor to avoid client applications to use constructor
    private SoundManager() {
    }

    public static SoundManager getSoundManager_INSTANCE() {return soundManager_INSTANCE;}




    public void updateSounds(String soundPath) {

    }

    public void setupMusic(String musicPath) { //should be implemented as own singelton class
        //music from https://www.fesliyanstudios.com/royalty-free-music/download/funny-bit/2399 -> royalty free
        try{
            currenMusic.stop();
            currenMusic.setLooping(false);
        }
        catch(Exception e) {
            //System.out.println("No music declared");
            log.error("No music declared");
        }


        currenMusic = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
        System.out.println("SoundManager: setupMusic: musicPath: " + musicPath);
        log.info("SoundManager: setupMusic: musicPath: " + musicPath);


        if(turnOnMusic){

            currenMusic.play();
            currenMusic.setLooping(true);
        }
    }

    public void playSound(String soundPath) {
        //play sound
        if(soundPath.contains("wav")) {
            Sound wavSound = Gdx.audio.newSound(Gdx.files.internal(soundPath));
            wavSound.play();
        }
        else if(soundPath.contains("mp3")) {


            Sound mp3Sound = Gdx.audio.newSound(Gdx.files.internal(soundPath));
            mp3Sound.play();
        }
        else {
            //System.out.println("SoundManager: updateSounds: unknown sound format");
            log.info("SoundManager: updateSounds: unknown sound format");
            return;
        }
    }



}
