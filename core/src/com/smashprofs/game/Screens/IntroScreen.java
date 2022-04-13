package com.smashprofs.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smashprofs.game.GameClass;
import com.smashprofs.game.Helper.SoundManager;

public class IntroScreen extends ScreenAdapter {
    int width = 1920;
    int height = 1080;

    private SoundManager soundManager = SoundManager.getSoundManager_INSTANCE();

    private float zoomFactor = 1f;
    OrthographicCamera camera = null;
    Texture texture = null;
    SpriteBatch batch = null;

    GameClass game = null;


    private String introSong = "music/introsong.mp3";

    public IntroScreen(GameClass game) {


        soundManager.setupMusic(introSong);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(width, height);

        camera.update();
        texture = new Texture(Gdx.files.internal("logo.jpeg"));

        batch = new SpriteBatch();
        this.game = game;

    }


    @Override
    public void render(float delta) {

        batch.setProjectionMatrix(camera.combined);
        if(zoomFactor > 0.935) zoomFactor -= 0.0005f;
        //System.out.println(zoomFactor);
        Gdx.gl.glClearColor(0, 0, 0, 1); //-> light blue
        //Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texture, (camera.position.x / 2 - width / 2) / zoomFactor, (camera.position.y / 2 - height / 2) / zoomFactor, width / zoomFactor, height/ zoomFactor);
        batch.end();

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ANY_KEY)) {
            game.setScreen(new PlayScreen(game));

        }
    }


    @Override
    public void dispose() {
        batch.dispose();
    }


}
