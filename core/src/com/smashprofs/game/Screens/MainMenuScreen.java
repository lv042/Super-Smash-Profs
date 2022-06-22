package com.smashprofs.game.Screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crashinvaders.vfx.VfxManager;
import com.smashprofs.game.Helper.Keys;
import com.smashprofs.game.Helper.PostProcessingSettings;
import com.smashprofs.game.Helper.SoundManager;
import com.smashprofs.game.Helper.gamePropertiesManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainMenuScreen implements Screen {
    private static Logger log = LogManager.getLogger(MainMenuScreen.class);
    private Game game;
    private SpriteBatch spriteBatch;
    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera camera;
    private Image playButton;
    private Image exitButton;
    private Texture playButtonInactive, playButtonActive, exitButtonInactive, exitButtonActive, logo;
    private Table mainTable;
    private VfxManager postProcessingManager;
    private SoundManager sound;
    private Label gametime,timesplayed, gamedevs;

    private float timer;
    private float zoomFactor;

    int screenWidth = Gdx.graphics.getWidth();
    int height = Gdx.graphics.getHeight();



    public MainMenuScreen(Game game) {

        this.game = game;
        this.spriteBatch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FillViewport(1920, 1080, camera);
        this.stage = new Stage(this.viewport, this.spriteBatch);

        playButtonInactive = new Texture("mainmenu/buttons/playButtonInactive.png");
        playButtonActive = new Texture("mainmenu/buttons/playButtonActive.png");
        exitButtonInactive = new Texture("mainmenu/buttons/exitButtonInactive.png");
        exitButtonActive = new Texture("mainmenu/buttons/exitButtonActive.png");
        mainTable = new Table();

        //add pre configured settings to PostProcessingManager
        PostProcessingSettings ppSetUpHandler = new PostProcessingSettings();
        this.postProcessingManager = ppSetUpHandler.getPostProcessingManager();
        sound=SoundManager.getSoundManager_INSTANCE();
        log.info("Created MainMenuScreen");
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
        logo=new Texture("mainmenu/ssp.png");

        //Add listeners to buttons
        playButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {

                //playButton = new Image(playButtonActive);
                playButton.setDrawable(new SpriteDrawable(new Sprite(playButtonActive)));
                if (Gdx.input.isButtonJustPressed(0)) {
                    sound.playSound("sounds/minecraft_click.mp3");
                    //game.setScreen(new PlayScreen((com.smashprofs.game.Game) game));
                    log.info("Redirecting to CharacterSelectScreen");
                    game.setScreen(new CharacterSelectScreen(game));
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
                    sound.playSound("sounds/minecraft_click.mp3");
                    log.info("Exiting game...");
                    Gdx.app.exit();
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                exitButton.setDrawable(new SpriteDrawable(new Sprite(exitButtonInactive)));
            }
        });

        // Setting the values for the game statistics displayed in the main menu.
        gametime = new Label("Gametime: "+gamePropertiesManager.getEntry(Keys.GAMETIME), new Label.LabelStyle(new BitmapFont(), Color.valueOf("B2E6AD")));
        timesplayed = new Label("Times played: "+gamePropertiesManager.getEntry(Keys.TIMESPLAYED), new Label.LabelStyle(new BitmapFont(), Color.valueOf("9BD096")));
        gamedevs= new Label("Devs: Leo, Maurice, Alex, Luca", new Label.LabelStyle(new BitmapFont(), Color.valueOf("9BD096")));


        mainTable.add(playButton).padBottom(100).maxSize(300, 100).padTop(500);
        mainTable.row();
        mainTable.add(exitButton).padBottom(180).maxSize(300, 100);
        mainTable.row().padRight(1700);
        mainTable.add(gametime).maxSize(500, 200);
        mainTable.row();
        mainTable.add(timesplayed).maxSize(300, 100).padRight(1700);
        mainTable.row();
        mainTable.add(gamedevs).maxSize(600, 200).padLeft(1650);
        mainTable.background(new TextureRegionDrawable(new Texture("mainmenu/bgmenu.png")));
        //Add table to stage
        mainTable.setDebug(false);
        stage.addActor(mainTable);

        log.info("Showing MainMenuScreen");
    }

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

        spriteBatch.begin();

        spriteBatch.draw(logo, stage.getViewport().getWorldWidth() / 2f -logo.getWidth() /2/ zoomFactor, 700f / zoomFactor, 1228f / zoomFactor, 104f / zoomFactor);
        //spriteBatch.draw(logoTexture, screenWidth / 2 - 614f, 104f );
        spriteBatch.end();

        postProcessingManager.endInputCapture();
        postProcessingManager.applyEffects();
        postProcessingManager.renderToScreen();


        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)
            && Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
            if(gamePropertiesManager.getEntry(Keys.EASTEREGG).equals("true")) {
                gamePropertiesManager.edit(Keys.EASTEREGG, "false");
            }
            else if(gamePropertiesManager.getEntry(Keys.EASTEREGG).equals("false")) {
                gamePropertiesManager.edit(Keys.EASTEREGG, "true");
            }
        }

    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        //this.width = width / 2;

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
