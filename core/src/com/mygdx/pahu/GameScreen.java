package com.mygdx.pahu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.pahu.util;
import com.mygdx.pahu.inputAxes;

public class GameScreen extends ScreenAdapter {
    SpriteBatch batch;
    Texture img;
    ShapeRenderer shaprenderer;
    int size = 100;
    Vector2 position = new Vector2 (util.getRealCentreX(size / 2), util.getRealCentreY(size / 2));
    int velocity = 300;
    SpriteBatch playerBatch;
    Texture playerTexture;





    public GameScreen() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        shaprenderer = new ShapeRenderer();
        playerBatch = new SpriteBatch();
        playerTexture = new Texture("knight.png");

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1); // clears the buffer after each frame with the chosen color » white

        // Defines active input method for moving the "character"
        position.x += inputAxes.adAxis() * delta * velocity;
        position.y += inputAxes.wsAxis() * delta * velocity;
        // Configure cursor catching
        Gdx.input.setCursorCatched(true);
        // Quitting with "ESC"
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))Gdx.app.exit();


        batch.begin();
        batch.draw(img, 0, 0); //draws  texture at (x|y) location
        batch.end();
        Gdx.input.setCursorPosition(util.getCentreX(),util.getCentreY());
        shaprenderer.begin(ShapeRenderer.ShapeType.Filled);
        shaprenderer.setColor(Color.BLUE);
        shaprenderer.circle(position.x, position.y, size);
        shaprenderer.end();
        //GameClass.INSTANCE.setScreen(new MainMenuScreen());

        playerBatch.begin();
        playerBatch.draw(playerTexture, 100,500);
        playerBatch.end();
    }

    @Override
    public void dispose() {
        //Frees memory since openGL works in c and therefore has no garbage collector
        shaprenderer.dispose();
        batch.dispose();
        img.dispose();
        playerBatch.dispose();
    }

    @Override
    public void hide() {
        this.dispose();
    }

}
