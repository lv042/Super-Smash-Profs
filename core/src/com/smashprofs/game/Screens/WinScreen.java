package com.smashprofs.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smashprofs.game.Game;

public class WinScreen implements Screen {
    private static final int BUTTON_WIDTH = 300, BUTTON_HEIGHT = 100, MENU_Y = 300;

    private float zoomFactor = 1.0f;
    private float timer = 0.0f;

    Texture menuButtonActive;
    Texture menuButtonInactive;
    Texture bgPicture;
    Texture pokal;

    Game game = null;
    SpriteBatch batch;


    public WinScreen(Game game) {

        menuButtonActive = new Texture("winscreen/menuButtonActive.png");
        menuButtonInactive = new Texture("winscreen/menuButtonInactive.png");
        bgPicture = new Texture("winscreen/winbg.png");
        pokal = new Texture("winscreen/pokal.png");

        this.batch = new SpriteBatch();
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        timer += 0.05f;
        zoomFactor = 1 + (float) Math.cos(timer) / 50;

        boolean exit = false;
        batch.begin();

        batch.draw(bgPicture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(pokal, Gdx.graphics.getWidth() / 2f -320f / zoomFactor, 400f , 740f / zoomFactor, 580f / zoomFactor);

        // Button Area
        int x = Gdx.graphics.getWidth() / 2 - BUTTON_WIDTH / 2;
        if (Gdx.input.getX() < x + BUTTON_WIDTH && Gdx.input.getX() > x && Gdx.graphics.getHeight() - Gdx.input.getY() < MENU_Y + BUTTON_HEIGHT && Gdx.graphics.getHeight() - Gdx.input.getY() > MENU_Y) {
            batch.draw(menuButtonActive, x, MENU_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            exit = true;
        } else {
            batch.draw(menuButtonInactive, x, MENU_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            exit = false;
        }

        batch.end();

        //Button Press
        if (exit && Gdx.input.isButtonJustPressed(0)) {
            game.setScreen(new MainMenuScreen(game));
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {


    }

}
