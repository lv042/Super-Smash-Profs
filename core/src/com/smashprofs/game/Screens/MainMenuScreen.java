package com.smashprofs.game.Screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen implements Screen {

    private Game game;
    private SpriteBatch spriteBatch;
    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera camera;
    private Image playButton, exitButton, logo;
    private Texture playButtonInactive, playButtonActive, exitButtonInactive, exitButtonActive;
    private Table mainTable;

    int width = 1920;
    int height = 1080;

    public MainMenuScreen(Game game) {
        this.game = game;
        this.spriteBatch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FillViewport(width, height, camera);
        this.stage = new Stage(this.viewport, this.spriteBatch);

        playButtonInactive = new Texture("mainmenu/buttons/playButtonInactive.png");
        playButtonActive = new Texture("mainmenu/buttons/playButtonActive.png");
        exitButtonInactive = new Texture("mainmenu/buttons/exitButtonInactive.png");
        exitButtonActive = new Texture("mainmenu/buttons/exitButtonActive.png");
        mainTable = new Table();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        //Create Table
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.top();

        //Create buttons
        playButton = new Image(playButtonInactive);
        exitButton = new Image(exitButtonInactive);
        logo = new Image(new Texture("mainmenu/ssp.png"));

        //Add listeners to buttons
        playButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {

                //playButton = new Image(playButtonActive);
                playButton.setDrawable(new SpriteDrawable(new Sprite(playButtonActive)));
                if (Gdx.input.isButtonJustPressed(0)) {
                    game.setScreen(new PlayScreen((com.smashprofs.game.Game) game));
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                playButton.setDrawable(new SpriteDrawable(new Sprite(playButtonInactive)));
            }
        });

        exitButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                exitButton.setDrawable(new SpriteDrawable(new Sprite(exitButtonActive)));
                if (Gdx.input.isButtonJustPressed(0)) {
                    Gdx.app.exit();
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                exitButton.setDrawable(new SpriteDrawable(new Sprite(exitButtonInactive)));
            }
        });

        mainTable.add(logo).maxSize(1228 , 104).pad(200).padBottom(200);
        mainTable.row();
        mainTable.add(playButton).padBottom(100).maxSize(300, 100);
        mainTable.row();
        mainTable.add(exitButton).padBottom(100).maxSize(300, 100);
        mainTable.background(new TextureRegionDrawable(new Texture("mainmenu/bgmenu.png")));
        //Add table to stage
        mainTable.setDebug(true);
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        this.stage.act();
        this.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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
        this.stage.dispose();
        this.spriteBatch.dispose();
    }
}
