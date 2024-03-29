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
import com.crashinvaders.vfx.VfxManager;
import com.smashprofs.game.Actors.Players.PlayerView;
import com.smashprofs.game.Helper.PostProcessingSettings;
import com.smashprofs.game.Helper.SoundManager;
/**
 * The win menu screen
 */
public class WinScreen implements Screen {

    private Game game;
    private SpriteBatch batch;
    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera camera;
    private Table mainTable;
    private Image menuButton, winner;
    private PlayerView playerOne, playerTwo;
    private final VfxManager postProcessingManager;
    private SoundManager sound;

    private final Texture menuButtonInactive = new Texture("winscreen/menuButtonInactive.png"),
            menuButtonActive = new Texture("winscreen/menuButtonActive.png"),
            player1 = new Texture("winscreen/player1.png"),
            player2 = new Texture("winscreen/player2.png"),
            pokal = new Texture("winscreen/pokal.png");


    private float timer;
    private float zoomFactor;
    int screenWidth = 1920;
    int screenHeight = 1080;


    /**
     * Constructor of WinScreen. Sets up postprocessing and soundmanager, creates the main table,
     * sets viewport,stage,camera,spritebatch
     * @param game
     * The game.
     */
    public WinScreen(Game game, PlayerView playerOne, PlayerView playerTwo) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FillViewport(screenWidth, screenHeight, camera);
        this.stage = new Stage(this.viewport, this.batch);
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.mainTable = new Table();

        //add pre configured settings to PostProcessingManager
        PostProcessingSettings ppSetUpHandler = new PostProcessingSettings();
        this.postProcessingManager = ppSetUpHandler.getPostProcessingManager();
        sound = SoundManager.getSoundManager_INSTANCE();

    }

    /**
     * Gets called when showing the WinScreen.
     * Fills the main table with Content(button, background), creates button with Listeners for Hovering/Clicking.
     * Detects, who won the Game.
     * Adds the maintable to the stage.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        //Create Table
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.top();

        //Create buttons
        menuButton = new Image(menuButtonInactive);

        // winner
        if (playerOne.getHP() >= playerTwo.getHP()) {
            winner = new Image(player1);
        } else {
            winner = new Image(player2);
        }

        //Add listeners to buttons
        menuButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {

                menuButton.setDrawable(new SpriteDrawable(new Sprite(menuButtonActive)));
                if (Gdx.input.isButtonJustPressed(0)) {
                    sound.playSound("sounds/minecraft_click.mp3");
                    game.setScreen(new MainMenuScreen(game));
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                menuButton.setDrawable(new SpriteDrawable(new Sprite(menuButtonInactive)));
            }
        });


        mainTable.add(winner).padBottom(100).maxSize(600, 200).padTop(500);
        mainTable.row();
        mainTable.add(menuButton).padBottom(100).maxSize(300, 100);
        mainTable.background(new TextureRegionDrawable(new Texture("winscreen/winbg.png")));

        //Add table to stage
        mainTable.setDebug(false);
        stage.addActor(mainTable);
    }

    /**
     * Renders the stage (maintable), sets and plays Trophy-Animation, adds post processing.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        postProcessingManager.cleanUpBuffers();

        // Begin render to an off-screen buffer.
        postProcessingManager.beginInputCapture();
        this.stage.act();
        this.stage.draw();

        timer += 0.06f;
        zoomFactor = 1 + (float) Math.cos(timer) / 50;

        //center the trophy image and playing animation
        batch.begin();
        batch.draw(pokal, stage.getViewport().getWorldWidth() / 2f - pokal.getWidth() / 2 / zoomFactor, 600f / zoomFactor, 290f / zoomFactor, 360f / zoomFactor);
        batch.end();

        postProcessingManager.endInputCapture();

        postProcessingManager.applyEffects();

        postProcessingManager.renderToScreen();
    }

    /**
     * Resizes the viewport.
     * @param width
     * The width the viewport should be set to.
     * @param height
     * The height the viewport should be set to.
     */
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

    /**
     * Disposes the stage and spriteBatch.
     */
    @Override
    public void dispose() {
        this.stage.dispose();
        this.batch.dispose();
    }
}



