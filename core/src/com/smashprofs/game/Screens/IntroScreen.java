package com.smashprofs.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smashprofs.game.GameClass;

public class IntroScreen extends ScreenAdapter {
    int w = 0;
    int h = 0;
    int tw = 0;
    int th = 0;
    OrthographicCamera camera = null;
    Texture texture = null;
    SpriteBatch batch = null;

    GameClass game = null;

    public IntroScreen(GameClass game) {
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(w, h);
        camera.position.set(w / 2, h / 2, 0); // Change the height --> h
        camera.update();
        texture = new Texture(Gdx.files.internal("logo.jpeg"));
        tw = texture.getWidth();
        th = texture.getHeight();
        batch = new SpriteBatch();
        this.game = game;

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); //-> light blue
        //Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texture, camera.position.x - (tw / 2) / 2, camera.position.y - (th / 2) / 2, tw / 2, th / 2);
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
