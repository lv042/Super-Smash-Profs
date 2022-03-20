package com.mygdx.pahu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends ScreenAdapter {
    SpriteBatch batch;
    Texture img;

    public GameScreen() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1); //clears the buffer after each frame with the chosen color » white
        batch.begin();
        batch.draw(img, 0, 0); //draws  texture at (xy) location
        batch.end();
        //GameClass.INSTANCE.setScreen(new MainMenuScreen());
    }

    @Override
    public void dispose() {
        //Frees memory since openGL works in c and therefore has no garbage collector
        batch.dispose();
        img.dispose();
    }

    @Override
    public void hide() {
        this.dispose();
    }
}
