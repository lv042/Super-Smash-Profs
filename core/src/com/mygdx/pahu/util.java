package com.mygdx.pahu;

import com.badlogic.gdx.Gdx;

public class util {
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
}
