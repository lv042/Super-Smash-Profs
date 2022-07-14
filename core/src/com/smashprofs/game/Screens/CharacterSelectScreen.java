package com.smashprofs.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crashinvaders.vfx.VfxManager;
import com.smashprofs.game.Helper.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CharacterSelectScreen implements Screen {

    private static Logger log = LogManager.getLogger(CharacterSelectScreen.class);
    int width = Gdx.graphics.getWidth();
    int height = Gdx.graphics.getHeight();
    private float zoomFactor = 1f;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private SoundManager soundManager;
    private Array<Controller> controllers;
    //private TextureRegion[] alex, maurice, luca, leo, viktor;
    private Image left1, right1, left2, right2, playButton, p1ctrls, p2ctrls;
    private Table table;
    private VfxManager postProcessingManager;
    private static int carouselCounter = 0;
    private static int carouselCounter2 = 0;
    //private TextureRegion[] playerImages;
    private List<TextureRegion> playerImages;
    private static String[] playerNames;
    private ImageButton currentSelection, currentSelection2;
    private Label p1Label, p2Label;
    private BitmapFont labelFont;
    SpriteBatch batch;
    Game game;

    public CharacterSelectScreen(Game game) {
        this.camera = new OrthographicCamera(width, height);
        this.batch = new SpriteBatch();
        this.game = game;
        this.viewport = new FillViewport(1920, 1080, this.camera);
        this.stage = new Stage(this.viewport, this.batch);
        this.soundManager = SoundManager.getSoundManager_INSTANCE();
        this.controllers = Controllers.getControllers();
        table = new Table();
        PostProcessingSettings ppSetUpHandler = new PostProcessingSettings();
        this.postProcessingManager = ppSetUpHandler.getPostProcessingManager();

        Map<String, String> players = Map.of("Alex", "Sprites/Alex/alex_stand.png", "Maurice", "Sprites/Momo/momo_strip.png", "Luca", "Sprites/Luca/luca_stand.png", "Leo", "Sprites/Leo/leo_stand.png", "Viktor", "Sprites/Viktor/viktor_stand.png");
        this.playerImages = players.values().stream().map(path -> new Texture(path)).map(t -> TextureRegion.split(t, 100, 100)[0]).map(tr -> tr[0]).toList();
        playerNames = players.keySet().toArray(new String[players.keySet().size()]);

        this.currentSelection = new ImageButton(new TextureRegionDrawable(playerImages.get(carouselCounter)));
        this.currentSelection2 = new ImageButton(new TextureRegionDrawable(playerImages.get(carouselCounter2)));

        this.labelFont = new BitmapFont();
        labelFont.getData().setScale(4f);
        this.p1Label = new Label(playerNames[carouselCounter], new Label.LabelStyle(labelFont, Color.valueOf("FFFFFF")));
        this.p2Label = new Label(playerNames[carouselCounter2], new Label.LabelStyle(labelFont, Color.valueOf("FFFFFF")));

        left1 = new Image(new Texture("ui/arrowLeft.png"));
        left2 = new Image(new Texture("ui/arrowLeft.png"));
        right1 = new Image(new Texture("ui/arrowRight.png"));
        right2 = new Image(new Texture("ui/arrowRight.png"));

        left1.scaleBy(2f);
        left2.scaleBy(2f);
        right1.scaleBy(2f);
        right2.scaleBy(2f);

        playButton = new Image(new Texture("mainmenu/buttons/playButtonInactive.png"));
        p1ctrls = new Image(new Texture("ui/ctrlsP1.png"));
        p2ctrls = new Image(new Texture("ui/ctrlsP2.png"));
        // Set control hints to "Gamepad" if every player has a connected gamepad.
        if(controllers.size > 1) {
            log.info("More than 1 gamepad connected! Showing controller hints.");
            p1ctrls = new Image(new Texture("ui/ctrlsGamepad.png"));
            p2ctrls = new Image(new Texture("ui/ctrlsGamepad.png"));
        }
        // Keep in mind: scaleBy(1f) equals setScale(2f)!
        p1ctrls.scaleBy(0.5f);
        p2ctrls.scaleBy(0.5f);

        left1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                    decrementCarouselCounter(1);
            }
        });
        left2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                decrementCarouselCounter(2);
            }
        });
        right1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                incrementCarouselCounter(1);
            }
        });
        right2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                incrementCarouselCounter(2);
            }
        });

        playButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {

                //playButton = new Image(playButtonActive);
                playButton.setDrawable(new SpriteDrawable(new Sprite(new Texture("mainmenu/buttons/playButtonActive.png"))));
                if (Gdx.input.isButtonJustPressed(0)) {
                    soundManager.playSound("sounds/minecraft_click.mp3");
                    log.info("PlayerOne selected the character " + getSelectedPlayerOne());
                    log.info("PlayerTwo selected the character " + getSelectedPlayerTwo());
                    game.setScreen(new PlayScreen((com.smashprofs.game.Game) game));
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                playButton.setDrawable(new SpriteDrawable(new Sprite(new Texture("mainmenu/buttons/playButtonInactive.png"))));
            }
        });

        log.info("Created CharacterSelectScreen");

    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        //Create Table
        //Set table to fill stage
        table.setFillParent(true);
        //Set alignment of contents in the table.
        renderTable();


        //table.setDebug(true);
        if(table.getDebug()) {
            log.warn("Table is set to debug mode!");
        }
        
        stage.addActor(table);

        log.info("Showing CharacterSelectScreen");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        postProcessingManager.cleanUpBuffers();
        postProcessingManager.beginInputCapture();

        //character selection with input keys
        scrollWithKeys();

        this.stage.act();
        this.stage.draw();

        postProcessingManager.endInputCapture();

        postProcessingManager.applyEffects();

        postProcessingManager.renderToScreen();

    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    private void scrollWithKeys() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
            decrementCarouselCounter(1);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D)){
            incrementCarouselCounter(  1);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            decrementCarouselCounter(2);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            incrementCarouselCounter(  2);
        }
    }

    public void incrementCarouselCounter(int NumberOfCarouselCounter) {

        if(NumberOfCarouselCounter == 1) {
            if(carouselCounter < playerImages.size()-1) {
                carouselCounter++;
            }
            else if (carouselCounter == playerImages.size() -1) {
                carouselCounter = 0;
            }
            System.out.println("CarouselCounter1: " + carouselCounter);
            System.out.println("CurrentPlayer1: " + playerNames[carouselCounter]);
        } else if (NumberOfCarouselCounter == 2) {
            if(carouselCounter2 < playerImages.size()-1) {
                carouselCounter2++;
            }
            else if (carouselCounter2 == playerImages.size() -1) {
                carouselCounter2 = 0;
            }
            System.out.println("CarouselCounter2: " + carouselCounter2);
            System.out.println("CurrentPlayer2: " + playerNames[carouselCounter2]);
        }

        updateScreenContents();
    }

    public void decrementCarouselCounter(int NumberOfCarouselCounter) {

        if(NumberOfCarouselCounter == 1) {
            if(carouselCounter > 0) {
                carouselCounter--;
            }
            else if (carouselCounter == 0) {
                carouselCounter = playerImages.size()-1;
            }
            System.out.println("CarouselCounter1: " + carouselCounter);
            System.out.println("CurrentPlayer1: " + playerNames[carouselCounter]);
        }
        else if(NumberOfCarouselCounter == 2) {
            if(carouselCounter2 > 0) {
                carouselCounter2--;
            }
            else if (carouselCounter2 == 0) {
                carouselCounter2 = playerImages.size()-1;
            }
            System.out.println("CarouselCounter2: " + carouselCounter2);
            System.out.println("CurrentPlayer2: " + playerNames[carouselCounter2]);
        }

        updateScreenContents();
    }

    public void updateScreenContents() {
        //this.currentSelection = new ImageButton(new TextureRegionDrawable(playerImages.get(carouselCounter)));
        this.currentSelection = new ImageButton(new TextureRegionDrawable(playerImages.get(carouselCounter)));
        this.p1Label.setText(playerNames[carouselCounter]);
        this.currentSelection2 = new ImageButton(new TextureRegionDrawable(playerImages.get(carouselCounter2)));
        this.p2Label.setText(playerNames[carouselCounter2]);
        table.clear();

        renderTable();
    }

    public void renderTable() {

        table.top();
        table.row().padTop(height/5f);
        table.add(currentSelection).colspan(2).width(width/2f);
        table.add(currentSelection2).colspan(2).width(width/2f);
        table.row().height(100).padTop(100).left();
        table.add(p1Label).colspan(2).center();
        table.add(p2Label).colspan(2).center();
        table.row().height(100).padTop(100).left();
        table.add(left1).maxSize(left1.getWidth(), left1.getHeight()).center();
        table.add(right1).maxSize(right1.getWidth(), right1.getHeight()).center();
        table.add(left2).maxSize(left2.getWidth(), left2.getHeight()).center();
        table.add(right2).maxSize(right2.getWidth(), right2.getHeight()).center();
        table.row().padTop(height/12f).height(p1ctrls.getHeight());
        table.add(p1ctrls).colspan(2);
        table.add(p2ctrls).colspan(2);
        table.row();
        table.add(playButton).center().colspan(4);
        table.background(new TextureRegionDrawable(new Texture("ui/selecbg.png")));
        stage.addActor(table);

    }

    public static String getSelectedPlayerOne() {
        if(com.smashprofs.game.Game.unitTestMode) {
            return "Alex";
        }
        return playerNames[carouselCounter];
    }
    public static String getSelectedPlayerTwo() {
        if(com.smashprofs.game.Game.unitTestMode) {
            return "Maurice";
        }
        return playerNames[carouselCounter2];
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
        this.batch.dispose();
    }
}
