package com.smashprofs.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smashprofs.game.Game;
import com.smashprofs.game.Helper.Keys;
import com.smashprofs.game.Helper.SoundManager;
import com.smashprofs.game.Helper.gamePropertiesManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntroScreen extends ScreenAdapter {
    private static Logger log = LogManager.getLogger(IntroScreen.class);
    int width = 1920;
    int height = 1080;

    private SoundManager soundManager = SoundManager.getSoundManager_INSTANCE();

    private float zoomFactor = 1f;
    OrthographicCamera camera = null;
    Texture texture = null;
    SpriteBatch batch = null;

    Game game = null;


    private String introSong = "music/introsong.mp3";

    public IntroScreen(Game game) {


        soundManager.setupMusic(introSong);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(width, height);

        camera.update();

        if(gamePropertiesManager.getEntry(Keys.EASTEREGG).equals("true")) {
            texture = new Texture(Gdx.files.internal("rickrollroll.gif"));
        }
        else {
            texture = new Texture(Gdx.files.internal("intro.png"));
        }

        batch = new SpriteBatch();
        this.game = game;
        log.info("Created IntroScreen");
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

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ANY_KEY) || Gdx.input.isButtonJustPressed(0)) {
            game.setScreen(new MainMenuScreen(game));
            // Zum Debuggen kann man hier die Weiterleitung auf den Character Select Screen einschalten:
            //game.setScreen(new CharacterSelectScreen(game));

        }
    }


    @Override
    public void dispose() {
        batch.dispose();
    }


}
