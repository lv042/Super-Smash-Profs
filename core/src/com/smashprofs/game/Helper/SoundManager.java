package com.smashprofs.game.Helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.smashprofs.game.Game;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles game sound effects and background music.
 */
public class SoundManager {

    private boolean turnOnMusic= Game.ingameMusic;
    private boolean turnOnSound= Game.soundEffects;

    private Music currenMusic;

    //Singleton for the sound manager
    private static final SoundManager soundManager_INSTANCE = new SoundManager();

    private static Logger log = LogManager.getLogger(SoundManager.class);


    /**
     * Private constructor to avoid client applications to use constructor
     */
    private SoundManager() {
    }

    public static SoundManager getSoundManager_INSTANCE() {return soundManager_INSTANCE;}


    /**
     * Initial setup of the game music.
     * @param musicPath
     * The path to the music file.
     */
    public void setupMusic(String musicPath) {
        //music from https://www.fesliyanstudios.com/royalty-free-music/download/funny-bit/2399 -> royalty free
        try{
            currenMusic.stop();
            currenMusic.setLooping(false);
        }
        catch(Exception e) {
            log.error("No music declared or the program just started");
        }


        currenMusic = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
        System.out.println("SoundManager: setupMusic: musicPath: " + musicPath);
        log.info("SoundManager: setupMusic: musicPath: " + musicPath);


        if(turnOnMusic){

            currenMusic.play();
            currenMusic.setLooping(true);
        }
    }

    /**
     * Play a sound from a specified path.
     * @param soundPath
     * The path of the sound file.
     */
    public void playSound(String soundPath) {
        //play sound
        if(turnOnSound){
            if(soundPath.contains(".wav")) {
                Sound wavSound = Gdx.audio.newSound(Gdx.files.internal(soundPath));
                wavSound.play();

            }
            else if(soundPath.contains(".mp3")) {
                Sound mp3Sound = Gdx.audio.newSound(Gdx.files.internal(soundPath));
                mp3Sound.play();

            }
            else {
                log.info("SoundManager: updateSounds: unknown sound format");
                return;
            }
        }
    }



}
