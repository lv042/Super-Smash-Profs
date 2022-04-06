package com.smashprofs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class inputAxes {
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
}
