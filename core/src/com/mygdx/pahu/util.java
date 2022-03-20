package com.mygdx.pahu;

import com.badlogic.gdx.Gdx;

public class util {
    public int getLength() {
        return Gdx.graphics.getWidth();
    }

    public int getHeight() {
        return Gdx.graphics.getHeight();
    }

    public int getCentreX() {
        return Gdx.graphics.getWidth() / 2;
    }

    public int getCentreY() {
        return Gdx.graphics.getHeight() / 2;
    }
}
