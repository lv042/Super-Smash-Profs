package com.smashprofs.game.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.smashprofs.game.Game;

public class MainMenuScreen implements Screen{

    private static final int BUTTON_WIDTH=300, BUTTON_HEIGHT=100,PLAY_Y=400,EXIT_Y=200;


    private float zoomFactor = 1f;
    private float timer=0f;

    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture bgPicture;
    Texture logo;

    Game game = null;
    SpriteBatch batch;


    public MainMenuScreen(Game game){
        playButtonActive=new Texture("mainmenu/buttons/playButtonActive.png");
        playButtonInactive=new Texture("mainmenu/buttons/playButtonInactive.png");
        exitButtonActive=new Texture("mainmenu/buttons/exitButtonActive.png");
        exitButtonInactive=new Texture("mainmenu/buttons/exitButtonInactive.png");
        bgPicture=new Texture("mainmenu/bgmenu.png");
        logo=new Texture("mainmenu/ssp.png");

        batch = new SpriteBatch();
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1); //-> light blue
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        timer+=0.05f;
        zoomFactor= 1+(float)Math.cos(timer)/50;

        boolean play = false, exit=false;
        batch.begin();

        batch.draw(bgPicture, 0,0);
        batch.draw(logo, Gdx.graphics.getWidth()/2-614/zoomFactor,700/zoomFactor, 1228/zoomFactor, 104/zoomFactor);

        //Button Area
        int x = Gdx.graphics.getWidth()/2  -BUTTON_WIDTH/2;
        if (Gdx.input.getX()<x+BUTTON_WIDTH && Gdx.input.getX()>x && Gdx.graphics.getHeight()-Gdx.input.getY()<PLAY_Y+BUTTON_HEIGHT && Gdx.graphics.getHeight()-Gdx.input.getY()>PLAY_Y) {
            batch.draw(playButtonActive, x, PLAY_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            play=true;
        } else {
            batch.draw(playButtonInactive, x, PLAY_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            play=false;
        }

        if (Gdx.input.getX()<x+BUTTON_WIDTH && Gdx.input.getX()>x && Gdx.graphics.getHeight()-Gdx.input.getY()<EXIT_Y+BUTTON_HEIGHT && Gdx.graphics.getHeight()-Gdx.input.getY()>EXIT_Y) {
            batch.draw(exitButtonActive, x, EXIT_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            exit=true;
        } else {
            batch.draw(exitButtonInactive, x, EXIT_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            exit=false;
        }



        batch.end();

        //Button Press
        if (play && Gdx.input.isButtonJustPressed(0)) {
            game.setScreen(new PlayScreen(game));

        }
        else if(exit && Gdx.input.isButtonJustPressed(0)){
            Gdx.app.exit();
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
