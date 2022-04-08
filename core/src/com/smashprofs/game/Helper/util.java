package com.smashprofs.game.Helper;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;

public class util {
    //Maybe make this class singleton?

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

    public static void setupMusic(boolean playMusic) {
        //music from https://www.fesliyanstudios.com/royalty-free-music/download/funny-bit/2399 -> royalty free

        Music gameSong = Gdx.audio.newMusic(Gdx.files.internal("music/beste music ever.wav"));

        if(playMusic){
            gameSong.setLooping(true);
            gameSong.play();
        }
    }

}